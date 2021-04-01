package distinct.digitalsolutions.prabudhh.HelperClasses;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class ProgressBarClass {

    private Activity mActivity;

    public ProgressBarClass(Activity activity){

        this.mActivity = activity;
    }

    public void setProgressBarVisible(RelativeLayout mProgressBarRelativeLayout){

        mProgressBarRelativeLayout.setVisibility(View.VISIBLE);

        mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    public void setProgressBarNotVisible(RelativeLayout mProgressBarRelativeLayout){

        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mProgressBarRelativeLayout.setVisibility(View.GONE);


    }
}
