import java.time.LocalTime;

/**
 * Created by evanllewellyn on 8/25/15.
 */

/*
 * The comment class allows a Time that comment was written to be stored individually with each comment.
 */
public class Comment {
    LocalTime time;
    String comment;

    public Comment(String s) {
        comment = s;
        time = LocalTime.now();

    }
}
