package com.example.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class game2_Activity extends AppCompatActivity {
    Button jeu2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);
        //jeu2= (Button) findViewById(R.id.jeu2);
    }
    // affiche le jeu
    public void startGame(View v){
        Intent intent = new Intent(this, map_Activity.class);
        startActivity(intent);
    }
    // affiche le meilleur score
    public void highScore(View v){
        Intent intent = new Intent(this, score2_Activity.class);
        startActivity(intent);
    }

    // retourne à l'accueil
    public void quit(View v) {
        onBackPressed();
    }
}
