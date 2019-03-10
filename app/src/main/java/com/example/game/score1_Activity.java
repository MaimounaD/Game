package com.example.game;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class score1_Activity extends AppCompatActivity {

    // Sharedpreferences pour le score
    private SharedPreferences sharedpref;

    TextView player;
    TextView score;
    TextView date;
    int theScore;
    String thePlayer;
    Date theDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score1);

        player = (TextView) findViewById(R.id.highscorePlayer);
        score = (TextView) findViewById(R.id.highScoreValue);
        date = (TextView) findViewById(R.id.highScoreDate);

        // récupération du joueur, du score et de la date
        sharedpref = this.getSharedPreferences("quizz_highscore", MODE_PRIVATE);
        thePlayer = sharedpref.getString("quizzPlayer","");
        if(thePlayer.equals("")) {
            player.setText("");
            score.setText("");
            date.setText("");
        }
        else {
            player.setText("Joueur : "+thePlayer);
            theScore = sharedpref.getInt("quizzScore", 0);
            score.setText("Score : "+theScore);
            long dateMillis = sharedpref.getLong("quizzDate", 0);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            theDate = new Date(dateMillis);
            date.setText("Date : " + sdf.format(theDate));
        }

    }
}
