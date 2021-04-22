package distinct.digitalsolutions.prabudhh.HelperClasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import distinct.digitalsolutions.prabudhh.Activities.CreateNotification;
import distinct.digitalsolutions.prabudhh.Activities.PlayListSongActivity;
import distinct.digitalsolutions.prabudhh.Activities.SingleSongActivity;
import distinct.digitalsolutions.prabudhh.Adapter.PlayListRecyclerviewAdapter;
import distinct.digitalsolutions.prabudhh.Database.FirebaseDatabaseClass;
import distinct.digitalsolutions.prabudhh.Interfaces.LoginInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.NotificationInterface;
import distinct.digitalsolutions.prabudhh.Model.CategoryViewModelClass;
import distinct.digitalsolutions.prabudhh.R;
import distinct.digitalsolutions.prabudhh.SharedPreference.PlaySongSharedPreference;

public class SingleSongHelperClass implements LoginInterface, Player.EventListener {

    private View mRootView;

    private Activity mContext;

    private RelativeLayout mPlayListLayout;
    private CategoryViewModelClass mCategoryViewModelClass;
    private ImageView mSongImage, mSongCloseButton, mSongPauseButton, mSongPlayButton, mSongForwardButton, mSongBackWordButton;
    private TextView mSongName, mSongArtist;
    private int value = 0;
    private int mValue = 2;

    private NotificationInterface notificationInterface;

    private List<CategoryViewModelClass> mFilteredSongsList = new ArrayList<>();

    private SimpleExoPlayer simpleExoPlayer;
    private String mCategoryName;

    private SeekBar mSimpleSeekBar;
    private TextView mStartSongTime, mEndSongTime;
    private Handler handler = new Handler();
    int songNumber = 0;
    private int mBackButton;
    private List<CategoryViewModelClass> mCategoryViewHelperClasses = new ArrayList<>();

    public AudioPlayerBroadcastReceiver myReceiver;

    private CreateNotification createNotification;
    private int songValue = 0;
    //  private String mCategoryId;

    private FirebaseDatabaseClass firebaseDatabaseClass;

    private ProgressBar mProgressBar;
    private PlaySongSharedPreference playSongSharedPreference;
    private RelativeLayout mProgressBarLayout;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            CreateNotification.MusicBinder mServiceBinder = (CreateNotification.MusicBinder) service;
            createNotification = mServiceBinder.getService();

            if (value == 1) {

                createNotification.player1.release();
                createNotification.player1 = null;

                createNotification.playSongMethod(mFilteredSongsList, mCategoryName, mCategoryViewModelClass);

            }

