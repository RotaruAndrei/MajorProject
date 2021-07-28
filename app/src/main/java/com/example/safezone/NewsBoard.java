package com.example.safezone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;


public class NewsBoard extends AppCompatActivity {
    private static final String TAG = "NewsBoard";

    private RecyclerView recyclerView;
    private ArrayList<NewsItem> newsList;
    private NewsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_board);

//        adapter = new NewsAdapter(this);
//        newsList = new ArrayList<>();
//
//        recyclerView = findViewById(R.id.newsBoard_RecyclerView);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        new getNews().execute();

    }

//    private class GetNews extends AsyncTask <Void, Void, Void> {
//        private static final String TAG = "GetNews background task";
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            InputStream inputStream = getInputStream();
//
//            if (inputStream != null){
//
//                try {
//                    intXmlPullParser(inputStream);
//                } catch (XmlPullParserException | IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            adapter.setNewsItem(newsList);
//        }
//
//        private InputStream getInputStream () {
//
//            try {
//                URL url = new URL("https://www.autosport.com/rss/feed/f1");
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//                connection.setDoInput(true);
//                return connection.getInputStream();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        private void intXmlPullParser (InputStream inputStream) throws XmlPullParserException, IOException {
//            Log.d(TAG, "intXmlPullParser: backgroupd task is undergoing");
//            XmlPullParser parser = Xml.newPullParser();
//            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//            parser.setInput(inputStream, null);
//            parser.next();
//
//            parser.require(XmlPullParser.START_TAG, null, "rss");
//            while (parser.next() != XmlPullParser.END_TAG) {
//                if (parser.getEventType() != XmlPullParser.START_TAG) {
//                    continue;
//                }
//
//
//                parser.require(XmlPullParser.START_TAG, null, "channel");
//                while (parser.next() != XmlPullParser.END_TAG) {
//                    if (parser.getEventType() != XmlPullParser.START_TAG) {
//                        continue;
//                    }
//
//
//                    if (parser.getName().equals("item")) {
//                        parser.require(XmlPullParser.START_TAG, null, "item");
//
//                        String title = "";
//                        String description = "";
//                        String pubdate = "";
//                        String link = "";
//
//                        while (parser.next() != XmlPullParser.END_TAG) {
//                            if (parser.getEventType() != XmlPullParser.START_TAG){
//                                continue;
//                            }
//
//                            String tagName = parser.getName();
//
//                            if(tagName.equals("title")){
//                                title = getContent(parser, tagName);
//                            }else if (tagName.equals("description")){
//                                description = getContent(parser, tagName);
//                            }else if (tagName.equals("link")){
//                                link = getContent(parser, tagName);
//
//                            }else if (tagName.equals("pubdate")){
//                                pubdate = getContent(parser, tagName);
//
//                            }else {
//                                skipTag(parser);
//                            }
//                        }
//
//                        NewsItem item = new NewsItem(title,description,pubdate,link);
//                        newsList.add(item);
//                    } else {
//                        skipTag(parser);
//                    }
//                }
//            }
//        }
//
//        private String getContent (XmlPullParser parser, String tagName) throws IOException, XmlPullParserException {
//
//            String content = "";
//            parser.require(XmlPullParser.START_TAG, null, tagName);
//
//            if (parser.next() == XmlPullParser.TEXT){
//                content = parser.getText();
//                parser.next();
//            }
//
//            return content;
//        }
//
//        private void skipTag (XmlPullParser parser) throws XmlPullParserException, IOException {
//
//            if (parser.getEventType() != XmlPullParser.START_TAG){
//                throw  new IllegalStateException();
//            }
//
//            int number = 1;
//
//            while (number != 0){
//                switch (parser.next()){
//
//                    case XmlPullParser.START_TAG:
//                        number++;
//                        break;
//                    case XmlPullParser.END_TAG:
//                        number--;
//                        break;
//                    default:
//                        break;
//
//                }
//            }
//
//        }
//    }


