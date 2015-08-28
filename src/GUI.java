import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;




/**
 * Created by evanllewellyn on 8/25/15.
 */
public class GUI extends Application {

    static ListView<String> comments;
    DailyInfo today;
    Label bottomLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Retrieves the DailyInfo object that is stored on the S3 server.
        DailyInfo s3info = JsonWriter.readJson(JsonWriter.retreiveJson());

        /* If there is no data in the S3 bucket or the Date from the online date is not current creates a new DailyInfo object with
        * the updated menu from http://dining.umd.edu/menus/.
        */
        if(s3info != null  && s3info.printDT().compareTo(LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))) == 0) {
            today = s3info;
        } else {
            ArrayList<ArrayList<String>> menu = MenuParser.parseMenu();
            today = new DailyInfo(menu.get(0), menu.get(1));
            JsonWriter.uploadJson(JsonWriter.writeJson(today));
        }

        //Using a VBox for the Layout of the GUI
        primaryStage.setTitle("UMDeats");
        VBox verLay = new VBox();

        //Creating HBox that will contain the top app Label and Picture.
        HBox topIcon = new HBox();
        Label topLabel = new Label("UMDEats");
        topLabel.getStyleClass().add("top-label");
        ImageView topImage = new ImageView("http://i.imgur.com/EriY4QU.png");

        topImage.setPreserveRatio(true);
        topImage.setFitHeight(100);
        topIcon.setSpacing(30);
        topIcon.getChildren().addAll(topLabel, topImage);
        topIcon.setAlignment(Pos.CENTER);
        topIcon.setMinHeight(150);

        //Creating HBox that will contain the menu text.
        HBox menuHbox = new HBox();
        Text menuDisplay = new Text(formatedMenuString(today));
        menuHbox.getChildren().add(menuDisplay);
        menuHbox.setAlignment(Pos.CENTER);

        //Creating the ListView that will display the comments. Calls loadComments to load them in the correct format.
        comments = new ListView<>();
        comments.setMaxHeight(150);
        loadComments(today);


        //Creates the text area that user uses to enter his/her comment.
        TextArea commentMaker = new TextArea();
        commentMaker.setMaxHeight(100);
        commentMaker.setPromptText("Enter comment here...");

        /*
         * Creates the button to enter the comment. On action the button gets the comment text, attempts to add the comment.
         * if the addComment() call returns false launches a popup window with the according failure info. If it adds, Clears
         * the comment text box and the comment list, and relists the comments in the ListView. Also uploads a new Updated Json
         * file to S3 so that the new comment can be viewed by concurrent users.
         */
        Button addComment = new Button("Comment");
        addComment.setOnAction(event -> {
            String s = commentMaker.getText();

            if( s.length() != 0) {
                if(today.addComment(s) == false){
                    InfoBox.infoB("Too Long", "Comment contains too many Characters. \n" +
                            "Make sure your comment is under 40 characters.");
                }
            } else {
                InfoBox.infoB("No Text", "No Text Entered.");
            }
            commentMaker.clear();
            comments.getItems().clear();

            JsonWriter.uploadJson(JsonWriter.writeJson(today));

            for(Comment c : today.comments) {
                String comToAdd = c.time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + ": " + c.comment;
                comments.getItems().add(comToAdd);
            }

        });

        //Creates an HBox that has a button to update the Apps info and label that shows last updated time/date.
        HBox bottom = new HBox();
        Button update = new Button("Update Comments");
        update.setOnAction(event -> {
            today = JsonWriter.readJson(JsonWriter.retreiveJson());
            menuDisplay.setText(formatedMenuString(today));
            loadComments(today);
            bottomLabel.setText("Last loaded: " + today.printDT());

        });

        bottomLabel = new Label("Last loaded: " + today.printDT());
        bottomLabel.getStyleClass().add("label-bottom");

        bottom.setSpacing(30);
        bottom.getChildren().addAll(bottomLabel,update);
        bottom.setAlignment(Pos.CENTER);

        //Adding all components to the VBox layout.
        verLay.getChildren().addAll(topIcon, menuHbox, comments, commentMaker, addComment, bottom);
        verLay.setSpacing(15);

        //Adding the layout to the scene and loading the corresponding .css design.
        Scene scene = new Scene(verLay);
        scene.getStylesheets().add("GUIDesign.css");

        //Adding the scene to the primary Stage and restricting the app from being resizeable.
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);

    }

    /*
     * formatedMenuString returns a string with a formatted String containing the Menu data from the DailyInfo parameter.
     * This string is loaded into the Text component on the GUI.
     */
    public static String formatedMenuString(DailyInfo today) {
        String menuText = "Today's Chef Feature: \n" +
                "   Lunch: \n";
        for(String s : today.lunchMenu) {
            menuText = menuText + "       " + s + "\n";
        }

        menuText = menuText + "    Dinner:\n";

        for(String s : today.dinnerMenu) {
            menuText = menuText + "       " + s + "\n";
        }

        return menuText;
    }

    /*
     * loadComments clears the ListView comments and adds each comment from the DailyInfo parameter in the form of
     * <Time of comment> : <Comment content> to the ListView in order from oldest to newest.
     */
    public static void loadComments(DailyInfo today) {
        comments.getItems().clear();
        for(Comment c : today.comments) {
            String comToAdd = c.time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + ": " + c.comment;
            comments.getItems().add(comToAdd);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
