package distinct.digitalsolutions.prabudhh.Activities;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import distinct.digitalsolutions.prabudhh.Database.FirebaseDatabaseClass;
import distinct.digitalsolutions.prabudhh.HelperClasses.AudioPlayerBroadcastReceiver;
import distinct.digitalsolutions.prabudhh.HelperClasses.SingleSongHelperClass;
import distinct.digitalsolutions.prabudhh.Interfaces.NotificationInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.UserUploadDataInterface;
import distinct.digitalsolutions.prabudhh.Model.CategoryViewModelClass;
import distinct.digitalsolutions.prabudhh.R;
import distinct.digitalsolutions.prabudhh.SharedPreference.PlaySongSharedPreference;

public class CreateNotification extends Service{

    private String PLAYBACK_CHANNEL_ID = "Prabuddha";
    private int PLAYBACK_NOTIFICATION_ID = 123;
    private Context context;

    private PlaySongSharedPreference playSongSharedPreference;

    public SimpleExoPlayer player1;
    private PlayerNotificationManager playerNotificationManager;
    private final Binder mBinder = new MusicBinder();
    public String mCategoryName,
            //mCategoryID
    mSongId;

    public CategoryViewModelClass modelClass;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;

    }

    public boolean isPlaying() {

        return player1.getPlaybackState() == Player.STATE_READY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void playSongMethod(List<CategoryViewModelClass> categoryViewModelClasses, String categoryName, CategoryViewModelClass viewModelClass,boolean value) {

        player1 = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
        //this.mCategoryID = mCategoryId;
        this.mSongId = viewModelClass.getId();

        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(
                context, Util.getUserAgent(context, "Prabuddha")
        );

        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();

        for (CategoryViewModelClass categoryViewModelClass : categoryViewModelClasses) {

            MediaSource mediaSource = new ExtractorMediaSource.Factory(defaultDataSourceFactory).createMediaSource(Uri.parse(categoryViewModelClass.getAudio_url()));
            concatenatingMediaSource.addMediaSource(mediaSource);

        }

        modelClass = viewModelClass;

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
                context, PLAYBACK_CHANNEL_ID, R.string.app_name, PLAYBACK_NOTIFICATION_ID, new PlayerNotificationManager.MediaDescriptionAdapter() {
                    @Override
                    public String getCurrentContentTitle(Player player) {

                        modelClass =categoryViewModelClasses.get(player.getCurrentWindowIndex());

                        return categoryViewModelClasses.get(player.getCurrentWindowIndex()).getTitle();

                    }

                    @Nullable
                    @Override
                    public PendingIntent createCurrentContentIntent(Player player) {

                        Intent playListIntent = new Intent(context, SingleSongActivity.class);
                        playListIntent.putExtra("category_name", categoryName);
                        playListIntent.putExtra("song_details", new Gson().toJson(viewModelClass));
                        playListIntent.putExtra("all_songs", new Gson().toJson(categoryViewModelClasses));
                        return PendingIntent.getActivity(context, 0, playListIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    }

                    @Nullable
                    @Override
                    public String getCurrentContentText(Player player) {

                        return categoryViewModelClasses.get(player.getCurrentWindowIndex()).getDescription();

                    }

                    @Nullable
                    @Override
                    public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {

                        return BitmapFactory.decodeResource(getResources(),R.mipmap.song_image);

                    }
                }
        );

        playerNotificationManager.setNotificationListener(new PlayerNotificationManager.NotificationListener() {
            @Override
            public void onNotificationStarted(int notificationId, Notification notification) {

                startForeground(notificationId, notification);
            }

            @Override
            public void onNotificationCancelled(int notificationId) {
                stopSelf();
            }
        });

        if (value){

            player1.prepare(concatenatingMediaSource);
            player1.setPlayWhenReady(true);

        }

        playerNotificationManager.setPlayer(player1);

    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        playSongSharedPreference = new PlaySongSharedPreference(context);

    }

    public void destroy() {

        player1.release();
        player1 = null;
        playerNotificationManager.setPlayer(null);

        playSongSharedPreference.isSongPlaying("is_song_playing",false);
        playSongSharedPreference.setPlayingSongDetails("playing_song_details","");
        playSongSharedPreference.setAllSongsList("all_songs_details","");
        playSongSharedPreference.setSongPlayedValue("song_value","0");


    }

    public class MusicBinder extends Binder {
        public CreateNotification getService() {
            return CreateNotification.this;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        destroy();

    }

}
