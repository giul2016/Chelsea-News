package com.example.lukaszwieczorek.chelseanews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.io.File;

public class NewsActivity extends AppCompatActivity {
    private ListView newsLV;

    File appDirectory;
    private XmlReader xmlReader = new XmlReader();

    PowerManager.WakeLock wl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appDirectory = getExternalFilesDir(null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Wake Lock in the main activity");
        wl.acquire();

        Intent intent = getIntent();
        String filename = intent.getStringExtra(SourcesPanelActivity.EXTRA_NEWS_FILE);

        newsLV = (ListView) findViewById(R.id.listViewNews);

        xmlReader.readXMLFile(appDirectory, filename);

        News news[] = xmlReader.getNews();

        NewsAdapter adapterNEws = new NewsAdapter(this, R.layout.listview_news_row, news);
        newsLV.setAdapter(adapterNEws);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wl.release();
    }
}
