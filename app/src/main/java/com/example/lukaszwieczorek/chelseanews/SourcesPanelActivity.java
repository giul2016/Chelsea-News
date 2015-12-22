package com.example.lukaszwieczorek.chelseanews;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class SourcesPanelActivity extends AppCompatActivity {
    ImageView chelsealiveB, skysportsB, goalcomB, talksportchelseaB, talksportpremierleagueB, dailymailB;

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

        chelsealiveB = (ImageView) findViewById(R.id.buttonChelseaLive);
        chelsealiveB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openNewsActivity(chelseaLive);
            }
        });

        skysportsB = (ImageView) findViewById(R.id.buttonSkySports);
        skysportsB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openNewsActivity(skySports);
            }
        });
        goalcomB = (ImageView) findViewById(R.id.buttonGoalCom);
        goalcomB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openNewsActivity(goalCom);
            }
        });
        talksportchelseaB = (ImageView) findViewById(R.id.buttonTalkSportChelsea);
        talksportchelseaB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openNewsActivity(talkSportChelsea);
            }
        });
        talksportpremierleagueB = (ImageView) findViewById(R.id.buttonTalkSportPremierLeague);
        talksportpremierleagueB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openNewsActivity(talkSportPremierLeague);
            }
        });
        dailymailB = (ImageView) findViewById(R.id.buttonDailyMail);
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
            changeButtonsLayoutToOffline();
            Toast.makeText(this, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wl.release();
    }

    private void collectRssChannels() {
        chelsealiveB.setEnabled(false);
        skysportsB.setEnabled(false);
        goalcomB.setEnabled(false);
        talksportchelseaB.setEnabled(false);
        talksportpremierleagueB.setEnabled(false);
        dailymailB.setEnabled(false);

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
        ImageView button;

        @Override
        protected Double doInBackground(Portal... params) {
            getRssGetData(params[0]);
            button = (ImageView) findViewById(getResources().getIdentifier(params[0].getButtonId(), "id", "com.example.lukaszwieczorek.chelseanews"));
            return null;
        }

        protected void onPostExecute(Double result) {
            button.setEnabled(true);
            changeButtonLayoutToReady(button);
        }

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

    public void changeButtonLayoutToReady(ImageView readyButton) {
        readyButton.setAlpha(220);
    }

    public void changeButtonsLayoutToDeprecated() {
        ImageView[] allButtons = {chelsealiveB, skysportsB, goalcomB, talksportchelseaB, talksportpremierleagueB, dailymailB};

        for(ImageView button : allButtons) {
            button.setAlpha(20);
        }
    }

    public void changeButtonsLayoutToOffline() {
        ImageView[] allButtons = {chelsealiveB, skysportsB, goalcomB, talksportchelseaB, talksportpremierleagueB, dailymailB};

        for(ImageView button : allButtons) {
            setLocked(button);
            button.setAlpha(200);
        }
    }

    public static void  setLocked(ImageView v) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(cf);
        v.setAlpha(128);   // 128 = 0.5
    }
}
