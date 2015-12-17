package com.example.lukaszwieczorek.chelseanews;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class XmlReader {
    private XmlPullParserFactory xmlFactoryObject;
    private String filename;
    private News news[];

    public void readXMLFile(final File destinationDirectory, final String filename) {
        this.filename = filename;

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(destinationDirectory, filename));

            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();

            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

            myparser.setInput(fileInputStream, null);

            parseXMLAndStoreIt(myparser);

            fileInputStream.close();

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        ArrayList<News> newsAL = new ArrayList<>();
        int event;
        String text=null;

        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();

                switch (event){
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (name.equals("title")) {
                            newsAL.add(new News(text.trim()));
                        }
                        else if (name.equals("link")) {}
                        else if (name.equals("description")) {
                            newsAL.get(newsAL.size()-1).setDescription(text.trim());
                        }
                        else if (name.equals("pubDate")) {
                            newsAL.get(newsAL.size()-1).setDate(text.trim());
                        }
                        else {}
                        break;
                }
                event = myParser.next();
            }
            news = newsAL.toArray(new News[newsAL.size()]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public News[] getNews() {
        if (filename.equals("chelsealive.xml") || filename.equals("talkSportChelsea.xml") || filename.equals("talkSportPremierLeague.xml"))
            return Arrays.copyOfRange(news, 1, news.length-1);
        else if (filename.equals("goalCom.xml") || filename.equals("dailymail.xml"))
            return Arrays.copyOfRange(news, 2, news.length-1);
        else if (filename.equals("skysports.xml"))
            return Arrays.copyOfRange(news, 3, news.length-1);
        else
            return news;
    }
}
