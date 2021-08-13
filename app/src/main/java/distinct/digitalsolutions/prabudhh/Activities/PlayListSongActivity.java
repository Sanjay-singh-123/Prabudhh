package distinct.digitalsolutions.prabudhh.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import distinct.digitalsolutions.prabudhh.HelperClasses.PlayListHelperClass;
import distinct.digitalsolutions.prabudhh.Interfaces.NotificationInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.PaymentAlertInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.PlayListInterface;
import distinct.digitalsolutions.prabudhh.Model.CategoryViewModelClass;

public class PlayListSongActivity extends AppCompatActivity implements NotificationInterface, PaymentAlertInterface {

    private PlayListHelperClass mPlayListHelperClass;
    private String mCategoryName,mSubCategoryName;
    //mCategoryId;
    private int mValue, mSameActivity,mBackButton;
    private List<CategoryViewModelClass> mPlayAllSongsModelClass = new ArrayList<>();

    private CategoryViewModelClass mPlayCategoryModelClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mBackButton = getIntent().getIntExtra("back_button",0);
        mSameActivity = getIntent().getIntExtra("same_activity", 0);
        mCategoryName = getIntent().getStringExtra("category_name");
        mSubCategoryName = getIntent().getStringExtra("sub_category_name");

        TypeToken<List<CategoryViewModelClass>> token = new TypeToken<List<CategoryViewModelClass>>() {};

        mPlayAllSongsModelClass = new Gson().fromJson(getIntent().getStringExtra("all_songs"), token.getType());
        mPlayCategoryModelClass = new Gson().fromJson(getIntent().getStringExtra("song_details"), CategoryViewModelClass.class);

        mPlayListHelperClass = new PlayListHelperClass(PlayListSongActivity.this, null, mPlayCategoryModelClass,
                mCategoryName, this, this, mSameActivity, mPlayAllSongsModelClass,mBackButton,mSubCategoryName);
        setContentView(mPlayListHelperClass.getRootView());

    }

    @Override
    public void onStart() {
        super.onStart();

        mPlayListHelperClass.initView();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPlayListHelperClass.backMethod(mBackButton);

    }

    @Override
    public void notificationEvents(int play, int pause,int next,int previous) {

        if (play == 1) {

            mPlayListHelperClass.playButton();

        } else if (pause == 1) {

            mPlayListHelperClass.pauseButton();

        } else if (next == 1) {

            mPlayListHelperClass.playNextMethod();

        } else if (previous == 1) {

            mPlayListHelperClass.playPreviousMethod();

        }

    }

    @Override
    public void showAlertDialog(CategoryViewModelClass categoryViewModelClass, String categoryName,List<CategoryViewModelClass> categoryViewModelClasses
                                //        , String categoryId
    ) {

        mPlayListHelperClass.showAlertDialog(categoryViewModelClass, categoryName,categoryViewModelClasses
                //, categoryId
        );

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        mPlayListHelperClass.Destroy();
    }
//    @Override
//    public void onPause(){
//        super.onPause();
//        mPlayListHelperClass.releaseExoPlayer();
//    }
//
//    @Override
//    public void onStop(){
//        super.onStop();
//        mPlayListHelperClass.releaseExoPlayer();
//    }

}