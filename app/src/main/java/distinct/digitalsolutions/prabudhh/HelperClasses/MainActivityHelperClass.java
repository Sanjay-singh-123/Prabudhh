package distinct.digitalsolutions.prabudhh.HelperClasses;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import distinct.digitalsolutions.prabudhh.Activities.CreateNotification;
import distinct.digitalsolutions.prabudhh.Activities.MainActivity;
import distinct.digitalsolutions.prabudhh.Activities.SingleSongActivity;
import distinct.digitalsolutions.prabudhh.Adapter.PlayListRecyclerviewAdapter;
import distinct.digitalsolutions.prabudhh.Authentication.LoginActivity;
import distinct.digitalsolutions.prabudhh.Interfaces.LoginInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.MainActivityInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.NotificationInterface;
import distinct.digitalsolutions.prabudhh.Model.CategoryViewModelClass;
import distinct.digitalsolutions.prabudhh.R;
import distinct.digitalsolutions.prabudhh.SharedPreference.PlaySongSharedPreference;

public class MainActivityHelperClass implements MainActivityInterface, LoginInterface {

    private MainActivity context;

    public AudioPlayerBroadcastReceiver myReceiver;
    private NotificationInterface notificationInterface;
    private PlaySongSharedPreference playSongSharedPreference;

    private TextView mViewSongSongName, mViewSongSongArtist;
    private ImageView mViewSongImage;
    private RelativeLayout mPlayListLayout;

    private CreateNotification createNotification;

    private List<CategoryViewModelClass> mPlayAllSongsModelClass = new ArrayList<>();
    private CategoryViewModelClass mPlayCategoryModelClass;

    private Handler mSetDetailsHandler = new Handler();

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            try {

                CreateNotification.MusicBinder mServiceBinder = (CreateNotification.MusicBinder) service;
                createNotification = mServiceBinder.getService();

//                mSetDetailsHandler.postDelayed(() -> {
//
//                    setDataMethod();
//
//                }, 1000);


            } catch (Exception e) {
                Log.d("Error_Value", e.getLocalizedMessage());
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.exit(0);

        }

    };


    public MainActivityHelperClass(MainActivity context, NotificationInterface notificationInterface) {

        this.context = context;
        this.notificationInterface = notificationInterface;
        playSongSharedPreference = new PlaySongSharedPreference(context);


    }

    @Override
    public void checkUserLoggedInOrNot() {

        // mMainActivityPresenter.getUserStatus();

    }

    @Override
    public void checkUserStatus(String userId) {

        if (userId.equalsIgnoreCase("")) {

            Intent checkUserStatusIntent = new Intent(context, LoginActivity.class);
            checkUserStatusIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(checkUserStatusIntent);

        }

    }

    @Override
    public void initView() {

        mPlayListLayout = context.findViewById(R.id.view_song_layout);
        mViewSongSongName = context.findViewById(R.id.song_name_two);
        mViewSongSongArtist = context.findViewById(R.id.song_artiest_two);
        mViewSongImage = context.findViewById(R.id.song_thumbnail_two);

        IntentFilter filter = new IntentFilter();
        filter.addAction(PlayerNotificationManager.ACTION_PAUSE);
        filter.addAction(PlayerNotificationManager.ACTION_PLAY);
        filter.addAction(PlayerNotificationManager.ACTION_NEXT);
        filter.addAction(PlayerNotificationManager.ACTION_PREVIOUS);

        myReceiver = new AudioPlayerBroadcastReceiver(notificationInterface);
        context.registerReceiver(myReceiver, filter);

        mPlayListLayout.setOnClickListener(v -> {

            String songDetails = playSongSharedPreference.getPlayingSongDetails("playing_song_details");
            CategoryViewModelClass categoryViewModelClass = new Gson().fromJson(songDetails, CategoryViewModelClass.class);

            TypeToken<List<CategoryViewModelClass>> token = new TypeToken<List<CategoryViewModelClass>>() {
            };
            List<CategoryViewModelClass> mPlayAllSongsModelClass = new Gson().fromJson(playSongSharedPreference.getAllSongsList("all_songs_details"), token.getType());


            Intent playListIntent = new Intent(context, SingleSongActivity.class);
            //playListIntent.putExtra("category_id", mCategoryId);
            playListIntent.putExtra("category_name", "mCategoryName");
            playListIntent.putExtra("back_button", 1);
            playListIntent.putExtra("song_details", new Gson().toJson(categoryViewModelClass));
            playListIntent.putExtra("all_songs", new Gson().toJson(mPlayAllSongsModelClass));
            context.startActivity(playListIntent);

        });


        setTheam();

    }

    public void playMethod() {

        playSongSharedPreference.isSongPlaying("is_song_playing", true);

        setTheam();

    }

    public void pauseMethod() {

        mPlayListLayout.setVisibility(View.GONE);
        playSongSharedPreference.isSongPlaying("is_song_playing", false);

    }

    public void nextMethod() {

        setDataMethod();
    }

    public void previousMethod() {

        setDataMethod();
    }

    private void setDataMethod() {

        if (createNotification == null) {

            Intent playService = new Intent(context, CreateNotification.class);
            Util.startForegroundService(context, playService);

            context.bindService(playService, serviceConnection, Context.BIND_AUTO_CREATE);

        } else {

            playSongSharedPreference.setPlayingSongDetails("playing_song_details", new Gson().toJson(createNotification.modelClass));

            mViewSongSongName.setText(createNotification.modelClass.getTitle());
            mViewSongSongArtist.setText(createNotification.modelClass.getDescription());

            if (!createNotification.modelClass.getImg_url().equalsIgnoreCase("")) {

                Picasso.get().load(createNotification.modelClass.getImg_url()).into(mViewSongImage);

            }


        }


    }

    private void setTheam() {

        if (playSongSharedPreference.getIsSongPlaying("is_song_playing")) {

            try {

                mPlayListLayout.setVisibility(View.VISIBLE);

                String songDetails = playSongSharedPreference.getPlayingSongDetails("playing_song_details");
                CategoryViewModelClass categoryViewModelClass = new Gson().fromJson(songDetails, CategoryViewModelClass.class);

                mViewSongSongName.setText(categoryViewModelClass.getTitle());
                mViewSongSongArtist.setText(categoryViewModelClass.getDescription());

                if (!categoryViewModelClass.getImg_url().equalsIgnoreCase("")) {

                    Picasso.get().load(categoryViewModelClass.getImg_url()).into(mViewSongImage);

                }

            } catch (Exception e) {
                Log.d("Error", e.getLocalizedMessage());
            }

        } else {

            mPlayListLayout.setVisibility(View.GONE);

        }

    }


    @Override
    public View getRootView() {
        return null;
    }

    public void clearData() {

        playSongSharedPreference.isSongPlaying("is_song_playing", false);
        playSongSharedPreference.setPlayingSongDetails("playing_song_details", "");
        playSongSharedPreference.setAllSongsList("all_songs_details", "");
        playSongSharedPreference.setSongPlayedValue("song_value", "0");

    }
}
