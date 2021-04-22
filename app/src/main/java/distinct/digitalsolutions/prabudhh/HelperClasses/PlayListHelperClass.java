package distinct.digitalsolutions.prabudhh.HelperClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import distinct.digitalsolutions.prabudhh.Activities.CategoryViewActivity;
import distinct.digitalsolutions.prabudhh.Activities.CreateNotification;
import distinct.digitalsolutions.prabudhh.Activities.MainActivity;
import distinct.digitalsolutions.prabudhh.Activities.PaymentActivity;
import distinct.digitalsolutions.prabudhh.Activities.PdfViewerActivity;
import distinct.digitalsolutions.prabudhh.Activities.PlayListSongActivity;
import distinct.digitalsolutions.prabudhh.Activities.SingleSongActivity;
import distinct.digitalsolutions.prabudhh.Adapter.PlayListRecyclerviewAdapter;
import distinct.digitalsolutions.prabudhh.Database.FirebaseDatabaseClass;
import distinct.digitalsolutions.prabudhh.Interfaces.LoginInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.NotificationInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.PaymentAlertInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.PlayListInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.SongPostFirebaseInterface;
import distinct.digitalsolutions.prabudhh.Model.CategoryViewModelClass;
import distinct.digitalsolutions.prabudhh.R;
import distinct.digitalsolutions.prabudhh.SharedPreference.PlaySongSharedPreference;

public class PlayListHelperClass implements LoginInterface, PlayListInterface {

    private View mRootView;

    private Activity mContext;

    private RecyclerView mPlayListRecyclerView;
    private PlayListRecyclerviewAdapter mPlayListRecyclerViewAdapter;
    private List<CategoryViewModelClass> mPlayListModelClass = new ArrayList<>();
    private List<CategoryViewModelClass> mAllSongsList = new ArrayList<>();

    private List<CategoryViewModelClass> mSortedSongsList = new ArrayList<>();
    private List<CategoryViewModelClass> mFilteredSongsList = new ArrayList<>();

    private RelativeLayout mPlayListLayout;
    private int mSongPlayPauseValue;
    private CategoryViewModelClass categoryViewModelClass;

    private FirebaseDatabaseClass mPlayListFirebaseDatabase;

    private ImageView mPlayListMainSongImage, mPlayListMainBackButton;
    private TextView mPlayListSongNameTextView, mPlayListSongArtistTextView;
    private String mCategoryName;

    private ImageView mPlaySongIcon, mPauseSongIcon;

    public AudioPlayerBroadcastReceiver myReceiver;

    private ProgressBar mProgressBarClass;
    private ImageView mViewSongImage, mViewPdfFileIcon;
    private TextView mViewSongSongName, mViewSongSongArtist;

    private CreateNotification createNotification;
    private PlayListInterface mValue;
    private int value = 0;
    private int myValue;
    private int sameActivivty;

    private List<CategoryViewModelClass> mPlayAllSongsModelClass;
    private FirebaseDatabaseClass mCategoryFirebaseDatabaseClass;

    private NotificationInterface notificationInterface;
    private PaymentAlertInterface paymentAlertInterface;

    private int mBackButton;
    private String isSongPlayingORnOt = "is_song_playing";

    private PlaySongSharedPreference mPlaySongSharedPreference;
    private RelativeLayout mProgressbarLayout;

