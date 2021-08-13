package distinct.digitalsolutions.prabudhh.HelperClasses;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import distinct.digitalsolutions.prabudhh.Activities.CreateNotification;
import distinct.digitalsolutions.prabudhh.Activities.PlayListSongActivity;
import distinct.digitalsolutions.prabudhh.Interfaces.LoginInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.NotificationInterface;
import distinct.digitalsolutions.prabudhh.Model.CategoryViewModelClass;
import distinct.digitalsolutions.prabudhh.R;
import distinct.digitalsolutions.prabudhh.SharedPreference.PlaySongSharedPreference;

public class PdfViewerHelperClass implements LoginInterface {

    private View mRootView;

    private Activity mContext;
    private ImageView mPdfBackImage;

    private PDFView mPdfViewer;
    private CategoryViewModelClass mCategoryModelClass;
    private String mCategoryName;

    private PlaySongSharedPreference playSongSharedPreference;
    private NotificationInterface notificationInterface;

    private int value;

    private String categoryId;
    private CreateNotification createNotification;

    public AudioPlayerBroadcastReceiver myReceiver;

    private List<CategoryViewModelClass> mAllSongs;
    private int mBackButton;

    private int mPdfPlayValue;



    public PdfViewerHelperClass(Activity activity, ViewGroup viewGroup, CategoryViewModelClass
            categoryViewModelClass, String CategoryName, NotificationInterface notificationInterface, String categoryId, int mValue,
                                int mBackButton, List<CategoryViewModelClass> mAllSongList) {

        mRootView = LayoutInflater.from(activity).inflate(R.layout.activity_pdf_viewer, viewGroup);

        this.mContext = activity;
        this.mCategoryModelClass = categoryViewModelClass;
        this.mCategoryName = CategoryName;
        this.notificationInterface = notificationInterface;
        this.categoryId = categoryId;
        this.mPdfPlayValue = mValue;
        this.mAllSongs = mAllSongList;
        this.mBackButton = mBackButton;

        playSongSharedPreference = new PlaySongSharedPreference(mContext);

    }

    @Override
    public void initView() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(PlayerNotificationManager.ACTION_PAUSE);
        filter.addAction(PlayerNotificationManager.ACTION_PLAY);
        filter.addAction(PlayerNotificationManager.ACTION_NEXT);
        filter.addAction(PlayerNotificationManager.ACTION_PREVIOUS);
        myReceiver = new AudioPlayerBroadcastReceiver(notificationInterface);
        mContext.registerReceiver(myReceiver, filter);

        mPdfViewer = mRootView.findViewById(R.id.pdf_viewer);
        mPdfBackImage = mRootView.findViewById(R.id.pdf_back_button);

        String pdfUrl = mCategoryModelClass.getPdf_url();

        try{

            new RetrievePdfStream().execute(pdfUrl);

        } catch (Exception e){

            Toast.makeText(mContext, "Failed to load Url :" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        mPdfBackImage.setOnClickListener(v -> {

            backMethod();

        });

    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    public void backMethod() {

        Intent playListIntent = new Intent(mContext, PlayListSongActivity.class);
        playListIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        playListIntent.putExtra("category_id",categoryId);
        playListIntent.putExtra("value",mPdfPlayValue);
        playListIntent.putExtra("song_details", new Gson().toJson(mCategoryModelClass));
        playListIntent.putExtra("category_name", mCategoryName);
        playListIntent.putExtra("all_songs",new Gson().toJson(mAllSongs));
        playListIntent.putExtra("back_button",mBackButton);
        mContext.startActivity(playListIntent);

    }

    public void playButton() {

        playSongSharedPreference.isSongPlaying("is_song_playing",true);

    }

    public void pauseButton() {

        playSongSharedPreference.isSongPlaying("is_song_playing",false);


    }

    private class RetrievePdfStream extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {

            InputStream inputStream = null;

            try {

                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                if (urlConnection.getResponseCode() == 200) {

                    inputStream = new BufferedInputStream(urlConnection.getInputStream());

                }

            } catch (IOException e) {
                return null;

            }
            return inputStream;
        }
        @Override
        protected void onPostExecute(InputStream inputStream) {

            mPdfViewer.fromStream(inputStream).load();

        }
    }

}
