package com.example.safezone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;


public class NewsBoard extends AppCompatActivity {
    private static final String TAG = "NewsBoard";

    private RecyclerView recyclerView;
    private ArrayList<NewsItem> newsList;
    private NewsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_board);

        adapter = new NewsAdapter(this);
        newsList = new ArrayList<>();

        recyclerView = findViewById(R.id.newsBoard_RecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // parse xml file in background
        CompletableFuture<Void> task = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            task = CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {

                    InputStream inputStream = getInputStream();

                    if (inputStream != null){
                        Log.d(TAG, "run: stream is not empty");
                        try {
                            inputXmlParser(inputStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            task.join();
        }

    }

    //get stream
    private InputStream getInputStream() {
        try {

            // create url object inside of try cath
            URL url = new URL("https://www.westminsterconservatives.com/news/feed");

            //create http connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET"); // this case is get method, app will receive news feed
            connection.setDoInput(true); //expecting some input form t he connection

            return connection.getInputStream();

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    //get content inside of the asynk task
    private void inputXmlParser (InputStream stream) throws IOException {
        XmlPullParser xmlPullParser = Xml.newPullParser(); // this object parse the xml receive form the internet

        try {

            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            xmlPullParser.setInput(stream, null);

            // parse the xml file element by element and extract data need it
            xmlPullParser.next(); // move the parser to the next element
            // make sure the tag is Rss element
            xmlPullParser.require(XmlPullParser.START_TAG,null,"rss"); // now is inside the rss element
            // create a while loop and move the tag in each element

            while (xmlPullParser.next() != XmlPullParser.END_TAG){ // if is not the rss end tag continue looping

                if (xmlPullParser.getEventType() != XmlPullParser.START_TAG){ // this returning the current event of the parser and it will avoid metadata until reaches a new tag
                    continue;
                }

                xmlPullParser.require(XmlPullParser.START_TAG,null,"channel"); // this makes parser to reach channel tag

                while (xmlPullParser.next() != XmlPullParser.END_TAG){ // continue looping until reach the end tag of channel element

                    if (xmlPullParser.getEventType() != XmlPullParser.START_TAG){ // this returning the current event of the parser and it will avoid metadata until reaches a new tag
                        continue;
                    }


                    if (xmlPullParser.getName().equals("item")){

                        xmlPullParser.require(XmlPullParser.START_TAG,null,"item"); // check if the start tag of the parser is item, is what we need

                        String title = "";
                        String description = "";
                        String link = "";
                        String pubdate = "";

                        while (xmlPullParser.next() != XmlPullParser.END_TAG){

                            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG){ // this returning the current event of the parser and it will avoid metadata until reaches a new tag
                                continue;
                            }

                            String tagName = xmlPullParser.getName();
                            if (tagName.equals("title")){

                                title = getContent(xmlPullParser,"title");

                            }else if (tagName.equals("description")){

                                description = getContent(xmlPullParser,"description");
                            }else if (tagName.equals("pubdate")){

                                pubdate = getContent(xmlPullParser,"puddate");
                            }else if (tagName.equals("link")){

                                link = getContent(xmlPullParser,"link");
                            }else {
                                skypTag(xmlPullParser);
                            }
                        }

                        // create a new news item and save the data received from xml file
                        NewsItem item = new NewsItem(title,description,link,pubdate);
                        newsList.add(item);
                        adapter.setNewsItem(newsList);

                    }else {
                        skypTag(xmlPullParser);
                    }


                }

            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }
    private String getContent (XmlPullParser parser, String tagName) throws IOException, XmlPullParserException {

        String content = " ";
        parser.require(XmlPullParser.START_TAG,null,tagName); // check the tag

        if (parser.next() == XmlPullParser.TEXT){  // we are looking at the content of our tag

            content = parser.getText();
            Log.d(TAG, "getContent: " + content);
            parser.next();  // after saving the content move the parser to the next element
        }

        return content;
    }

    // create a method to skip unwanted tags
    private void skypTag (XmlPullParser parser) throws XmlPullParserException, IOException {

        if (parser.getEventType() != XmlPullParser.START_TAG){ // check for the opening tag
            throw new IllegalStateException();
        }

        int number = 1;

        while (number != 0){ // if reaches the end tag of need element, and this will work even if the parser meets a nested loop

            switch (parser.next()){

                case XmlPullParser.START_TAG:
                    number++;
                    break;

                case XmlPullParser.END_TAG:
                    number --;
                    break;

                default:
                    break;
            }
        }

    }


    }



