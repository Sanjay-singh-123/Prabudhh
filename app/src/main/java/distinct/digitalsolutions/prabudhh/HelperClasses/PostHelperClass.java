package distinct.digitalsolutions.prabudhh.HelperClasses;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;

import distinct.digitalsolutions.prabudhh.Activities.MainActivity;
import distinct.digitalsolutions.prabudhh.Database.FirebaseDatabaseClass;
import distinct.digitalsolutions.prabudhh.Interfaces.LoginInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.SongPostFirebaseInterface;
import distinct.digitalsolutions.prabudhh.R;
import distinct.digitalsolutions.prabudhh.SharedPreference.UserSharedPreferences;

public class PostHelperClass implements LoginInterface {

    private EditText mPostSongEditText;
    private Button mPostSongButton;
    private ImageView mPostBackButton;

    private View mRootView;
    private Activity mContext;

    private FirebaseDatabaseClass mPostHelperDatabase;
    private UserSharedPreferences mPostUserSharedPreference;

    private ProgressBar progressBar ;

   private RelativeLayout mProgressBarRelativeLayout;

    public PostHelperClass(Activity context, ViewGroup viewGroup){

        mRootView = LayoutInflater.from(context).inflate(R.layout.activity_post,viewGroup);
        this.mContext = context;

        mPostHelperDatabase = new FirebaseDatabaseClass();

        mPostUserSharedPreference = new UserSharedPreferences(mContext);

    }


    @Override
    public void initView() {

        mPostSongEditText = mRootView.findViewById(R.id.song_edit_text);
        mPostSongButton = mRootView.findViewById(R.id.post_layout);
        mPostBackButton = mRootView.findViewById(R.id.settings_icon);
        progressBar = mRootView.findViewById(R.id.progress_bar);
        mProgressBarRelativeLayout = mRootView.findViewById(R.id.progress_bar_layout);

        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);

        mPostBackButton.setOnClickListener(v->{

            BackMethod();

        });

        mPostSongButton.setOnClickListener(v -> {

            String song = mPostSongEditText.getText().toString();
            String userId = mPostUserSharedPreference.getUserId("user_id");

            if (TextUtils.isEmpty(song) || song.isEmpty()){

                return;
            }

            mProgressBarRelativeLayout.setVisibility(View.VISIBLE);

            mPostHelperDatabase.postSongMethod(song,userId, new SongPostFirebaseInterface() {
                @Override
                public void onSuccess(String success,String date,String expiry_date) {

                    Toast.makeText(mContext, "Successfully Posted", Toast.LENGTH_SHORT).show();

                    Intent postIntent = new Intent(mContext, MainActivity.class);
                    postIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(postIntent);
                    mContext.finish();

                    mProgressBarRelativeLayout.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(String error) {

                    Toast.makeText(mContext, "Try again later.", Toast.LENGTH_SHORT).show();
                    mProgressBarRelativeLayout.setVisibility(View.GONE);
                }
            });

        });


    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    public void BackMethod(){

        Intent intent = new Intent(mContext,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

    }
}
