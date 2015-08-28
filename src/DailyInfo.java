import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;


/**
 * Created by evanllewellyn on 8/25/15. The DailyInfo class that will contain the day's current date, lunch menu, dinner menu,
 * and collective comment section. Getters/setters are for the Gson api to correctly convert to/from a json object.
 */
public class DailyInfo {
    LocalDateTime date;
    ArrayList<String> lunchMenu;
    ArrayList<String> dinnerMenu;
    ArrayList<Comment> comments;

    public DailyInfo(ArrayList<String> lm, ArrayList<String> dm) {
        date = LocalDateTime.now();
        lunchMenu = lm;
        dinnerMenu = dm;
        comments = new ArrayList<>();
    }

    // Add comment checks if the comment is too long, if it is returns false, returns true if successfully added.
    public boolean addComment(String comment) {
        if(comment.length() > 60) {
            return false;
        } else {
            this.comments.add(new Comment(comment));
            return true;
        }
    }
    //Prints a string of the current date for this menu.
    public String printDT() {
        return date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public ArrayList<String> getLunchMenu() {
        return lunchMenu;
    }

    public void setLunchMenu(ArrayList<String> lunchMenu) {
        this.lunchMenu = lunchMenu;
    }

    public ArrayList<String> getDinnerMenu() {
        return dinnerMenu;
    }

    public void setDinnerMenu(ArrayList<String> dinnerMenu) {
        this.dinnerMenu = dinnerMenu;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

}
