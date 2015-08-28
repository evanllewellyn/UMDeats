
import java.io.*;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.google.gson.Gson;



/**
 * Created by evanllewellyn on 8/25/15.
 */
/*
 * Json writer class handles the conversion the DailyInfo object's conversion to and from a Json object. It also contains
 * the methods that upload the Json file (in .txt form) to the correct AmazonWebService S3 bucket. The Bucket's policy allows only
 * the upload/download of only the menuData.txt file. It will not grant permission for any other form of data.
 *
 * The AWS API is used to put/get the data. The reason for using the S3 is so that the comment section can be updated between
 * seperate users of the application.
 *
 * NOTE: The use of a AWS S3 server is not ideal/very secure technique for this type of data storage, This is only a temporary
 * fix to keep the comment data shared between users.
 */

public class JsonWriter {

    /*
     * writeJson takes a DailyInfo object and writes creates a Json object through Gson and writes it to a File (menuData.txt).
     * returns the file.
     */

    public static File writeJson(DailyInfo di) {
        Gson gson = new Gson();
        FileWriter out = null;
        File jfile = new File("menuData.txt");
        String jsonStr = null;

        try {
            out = new FileWriter(jfile);
            jsonStr = gson.toJson(di);
            out.write(jsonStr);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if( out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return jfile;

    }

    /*
     * readJson takes a string in the Json format and uses Gson to create a DailyInfo object with the relevant data
     */
    public static DailyInfo readJson(String s) {
        Gson gson = new Gson();
        DailyInfo dailyInfo = gson.fromJson(s, DailyInfo.class);

        return dailyInfo;
    }

    /*
     * uploadJson() uses AWS S3 client to upload the File, which is a .txt file in a Json form. The S3 server and bucket
     * is set up to only allow uploading/downloading a .txt file.
     */
    public static void uploadJson(File f) {

        AmazonS3Client amazonS3Client = new AmazonS3Client();
        if( amazonS3Client.doesBucketExist("umdeatsjson") != true ) {
            amazonS3Client.createBucket("umdeatsjson");

        }

        amazonS3Client.putObject("umdeatsjson", "menuData.txt", f);


    }

    /*
     * retreiveJson() uses a AWS S3 client to donwload the data file into an S3Object. Puts the S3Object's data into
     * an inputstream which is read through a buffered reader into a stringBuilder. This creates the String from the
     * text on the .txt file in the S3 bucket on the AWS.
     */
    public static String retreiveJson() {

        AmazonS3Client amazonS3Client = new AmazonS3Client();

        S3Object data = amazonS3Client.getObject("umdeatsjson", "menuData.txt");
        InputStream in = null;

        BufferedReader br;
        String line;
        StringBuilder strB = new StringBuilder();

        try {


            in = data.getObjectContent();
            br = new BufferedReader(new InputStreamReader(in));
            while( (line = br.readLine()) != null ) {
                strB.append(line);
            }

        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if( in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return strB.toString();

    }

}