//    private  class getNews extends AsyncTask<Void,Void,Void>{
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            InputStream stream = getInputStream();
//
//            if (stream != null){
//                try {
//                    inputXmlParser(stream);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            adapter.setNewsItem(newsList);
//        }
//
//        private InputStream getInputStream() {
//            try {
//
//                // create url object inside of try cath
//                URL url = new URL("https://www.autosport.com/rss/f1/news/");
//
//                //create http connection
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET"); // this case is get method, app will receive news feed
//                connection.setDoInput(true); //expecting some input form t he connection
//
//                return connection.getInputStream();
//
//            } catch (IOException e) {
//
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        //get content inside of the asynk task
//        private void inputXmlParser (InputStream stream) throws IOException {
//            XmlPullParser xmlPullParser = Xml.newPullParser(); // this object parse the xml receive form the internet
//
//            try {
//
//                xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
//                xmlPullParser.setInput(stream, null);
//
//                // parse the xml file element by element and extract data need it
//                xmlPullParser.next(); // move the parser to the next element
//                // make sure the tag is Rss element
//                xmlPullParser.require(XmlPullParser.START_TAG,null,"rss"); // now is inside the rss element
//                // create a while loop and move the tag in each element
//
//                while (xmlPullParser.next() != XmlPullParser.END_TAG){ // if is not the rss end tag continue looping
//
//                    if (xmlPullParser.getEventType() != XmlPullParser.START_TAG){ // this returning the current event of the parser and it will avoid metadata until reaches a new tag
//                        continue;
//                    }
//
//                    xmlPullParser.require(XmlPullParser.START_TAG,null,"channel"); // this makes parser to reach channel tag
//
//                    while (xmlPullParser.next() != XmlPullParser.END_TAG){ // continue looping until reach the end tag of channel element
//
//                        if (xmlPullParser.getEventType() != XmlPullParser.START_TAG){ // this returning the current event of the parser and it will avoid metadata until reaches a new tag
//                            continue;
//                        }
//
//
//                        if (xmlPullParser.getName().equals("item")){
//
//                            xmlPullParser.require(XmlPullParser.START_TAG,null,"item"); // check if the start tag of the parser is item, is what we need
//
//                            String title = "";
//                            String description = "";
//                            String link = "";
//                            String pubdate = "";
//
//                            while (xmlPullParser.next() != XmlPullParser.END_TAG){
//
//                                if (xmlPullParser.getEventType() != XmlPullParser.START_TAG){ // this returning the current event of the parser and it will avoid metadata until reaches a new tag
//                                    continue;
//                                }
//
//                                String tagName = xmlPullParser.getName();
//
//                                if (tagName == "title"){
//
//                                    title = getContent(xmlPullParser,"title");
//                                    Log.d(TAG, "inputXmlParser: " +title);
//                                }else if (tagName == "description"){
//
//                                    description = getContent(xmlPullParser,"description");
//                                }else if (tagName == "pubdate"){
//
//                                    pubdate = getContent(xmlPullParser,"pubdate");
//                                }else if (tagName == "link"){
//
//                                    link = getContent(xmlPullParser,"link");
//                                }else {
//                                    skypTag(xmlPullParser);
//                                }
//                            }
//
//                            // create a new news item and save the data received from xml file
//                            NewsItem item = new NewsItem(title,description,link,pubdate);
//                            newsList.add(item);
//
//                        }else {
//                            skypTag(xmlPullParser);
//                        }
//
//
//                    }
//
//                }
//
//            } catch (XmlPullParserException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        private String getContent (XmlPullParser parser, String tagName) throws IOException, XmlPullParserException {
//
//            String content = "";
//            parser.require(XmlPullParser.START_TAG,null,tagName); // check the tag
//
//            if (parser.next() == XmlPullParser.TEXT){  // we are looking at the content of our tag
//
//                content = parser.getText();
//                parser.next();  // after saving the content move the parser to the next element
//            }
//
//            return content;
//        }
//
//        // create a method to skip unwanted tags
//        private void skypTag (XmlPullParser parser) throws XmlPullParserException, IOException {
//
//            if (parser.getEventType() != XmlPullParser.START_TAG){ // check for the opening tag
//                throw new IllegalStateException();
//            }
//
//            int number = 1;
//
//            while (number != 0){ // if reaches the end tag of need element, and this will work even if the parser meets a nested loop
//
//                switch (parser.next()){
//
//                    case XmlPullParser.START_TAG:
//                        number++;
//                        break;
//
//                    case XmlPullParser.END_TAG:
//                        number --;
//                        break;
//
//                    default:
//                        break;
//                }
//            }
//
//        }
//    }


    }