            setSongDetails(createNotification.modelClass.getTitle(), createNotification.modelClass.getDescription(), createNotification.modelClass.getImg_url());
            mProgressBarLayout.setVisibility(View.GONE);
            mContext.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            updateSeekBack();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.exit(0);
        }
    };

    public SingleSongHelperClass(Activity mContext, ViewGroup viewGroup, CategoryViewModelClass mCategoryViewModelClass,
                                 String mCategoryName, List<CategoryViewModelClass> mCategoryViewHelperClasses, int value,
                                 NotificationInterface notificationInterface, int mBackButton,List<CategoryViewModelClass> filteredSongsList
    ) {

        mRootView = LayoutInflater.from(mContext).inflate(R.layout.activity_single_song, viewGroup);
        this.mCategoryViewModelClass = mCategoryViewModelClass;
        this.mCategoryViewHelperClasses = mCategoryViewHelperClasses;

        //    this.mCategoryId = mCategoryId;
        this.notificationInterface = notificationInterface;

        this.mCategoryName = mCategoryName;
        this.mContext = mContext;

        this.value = value;
        this.mBackButton = mBackButton;

        firebaseDatabaseClass = new FirebaseDatabaseClass();

        playSongSharedPreference = new PlaySongSharedPreference(mContext);

        this.mFilteredSongsList = filteredSongsList;

//        this.mSecondCategoryViewHelperClass.add(mCategoryViewModelClass);
//        this.mSecondCategoryViewHelperClass.addAll(mCategoryViewHelperClasses);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initView() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(PlayerNotificationManager.ACTION_PAUSE);
        filter.addAction(PlayerNotificationManager.ACTION_PLAY);
        filter.addAction(PlayerNotificationManager.ACTION_NEXT);
        filter.addAction(PlayerNotificationManager.ACTION_PREVIOUS);
        myReceiver = new AudioPlayerBroadcastReceiver(notificationInterface);
        mContext.registerReceiver(myReceiver, filter);

        mProgressBarLayout = mRootView.findViewById(R.id.progress_bar_layout);

        mProgressBar = mRootView.findViewById(R.id.progress_bar);

        mSimpleSeekBar = mRootView.findViewById(R.id.song_seek_bar);
        mStartSongTime = mRootView.findViewById(R.id.start_seek_bar_time);
        mEndSongTime = mRootView.findViewById(R.id.end_seek_bar_time);

        mSongName = mRootView.findViewById(R.id.song_title);
        mSongArtist = mRootView.findViewById(R.id.song_artiest);

        mSongImage = mRootView.findViewById(R.id.song_thumbnail);
        mSongCloseButton = mRootView.findViewById(R.id.close_icon);
        mSongPauseButton = mRootView.findViewById(R.id.pause_button);
        mSongPlayButton = mRootView.findViewById(R.id.play_button);
        mSongForwardButton = mRootView.findViewById(R.id.right_arrow_song);
        mSongBackWordButton = mRootView.findViewById(R.id.left_arrow_song);

        mSimpleSeekBar.setMax(100);

        if (createNotification == null || createNotification.player1 == null) {

            Intent playService = new Intent(mContext, CreateNotification.class);
            Util.startForegroundService(mContext, playService);
            mContext.bindService(playService, serviceConnection, Context.BIND_AUTO_CREATE);


        }

        mSongPauseButton.setOnClickListener(v -> {

            pauseButtonClicked();
            pausePlayer();

        });

        mSongForwardButton.setOnClickListener(v -> {

            if (createNotification.player1.hasNext()) {

                createNotification.player1.next();

                nextSongMethod();

            } else {

                Intent playListIntent = new Intent(mContext, SingleSongActivity.class);
                playListIntent.putExtra("songValue", 1);
                playListIntent.putExtra("category_name", mCategoryName);
                playListIntent.putExtra("song_details", new Gson().toJson(mCategoryViewHelperClasses.get(0)));
                playListIntent.putExtra("all_songs", new Gson().toJson(mCategoryViewHelperClasses));
                playListIntent.putExtra("filtered_songs",new Gson().toJson(mFilteredSongsList));
                playListIntent.putExtra("back_button", mBackButton);
                mContext.startActivity(playListIntent);
                mContext.overridePendingTransition(0, 0);

            }

        });

        mSongBackWordButton.setOnClickListener(v -> {

            if (createNotification.player1.hasPrevious()) {

                createNotification.player1.previous();

                int vlaue = mCategoryViewHelperClasses.indexOf(createNotification.modelClass);

                if (vlaue == 0) {

                    return;
                }

                saveSongCount(createNotification.modelClass.getSong_id());
                setSongDetails(createNotification.modelClass.getTitle(), createNotification.modelClass.getDescription(), createNotification.modelClass.getImg_url());

            }

        });

        mSongCloseButton.setOnClickListener(v -> {

            BackPressed();

        });

        mSongPlayButton.setOnClickListener(v -> {

            playButtonClicked();
            startPlayer();

        });

        mSimpleSeekBar.setOnTouchListener((v, event) -> {

            playerState();

            SeekBar seekBar = (SeekBar) v;
            long playPosition = (createNotification.player1.getDuration() / 100) * seekBar.getProgress();
            createNotification.player1.seekTo((playPosition));
            mStartSongTime.setText(milliSecondsToTimer(createNotification.player1.getContentPosition()));
            return false;

        });

    }

    private void saveSongCount(String song_id) {

        firebaseDatabaseClass.getSongAndUpdateCount(song_id, value -> {

            if (value) {

                Log.d("SongCount", "Success");

            } else {
                Log.d("SongCount", "Failed");

            }

        });

    }

    public void pauseButtonClicked() {

        playSongSharedPreference.isSongPlaying("is_song_playing", false);

        mSongPlayButton.setVisibility(View.VISIBLE);
        mSongPauseButton.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);

    }

    public void playButtonClicked() {


        playSongSharedPreference.isSongPlaying("is_song_playing", true);

        mSongPauseButton.setVisibility(View.VISIBLE);
        mSongPlayButton.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);

    }

    private void nextSongMethod() {

//        if (createNotification.modelClass.getPaid_content().equalsIgnoreCase("1")) {
//
//            createNotification.player1.next();
//
//        }

        saveSongCount(createNotification.modelClass.getSong_id());
        setSongDetails(createNotification.modelClass.getTitle(), createNotification.modelClass.getDescription(), createNotification.modelClass.getImg_url());

    }

    private void setSongDetails(String title, String description, String img_url) {

        try {

            mSongName.setText(title);
            mSongArtist.setText(description);
            Picasso.get().load(img_url).into(mSongImage);

            mCategoryViewModelClass = createNotification.modelClass;

        } catch (Exception e) {

            Log.d("Error", e.getLocalizedMessage());

        }

    }

    public void stopPlayer() {

        createNotification.player1.release();
        createNotification.player1 = null;
        mSimpleSeekBar.setProgress(0);

    }

    public void pausePlayer() {

        createNotification.player1.setPlayWhenReady(false);
        createNotification.player1.getPlaybackState();

    }

    public void startPlayer() {

        createNotification.player1.setPlayWhenReady(true);
        createNotification.player1.getPlaybackState();

        updateSeekBack();

    }

    @Override
    public View getRootView() {

        return mRootView;

    }

    public void BackPressed() {

        playSongSharedPreference.setPlayingSongDetails("playing_song_details", new Gson().toJson(mCategoryViewModelClass));

        Intent intent = new Intent(mContext, PlayListSongActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//intent.putExtra("category_id",mCategoryId);
        intent.putExtra("category_name", mCategoryName);
        intent.putExtra("song_details", new Gson().toJson(mCategoryViewModelClass));
        intent.putExtra("all_songs", new Gson().toJson(mCategoryViewHelperClasses));
        intent.putExtra("back_button", mBackButton);
        mContext.startActivity(intent);
        mContext.finish();

    }

    private Runnable updater = new Runnable() {
        @Override
        public void run() {

            if (value == 0) {

                updateSeekBack();
                long currentDuration = createNotification.player1.getCurrentPosition();
                mStartSongTime.setText(milliSecondsToTimer(currentDuration));

            }
        }
    };

    private void updateSeekBack() {

        if (createNotification.player1.getPlayWhenReady()) {

            mSimpleSeekBar.setProgress((int) (((float) createNotification.player1.getCurrentPosition() / createNotification.player1.getDuration()) * 100));
            handler.postDelayed(updater, 1000);

        }

    }

    private void playerState() {

        if (createNotification != null) {

            if (createNotification.player1 != null) {

//                createNotification.player1.addListener(new ExoPlayer.EventListener() {
//                    @Override
//                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//
//                        if (playbackState == PlaybackStateCompat.STATE_PLAYING){
//
//                            setSongDetails(createNotification.modelClass.getTitle(),createNotification.modelClass.getDescription(),createNotification.modelClass.getImg_url());
//                            mProgressBarLayout.setVisibility(View.GONE);
//                            mContext.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//
//                        }
//                    }
//                });

                createNotification.player1.addListener(new Player.EventListener() {
                    @Override
                    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                        setSongDetails(createNotification.modelClass.getTitle(), createNotification.modelClass.getDescription(), createNotification.modelClass.getImg_url());
                        mProgressBarLayout.setVisibility(View.GONE);
                        mContext.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        mCategoryViewModelClass = createNotification.modelClass;

                    }

                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                        if (playbackState == Player.STATE_ENDED) {

                            setSongDetails(createNotification.modelClass.getTitle(), createNotification.modelClass.getDescription(), createNotification.modelClass.getImg_url());
                            mProgressBarLayout.setVisibility(View.GONE);
                            mContext.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            mCategoryViewModelClass = createNotification.modelClass;

                        } else if (playbackState == Player.STATE_READY) {

                            setSongDetails(createNotification.modelClass.getTitle(), createNotification.modelClass.getDescription(), createNotification.modelClass.getImg_url());
                            mProgressBarLayout.setVisibility(View.GONE);
                            mContext.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            mCategoryViewModelClass = createNotification.modelClass;

                        } else if (playbackState == Player.STATE_IDLE) {

                            setSongDetails(createNotification.modelClass.getTitle(), createNotification.modelClass.getDescription(), createNotification.modelClass.getImg_url());
                            mProgressBarLayout.setVisibility(View.GONE);
                            mContext.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            mCategoryViewModelClass = createNotification.modelClass;

                        } else if (playbackState == Player.STATE_BUFFERING) {

                            mProgressBarLayout.setVisibility(View.VISIBLE);
                            mContext.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        } else {

                            mProgressBarLayout.setVisibility(View.GONE);
                            mContext.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        }
                    }
                });


            }
        }

    }

    private String milliSecondsToTimer(long milliSeconds) {

        String timerString = "";
        String secondString;

        int hours = (int) (milliSeconds / (1000 * 60 * 60));
        int minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {

            timerString = hours + ":";
        }

        if (seconds < 10) {

            secondString = "0" + seconds;
        } else {

            secondString = "" + seconds;
        }

        timerString = timerString + minutes + ":" + secondString;
        return timerString;
    }

    public void destroy() {

        handler.removeCallbacks(updater);
        mContext.unregisterReceiver(myReceiver);

    }


}
