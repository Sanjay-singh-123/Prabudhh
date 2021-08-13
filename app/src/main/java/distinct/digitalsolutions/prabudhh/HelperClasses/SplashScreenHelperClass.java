package distinct.digitalsolutions.prabudhh.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import distinct.digitalsolutions.prabudhh.Authentication.LoginActivity;
import distinct.digitalsolutions.prabudhh.Interfaces.SplashScreenInterface;
import distinct.digitalsolutions.prabudhh.Activities.MainActivity;
import distinct.digitalsolutions.prabudhh.R;
import distinct.digitalsolutions.prabudhh.SharedPreference.UserSharedPreferences;

public class SplashScreenHelperClass implements SplashScreenInterface {

    private final int mSplashScreenTimeStamp = 1;

    private UserSharedPreferences mUserSharedPreference;

    private Context context;

    private View mRootView;

    public SplashScreenHelperClass(Context context, ViewGroup viewGroup){

        mRootView = LayoutInflater.from(context).inflate(R.layout.activity_splash_screen,viewGroup);

        this.context = context;
        mUserSharedPreference = new UserSharedPreferences(context);

    }

    @Override
    public View getRootView() {

        return mRootView;

    }

    @Override
    public void checkForUserStatus() {

        SplashIntent splashIntent = new SplashIntent();
        splashIntent.start();

    }

    public class SplashIntent extends Thread {

        @Override
        public void run() {

            try {

                Thread.sleep(mSplashScreenTimeStamp * 4000);

            } catch (Exception e) {

                Log.d("Splash_Screen","Failed");

            } finally {

                String sharedUserId = mUserSharedPreference.getUserId("user_id");

                Intent mMainActivityIntent;

                if (sharedUserId.equalsIgnoreCase("")){

                    mMainActivityIntent = new Intent(context, LoginActivity.class);

                }else {

                    mMainActivityIntent = new Intent(context, MainActivity.class);

                }


                mMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mMainActivityIntent);

            }
        }
    }

}