    private Handler handler = new Handler();


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            try {

                CreateNotification.MusicBinder mServiceBinder = (CreateNotification.MusicBinder) service;
                createNotification = mServiceBinder.getService();

                myValue = 1;

                if (createNotification.player1 != null) {

                    if (createNotification.mSongId.equalsIgnoreCase(categoryViewModelClass.getId())) {

                        createNotification.player1.addListener(new Player.EventListener() {
                            @Override
                            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                                if (playbackState == Player.STATE_ENDED) {

                                    createNotification.player1.setPlayWhenReady(false);
                                    createNotification.player1.release();
                                    createNotification.player1 = null;
                                }
                            }
                        });


                    } else {

                        createNotification.player1.release();
                        createNotification.player1 = null;

                        saveSongCount(categoryViewModelClass.getSong_id());
                        createNotification.playSongMethod(mFilteredSongsList, mCategoryName, categoryViewModelClass);


                        mPlaySongSharedPreference.isSongPlaying("is_song_playing", true);
                        mPlaySongSharedPreference.setPlayingSongDetails("playing_song_details", new Gson().toJson(categoryViewModelClass));
                        mPlaySongSharedPreference.setAllSongsList("all_songs_details", new Gson().toJson(mSortedSongsList));
                        mPlaySongSharedPreference.setSongPlayedValue("song_value", "1");


                    }

                } else {


                    mPlaySongSharedPreference.isSongPlaying("is_song_playing", true);
                    mPlaySongSharedPreference.setPlayingSongDetails("playing_song_details", new Gson().toJson(categoryViewModelClass));
                    mPlaySongSharedPreference.setAllSongsList("all_songs_details", new Gson().toJson(mSortedSongsList));
                    mPlaySongSharedPreference.setSongPlayedValue("song_value", "1");


                    saveSongCount(categoryViewModelClass.getSong_id());
                    createNotification.playSongMethod(mFilteredSongsList, mCategoryName, categoryViewModelClass);

                }

            } catch (Exception e) {
                Log.d("Error", e.getLocalizedMessage());
            }

            playState();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.exit(0);
        }

    };

    private void saveSongCount(String song_id) {

        mPlayListFirebaseDatabase.getSongAndUpdateCount(song_id, value -> {

            if (value) {

                Log.d("SongCount", "Success");

            } else {
                Log.d("SongCount", "Failed");

            }

        });

    }

    public PlayListHelperClass(Activity mContext, ViewGroup viewGroup, CategoryViewModelClass categoryViewModelClass,
                               String mCategoryName,
                               //String mCategoryId,
                               NotificationInterface notificationInterface,
                               PaymentAlertInterface paymentAlertInterface, int sameActivity,
                               List<CategoryViewModelClass> mPlayAllSongsModelClass, int mBackButton) {

        mRootView = LayoutInflater.from(mContext).inflate(R.layout.activity_play_list_song, viewGroup);

        this.categoryViewModelClass = categoryViewModelClass;
        this.mContext = mContext;
        this.mCategoryName = mCategoryName;
        mPlayListFirebaseDatabase = new FirebaseDatabaseClass();
        //this.mCategoryId = mCategoryId;
        this.notificationInterface = notificationInterface;
        this.mPlayAllSongsModelClass = mPlayAllSongsModelClass;

        this.mBackButton = mBackButton;
        mValue = this;

        mCategoryFirebaseDatabaseClass = new FirebaseDatabaseClass();
        this.paymentAlertInterface = paymentAlertInterface;

        this.sameActivivty = sameActivity;

        mPlaySongSharedPreference = new PlaySongSharedPreference(mContext);

    }

    @Override
    public void initView() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(PlayerNotificationManager.ACTION_PAUSE);
        filter.addAction(PlayerNotificationManager.ACTION_PLAY);
        filter.addAction(PlayerNotificationManager.ACTION_NEXT);
        filter.addAction(PlayerNotificationManager.ACTION_PREVIOUS);

        mProgressbarLayout = mRootView.findViewById(R.id.progress_bar_layout);

        myReceiver = new AudioPlayerBroadcastReceiver(notificationInterface);
        mContext.registerReceiver(myReceiver, filter);

        mPlayListMainSongImage = mRootView.findViewById(R.id.song_thumbnail);

        mPlayListMainBackButton = mRootView.findViewById(R.id.back_button);

        mViewPdfFileIcon = mRootView.findViewById(R.id.view_pdf_icon);

        mPlaySongIcon = mRootView.findViewById(R.id.song_play_icon);
        mPauseSongIcon = mRootView.findViewById(R.id.song_pause_icon);

        mPlayListSongNameTextView = mRootView.findViewById(R.id.song_title);
        mPlayListSongArtistTextView = mRootView.findViewById(R.id.song_artiest);
        mPlayListLayout = mRootView.findViewById(R.id.view_song_layout);

        mViewSongSongName = mRootView.findViewById(R.id.song_name_two);
        mViewSongSongArtist = mRootView.findViewById(R.id.song_artiest_two);
        mViewSongImage = mRootView.findViewById(R.id.song_thumbnail_two);

        mProgressBarClass = mRootView.findViewById(R.id.progress_bar);
        Sprite doubleBounce = new Wave();
        mProgressBarClass.setIndeterminateDrawable(doubleBounce);

        mPlayListRecyclerView = mRootView.findViewById(R.id.play_list_recycler_view);
        mPlayListRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mPlayListRecyclerView.setLayoutManager(layoutManager);

        LoadCategoryPosts();

        mPlayListSongNameTextView.setText(categoryViewModelClass.getTitle());
        mPlayListSongArtistTextView.setText(categoryViewModelClass.getDescription());

        if (!categoryViewModelClass.getImg_url().equalsIgnoreCase("")) {

            Picasso.get().load(categoryViewModelClass.getImg_url()).into(mPlayListMainSongImage);

        }

        mViewSongSongName.setText(categoryViewModelClass.getTitle());
        mViewSongSongArtist.setText(categoryViewModelClass.getDescription());

        if (!categoryViewModelClass.getImg_url().equalsIgnoreCase("")) {

            Picasso.get().load(categoryViewModelClass.getImg_url()).into(mViewSongImage);

        }

        mPlayListLayout.setOnClickListener(v -> {

            Intent playListIntent = new Intent(mContext, SingleSongActivity.class);
            playListIntent.putExtra("category_name", mCategoryName);
            playListIntent.putExtra("song_details", new Gson().toJson(categoryViewModelClass));
            playListIntent.putExtra("all_songs", new Gson().toJson(mPlayAllSongsModelClass));
            playListIntent.putExtra("filtered_songs",new Gson().toJson(mFilteredSongsList));
            playListIntent.putExtra("back_button", mBackButton);
            mContext.startActivity(playListIntent);

        });

        mPlayListMainBackButton.setOnClickListener(v -> {

            backMethod(mBackButton);

        });


        mPlaySongIcon.setOnClickListener(v -> {

            mPlayListLayout.setVisibility(View.VISIBLE);
            mPlaySongIcon.setVisibility(View.GONE);
            mPauseSongIcon.setVisibility(View.VISIBLE);

            startForgroundServices();

        });

        mPauseSongIcon.setOnClickListener(v -> {

            try {

                pauseButton();

                releaseExoPlayer();

            } catch (Exception e) {

                Log.d("Error", e.getLocalizedMessage());

            }


        });


        mViewPdfFileIcon.setOnClickListener(v -> {

            Intent playListIntent = new Intent(mContext, PdfViewerActivity.class);
            playListIntent.putExtra("song_details", new Gson().toJson(categoryViewModelClass));
            playListIntent.putExtra("category_name", mCategoryName);
            playListIntent.putExtra("all_songs", new Gson().toJson(mPlayAllSongsModelClass));
            playListIntent.putExtra("back_button", mBackButton);
            mContext.startActivity(playListIntent);

        });

        setThem();

