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
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;


class DatabaseHolder {
    private static final String database_name = "Irrigo";
    private static final int database_version = 1;

    private final String crop_table = "Crop";
    private final String sprinkler_specs_table = "SprinklerSpecs";
    private final String pipe_sizes_table = "PipeSizes";

    private static final String create_table_farmer_data = "create table if not exists FarmerData (Id integer not null, CropName text not null, Sprinkler text not null, area integer not null);";

    private static final String create_table_crop = "create table if not exists Crop (CropName text not null, DailyWaterRequirement text not null);";

    private static final String create_table_sprinkler_specs = "create table if not exists SprinklerSpecs (SprinklerName text not null, Pressure text not null, NozzleType text not null, WettedDiameter text, Discharge integer);";

    private static final String create_table_pipe_sizes = "create table if not exists PipeSizes (OuterDiameter integer not null, InnerDiameter text not null);";

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
    long insertInCropTable(String cropName, String dailyWaterRequirement){
        ContentValues content = new ContentValues();
        content.put("CropName", cropName);
        content.put("DailyWaterRequirement", dailyWaterRequirement);
        return db.insertOrThrow(crop_table, null, content);
    }

//    long insertInSprinklerNozzle(String sprinklerName, String nozzleType){
//        ContentValues content = new ContentValues();
//        content.put("SprinklerName", sprinklerName);
//        content.put("NozzleType", nozzleType);
//        return db.insertOrThrow(sprinkler_nozzle_table, null, content);
//    }

//    long insertInSprinklerPressure(String sprinklerName, int pressure){
//        ContentValues content = new ContentValues();
//        content.put("SprinklerName", sprinklerName);
//        content.put("Pressure", pressure);
//        return db.insertOrThrow(sprinkler_pressure_table, null, content);
//    }

    long insertInSprinklerSpecs(String sprinklerName, String nozzleType, String pressure, String wettedDiameter, int discharge) {
        ContentValues content = new ContentValues();
        content.put("SprinklerName", sprinklerName);
        content.put("Pressure", pressure);
        content.put("NozzleType", nozzleType);
        content.put("WettedDiameter", wettedDiameter);
        content.put("Discharge", discharge);
        return db.insertOrThrow(sprinkler_specs_table, null, content);
    }

    long insertInPipeSizes(int outerDiameter, String innerDiameter){
        ContentValues content = new ContentValues();
        content.put("OuterDiameter", outerDiameter);
        content.put("InnerDiameter", innerDiameter);
        return db.insertOrThrow(pipe_sizes_table, null, content);
    }

    /*
    * RETRIEVAL METHODS
     */
    String returnDailyWaterRequirement(String cropName){
        Cursor cursor = null;
        String dailyWaterRequirement = null;
        try {
            cursor = db.query(true, crop_table,
                    new String[]{"DailyWaterRequirement"},
                    "CropName = '" + cropName + "'",
                    null, null, null, null, null);
            cursor.moveToFirst();
            dailyWaterRequirement = cursor.getString(cursor.getColumnIndex("DailyWaterRequirement"));
        } catch (Exception e){
            Log.e("ERROR!:", "Returning dailyWaterRequirement: " + e);
        }
        if (cursor != null) {
            cursor.close();
        }
        return dailyWaterRequirement;
    }

    ArrayList<String> returnSprinklerSpecs(String sprinklerName, @Nullable String nozzleType, @Nullable String pressure,
                                           String whichColumn, boolean distinctValue){
//        Columns can be: NozzleType, Pressure, WettedDiameter, Discharge
        ArrayList<String> result = new ArrayList<>();
        Cursor cursor = null;
        try {
            if (nozzleType == null && pressure == null) {
                cursor = db.query(distinctValue, sprinkler_specs_table,                             /*FINDS OUT NOZZLE TYPES*/
                        new String[]{whichColumn},
                        "SprinklerName = '" + sprinklerName + "'",
                        null, null, null, null, null);
            } else if (nozzleType != null && pressure == null){
                cursor = db.query(distinctValue, sprinkler_specs_table,                             /*FINDS OUT PRESSURES*/
                        new String[]{whichColumn},
                        "SprinklerName = '" + sprinklerName + "' AND NozzleType = '" + nozzleType + "'",
                        null, null, null, null, null);
            } else {
                cursor = db.query(distinctValue, sprinkler_specs_table,                             /*FINDS WETTED DIAMETER AND DISCHARGE*/
                        new String[]{whichColumn},
                        "SprinklerName = '" + sprinklerName + "' AND NozzleType = '" + nozzleType + "' AND Pressure = '" + pressure + "'",
                        null, null, null, null, null);
            }
        } catch (SQLiteException e){
            if (e.getMessage().contains("no such table")){
                Toast.makeText(context, "ERROR: Table doesn't exist", Toast.LENGTH_SHORT).show();
            }
        }
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                result.add(cursor.getString(cursor.getColumnIndex(whichColumn)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return result;
    }

    float returnPipeInnerDiameter(int outerDiameter) {
        if (outerDiameter != 0) {
            float innerDiameter = 0;
            Cursor cursor = null;
            try {
                cursor = db.query(true, pipe_sizes_table,
                        new String[]{"InnerDiameter"},
                        "OuterDiameter = '" + outerDiameter + "'",
                        null, null, null, null, null);
            } catch (SQLiteException e) {
                if (e.getMessage().contains("no such table")) {
                    Toast.makeText(context, "ERROR: Table doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }
            if (cursor != null) {
                cursor.moveToFirst();
                innerDiameter = Float.parseFloat(cursor.getString(0));
                cursor.close();
            }
            return innerDiameter;
        } else return 0;
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
                db.execSQL(create_table_crop);
                db.execSQL(create_table_pipe_sizes);
                db.execSQL(create_table_sprinkler_specs);
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