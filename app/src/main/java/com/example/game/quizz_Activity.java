package com.example.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class quizz_Activity extends AppCompatActivity {
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor spEditor;
    private EditText player;
    private Button btnSave;
    private TextView playerLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);

        // enregistement du joueur
        sharedpref = getPreferences(MODE_PRIVATE);

        // nom du joueur et bouton ok
        player = (EditText) findViewById(R.id.player);
        btnSave = (Button) findViewById(R.id.savePlayer);
        playerLabel = (TextView) findViewById(R.id.playerLabel);

    }

    // enregistre le nom du joueur dans les sharedpreferences
    public void savePlayer(View v) {
        // recuperer le nom du joueur
        String pseudo = player.getText().toString();

        // lancer l'editeur de sharepreferences
        spEditor = sharedpref.edit();
        spEditor.putString("keyPlayer", pseudo);
        spEditor.commit();

        // affiche le nom du joueur saisi
        String str = sharedpref.getString("keyPlayer","");
        playerLabel.setText("Good Luck "+str+ " !");
        player.setVisibility(View.INVISIBLE);
        btnSave.setVisibility(View.INVISIBLE);
    }

}
