package com.example.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button jeu2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // jeu2= (Button) findViewById(R.id.jeu2);
    }
    // affiche le menu du premier jeu : quizz
    public void game1(View v){
        Intent intent = new Intent(this, game1_Activity.class);
        startActivity(intent);
    }
    // affiche le menu du second jeu : carte
    public void game2(View v){
        Intent intent = new Intent(this, game2_Activity.class);
        startActivity(intent);
    }
    // quitte l'application
    public void quit(View v){
        finish();
    }
}
