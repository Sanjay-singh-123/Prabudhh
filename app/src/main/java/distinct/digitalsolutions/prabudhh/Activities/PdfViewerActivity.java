package distinct.digitalsolutions.prabudhh.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import distinct.digitalsolutions.prabudhh.HelperClasses.PdfViewerHelperClass;
import distinct.digitalsolutions.prabudhh.HelperClasses.PlayListHelperClass;
import distinct.digitalsolutions.prabudhh.Interfaces.NotificationInterface;
import distinct.digitalsolutions.prabudhh.Model.CategoryViewModelClass;
import distinct.digitalsolutions.prabudhh.R;

public class PdfViewerActivity extends AppCompatActivity implements NotificationInterface {

    private PdfViewerHelperClass mPdfViewerClass;

    private CategoryViewModelClass mCategoryViewModelClass;
    private String mCategoryName,mCategoryId;
    private int mValue,mBackButton;
    private List<CategoryViewModelClass> mAllSongList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TypeToken<List<CategoryViewModelClass>> token = new TypeToken<List<CategoryViewModelClass>>() {
        };

        mValue = getIntent().getIntExtra("value",0);
        mCategoryViewModelClass = new Gson().fromJson(getIntent().getStringExtra("song_details"), CategoryViewModelClass.class);
        mCategoryName = getIntent().getStringExtra("category_name");
        mCategoryId = getIntent().getStringExtra("category_id");
        mBackButton = getIntent().getIntExtra("back_button",0);
        mAllSongList = new Gson().fromJson(getIntent().getStringExtra("all_songs"), token.getType());


        mPdfViewerClass = new PdfViewerHelperClass(
                PdfViewerActivity.this,null, mCategoryViewModelClass,mCategoryName,this,
                mCategoryId,mValue,mBackButton,mAllSongList);
        setContentView(mPdfViewerClass.getRootView());

    }

    @Override
    public void onStart(){
        super.onStart();

        mPdfViewerClass.initView();

    }

    @Override
    public void onBackPressed(){

        mPdfViewerClass.backMethod();

    }

    @Override
    public void notificationEvents(int play, int pause) {

        if (play == 1){

            mPdfViewerClass.playButton();

        }else if (pause == 1){

            mPdfViewerClass.pauseButton();
        }

    }
}