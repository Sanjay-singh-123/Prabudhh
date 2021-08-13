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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import distinct.digitalsolutions.prabudhh.Activities.CreateNotification;
import distinct.digitalsolutions.prabudhh.Activities.MainActivity;
import distinct.digitalsolutions.prabudhh.Activities.PaymentActivity;
import distinct.digitalsolutions.prabudhh.Activities.PlayListSongActivity;
import distinct.digitalsolutions.prabudhh.Adapter.HomeRecyclerViewAdapter;
import distinct.digitalsolutions.prabudhh.Adapter.SubCategoryViewRecyclerViewAdapter;
import distinct.digitalsolutions.prabudhh.Database.FirebaseDatabaseClass;
import distinct.digitalsolutions.prabudhh.Interfaces.CategoryFirebaseInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.FirebaseDatabaseInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.LoginInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.NotificationInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.PaymentAlertInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.SongPostFirebaseInterface;
import distinct.digitalsolutions.prabudhh.Model.CategoryViewModelClass;
import distinct.digitalsolutions.prabudhh.Model.HomeModelClass;
import distinct.digitalsolutions.prabudhh.R;
import distinct.digitalsolutions.prabudhh.SharedPreference.PlaySongSharedPreference;

public class CategoryViewHelperClass implements LoginInterface {

    private View mRootView;

    private Activity mContext;

    private ImageView mCategoryBackButton;
    private TextView mCategoryName;

    private RecyclerView mCategoryViewRecyclerview;
    private HomeRecyclerViewAdapter mCategoryViewRecyclerViewAdapter;
    private List<HomeModelClass> mCategoryViewModelClass = new ArrayList<>();
    private String categoryName;

    private FirebaseDatabaseClass mCategoryFirebaseDatabaseClass;

    private EditText mSearchSongHomeEditText;
    private RelativeLayout mSearchSongHomeLayout;
    // private String mCategoryid;
    private PaymentAlertInterface paymentAlertInterface;

    private RelativeLayout mProgressBarLayout;
    private ProgressBarClass progressBarClass;

    public AudioPlayerBroadcastReceiver myReceiver;
    private NotificationInterface notificationInterface;
    private PlaySongSharedPreference playSongSharedPreference;

    private TextView mViewSongSongName, mViewSongSongArtist;
    private ImageView mViewSongImage;
    private RelativeLayout mPlayListLayout;

