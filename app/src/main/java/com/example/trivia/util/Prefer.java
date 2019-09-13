package com.example.trivia.util;

import android.app.Activity;
import android.content.SharedPreferences;

public class Prefer {
    private SharedPreferences preferences;

    public Prefer(Activity activity) {                                                   // نريده يوبط بالاكتفتي(contect)
        this.preferences = activity.getPreferences(activity.MODE_PRIVATE);     //هذا البرافنسس = من الاكتفتي حيب البرافنسس (اي دي ... بدافيت مود)
    }


    public void saveHighScore(int score) {
        int currentscore = score;

        int lastScore = preferences.getInt("highscore", 0);                               //برفنسس حيب انت كي وقيمه
        if (currentscore > lastScore) {
            preferences.edit().putInt("highscore", currentscore).apply();
        }

    }
    public int getHighScore(){
        return preferences.getInt("highscore",0);




    }







    public void setState(int index){
        preferences.edit().putInt("index_Stat",index).apply();
    }


    public int getStat(){
        return preferences.getInt("index_Stat",0);
    }


}