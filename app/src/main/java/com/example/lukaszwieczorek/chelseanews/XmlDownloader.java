package com.example.lukaszwieczorek.chelseanews;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class XmlDownloader {
    public void downloadXMLFile(final String sourceUrl, final File destinationDirectory, final String filename) {
        try {
            URL url = new URL(sourceUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // Starts the query
            conn.connect();
            InputStream stream = conn.getInputStream();

            File file = new File(destinationDirectory, filename);
            FileOutputStream fileOutput = new FileOutputStream(file);
            byte[] buffer = new byte[1024];

            int bufferLength = 0; //used to store a temporary size of the buffer
            while ( (bufferLength = stream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
            }
            fileOutput.close();
            stream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}
