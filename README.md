# UMDeats

UMDeats displays the current day's lunch and dinner menu that being served at The University of Maryland's diners. It also features a cross user comment section that is refreshed daily with each updated menu. 
The comment system is anonymous between users, only displaying the time that the comment was made. The comment section allows users to share their opinions of the daily menu between each other. UMDeats is written
in Java and utilizes Amazon Web Services. It's GUI is designed with JavaFX and CSS.

# Functions

* UMDeats menu automatically updates at the start of each day, erasing the previous days comments. 
* Features an "Update Comments" button that will reload the menu as well any new comments that were made since the last launch or update. 
* Comments must be between 1 and 60 characters in length. Displays popup info box if specifications are not met, as shown below.
![alt text](https://github.com/evanllewellyn/UMDeats/blob/master/UMDEatspics/toomany.png "too many char")


# An example photo of the application's use: 
![alt text](https://github.com/evanllewellyn/UMDeats/blob/master/UMDEatspics/overall.png "overall")


# Details

UMDeats retrieves University of Maryland's daily menu information off of its [dining menu webpage](http://dining.umd.edu/menus/) by using the [Jsoup API](http://jsoup.org/) to retrieve and parse the HTML code. The parsed data is then created into an object that converted to and from Json objects using [Google's Gson API](https://github.com/google/gson). Comments are also contained in the Json object along with the day's menu.

This Json object is uploaded and downloaded to a AWS S3 bucket through the [AWS SDK for Java](https://aws.amazon.com/sdk-for-java/). Keeping the Json file in a S3 bucket online 
allows the daily comment section to persist between different users. The S3 Bucket has a set policy to only accept data in the form of a .txt file.  

NOTE: It is known that using an AWS S3 bucket to upload and download the Json object is not a very practical or secure way to host the data. This was implemented for sake of time and ease. 
TODO: Find alternative to storing data on AWS S3 bucket, convert concept to iOS with Swift or Andriod with Andriod Studio. 



Written by Evan Llewellyn, August 2015.
