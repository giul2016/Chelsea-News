package com.example.lukaszwieczorek.chelseanews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class SourcesPanelActivity extends AppCompatActivity {
    Button chelsealiveB, skysportsB, goalcomB, talksportchelseaB, talksportpremierleagueB, dailymailB;

    PowerManager.WakeLock wl;

    File appDirectory;
    private XmlDownloader xmlDownloader = new XmlDownloader();

    private Portal chelseaLive = new Portal("http://www.chelsealive.pl/news/rss",
                                            "chelsealive.xml",
                                            "buttonChelseaLive");

    private Portal skySports = new Portal("http://feeds.skynews.com/feeds/rss/sports.xml",
                                          "skysports.xml",
                                          "buttonSkySports");

    private Portal goalCom = new Portal("http://www.goal.com/en/feeds/news?fmt=rss",
                                        "goalCom.xml",
                                        "buttonGoalCom");

    private Portal talkSportChelsea = new Portal("http://talksport.com/rss/football/chelsea/feed",
                                                 "talkSportChelsea.xml",
                                                 "buttonTalkSportChelsea");

    private Portal talkSportPremierLeague = new Portal("http://talksport.com/rss/football/premier-league/feed",
                                                       "talkSportPremierLeague.xml",
                                                       "buttonTalkSportPremierLeague");

    private Portal dailyMail = new Portal("http://www.dailymail.co.uk/sport/index.rss",
                                          "dailymail.xml",
                                          "buttonDailyMail");

    public final static String EXTRA_NEWS_FILE = "com.example.lukaszwieczorek.chelseanews.NEWS_FILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appDirectory = getExternalFilesDir(null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources_panel);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Wake Lock in the main activity");
        wl.acquire();

        chelsealiveB = (Button) findViewById(R.id.buttonChelseaLive);
        chelsealiveB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openNewsActivity(chelseaLive);
            }
        });

        skysportsB = (Button) findViewById(R.id.buttonSkySports);
        skysportsB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openNewsActivity(skySports);
            }
        });
        goalcomB = (Button) findViewById(R.id.buttonGoalCom);
        goalcomB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openNewsActivity(goalCom);
            }
        });
        talksportchelseaB = (Button) findViewById(R.id.buttonTalkSportChelsea);
        talksportchelseaB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openNewsActivity(talkSportChelsea);
            }
        });
        talksportpremierleagueB = (Button) findViewById(R.id.buttonTalkSportPremierLeague);
        talksportpremierleagueB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openNewsActivity(talkSportPremierLeague);
            }
        });
        dailymailB = (Button) findViewById(R.id.buttonDailyMail);
        dailymailB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openNewsActivity(dailyMail);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeButtonsLayoutToDeprecated();

        if (Services.isEstablishedInternetConnectivity(this)) {
            collectRssChannels();
        } else {
            Toast.makeText(this, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wl.release();
    }

    private void collectRssChannels() {
        getNewsAsyncTask chelseaLiveTask = new getNewsAsyncTask();
        chelseaLiveTask.execute(chelseaLive);
        getNewsAsyncTask skySportsTask = new getNewsAsyncTask();
        skySportsTask.execute(skySports);
        getNewsAsyncTask goalComTask = new getNewsAsyncTask();
        goalComTask.execute(goalCom);
        getNewsAsyncTask talkSportChelseaTask = new getNewsAsyncTask();
        talkSportChelseaTask.execute(talkSportChelsea);
        getNewsAsyncTask talkSportPremierLeagueTask = new getNewsAsyncTask();
        talkSportPremierLeagueTask.execute(talkSportPremierLeague);
        getNewsAsyncTask dailyMailTask = new getNewsAsyncTask();
        dailyMailTask.execute(dailyMail);
    }

    private class getNewsAsyncTask extends AsyncTask<Portal, Integer, Double> {
        Button button;

        @Override
        protected Double doInBackground(Portal... params) {
            getRssGetData(params[0]);
            button = (Button) findViewById(getResources().getIdentifier(params[0].getButtonId(), "id", "com.example.lukaszwieczorek.chelseanews"));
            return null;
        }

        protected void onPostExecute(Double result) { changeButtonLayoutToReady(button); }

        protected void onProgressUpdate(Integer... progress) { }
    }

    public void getRssGetData(Portal portal) {
        xmlDownloader.downloadXMLFile(portal.getUrl(), appDirectory, portal.getFilename());
    }

    public void openNewsActivity(Portal portal) {
        Intent startNewsActivity = new Intent(getApplicationContext(), NewsActivity.class);
        startNewsActivity.putExtra(EXTRA_NEWS_FILE, portal.getFilename());
        startActivity(startNewsActivity);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void changeButtonLayoutToReady(Button readyButton) {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            readyButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_ready));
        } else {
            readyButton.setBackground(getResources().getDrawable(R.drawable.button_ready));
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void changeButtonsLayoutToDeprecated() {
        Button[] allButtons = {chelsealiveB, skysportsB, goalcomB, talksportchelseaB, talksportpremierleagueB, dailymailB};

        for(Button button : allButtons) {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_deprecated));
            } else {
                button.setBackground(getResources().getDrawable(R.drawable.button_deprecated));
            }
        }
    }
}
