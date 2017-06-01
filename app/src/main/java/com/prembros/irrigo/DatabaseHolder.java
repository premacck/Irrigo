package com.prembros.irrigo;

/*
 * Created by Prem $ on 4/7/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


class DatabaseHolder {
    private static final String database_name = "Irrigo";
    private static final int database_version = 1;

    private final String symptomList_tableName = "FarmerData";
    private final String selectedSymptoms_tableName = "SelectedSymptoms";
    private final String emergencyNumbers_tableName = "emergencyNumbers";

    private static final String create_table_farmer_data = "create table if not exists FarmerData (Id integer not null, CropName text not null, Sprinkler text not null, area integer not null, );";

    private static DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;


    DatabaseHolder(Context context) {
        this.context = context;
        if (dbHelper == null) {
            dbHelper = new DatabaseHelper(context);
        }
    }

    DatabaseHolder open() {
        db  = dbHelper.getWritableDatabase();
        return this;
    }

    void close() {
        dbHelper.close();
    }

    /*
    *INSERTION / REMOVAL METHODS
     */
    long insertInSymptomListTable(String symptom, String bodyPart, String sex){
        ContentValues content = new ContentValues();
        content.put("Symptom", symptom);
        content.put("BodyPart", bodyPart);
        content.put("Sex", sex);
        return db.insertOrThrow(symptomList_tableName, null, content);
    }

    long insertInEmergencyNumbersTable(String country, String code, String number){
        ContentValues content = new ContentValues();
        content.put("Country", country);
        content.put("Code", code);
        content.put("Number", number);
        return db.insertOrThrow(emergencyNumbers_tableName, null, content);
    }

    long insertInSelectedSymptomsTable(String symptom) throws java.sql.SQLException {
        removeFromSelectedSymptomsTable(symptom);

        ContentValues content = new ContentValues();
        content.put("Symptom", symptom);
        return db.insertOrThrow(selectedSymptoms_tableName, null, content);
    }

    long removeFromSelectedSymptomsTable(String symptom){
        return db.delete(selectedSymptoms_tableName, "Symptom='" + symptom + "'", null);
    }
//
//    void resetSelectedSymptomsTable(){
//        db.execSQL("DROP TABLE IF EXISTS " + selectedSymptoms_tableName);
//        db.execSQL(create_table_selected_symptoms);
//    }

    /*
    * RETRIEVAL METHODS
     */
    Cursor returnSymptoms(String sex, String bodyPart){
        Cursor cursor = null;
        if (sex.equalsIgnoreCase("male")){
            try{
                cursor = db.query(true, symptomList_tableName,
                        new String[]{"Symptom"},
                        "BodyPart = '" + bodyPart + "' AND (Sex = '"+ sex +"' OR sex = 'All')",
                        null, null, null, "Symptom ASC", null);
            }
            catch (SQLiteException e){
                if (e.getMessage().contains("no such table")){
                    Toast.makeText(context, "ERROR: Table doesn't exist!", Toast.LENGTH_SHORT).show();
                    // create table
                    // re-run query, etc.
                } else e.printStackTrace();
            }
        }
        else if (sex.equalsIgnoreCase("female")){
            try{
                cursor = db.query(true, symptomList_tableName,
                        new String[]{"Symptom"},
                        "BodyPart = '" + bodyPart + "' AND (Sex = '"+ sex +"' OR sex = 'All')",
                        null, null, null, "Symptom ASC", null);
            }
            catch (SQLiteException e){
                if (e.getMessage().contains("no such table")){
                    Toast.makeText(context, "ERROR: Table doesn't exist!", Toast.LENGTH_SHORT).show();
                    // create table
                    // re-run query, etc.
                } else e.printStackTrace();
            }
        } else cursor = null;

        return cursor;
    }

    Cursor returnAllSymptoms(){
        Cursor cursor = null;
        try {
            cursor = db.query(true, symptomList_tableName, new String[]{"Symptom"}, null, null, null, null, "Symptom ASC", null);
        } catch (SQLiteException e){
            if (e.getMessage().contains("no such table")){
                Toast.makeText(context, "ERROR: Table doesn't exist", Toast.LENGTH_SHORT).show();
            }
        }
        return cursor;
    }

    Cursor returnEmergencyNumber(String countryCode){
        Cursor cursor = null;
        try{
            cursor = db.query(emergencyNumbers_tableName,
                    new String[]{"Country", "Number"},
                    "Code = '" + countryCode.toUpperCase() + "'",
                    null, null, null, null, null);
        }
        catch (SQLiteException e){
            if (e.getMessage().contains("no such table")){
                Toast.makeText(context, "ERROR: Table doesn't exist!", Toast.LENGTH_SHORT).show();
                // create table
                // re-run query, etc.
            } else e.printStackTrace();
        }
        return cursor;
    }

    Cursor returnSelectedSymptoms(){
        Cursor cursor = null;
        try {
            cursor  = db.query(true, selectedSymptoms_tableName, new String[]{"Symptom"}, null, null, null, null, "Symptom ASC", null);
        } catch (SQLiteException e) {
            if (e.getMessage().contains("no such table")) {
                Toast.makeText(context, "ERROR: Table doesn't exist", Toast.LENGTH_SHORT).show();
            }
        }
        return cursor;
    }

    Cursor isStringAvailableInTable(String item) {
        Cursor cursor = null;
        try {
            cursor = db.query(true, selectedSymptoms_tableName, new String[]{"Symptom"}, "Symptom='" + item + "'", null, null, null, null, null);
        } catch (SQLiteException e) {
            if (e.getMessage().contains("no such table")) {
                Toast.makeText(context, "ERROR: Table doesn't exist", Toast.LENGTH_SHORT).show();
            }
        }
        return cursor;
    }
    /*RESET TABLES METHOD*/
//    public void resetTables(){
//        db.execSQL("DROP TABLE IF EXISTS SymptomList");
//        db.execSQL("DROP TABLE IF EXISTS SelectedSymptoms");
//        db.execSQL("DROP TABLE IF EXISTS emergencyNumbers");
////        db.execSQL("DROP TABLE IF EXISTS Nurse");
////        db.execSQL("DROP TABLE IF EXISTS Hospital");
////        db.execSQL("DROP TABLE IF EXISTS Patient");
////        db.execSQL("DROP TABLE IF EXISTS Ambulance");
//        try{
//            db.execSQL(create_table_farmer_data);
//            db.execSQL(create_table_selected_symptoms);
//            db.execSQL(create_table_emergency_numbers);
////            db.execSQL(create_table_hospital);
////            db.execSQL(create_table_ambulance);
////            db.execSQL(create_table_nurse);
////            db.execSQL(create_table_patient);
//        } catch(SQLException e) {
//            e.printStackTrace();
//        }
//    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

//        private static DatabaseHelper mInstance = null;

        DatabaseHelper(Context context) {
            super(context, database_name, null, database_version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(create_table_farmer_data);
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS FarmerData");
            onCreate(db);
        }
    }
}