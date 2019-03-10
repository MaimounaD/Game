package com.example.game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class quizz_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);
    }

    public void goQuestions(View view) {
        String playerName = getIntent().getExtras().getString("PLAYER_NAME");
        Intent intent = new Intent(this, questions_Activity.class);
        intent.putExtra("PLAYER_NAME", playerName);
        startActivity(intent);
    }

    public void goPictures(View view) {
        String playerName = getIntent().getExtras().getString("PLAYER_NAME");
        Intent intent = new Intent(this, pictures_Activity.class);
        intent.putExtra("PLAYER_NAME", playerName);
        startActivity(intent);
    }
}
