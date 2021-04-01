package distinct.digitalsolutions.prabudhh.HelperClasses;

import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.List;

import distinct.digitalsolutions.prabudhh.Activities.MainActivity;
import distinct.digitalsolutions.prabudhh.Activities.SingleSongActivity;
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

    public MainActivityHelperClass(MainActivity context, NotificationInterface notificationInterface) {

        this.context = context;
        this.notificationInterface = notificationInterface;
        playSongSharedPreference = new PlaySongSharedPreference(context);


    }

    @Override
    public void checkUserLoggedInOrNot(){

       // mMainActivityPresenter.getUserStatus();

    }

    @Override
    public void checkUserStatus(String userId) {

        if (userId.equalsIgnoreCase("")){

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
            CategoryViewModelClass categoryViewModelClass = new Gson().fromJson(songDetails,CategoryViewModelClass.class);

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

    public void playMethod(){

        playSongSharedPreference.isSongPlaying("is_song_playing",true);

        setTheam();
    }

    public void pauseMethod(){

        mPlayListLayout.setVisibility(View.GONE);
        playSongSharedPreference.isSongPlaying("is_song_playing",false);

    }

    private void setTheam() {

        if (playSongSharedPreference.getIsSongPlaying("is_song_playing")){

            try {

                mPlayListLayout.setVisibility(View.VISIBLE);

                String songDetails = playSongSharedPreference.getPlayingSongDetails("playing_song_details");
                CategoryViewModelClass categoryViewModelClass = new Gson().fromJson(songDetails,CategoryViewModelClass.class);

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

    public void clearData(){

        playSongSharedPreference.isSongPlaying("is_song_playing",false);
        playSongSharedPreference.setPlayingSongDetails("playing_song_details","");
        playSongSharedPreference.setAllSongsList("all_songs_details","");
        playSongSharedPreference.setSongPlayedValue("song_value","0");

    }
}
