package distinct.digitalsolutions.prabudhh.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSharedPreferences {

    public Context mUserSharedContext;
    public SharedPreferences mUserSharedSharedPreference;
    public SharedPreferences.Editor mUserSharedSharedPreferenceEditor;

    public static final String MyPREFERENCES = "MyPrefs";

    public UserSharedPreferences(Context mUserSharedContext) {

        this.mUserSharedContext = mUserSharedContext;
        mUserSharedSharedPreference = mUserSharedContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mUserSharedSharedPreferenceEditor = mUserSharedSharedPreference.edit();

    }

    public void setUserId(String key,String value){

        mUserSharedSharedPreferenceEditor.putString(key,value);
        mUserSharedSharedPreferenceEditor.commit();
    }

    public String getUserId(String key){

        return mUserSharedSharedPreference.getString(key,"");

    }

    public void isFirstUser(String key,boolean value){

        mUserSharedSharedPreferenceEditor.putBoolean(key,value);
        mUserSharedSharedPreferenceEditor.commit();
    }

    public boolean getIsFirstUser(String key){

        return mUserSharedSharedPreference.getBoolean(key,false);

    }

    public void paidUserStatus(String status,boolean value){

        mUserSharedSharedPreferenceEditor.putBoolean(status,value);
        mUserSharedSharedPreferenceEditor.commit();

    }

    public boolean getPaidUserStatus(String status){

        return mUserSharedSharedPreference.getBoolean(status,false);

    }
}
