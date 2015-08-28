import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by evanllewellyn on 8/25/15. InfoBox creates the popup window that displays the
 * error messages when an incorrect comment form occurs
 */
public class InfoBox {


    static void infoB(String title, String text) {

        //Setting up stage, configuring modality so it must be closed before proceeding.
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinHeight(150);
        stage.setMinWidth(200);

        //Setting up label
        Label label = new Label(text);
        label.setTextAlignment(TextAlignment.CENTER);
        label.autosize();

        //Setting up HBox
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(40);

        //Creating Ok button to close the window
        Button ok = new Button("Ok");
        ok.setOnAction(event -> {
            stage.close();
        });

        vbox.getChildren().addAll(label, ok);

        //Adding CSS design to the scene and adding to stage.
        Scene scene = new Scene(vbox);
        scene.getStylesheets().add("InfoBoxDesign.css");
        stage.setScene(scene);
        stage.showAndWait();


    }
}