//        mPlayListLayout.setVisibility(View.VISIBLE);
//        mPlaySongIcon.setVisibility(View.GONE);
//        mPauseSongIcon.setVisibility(View.VISIBLE);
//
//        startForgroundServices();

        try {

            if (sameActivivty == 1) {

                mPlayListLayout.setVisibility(View.VISIBLE);
                mPlaySongIcon.setVisibility(View.GONE);
                mPauseSongIcon.setVisibility(View.VISIBLE);

                startForgroundServices();

            }

        } catch (Exception e) {

            Log.d("Errror", e.getLocalizedMessage());
        }

    }


    private void playState() {

        if (createNotification != null) {

            if (createNotification.player1 != null) {

                createNotification.player1.addListener(new Player.EventListener() {
                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                        if (playbackState == Player.STATE_ENDED) {

                            mPauseSongIcon.setVisibility(View.GONE);
                            mPlaySongIcon.setVisibility(View.VISIBLE);

                            createNotification.player1.setPlayWhenReady(false);
                            createNotification.player1.release();
                            createNotification.player1 = null;

                        } else if (playbackState == Player.STATE_READY) {

                            mProgressbarLayout.setVisibility(View.GONE);
                            mContext.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        } else if (playbackState == Player.STATE_BUFFERING) {

                            mProgressbarLayout.setVisibility(View.VISIBLE);
                            mContext.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        } else {

                            mProgressbarLayout.setVisibility(View.GONE);
                            mContext.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        }
                    }
                });

            }
        }

        handler.postDelayed(updater, 1000);

    }

    private Runnable updater = () -> playState();

    private void setThem() {

        if (mPlaySongSharedPreference.getIsSongPlaying(isSongPlayingORnOt)) {

            try {


                String songDetails = mPlaySongSharedPreference.getPlayingSongDetails("playing_song_details");
                CategoryViewModelClass modelClass = new Gson().fromJson(songDetails, CategoryViewModelClass.class);


                if (modelClass.getSong_id().equalsIgnoreCase(categoryViewModelClass.getSong_id())) {

                    mPlaySongIcon.setVisibility(View.GONE);
                    mPauseSongIcon.setVisibility(View.VISIBLE);

                    Intent playService = new Intent(mContext, CreateNotification.class);
                    Util.startForegroundService(mContext, playService);

                    mContext.bindService(playService, serviceConnection, Context.BIND_AUTO_CREATE);

                }

                mPlayListLayout.setVisibility(View.VISIBLE);

                mViewSongSongName.setText(modelClass.getTitle());
                mViewSongSongArtist.setText(modelClass.getDescription());

                if (!modelClass.getImg_url().equalsIgnoreCase("")) {

                    Picasso.get().load(categoryViewModelClass.getImg_url()).into(mViewSongImage);

                }

            } catch (Exception e) {
                Log.d("Error", e.getLocalizedMessage());
            }

        }

    }

    private void LoadCategoryPosts() {

        mPlayListModelClass.clear();
        mAllSongsList.clear();

        for (CategoryViewModelClass viewModelClass : mPlayAllSongsModelClass) {

            if (!viewModelClass.getSong_id().equalsIgnoreCase(categoryViewModelClass.getSong_id())) {


                mPlayListModelClass.add(viewModelClass);

            }
        }


        for (CategoryViewModelClass modelClass : mPlayAllSongsModelClass){

            if (modelClass.getPaid_content().equalsIgnoreCase("0")){

                mAllSongsList.add(modelClass);

            }
        }

        mFilteredSongsList.add(categoryViewModelClass);
        mFilteredSongsList.addAll(mAllSongsList);


        mSortedSongsList.add(categoryViewModelClass);
        mSortedSongsList.addAll(mPlayListModelClass);

        mPlayListRecyclerViewAdapter = new PlayListRecyclerviewAdapter(mContext,
                mPlayListModelClass, mCategoryName, mValue, paymentAlertInterface
                //, mCategoryId
        );
        mPlayListRecyclerView.setAdapter(mPlayListRecyclerViewAdapter);
        mPlayListRecyclerViewAdapter.notifyDataSetChanged();


    }

    @Override
    public View getRootView() {

        return mRootView;

    }

    private void startForgroundServices() {

        if (mPlaySongSharedPreference.getSongPlayedValue("song_value") != null &&
                mPlaySongSharedPreference.getSongPlayedValue("song_value").equalsIgnoreCase("1")) {

            if (createNotification != null) {

                if (createNotification.player1 != null) {
                    initExoPlayer();

                    myValue = 6;
                }

            }

        } else {

            mProgressbarLayout.setVisibility(View.VISIBLE);
            mContext.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }

        Intent playService = new Intent(mContext, CreateNotification.class);
        Util.startForegroundService(mContext, playService);

        mContext.bindService(playService, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    public void Destroy() {

        handler.removeCallbacks(updater);

    }

    private void initExoPlayer() {

        try {

            createNotification.player1.setPlayWhenReady(true);
            playButton();

        } catch (Exception e) {

            Log.d("Error", e.getLocalizedMessage());
        }

    }

    public void playButton() {

        mPlaySongIcon.setVisibility(View.GONE);
        mPauseSongIcon.setVisibility(View.VISIBLE);
        mPlaySongSharedPreference.isSongPlaying("is_song_playing", true);
        setThem();
    }

    public void pauseButton() {

        mPlaySongIcon.setVisibility(View.VISIBLE);
        mPauseSongIcon.setVisibility(View.GONE);

        mPlaySongSharedPreference.isSongPlaying("is_song_playing", false);

        mPlayListLayout.setVisibility(View.GONE);

    }

    public void releaseExoPlayer() {

        try {

            createNotification.player1.setPlayWhenReady(false);

        } catch (Exception e) {
        }

    }

    public void backMethod(int value) {

        Intent intent;

        if (value == 1) {

            intent = new Intent(mContext, MainActivity.class);

        } else {

            intent = new Intent(mContext, CategoryViewActivity.class);
            intent.putExtra("category_name", mCategoryName);

        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);


    }

    @Override
    public void playListResponse(CategoryViewModelClass viewModelClass, String categoryName) {


        try {

            createNotification.player1.release();
            createNotification.player1 = null;

        } catch (Exception e) {
            Log.d("Error", e.getLocalizedMessage());
        }

        mPlaySongSharedPreference.isSongPlaying("is_song_playing", true);
        mPlaySongSharedPreference.setPlayingSongDetails("playing_song_details", new Gson().toJson(viewModelClass));
        mPlaySongSharedPreference.setAllSongsList("all_songs_details", new Gson().toJson(mPlayAllSongsModelClass));
        mPlaySongSharedPreference.setSongPlayedValue("song_value", "0");

        Intent playListIntent = new Intent(mContext, PlayListSongActivity.class);
        playListIntent.putExtra("same_activity", 1);
        playListIntent.putExtra("song_details", new Gson().toJson(viewModelClass));
        playListIntent.putExtra("category_name", categoryName);
        playListIntent.putExtra("back_button", mBackButton);
        playListIntent.putExtra("all_songs", new Gson().toJson(mPlayAllSongsModelClass));
        playListIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(playListIntent);
        mContext.overridePendingTransition(0, 0);

    }

    public void showAlertDialog(CategoryViewModelClass categoryViewModelClass, String categoryName, List<CategoryViewModelClass> categoryViewModelClasses
                                //, String mCategoryid
    ) {

        mCategoryFirebaseDatabaseClass.checkUserPaymentStatus(new SongPostFirebaseInterface() {
            @Override
            public void onSuccess(String success, String date, String expiryDate) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                Date strDate = null;

                try {

                    strDate = sdf.parse(expiryDate);

                } catch (ParseException e) {

                    e.printStackTrace();

                }

                if (new Date().after(strDate)) {

                    new AlertDialog.Builder(mContext)
                            .setTitle("Subscription Expired")
                            .setMessage("You need to renewal subscription plan.")
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {

                                // Continue with delete operation
                                Intent alertIntent = new Intent(mContext, PaymentActivity.class);
                                alertIntent.putExtra("category_name", categoryName);
                                //alertIntent.putExtra("category_id", mCategoryid);
                                alertIntent.putExtra("back_button", mBackButton);
                                mContext.startActivity(alertIntent);

                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.mipmap.music_icon)
                            .show();

                    return;

                }

//                if (TextUtils.isEmpty(mCategoryid) || mCategoryid == null) {
//
//                    Intent intent = new Intent(mContext, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    mContext.startActivity(intent);
//                    return;
//
//                }
//

                mPlaySongSharedPreference.isSongPlaying("is_song_playing", true);
                mPlaySongSharedPreference.setPlayingSongDetails("playing_song_details", new Gson().toJson(categoryViewModelClass));
                mPlaySongSharedPreference.setAllSongsList("all_songs_details", new Gson().toJson(mPlayAllSongsModelClass));
                mPlaySongSharedPreference.setSongPlayedValue("song_value", "0");


                Intent playListIntent = new Intent(mContext, PlayListSongActivity.class);
                playListIntent.putExtra("song_details", new Gson().toJson(categoryViewModelClass));
                playListIntent.putExtra("category_name", categoryName);
                playListIntent.putExtra("all_songs", new Gson().toJson(mPlayAllSongsModelClass));
                playListIntent.putExtra("same_activity", 1);
                playListIntent.putExtra("back_button", mBackButton);
                mContext.startActivity(playListIntent);
                mContext.overridePendingTransition(0, 0);

            }

            @Override
            public void onFailure(String error) {

                new AlertDialog.Builder(mContext)
                        .setTitle("Paid Content")
                        .setMessage("You need to subscribe to view this content.")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {

                            // Continue with delete operation
                            Intent alertIntent = new Intent(mContext, PaymentActivity.class);
                            alertIntent.putExtra("category_name", categoryName);
                            //alertIntent.putExtra("category_id", mCategoryid);
                            mContext.startActivity(alertIntent);

                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.mipmap.music_icon)
                        .show();

            }
        });
    }
}
