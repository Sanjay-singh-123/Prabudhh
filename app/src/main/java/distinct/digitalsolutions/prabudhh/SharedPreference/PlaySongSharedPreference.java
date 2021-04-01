package distinct.digitalsolutions.prabudhh.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

public class PlaySongSharedPreference {

    public Context mSaveMerchantDetailsContext;
    public SharedPreferences mSaveMerchantSharedPreferences;
    public SharedPreferences.Editor mSaveMerchantSharedPreferencesEditor;

    public static final String MyPREFERENCES = "MyPrefs";

    public PlaySongSharedPreference(Context mSaveMerchantDetailsContext) {

        this.mSaveMerchantDetailsContext = mSaveMerchantDetailsContext;
        mSaveMerchantSharedPreferences = mSaveMerchantDetailsContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mSaveMerchantSharedPreferencesEditor = mSaveMerchantSharedPreferences.edit();

    }

    public void isSongPlaying(String key, boolean value) {

        mSaveMerchantSharedPreferencesEditor.putBoolean(key, value);
        mSaveMerchantSharedPreferencesEditor.commit();
    }

    public boolean getIsSongPlaying(String key){

        boolean isSongPlaying = mSaveMerchantSharedPreferences.getBoolean(key,false);
        return isSongPlaying;
    }

    public String getPlayingSongDetails(String key) {

        String  songDetails = mSaveMerchantSharedPreferences.getString(key,null);
        return songDetails;
    }

    public void setPlayingSongDetails(String key, String  value) {

        mSaveMerchantSharedPreferencesEditor.putString(key, value);
        mSaveMerchantSharedPreferencesEditor.commit();
    }

    public String getAllSongsList(String key) {

        String allSongs = mSaveMerchantSharedPreferences.getString(key, null);
        return allSongs;
    }

    public void setAllSongsList(String key, String  value) {

        mSaveMerchantSharedPreferencesEditor.putString(key, value);
        mSaveMerchantSharedPreferencesEditor.commit();
    }

    public String getSongPlayedValue(String key) {

        String songValue = mSaveMerchantSharedPreferences.getString(key, "0");
        return songValue;
    }

    public void setSongPlayedValue(String key, String  value) {

        mSaveMerchantSharedPreferencesEditor.putString(key, value);
        mSaveMerchantSharedPreferencesEditor.commit();
    }


}