    private CreateNotification createNotification;
    private Handler mSetDetailsHandler = new Handler();

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            try {

                CreateNotification.MusicBinder mServiceBinder = (CreateNotification.MusicBinder) service;
                createNotification = mServiceBinder.getService();

            } catch (Exception e) {
                Log.d("Error_Value", e.getLocalizedMessage());
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.exit(0);
        }

    };

    public CategoryViewHelperClass(Activity context, ViewGroup viewGroup, String categoryName,
                                   //String mCategoryid
                                   PaymentAlertInterface paymentAlertInterface,NotificationInterface notificationInterface) {

        mRootView = LayoutInflater.from(context).inflate(R.layout.activity_category_view, viewGroup, false);
        this.categoryName = categoryName;
        this.mContext = context;
        //this.mCategoryid = mCategoryid;

        this.paymentAlertInterface = paymentAlertInterface;

        mCategoryFirebaseDatabaseClass = new FirebaseDatabaseClass(mContext);

        progressBarClass = new ProgressBarClass(mContext);

        playSongSharedPreference = new PlaySongSharedPreference(context);

        this.notificationInterface = notificationInterface;

    }

    @Override
    public void initView() {

        mPlayListLayout = mRootView.findViewById(R.id.view_song_layout);
        mViewSongSongName = mRootView.findViewById(R.id.song_name_two);
        mViewSongSongArtist = mRootView.findViewById(R.id.song_artiest_two);
        mViewSongImage = mRootView.findViewById(R.id.song_thumbnail_two);

        IntentFilter filter = new IntentFilter();
        filter.addAction(PlayerNotificationManager.ACTION_PAUSE);
        filter.addAction(PlayerNotificationManager.ACTION_PLAY);
        filter.addAction(PlayerNotificationManager.ACTION_NEXT);
        filter.addAction(PlayerNotificationManager.ACTION_PREVIOUS);

        myReceiver = new AudioPlayerBroadcastReceiver(notificationInterface);
        mContext.registerReceiver(myReceiver, filter);

        mProgressBarLayout = mRootView.findViewById(R.id.progress_bar_layout);

        mSearchSongHomeEditText = mRootView.findViewById(R.id.search_songs_edit_text);
        mSearchSongHomeLayout = mRootView.findViewById(R.id.search_button);

        mCategoryViewRecyclerview = mRootView.findViewById(R.id.songs_recycler_view);

        mCategoryBackButton = mRootView.findViewById(R.id.back_button);
        mCategoryName = mRootView.findViewById(R.id.screen_title);

        mCategoryName.setText(categoryName);

        mSearchSongHomeEditText.setText("");

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);

        mCategoryViewRecyclerview.setLayoutManager(layoutManager);

        mCategoryBackButton.setOnClickListener(v -> {

            backMethod();

        });

        LoadCategoryPosts();

        mSearchSongHomeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                SearchSongMethod();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchSongHomeLayout.setOnClickListener(v -> {

            SearchSongMethod();

        });

        setTheam();

    }

    public void backMethod() {
        Intent backIntent = new Intent(mContext, MainActivity.class);
        backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(backIntent);

    }

    private void SearchSongMethod() {

        String searchValue = mSearchSongHomeEditText.getText().toString().trim();

        if (TextUtils.isEmpty(searchValue)) {

            mCategoryViewModelClass.clear();

            LoadCategoryPosts();

        } else {

            mCategoryFirebaseDatabaseClass.getSubCategoriesList(categoryName, searchValue, new FirebaseDatabaseInterface() {
                @Override
                public void onSuccess(List<HomeModelClass> homeModelClasses) {

                    loadDataIntoRecyclerView(homeModelClasses);

                }

                @Override
                public void onFailure(String error) {

                }
            });

        }
    }

    private void LoadCategoryPosts() {

        mCategoryViewModelClass.clear();

        progressBarClass.setProgressBarVisible(mProgressBarLayout);

        mCategoryFirebaseDatabaseClass.getSubCategoriesList(categoryName, "", new FirebaseDatabaseInterface() {

            @Override
            public void onSuccess(List<HomeModelClass> homeModelClasses) {

                progressBarClass.setProgressBarNotVisible(mProgressBarLayout);
                loadDataIntoRecyclerView(homeModelClasses);

            }

            @Override
            public void onFailure(String error) {

                progressBarClass.setProgressBarNotVisible(mProgressBarLayout);

            }
        });


    }

    private void loadDataIntoRecyclerView(List<HomeModelClass> homeModelClasses) {

        mCategoryViewModelClass = homeModelClasses;

        mCategoryViewRecyclerViewAdapter = new HomeRecyclerViewAdapter(mContext,homeModelClasses,1,categoryName);
        mCategoryViewRecyclerview.setAdapter(mCategoryViewRecyclerViewAdapter);
        mCategoryViewRecyclerViewAdapter.notifyDataSetChanged();

    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    public void showAlertDialog(CategoryViewModelClass categoryViewModelClass, String categoryName,List<CategoryViewModelClass> categoryViewHelperClasses
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
                                mContext.startActivity(alertIntent);

                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.mipmap.music_icon)
                            .show();

                    return;

                }

                Intent playListIntent = new Intent(mContext, PlayListSongActivity.class);
                playListIntent.putExtra("song_details", new Gson().toJson(categoryViewModelClass));
                playListIntent.putExtra("category_name", categoryName);
                playListIntent.putExtra("all_songs",new Gson().toJson(categoryViewHelperClasses));
                //playListIntent.putExtra("category_id", mCategoryid);
                mContext.startActivity(playListIntent);

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
                        //    alertIntent.putExtra("category_id", mCategoryid);
                            mContext.startActivity(alertIntent);

                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.mipmap.music_icon)
                        .show();

            }
        });
    }

    public void playMethod(){

        playSongSharedPreference.isSongPlaying("is_song_playing",true);
        setTheam();

    }

    public void pauseMethod(){

        mPlayListLayout.setVisibility(View.GONE);
        playSongSharedPreference.isSongPlaying("is_song_playing",false);

    }

    public void nextMethod(){

        mSetDetailsHandler.postDelayed(() -> {

            setDataMethod();

        }, 1000);


    }

    public void previousMethod(){

        mSetDetailsHandler.postDelayed(() -> {

            setDataMethod();

        }, 1000);


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

    private void setDataMethod() {

        if (createNotification == null) {

            Intent playService = new Intent(mContext, CreateNotification.class);
            Util.startForegroundService(mContext, playService);

            mContext.bindService(playService, serviceConnection, Context.BIND_AUTO_CREATE);

        } else {

            playSongSharedPreference.setPlayingSongDetails("playing_song_details", new Gson().toJson(createNotification.modelClass));

            mViewSongSongName.setText(createNotification.modelClass.getTitle());
            mViewSongSongArtist.setText(createNotification.modelClass.getDescription());

            if (!createNotification.modelClass.getImg_url().equalsIgnoreCase("")) {

                Picasso.get().load(createNotification.modelClass.getImg_url()).into(mViewSongImage);

            }
        }
    }
}
