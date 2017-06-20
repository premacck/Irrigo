package com.prembros.irrigo;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

/**
 *
 * Created by Prem $ on 6/19/2017.
 */

class InsertInDatabase extends AsyncTask<Void, Void, Void> {

    private Context applicationContext;
    private Context context;

    InsertInDatabase(Context applicationContext, Context context) {
        this.applicationContext = applicationContext;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        //  If the activity has never started before
        if (PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("firstStart", true)) {
            final DatabaseHolder db = new DatabaseHolder(context);
            db.open();
            db.insertInCropTable("Onion", "1");
            db.insertInCropTable("Pea", "2");
            db.insertInCropTable("Cabbage", "3");
            db.insertInCropTable("Chilli", "4");
            db.insertInCropTable("Cauliflower", "5");
            db.insertInCropTable("Brinjal", "6");
            db.insertInCropTable("Potato", "7");
            db.insertInCropTable("Tomato", "8");
            db.insertInCropTable("Wheat", "9");
            db.insertInCropTable("Green Gram", "10");
            db.insertInCropTable("Groundnut", "11");
            db.insertInCropTable("Mustard", "12");
            db.insertInCropTable("Cotton", "13");
            db.insertInCropTable("Sugarcane", "14");
            db.insertInCropTable("Maize", "15");
            db.insertInCropTable("Mango", "16");
            db.insertInCropTable("Coconut", "17");
            db.insertInCropTable("Cashew", "18");

            db.insertInSprinklerSpecs("Micro", "Black", "1", "2.5", 18);
            db.insertInSprinklerSpecs("Micro", "Black", "1.5", "2.9", 20);
            db.insertInSprinklerSpecs("Micro", "Black", "2", "3.4", 25);
            db.insertInSprinklerSpecs("Micro", "Black", "2.5", "3.9", 29);
            db.insertInSprinklerSpecs("Micro", "Black", "3", "4.2", 28);
            db.insertInSprinklerSpecs("Micro", "Blue", "1", "3.5", 29);
            db.insertInSprinklerSpecs("Micro", "Blue", "1.5", "3.8", 35);
            db.insertInSprinklerSpecs("Micro", "Blue", "2", "4.5", 40);
            db.insertInSprinklerSpecs("Micro", "Blue", "2.5", "4.5", 45);
            db.insertInSprinklerSpecs("Micro", "Blue", "3", "4.8", 50);
            db.insertInSprinklerSpecs("Micro", "Green", "", "", 45);
            db.insertInSprinklerSpecs("Micro", "Green", "", "", 59);
            db.insertInSprinklerSpecs("Micro", "Green", "", "", 65);
            db.insertInSprinklerSpecs("Micro", "Green", "", "", 75);
            db.insertInSprinklerSpecs("Micro", "Green", "", "", 80);
            db.insertInSprinklerSpecs("Micro", "Red", "", "", 90);
            db.insertInSprinklerSpecs("Micro", "Red", "", "", 110);
            db.insertInSprinklerSpecs("Micro", "Red", "", "", 120);
            db.insertInSprinklerSpecs("Micro", "Red", "", "", 130);
            db.insertInSprinklerSpecs("Micro", "Red", "", "", 140);
            db.insertInSprinklerSpecs("Micro", "White", "", "", 0);
            db.insertInSprinklerSpecs("Micro", "White", "", "", 0);
            db.insertInSprinklerSpecs("Micro", "White", "", "", 0);
            db.insertInSprinklerSpecs("Micro", "White", "", "", 0);
            db.insertInSprinklerSpecs("Micro", "White", "", "", 0);
            db.insertInSprinklerSpecs("Micro", "Orange", "", "", 0);
            db.insertInSprinklerSpecs("Micro", "Orange", "", "", 0);
            db.insertInSprinklerSpecs("Micro", "Orange", "", "", 0);
            db.insertInSprinklerSpecs("Micro", "Orange", "", "", 0);
            db.insertInSprinklerSpecs("Micro", "Orange", "", "", 0);
            db.insertInSprinklerSpecs("Micro", "Yellow", "", "", 0);
            db.insertInSprinklerSpecs("Micro", "Yellow", "", "", 0);
            db.insertInSprinklerSpecs("Micro", "Yellow", "", "", 0);
            db.insertInSprinklerSpecs("Micro", "Yellow", "", "", 0);
            db.insertInSprinklerSpecs("Micro", "Yellow", "", "", 0);
            db.insertInSprinklerSpecs("Mini", "Silver", "2", "11", 460);
            db.insertInSprinklerSpecs("Mini", "Silver", "2.5", "12", 520);
            db.insertInSprinklerSpecs("Mini", "Silver", "3", "14", 570);
            db.insertInSprinklerSpecs("Mini", "Silver", "3.5", "18", 600);
            db.insertInSprinklerSpecs("Mini", "Silver", "4", "25", 650);
            db.insertInSprinklerSpecs("Mini", "Yellow", "2", "10", 520);
            db.insertInSprinklerSpecs("Mini", "Yellow", "2.5", "13", 520);
            db.insertInSprinklerSpecs("Mini", "Yellow", "3", "15", 565);
            db.insertInSprinklerSpecs("Mini", "Yellow", "3.5", "18", 610);
            db.insertInSprinklerSpecs("Mini", "Yellow", "4", "23", 663);
            db.insertInSprinklerSpecs("Mini", "Purple", "2", "13", 510);
            db.insertInSprinklerSpecs("Mini", "Purple", "2.5", "12", 540);
            db.insertInSprinklerSpecs("Mini", "Purple", "3", "13", 600);
            db.insertInSprinklerSpecs("Mini", "Purple", "3.5", "18", 640);
            db.insertInSprinklerSpecs("Mini", "Purple", "4", "25", 695);
            db.insertInSprinklerSpecs("Mini", "Orange", "2", "12", 630);
            db.insertInSprinklerSpecs("Mini", "Orange", "2.5", "13", 710);
            db.insertInSprinklerSpecs("Mini", "Orange", "3", "15", 800);
            db.insertInSprinklerSpecs("Mini", "Orange", "3.5", "18", 840);
            db.insertInSprinklerSpecs("Mini", "Orange", "4", "28", 870);
            db.insertInSprinklerSpecs("Rail Gun", "8 mm", "2", "20", 5620);
            db.insertInSprinklerSpecs("Rail Gun", "8 mm", "3", "23", 6520);
            db.insertInSprinklerSpecs("Rail Gun", "8 mm", "4", "25", 7520);
            db.insertInSprinklerSpecs("Rail Gun", "8 mm", "5", "27", 7800);
            db.insertInSprinklerSpecs("Rail Gun", "8 mm", "6", "", 7950);
            db.insertInSprinklerSpecs("Rail Gun", "10 mm", "2", "22", 6804);
            db.insertInSprinklerSpecs("Rail Gun", "10 mm", "3", "25", 8316);
            db.insertInSprinklerSpecs("Rail Gun", "10 mm", "4", "28", 9215);
            db.insertInSprinklerSpecs("Rail Gun", "10 mm", "5", "30", 10728);
            db.insertInSprinklerSpecs("Rail Gun", "10 mm", "6", "", 10125);
            db.insertInSprinklerSpecs("Rail Gun", "12 mm", "2", "24", 11872);
            db.insertInSprinklerSpecs("Rail Gun", "12 mm", "3", "28", 15420);
            db.insertInSprinklerSpecs("Rail Gun", "12 mm", "4", "30", 16785);
            db.insertInSprinklerSpecs("Rail Gun", "12 mm", "5", "32", 18685);
            db.insertInSprinklerSpecs("Rail Gun", "12 mm", "6", "35", 21210);
            db.insertInSprinklerSpecs("Rail Gun", "14 mm", "2", "", 0);
            db.insertInSprinklerSpecs("Rail Gun", "14 mm", "3", "", 0);
            db.insertInSprinklerSpecs("Rail Gun", "14 mm", "4", "", 0);
            db.insertInSprinklerSpecs("Rail Gun", "14 mm", "5", "", 0);
            db.insertInSprinklerSpecs("Rail Gun", "14 mm", "6", "", 0);
            db.insertInSprinklerSpecs("Rail Gun", "16 mm", "2", "", 0);
            db.insertInSprinklerSpecs("Rail Gun", "16 mm", "3", "", 0);
            db.insertInSprinklerSpecs("Rail Gun", "16 mm", "4", "", 0);
            db.insertInSprinklerSpecs("Rail Gun", "16 mm", "5", "", 0);
            db.insertInSprinklerSpecs("Rail Gun", "16 mm", "6", "", 0);

            db.insertInPipeSizes(12, "11.4");
            db.insertInPipeSizes(16, "15.4");
            db.insertInPipeSizes(20, "19.2");
            db.insertInPipeSizes(25, "24.1");
            db.insertInPipeSizes(32, "30.8");
            db.insertInPipeSizes(40, "38.2");
            db.insertInPipeSizes(50, "47.9");
            db.insertInPipeSizes(63, "61.1");
            db.insertInPipeSizes(75, "72.8");
            db.insertInPipeSizes(90, "87.4");
            db.insertInPipeSizes(110, "107");
            db.insertInPipeSizes(125, "121.6");
            db.insertInPipeSizes(140, "136.2");
            db.insertInPipeSizes(160, "155.7");
            db.insertInPipeSizes(180, "175");
            db.insertInPipeSizes(200, "194.7");
            db.insertInPipeSizes(225, "219");
            db.insertInPipeSizes(250, "243.5");
            db.insertInPipeSizes(280, "272.6");
            db.insertInPipeSizes(315, "306.7");

            db.close();
        }
        return null;
    }
}
