package com.example.game;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



public class MainActivity extends AppCompatActivity {
    Button jeu2;
    // creation des tables dans sqlite
    private Countries_DbHelper dbHelper;
    private SQLiteDatabase db ;

    // sauvegarde du joueur
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor spEditor;
    private EditText player;
    private Button btnSave;
    private Button game1;
    private Button game2;
    private TextView playerLabel;
    private String pseudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // creation de la table tablePays
        addCountriesValues();

        // saisie du joueur
        player = findViewById(R.id.player);
        btnSave = findViewById(R.id.savePlayer);
        playerLabel = findViewById(R.id.playerLabel);
        game1 = findViewById(R.id.game1);
        game2 = findViewById(R.id.game2);
        pseudo = "";
    }

    // affiche le nom du joueur
    public void savePlayer(View v) {
        // recuperer le nom du joueur
        pseudo = player.getText().toString();
        if(player.getText().toString().matches("")) {
            Toast.makeText(this, "Veuillez saisir un pseudo", Toast.LENGTH_SHORT).show();
            game1.setEnabled(false);
            game2.setEnabled(false);
        }
        else {
            playerLabel.setText("Bonne chance "+pseudo+ " !");
            player.setVisibility(View.INVISIBLE);
            player.setEnabled(false); // ferme le clavier en même temps
            btnSave.setVisibility(View.INVISIBLE);
            // cacher le clavier
            player.onEditorAction(EditorInfo.IME_ACTION_DONE);
            // active les boutons
            game1.setEnabled(true);
            game2.setEnabled(true);
        }

    }

    // creation de la table tablePays avec les infos du fichier csv
    private void addCountriesValues() {
        // structure table TablePays
        dbHelper = new Countries_DbHelper(this);
        db = dbHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + DBTable.CountriesTable.TABLE_NAME);
        final String SQL_CREATE_COUNTRIES_TABLE = "CREATE TABLE " +
                DBTable.CountriesTable.TABLE_NAME + " ( " +
                DBTable.CountriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBTable.CountriesTable.COLUMN_PAYS + " TEXT, " +
                DBTable.CountriesTable.COLUMN_CAPITAL + " TEXT, " +
                DBTable.CountriesTable.COLUMN_DEVISE + " TEXT, " +
                DBTable.CountriesTable.COLUMN_MONNAIE + " TEXT, " +
                DBTable.CountriesTable.COLUMN_HABITANTS + " INTEGER, " +
                DBTable.CountriesTable.COLUMN_FLEUVES + " TEXT, " +
                DBTable.CountriesTable.COLUMN_VOISINS + " TEXT, " +
                DBTable.CountriesTable.COLUMN_MONUMENT + " TEXT, " +
                DBTable.CountriesTable.COLUMN_IMGDRAPEAU + " TEXT " +
                ")";
        db.execSQL(SQL_CREATE_COUNTRIES_TABLE);
        // lecture du fichier csv
        InputStream inputStream;
        inputStream = getResources().openRawResource(R.raw.listepays);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        db.beginTransaction();
        try {
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                if(lineNumber==0) {
                    lineNumber++;
                    continue;
                }
                else {
                    String[] colums = line.split(",");
                    ContentValues cv = new ContentValues();
                    cv.put(DBTable.CountriesTable.COLUMN_PAYS, colums[0].trim());
                    cv.put(DBTable.CountriesTable.COLUMN_CAPITAL, colums[1].trim());
                    cv.put(DBTable.CountriesTable.COLUMN_DEVISE, colums[2].trim());
                    cv.put(DBTable.CountriesTable.COLUMN_MONNAIE, colums[3].trim());
                    cv.put(DBTable.CountriesTable.COLUMN_HABITANTS, colums[4].trim());
                    cv.put(DBTable.CountriesTable.COLUMN_FLEUVES, colums[5].trim());
                    cv.put(DBTable.CountriesTable.COLUMN_VOISINS, colums[6].trim());
                    cv.put(DBTable.CountriesTable.COLUMN_MONUMENT, colums[7].trim());
                    cv.put(DBTable.CountriesTable.COLUMN_IMGDRAPEAU, colums[8].trim());
                    db.insert(DBTable.CountriesTable.TABLE_NAME, null, cv);
                    lineNumber++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    // affiche le menu du premier jeu : quizz
    public void game1(View v){
        Intent intent = new Intent(this, game1_Activity.class);
        intent.putExtra("PLAYER_NAME", pseudo); // permet de récupérer le nom du joueur
        startActivity(intent);
    }

    // affiche le menu du second jeu : carte
    public void game2(View v){
        Intent intent = new Intent(this, game2_Activity.class);
        intent.putExtra("PLAYER_NAME", pseudo); // permet de récupérer le nom du joueur
        startActivity(intent);
    }

    // quitte l'application
    public void quit(View v){
        finish();
    }
}
