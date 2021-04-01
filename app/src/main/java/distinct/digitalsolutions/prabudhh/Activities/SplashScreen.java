package distinct.digitalsolutions.prabudhh.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import distinct.digitalsolutions.prabudhh.HelperClasses.SplashScreenHelperClass;

public class SplashScreen extends AppCompatActivity {


    private SplashScreenHelperClass mSplashScreenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSplashScreenHelper = new SplashScreenHelperClass(SplashScreen.this,null);
        setContentView(mSplashScreenHelper.getRootView());


    }

    @Override
    public void onStart(){
        super.onStart();

        mSplashScreenHelper.checkForUserStatus();

    }
}