package com.example.game;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class questions_Activity extends AppCompatActivity {

    private long clickReturnTime;

    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCurrentQuestion;
    private RadioGroup answerGroup;
    private RadioButton choice1;
    private RadioButton choice2;
    private RadioButton choice3;
    private RadioButton answerSelected;
    private Button buttonNext;

    private List<QuestionQuizz> theQuestions;
    private int questionCounter;
    private int cptTotalQuestion;
    private QuestionQuizz questionAnswer;
    private String currentAnswer;
    private boolean answered;

    // Sharedpreferences pour le score
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor spEditor;
    private int score;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        // debut du quizz
        textViewScore = findViewById(R.id.currentScore);
        textViewQuestionCount = findViewById(R.id.questionLabel);
        textViewCurrentQuestion = findViewById(R.id.currentQuestion);
        answerGroup = findViewById(R.id.answerGroup);
        choice1 = findViewById(R.id.answerGroup1);
        choice2 = findViewById(R.id.answerGroup2);
        choice3 = findViewById(R.id.answerGroup3);
        buttonNext = findViewById(R.id.nextQuestion);
        questionCounter = 0;
        answerSelected = findViewById(answerGroup.getCheckedRadioButtonId());

        // recuperation des questions dans la bd
        Countries_DbHelper dbHelper = new Countries_DbHelper(this);
        theQuestions = new ArrayList<QuestionQuizz>();
        theQuestions.clear();
        theQuestions = dbHelper.getQuizzQuestions();
        Collections.shuffle(theQuestions);
        cptTotalQuestion = 10;

        // afficher la question suivante
        showNextQuestion();
    }

    // action sur le bouton next
    @SuppressLint("NewApi")
    public void goNext(View v) {
        if (!answered) { // réponse pas encore validée
            if (choice1.isChecked() || choice2.isChecked() || choice3.isChecked()) {
                checkAnswer();
            } else { // aucune réponse saisie
                Toast.makeText(this, "Veuillez choisir une réponse !", Toast.LENGTH_SHORT).show();
            }
        } else { // passer à la question suivante
            showNextQuestion();
        }
    }

    // afficher la question suivante au clic du bouton "Next"
    private void showNextQuestion() {
        for (int i = 0; i < answerGroup.getChildCount(); i++) {
            answerGroup.getChildAt(i).setEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            choice1.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
            choice2.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
            choice3.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
        }
        answerGroup.clearCheck();

        if (questionCounter < cptTotalQuestion) {
            questionCounter++;
            textViewQuestionCount.setText("Question : " + questionCounter + "/" + cptTotalQuestion);
            questionAnswer = theQuestions.get(questionCounter);
            textViewCurrentQuestion.setText(questionAnswer.getQuestion());
            choice1.setText(questionAnswer.getChoice1());
            choice2.setText(questionAnswer.getChoice2());
            choice3.setText(questionAnswer.getChoice3());
            buttonNext.setText("Valider");
            answered = false;
        } else {
            finishQuizz();
        }
    }

    // vérifier si la réponse saisie est correcte
    private void checkAnswer() {
        answered = true;
        RadioButton answerSelected = findViewById(answerGroup.getCheckedRadioButtonId());
        String selectedStr = answerSelected.getText().toString();
        if (selectedStr.equals(questionAnswer.getAnswer())) {
            score++;
            textViewScore.setText("Score : " + score);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                answerSelected.setButtonTintList(ColorStateList.valueOf(Color.GREEN));
            }
            Toast.makeText(this, "Bonne réponse !", Toast.LENGTH_SHORT).show();
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                answerSelected.setButtonTintList(ColorStateList.valueOf(Color.RED));
            }
            Toast.makeText(this, "La bonne réponse est : " + questionAnswer.getAnswer(), Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < answerGroup.getChildCount(); i++) {
            answerGroup.getChildAt(i).setEnabled(false);
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
