
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;




/**
 * Created by evanllewellyn on 8/24/15.
 */
public class MenuParser {


    /*
     *  parseMenu retrieves the HTML code from http://dining.umd.edu/menus/. Parses the HTML code into ArrayLists
     *  containing the menu contents with the Jsoup API.
     *
     */
        public static ArrayList<ArrayList<String>> parseMenu() {

            //Creating string of current date to help parse the portion of the menu for the current day
            LocalDateTime dateTime = LocalDateTime.now();
            String dateFormat = dateTime.format(DateTimeFormatter.ofPattern("MM dd yyyy"));
            String dfWHyph = dateFormat.replace(' ', '-');

            String dateSel = "div#menu-" + dfWHyph;

            String url = "http://dining.umd.edu/menus/#";

            //creating arrayList that will contain the two ArrayLists for the lunch and dinner menu, respectively.
            ArrayList<ArrayList<String>> menu = new ArrayList<ArrayList<String>>();
            ArrayList<String> lunch = new ArrayList<>();
            ArrayList<String> dinner = new ArrayList<>();

            try {
                // Parsing the HTML code by Div IDs and list content. Adding the correct parsed lines to the ArrayLists for menu contents
                Document doc = Jsoup.connect(url).get();
                Elements daysMenu = doc.select(dateSel);
                Elements lunchMenu = daysMenu.select("div.medium-6.columns.left-box");
                Elements dinnerMenu = daysMenu.select("div.medium-6.columns.right-box");


                for( Element ele : lunchMenu) {
                    Elements seperateOne = ele.select("ul");
                    Element seperateTwo = seperateOne.select("ul").first();

                    for (Node node : seperateTwo.childNodes()) {
                        String[] remBr = node.toString().split("<br>");
                        String[] remLi = remBr[0].split("<li>");
                        lunch.add(remLi[1]);


                    }
                }

                for( Element ele : dinnerMenu) {
                    Elements test = ele.select("ul");
                    Element blah = test.select("ul").first();

                    for (Node node : blah.childNodes()) {
                        String[] remBr = node.toString().split("<br>");
                        String[] remLi = remBr[0].split("<li>");
                        dinner.add(remLi[1]);

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            menu.add(lunch);
            menu.add(dinner);
            return menu;
        }

}
