package com.example.game;
import android.provider.BaseColumns;

public final class DBTable {
    private DBTable() {
    }

    // structure de la table tablePays
    public static class CountriesTable implements BaseColumns {
        public static final String TABLE_NAME = "tablePays";
        public static final String COLUMN_PAYS = "pays";
        public static final String COLUMN_CAPITAL = "capital";
        public static final String COLUMN_DEVISE = "devise";
        public static final String COLUMN_MONNAIE = "monnaie";
        public static final String COLUMN_HABITANTS = "habitants";
        public static final String COLUMN_FLEUVES = "fleuves";
        public static final String COLUMN_VOISINS = "voisins";
        public static final String COLUMN_MONUMENT = "monument";
        public static final String COLUMN_IMGDRAPEAU = "imgdrapeau";
    }

}
