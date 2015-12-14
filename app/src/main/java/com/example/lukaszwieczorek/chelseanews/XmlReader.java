package com.example.lukaszwieczorek.chelseanews;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class XmlReader {
    private XmlPullParserFactory xmlFactoryObject;

    private News news[];

    public void readXMLFile(final File destinationDirectory, final String filename) {
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
                            newsAL.add(new News(text));
                        }
                        else if (name.equals("link")) {}
                        else if (name.equals("description")) {
                            newsAL.get(newsAL.size()-1).setDescription(text);
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
        return news;
    }
}
