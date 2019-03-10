package com.example.game;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ImageView;

import com.example.game.DBTable.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Countries_DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "GeoQuizz.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;
    private ImageView iv;

    public Countries_DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

    }

    // mise à jour de la bd
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CountriesTable.TABLE_NAME);
        onCreate(db);
    }

    // GAME 1 : liste de questions pour le quizz
    public List<QuestionQuizz> getQuizzQuestions() {
        // arraylist pour stocker la question, les choix et la réponse
        List<QuestionQuizz> questionList = new ArrayList<>();
        questionList.clear();
        // accés à la bd et sélection de tous les éléments de la table tablePays
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CountriesTable.TABLE_NAME, null);
        String question = "", answer = "";
        String choice1 = "", choice2 = "", choice3 = "";
        String[] tabChoice;
        QuestionQuizz questionAnswer;
        // au moins 3 lignes dans la table tablePays
        if (c.getCount() >= 3) {
            c.moveToFirst();
            do {
                // ajout d'un article (du, de la, de l') pour la question en fonction du pays
                String article = "";
                if(c.getString(c.getColumnIndex(CountriesTable.COLUMN_PAYS)).toLowerCase().startsWith("le ")) {
                    article += "du";
                }
                else if (c.getString(c.getColumnIndex(CountriesTable.COLUMN_PAYS)).toLowerCase().startsWith("la ")) {
                    article += "de la";
                }
                else {
                    article += "de l'";
                }
                // Parcours des colonnes pour formuler les questions
                String countryName = c.getString(c.getColumnIndex(CountriesTable.COLUMN_PAYS));
                for (int i = 2; i <= 8; i++) { // colonnes capital -> monument
                    switch (i) {
                        case 2 :    question = "Quelle est la capitale " + article + countryName.substring(2) + " ? ";
                                    break;
                        case 3 :    question = "Quelle est la devise " + article + countryName.substring(2) + " ? ";
                                    break;
                        case 4 :    question = "Quelle est la monnaie "  + article + countryName.substring(2) + " ? ";
                                    break;
                        case 5 :    question = "Quelle est le nombre d'habitants " + article + countryName.substring(2) + " ? ";
                                    break;
                        case 6 :    question = "Quels sont les fleuves "  + article + countryName.substring(2) + " ? ";
                                    break;
                        case 7 :    question = "Quels sont les voisins "  + article + countryName.substring(2) + " ? ";
                                    break;
                        case 8 :    question = "Quelle est l'un des monuments emblématiques "  + article + countryName.substring(2) + " ? ";
                                    break;
                    }

                    // stockage de la bonne réponse
                    answer = c.getString(c.getColumnIndex(c.getColumnName(i)));
                    // 2 autres réponses aléatoires selectionnées dans la bd
                    Cursor rand = db.rawQuery("SELECT * FROM ( SELECT DISTINCT " + c.getColumnName(i) + " AS res FROM " + CountriesTable.TABLE_NAME +
                            " WHERE " + c.getColumnName(i) + " <>  \"" + answer + "\") ORDER BY RANDOM() LIMIT 2", null);
                    rand.moveToFirst();
                    if(i == 7 || i == 8) { // cas des fleuves et des voisins
                        // remplacement du '/' par ', '
                        choice1 = rand.getString(rand.getColumnIndex("res")).replaceAll("/",", ");
                        rand.moveToNext();
                        choice2 = rand.getString(rand.getColumnIndex("res")).replaceAll("/", ", ");
                        rand.close();
                        choice3 = answer.replaceAll("/", ", ");
                        answer = choice3;
                    }
                    else {
                        choice1 = rand.getString(rand.getColumnIndex("res"));
                        rand.moveToNext();
                        choice2 = rand.getString(rand.getColumnIndex("res"));
                        rand.close();
                        choice3 = answer;
                    }

                    // changement de position des choix
                    tabChoice = new String[] {choice1, choice2, choice3};
                    String[] tabC = RandomizeArray(tabChoice);

                    // formulation et ajout de la question à l'arraylist questionList
                    questionAnswer = new QuestionQuizz();
                    questionAnswer.setQuestion(question);
                    questionAnswer.setChoice1(tabC[0]);
                    questionAnswer.setChoice2(tabC[1]);
                    questionAnswer.setChoice3(tabC[2]);
                    questionAnswer.setAnswer(answer);
                    questionList.add(questionAnswer);

                }

            } while(c.moveToNext());
        }
        c.close();
        db.close();
        return questionList;
    }

    // GAME 1 : liste des questions pour la réponse par clic sur l'image : drapeaux et monuments
    public List<QuestionQuizz> getQuizzPictures() {
        // pour stocker la question et les liens des images
        List<QuestionQuizz> picturesList = new ArrayList<>();
        picturesList.clear();
        String question = "", answer = "";
        String img1 = "", img2 = "", img3 = "";
        String[] tabChoice;
        QuestionQuizz questionAnswer;

        // accés à la bd et sélection des colonnes ImageMonument et ImageDrapeau de la table TablePays
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + CountriesTable.COLUMN_PAYS + ", " + CountriesTable.COLUMN_IMGDRAPEAU + " FROM " + CountriesTable.TABLE_NAME, null);
        // contient au moins 4 lignes
        if (c.getCount() >= 4) {
            c.moveToFirst();
            do {
                // ajout d'un article (du, de la, de l') pour la question en fonction du pays
                String article = "";
                if(c.getString(c.getColumnIndex(CountriesTable.COLUMN_PAYS)).toLowerCase().startsWith("le ")) {
                    article += "du";
                }
                else if (c.getString(c.getColumnIndex(CountriesTable.COLUMN_PAYS)).toLowerCase().startsWith("la ")) {
                    article += "de la";
                }
                else {
                    article += "de l'";
                }
                // Parcours des colonnes pour formuler les questions
                String countryName = c.getString(c.getColumnIndex(CountriesTable.COLUMN_PAYS));
                img1 = ""; img2 = ""; img3 = ""; answer="";
                question = "Veuillez sélectionner le drapeau " + article + countryName.substring(2) + ".";
                // bonne réponse
                answer = c.getString(c.getColumnIndex(c.getColumnName(1)));
                // 3 autres réponses aléatoires selectionnées dans la bd
                Cursor rand = db.rawQuery("SELECT * FROM ( SELECT DISTINCT " + c.getColumnName(1) + " AS res FROM " + CountriesTable.TABLE_NAME +
                        " WHERE " + c.getColumnName(1) + " <>  \"" + answer + "\") ORDER BY RANDOM() LIMIT 3", null);
                rand.moveToFirst();
                img1 = rand.getString(rand.getColumnIndex("res"));
                rand.moveToNext();
                img2 = rand.getString(rand.getColumnIndex("res"));
                rand.moveToLast();
                img3 = rand.getString(rand.getColumnIndex("res"));

                tabChoice = new String[] {img1, img2, img3, answer};
                String[] tabC = RandomizeArray(tabChoice); // changement alea des positions
                // ajout de la question à l'arraylist picturesList
                questionAnswer = new QuestionQuizz();
                questionAnswer.setQuestion(question);
                questionAnswer.setChoice1(tabC[0]);
                questionAnswer.setChoice2(tabC[1]);
                questionAnswer.setChoice3(tabC[2]);
                questionAnswer.setChoice4(tabC[3]);
                questionAnswer.setAnswer(answer);
                picturesList.add(questionAnswer);

            } while(c.moveToNext());
        }
        c.close();
        db.close();
        return picturesList;
    }

    // mélange les éléments d'un tableau de chaine de caracteres
    public static String[] RandomizeArray(String[] array){
        Random rgen = new Random();  // Random number generator

        for (int i=0; i<array.length; i++) {
            int randomPosition = rgen.nextInt(array.length);
            String temp = array[i];
            array[i] = array[randomPosition];
            array[randomPosition] = temp;
        }
        return array;
    }

    // GAME 2 : liste des questions pour la carte
    public List<String> getMapQuestions() {
        List<String> questionList = new ArrayList<>();
        // accés à la bd
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + CountriesTable.COLUMN_CAPITAL + " FROM " + CountriesTable.TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                // question sur la capitale
                String questionCap = "Où se trouve "  + c.getString(c.getColumnIndex(CountriesTable.COLUMN_CAPITAL)) + " ? ";
                questionList.add(questionCap);
            } while (c.moveToNext());
        }
        c.close();
        db.close();

        return questionList;

    }

}
