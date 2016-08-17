package com.example.mati.chatappjava8.commons;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.ActorProfile;
import com.google.gson.Gson;

/**
 * Created by mati on 17/08/16.
 */
public class Utils {


    public static ActorProfile getActorProfileFromDisk(Activity context){
        SharedPreferences sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        ActorProfile actorProfile = null;
        if(sharedPref.contains(PreferenceSettingsConstants.MY_PROFILE)){
            String json = sharedPref.getString(PreferenceSettingsConstants.MY_PROFILE, null);
            if (json!=null){
                Gson gson = new Gson();
                actorProfile = gson.fromJson(json, ActorProfile.class);
            }
        }
        return actorProfile;
    }

    public static void saveActorProfileSettings(Activity activity,ActorProfile actorProfile){
        SharedPreferences mPrefs = activity.getSharedPreferences("settings",Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        prefsEditor.putString(PreferenceSettingsConstants.MY_PROFILE, gson.toJson(actorProfile));
        Log.i("Settings save:", String.valueOf(prefsEditor.commit()));
    }

}
