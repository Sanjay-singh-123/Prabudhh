package distinct.digitalsolutions.prabudhh.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import distinct.digitalsolutions.prabudhh.HelperClasses.PlayListHelperClass;
import distinct.digitalsolutions.prabudhh.HelperClasses.SingleSongHelperClass;
import distinct.digitalsolutions.prabudhh.Interfaces.NotificationInterface;
import distinct.digitalsolutions.prabudhh.Model.CategoryViewModelClass;

public class SingleSongActivity extends AppCompatActivity implements NotificationInterface {

    private SingleSongHelperClass mSingleSongHelperClass;
    private CategoryViewModelClass mCategoryViewModelClass;
    private String mCategoryName;
    private int mBackButton;
    private List<CategoryViewModelClass> mPlayAllSongsList = new ArrayList<>();
    private List<CategoryViewModelClass> mFilteredSongsList = new ArrayList<>();
    private String mSubCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Type type = new TypeToken<List<CategoryViewModelClass>>() {}.getType();
        mPlayAllSongsList = new Gson().fromJson(getIntent().getStringExtra("all_songs"), type);
        mFilteredSongsList = new Gson().fromJson(getIntent().getStringExtra("filtered_songs"),type);
        mBackButton = getIntent().getIntExtra("back_button",0);
        mSubCategoryName = getIntent().getStringExtra("sub_category_name");

        int songValue = getIntent().getIntExtra("songValue",0);
       // mCategoryId = getIntent().getStringExtra("category_id");

        mCategoryViewModelClass = new Gson().fromJson(getIntent().getStringExtra("song_details"),CategoryViewModelClass.class);
        mCategoryName = getIntent().getStringExtra("category_name");


        mSingleSongHelperClass = new SingleSongHelperClass(SingleSongActivity.this,null,mCategoryViewModelClass,mCategoryName,
                mPlayAllSongsList,songValue,this,mBackButton,mFilteredSongsList,mSubCategoryName);
        setContentView(mSingleSongHelperClass.getRootView());

    }

    @Override
    public void onStart(){
        super.onStart();

        mSingleSongHelperClass.initView();

    }

    @Override
    public void onBackPressed(){
        mSingleSongHelperClass.BackPressed();
        super.onBackPressed();

    }

//    @Override
//    public void onPause(){
//        super.onPause();
//        mSingleSongHelperClass.pausePlayer();
//    }
//
//    @Override
//    public void onStop(){
//        super.onStop();
//        mSingleSongHelperClass.stopPlayer();
//    }

    @Override
    public void onDestroy(){
        mSingleSongHelperClass.destroy();
        super.onDestroy();
    }

    @Override
    public void notificationEvents(int play, int pause,int next,int previous) {

        if (play == 1){

            mSingleSongHelperClass.playButtonClicked();

        }else if (pause == 1){

            mSingleSongHelperClass.pauseButtonClicked();

        }else if (next == 1){

            mSingleSongHelperClass.nextSongMethod();

        }else if (previous == 1){

            mSingleSongHelperClass.nextSongMethod();

        }

    }
}