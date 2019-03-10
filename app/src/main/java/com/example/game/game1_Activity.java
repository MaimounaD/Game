package com.example.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class game1_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1);
    }
    // affiche le jeu
    public void startGame(View v){
        String playerName = getIntent().getExtras().getString("PLAYER_NAME");
        Intent intent = new Intent(this, quizz_Activity.class);
        intent.putExtra("PLAYER_NAME", playerName); // permet de récupérer le nom du joueur*/
        startActivity(intent);
    }
    // affiche le meilleur score
    public void highScore(View v){
        Intent intent = new Intent(this, score1_Activity.class);
        startActivity(intent);
    }
    // retourne sur la vue précédente
    public void quit(View view) {
        onBackPressed();
    }
}
