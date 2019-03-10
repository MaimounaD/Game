package com.example.game;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class pictures_Activity extends AppCompatActivity {

    private long clickReturnTime;

    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCurrentQuestion;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView[] tabImg;
    private String[] tabChoices;
    private Button buttonNext;

    private List<QuestionQuizz> thePictures;
    private int questionCounter;
    private int cptTotalQuestion;
    private QuestionQuizz questionAnswer;
    private boolean answered;

    // Sharedpreferences pour le score
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor spEditor;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures);

        textViewScore = findViewById(R.id.currentScore);
        textViewQuestionCount = findViewById(R.id.questionLabel);
        textViewCurrentQuestion = findViewById(R.id.currentQuestion);
        img1 = findViewById(R.id.image1);
        img2 = findViewById(R.id.image2);
        img3 = findViewById(R.id.image3);
        img4 = findViewById(R.id.image4);
        tabImg = new ImageView[]{img1,img2,img3,img4};
        tabChoices = new String[]{"","","",""};
        buttonNext = findViewById(R.id.nextQuestion);

        // recuperation des questions dans la bd
        Countries_DbHelper dbHelper = new Countries_DbHelper(this);
        thePictures = new ArrayList<QuestionQuizz>();
        thePictures.clear();
        thePictures = dbHelper.getQuizzPictures();
        Collections.shuffle(thePictures);

        cptTotalQuestion = 10;
        questionCounter = 0;


        // afficher la question suivante
        showNextQuestion();
    }
    // bordure de l'image clickée
    public void selectImage(View view) {
        for (ImageView img : tabImg) {
            img.setPadding(0,0,0,0);
            img.setBackgroundColor(0);
            img.setSelected(false);
        }
        view.setPadding(5,5,5,5);
        view.setBackgroundColor(Color.GRAY);
        view.setSelected(true);
    }

    // action sur le bouton next
    public void goNext(View v) {
        if (!answered) { // réponse pas encore validée
            if (img1.isSelected()|| img2.isSelected() || img3.isSelected() || img4.isSelected()) {
                checkAnswer();
            } else { // aucune image choisie
                Toast toast = Toast.makeText(this, "Veuillez choisir une réponse !", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
        } else { // passer à la question suivante
            for(ImageView img : tabImg) {
                img.setSelected(false);
                img.setClickable(true);
                img.setBackgroundColor(0);
            }
            showNextQuestion();
        }
    }

    // passe à la question suivante
    private void showNextQuestion() {
        if (questionCounter < cptTotalQuestion) {
            questionCounter++;
            textViewQuestionCount.setText("Question : " + questionCounter + "/" + cptTotalQuestion);
            questionAnswer = thePictures.get(questionCounter);
            textViewCurrentQuestion.setText(questionAnswer.getQuestion());
            tabChoices[0] = questionAnswer.getChoice1();
            tabChoices[1] = questionAnswer.getChoice2();
            tabChoices[2] = questionAnswer.getChoice3();
            tabChoices[3] = questionAnswer.getChoice4();
            // chargement de l'url dans l'imageview grace à la librairie Picasso
            for (int i = 0; i < tabImg.length; i++) {
                Picasso.with(this).load("" + tabChoices[i]).resize(150, 75).into(tabImg[i]);
            }
            buttonNext.setText("Valider");
            answered = false;
        } else {
            finishQuizz();
        }
    }

    // vérifie les réponses
    private void checkAnswer() {
        answered = true;
        for (int i = 0; i < tabImg.length; i++) {
            if(tabImg[i].isSelected()) {
                if(tabChoices[i].equals(questionAnswer.getAnswer())) {
                    score++;
                    textViewScore.setText("Score : " + score);
                    Toast toast = Toast.makeText(this, "Bonne réponse", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    tabImg[i].setBackgroundColor(Color.GREEN);
                }
                else {
                    Toast toast = Toast.makeText(this, "Mauvaise réponse", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
            }
        }
        // les images ne sont plus cliquables
        for(ImageView img : tabImg) {
            img.setClickable(false);
        }
        // label du bouton suivant
        if (questionCounter < cptTotalQuestion) {
            buttonNext.setText("Suivant");
        }
        else {
            buttonNext.setText("Fin");
        }
    }

    // finir le quizz
    private void finishQuizz() {
        // pour stocker le score
        sharedpref = getSharedPreferences("quizz_highscore", MODE_PRIVATE);
        int highscore = sharedpref.getInt("quizzScore",0);
        // si c'est le meilleur score, le stocker dans les sharedpreferences
        if (score > 0 && score >= highscore) {
            // ajout du joueur, de son score et de la date dans les sharedpreferences
            String playerName = "Anonyme";
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                playerName = extras.getString("PLAYER_NAME");
            }
            spEditor = sharedpref.edit();
            spEditor.putString("quizzPlayer", playerName);
            spEditor.putInt("quizzScore", score);
            spEditor.putLong("quizzDate", System.currentTimeMillis());
            spEditor.commit();
            // affichage du meilleur score
            Toast.makeText(this, "Bravo ! Meilleur score : " + score + "/" + cptTotalQuestion, Toast.LENGTH_SHORT).show();
        }
        else {
            // affichage du score
            Toast.makeText(this, "Score : " + score + "/" + cptTotalQuestion, Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    // quitte le jeu après un deuxième click sur le bouton retour du tel
    @Override
    public void onBackPressed() {
        if (clickReturnTime + 2000 > System.currentTimeMillis()) {
            finishQuizz();
        } else {
            Toast.makeText(this, "Veuillez recliquer pour quitter", Toast.LENGTH_SHORT).show();
        }
        clickReturnTime = System.currentTimeMillis();
    }

}