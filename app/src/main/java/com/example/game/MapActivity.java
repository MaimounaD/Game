package com.example.game;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Countries_DbHelper dbHelper;
    private Button valid;
    private Button tests;
    private Button newQuestion;
    private TextView distance;
    private TextView position;
    private TextView scoreJoueur;
    private SupportMapFragment mapFragment;
    private List<String> questionList;
    private double latitude2;
    private double longitude2;
    private double latitude1 = 0.0;
    private double longitude1 = 0.0;
    int score = 0;
    private int cptquestion = 0;

    private long clickReturnTime;

    // Sharedpreferences pour le score
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor spEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // récupère le SupportMapFragment et notifie quand le map est prêt a être utilisé
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        distance = findViewById(R.id.distance);
        position = findViewById(R.id.questPosition);
        valid = findViewById(R.id.valid);
        tests = findViewById(R.id.tests);
        newQuestion = findViewById(R.id.newQuestion);
        scoreJoueur = findViewById(R.id.currentScore);

        // liste des questions
        dbHelper = new Countries_DbHelper(this);
        questionList = dbHelper.getMapQuestions();
        Collections.shuffle(questionList);
    }

    // manipulation de la map une fois qu'elle est disponible
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        cordVille();
        newQuestion.setEnabled(false);
        gestionMap(cptquestion);

    }

    // calcul de la distance entre la ville demandée et la zone cliquée
    public void distance(View v) {

       double dist = tests(v);
        //Ajoute un Marker à la position de la ville
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).
                title("Vous êtes ici" + " Latitude : " + latitude1 + " | Longitude : " + longitude1));
        //Tracer une ligne entre la position de la ville et la position cliquée
        mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(latitude1, longitude1), new LatLng(latitude2, longitude2))
                .width(5)
                .color(Color.RED));
        // affectation du score en fonction de la distance
        if (dist <= 5)
            score += 8;
        else if (dist > 5 && dist <= 10)
            score += 5;
        else if (dist > 10 && dist <= 20)
            score += 2;

        scoreJoueur.setText("Score: " + score);
        //Désactivation des boutons valid et tests
        valid.setEnabled(false);
        tests.setEnabled(false);
        newQuestion.setEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mapFragment.getView().setClickable(false);
            }
        });

    }

    // clic sur le bouton Tests
    public double tests(View v) {
        //Ajouter marker
        float[] results = new float[1];
        Location.distanceBetween(latitude1, longitude1,
                latitude2, longitude2, results);
        double distanceKm = results[0] / 1000;
        //Affiche la distance avec deux chiffres après la virgule
        DecimalFormat df = new DecimalFormat("#0.00");
        distance.setText("Distance : " + df.format(distanceKm) + " km");
         return distanceKm;
    }

    //Fonction pour générer une nouvelle question
    public void newQuestion(View v) {
        if(cptquestion < questionList.size()) {
            cptquestion++;
            mMap.clear();
            cordVille();
            gestionMap(cptquestion);
            distance.setText("...");
            newQuestion.setEnabled(false);
        }
        else{
            Toast.makeText(this, "Jeu terminé ! ", Toast.LENGTH_SHORT).show();
        }

    }

    // fin du jeu de map
    public void finishMap(View view) {
        // pour stocker le score
        sharedpref = getSharedPreferences("map_highscore", MODE_PRIVATE);
        int highscore = sharedpref.getInt("mapScore", 0);
        // si c'est le meilleur score, le stocker dans les sharedpreferences
        if (score > highscore) {
            // ajout du joueur, de son score et de la date dans les sharedpreferences
            String playerName2 = "Anonyme";
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                playerName2 = extras.getString("PLAYER_NAME");
            }
            spEditor = sharedpref.edit();
            spEditor.putString("mapPlayer", playerName2);
            spEditor.putInt("mapScore", score);
            Date date = new Date();
            spEditor.putLong("mapDate", System.currentTimeMillis());
            spEditor.commit();
            // affichage du meilleur score
            Toast.makeText(this, "Bravo ! Meilleur score : " + score, Toast.LENGTH_SHORT).show();
        }
        else {
            // affichage du score
            Toast.makeText(this, "Score : " + score, Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    // fonction permettant de récupérer les coordonnées d'une ville via le nom
    public void cordVille() {
        if (Geocoder.isPresent()) {
            try {
                Geocoder gc = new Geocoder(this);
                String c = questionList.get(cptquestion);
                List<Address> listAdr = gc.getFromLocationName(c, 5); //Prend en param le nom de la ville et récupère une adresse
                for (Address a : listAdr) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        latitude1 = a.getLatitude();
                        longitude1 = a.getLongitude();
                    }
                }
            } catch (IOException e) {

            }
        }
    }

    // gère les événements de la map
    public void gestionMap(int cpt) {
        LatLng ici = new LatLng(latitude1 + 10, longitude1 + 10);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ici));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        position.setText(questionList.get(cpt));
        //Désactivation des boutons tests et valid
        valid.setEnabled(false);
        tests.setEnabled(false);
        // gestion du click sur le map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //Activation du boutons
                valid.setEnabled(true);
                tests.setEnabled(true);
                // création d'un marker
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(latLng.latitude + " : " + latLng.longitude);
                // efface les positions des clics précédents
                mMap.clear();
                // animation sur la position cliquée
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                // placement d'un marqueur sur la position touchée
                mMap.addMarker(markerOptions);
                latitude2 = latLng.latitude;
                longitude2 = latLng.longitude;
                mMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(50000)
                        .strokeColor(Color.RED)
                        .fillColor(Color.argb(70, 50, 50, 150))
                        .strokeWidth(2));
            }
        });
    }

    // quitte le jeu après un deuxième click sur le bouton retour du tel
    @Override
    public void onBackPressed() {
        if (clickReturnTime + 2000 > System.currentTimeMillis()) {
            finishMap(findViewById(R.id.finish));
        } else {
            Toast.makeText(this, "Veuillez recliquer pour quitter", Toast.LENGTH_SHORT).show();
        }
        clickReturnTime = System.currentTimeMillis();
    }
}