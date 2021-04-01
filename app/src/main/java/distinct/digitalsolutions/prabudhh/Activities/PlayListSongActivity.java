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
    private CategoryViewModelClass mPlayCategoryModelClass;
    private String mCategoryName;
    //mCategoryId;
    private int mValue, mSameActivity,mBackButton;
    private List<CategoryViewModelClass> mPlayAllSongsModelClass = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TypeToken<List<CategoryViewModelClass>> token = new TypeToken<List<CategoryViewModelClass>>() {
        };

        mBackButton = getIntent().getIntExtra("back_button",0);
        mSameActivity = getIntent().getIntExtra("same_activity", 0);
        mPlayCategoryModelClass = new Gson().fromJson(getIntent().getStringExtra("song_details"), CategoryViewModelClass.class);
        mCategoryName = getIntent().getStringExtra("category_name");
        mPlayAllSongsModelClass = new Gson().fromJson(getIntent().getStringExtra("all_songs"), token.getType());

        mPlayListHelperClass = new PlayListHelperClass(PlayListSongActivity.this, null, mPlayCategoryModelClass,
                mCategoryName,
                this, this, mSameActivity, mPlayAllSongsModelClass,mBackButton);
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
    public void notificationEvents(int play, int pause) {

        if (play == 1) {

            mPlayListHelperClass.playButton();

        } else if (pause == 1) {

            mPlayListHelperClass.pauseButton();

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