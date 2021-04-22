package distinct.digitalsolutions.prabudhh.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import distinct.digitalsolutions.prabudhh.Adapter.SubCategoryViewRecyclerViewAdapter;
import distinct.digitalsolutions.prabudhh.Database.FirebaseDatabaseClass;
import distinct.digitalsolutions.prabudhh.HelperClasses.AudioPlayerBroadcastReceiver;
import distinct.digitalsolutions.prabudhh.HelperClasses.ProgressBarClass;
import distinct.digitalsolutions.prabudhh.Interfaces.CategoryFirebaseInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.NotificationInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.PaymentAlertInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.SongPostFirebaseInterface;
import distinct.digitalsolutions.prabudhh.Model.CategoryViewModelClass;
import distinct.digitalsolutions.prabudhh.R;
import distinct.digitalsolutions.prabudhh.SharedPreference.PlaySongSharedPreference;

public class SubCategoryActivity extends AppCompatActivity implements PaymentAlertInterface, NotificationInterface{

    private ImageView mSubCategoryBackButton;
    private TextView mSubCategoryName;

    private RecyclerView mSubCategoryViewRecyclerview;
    private SubCategoryViewRecyclerViewAdapter mSubSubCategoryViewRecyclerViewAdapter;
    private List<CategoryViewModelClass> mSubCategoryViewModelClass = new ArrayList<>();
    private String subCategoryName,categoryName;

    private FirebaseDatabaseClass mSubCategoryFirebaseDatabaseClass;

    private EditText mSubSearchSongHomeEditText;
    private RelativeLayout mSubSearchSongHomeLayout;
   // private PaymentAlertInterface paymentAlertInterface;

    private RelativeLayout mSubProgressBarLayout;
    private ProgressBarClass subProgressBarClass;

    public AudioPlayerBroadcastReceiver myReceiver;
   // private NotificationInterface notificationInterface;
    private PlaySongSharedPreference playSongSharedPreference;

    private TextView mSubViewSongSongName, mSubViewSongSongArtist;
    private ImageView mSubViewSongImage;
    private RelativeLayout mSubPlayListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        subCategoryName = getIntent().getStringExtra("category_name");
        categoryName = getIntent().getStringExtra("category");

        mSubCategoryFirebaseDatabaseClass = new FirebaseDatabaseClass();

        subProgressBarClass = new ProgressBarClass(SubCategoryActivity.this);

        playSongSharedPreference = new PlaySongSharedPreference(SubCategoryActivity.this);

        mSubPlayListLayout = findViewById(R.id.sub_category_view_song_layout);
        mSubViewSongSongName = findViewById(R.id.song_name_two);
        mSubViewSongSongArtist = findViewById(R.id.song_artiest_two);
        mSubViewSongImage = findViewById(R.id.song_thumbnail_two);

        IntentFilter filter = new IntentFilter();
        filter.addAction(PlayerNotificationManager.ACTION_PAUSE);
        filter.addAction(PlayerNotificationManager.ACTION_PLAY);
        filter.addAction(PlayerNotificationManager.ACTION_NEXT);
        filter.addAction(PlayerNotificationManager.ACTION_PREVIOUS);

        myReceiver = new AudioPlayerBroadcastReceiver(this);
        registerReceiver(myReceiver, filter);

        mSubProgressBarLayout = findViewById(R.id.progress_bar_layout);

        mSubSearchSongHomeEditText = findViewById(R.id.sub_category_search_songs_edit_text);
        mSubSearchSongHomeLayout = findViewById(R.id.sub_category_search_button);

        mSubCategoryViewRecyclerview = findViewById(R.id.sub_category_songs_recycler_view);

        mSubCategoryBackButton = findViewById(R.id.sub_category_back_button);
        mSubCategoryName = findViewById(R.id.sub_category_screen_title);

        mSubCategoryName.setText(subCategoryName);

        mSubSearchSongHomeEditText.setText("");

        LinearLayoutManager layoutManager = new LinearLayoutManager(SubCategoryActivity.this);

        mSubCategoryViewRecyclerview.setLayoutManager(layoutManager);

        mSubCategoryBackButton.setOnClickListener(v -> {

            backMethod();

        });

        LoadCategoryPosts();

        mSubSearchSongHomeEditText.addTextChangedListener(new TextWatcher() {
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

        mSubSearchSongHomeLayout.setOnClickListener(v -> {

            SearchSongMethod();

        });

        setTheam();


    }

    public void backMethod() {

        Intent backIntent = new Intent(SubCategoryActivity.this, CategoryViewActivity.class);
        backIntent.putExtra("category_name",categoryName);
        backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(backIntent);

    }

    private void SearchSongMethod() {

        String searchValue = mSubSearchSongHomeEditText.getText().toString().trim();

        if (TextUtils.isEmpty(searchValue)) {

            mSubCategoryViewModelClass.clear();

            LoadCategoryPosts();

        } else {

            mSubCategoryFirebaseDatabaseClass.getContent(subCategoryName, searchValue, new CategoryFirebaseInterface() {
                @Override
                public void onSuccess(List<CategoryViewModelClass> categoryViewModelClasses, List<CategoryViewModelClass> allSongsList) {

                    loadDataIntoRecyclerView(categoryViewModelClasses);
                }

                @Override
                public void onFailure(String error) {

                }
            });

        }
    }

    private void LoadCategoryPosts() {

        mSubCategoryViewModelClass.clear();

        subProgressBarClass.setProgressBarVisible(mSubProgressBarLayout);

        mSubCategoryFirebaseDatabaseClass.getContent(subCategoryName, "", new CategoryFirebaseInterface() {
            @Override
            public void onSuccess(List<CategoryViewModelClass> categoryViewModelClasses, List<CategoryViewModelClass> allSongsList) {

                subProgressBarClass.setProgressBarNotVisible(mSubProgressBarLayout);
                loadDataIntoRecyclerView(categoryViewModelClasses);

            }

            @Override
            public void onFailure(String error) {

                subProgressBarClass.setProgressBarNotVisible(mSubProgressBarLayout);

            }
        });


    }

    private void loadDataIntoRecyclerView(List<CategoryViewModelClass> categoryViewModelClasses) {

        mSubCategoryViewModelClass = categoryViewModelClasses;

        mSubSubCategoryViewRecyclerViewAdapter = new SubCategoryViewRecyclerViewAdapter(
                subCategoryName, SubCategoryActivity.this, mSubCategoryViewModelClass, this);
        mSubCategoryViewRecyclerview.setAdapter(mSubSubCategoryViewRecyclerViewAdapter);
        mSubSubCategoryViewRecyclerViewAdapter.notifyDataSetChanged();

    }


    public void showAlertDialog(CategoryViewModelClass categoryViewModelClass, String categoryName,List<CategoryViewModelClass> categoryViewHelperClasses
                                //, String mCategoryid
    ) {

        mSubCategoryFirebaseDatabaseClass.checkUserPaymentStatus(new SongPostFirebaseInterface() {
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


                    new AlertDialog.Builder(SubCategoryActivity.this)
                            .setTitle("Subscription Expired")
                            .setMessage("You need to renewal subscription plan.")
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {

                                // Continue with delete operation
                                Intent alertIntent = new Intent(SubCategoryActivity.this, PaymentActivity.class);
                                alertIntent.putExtra("category_name", categoryName);
                                //alertIntent.putExtra("category_id", mCategoryid);
                                startActivity(alertIntent);

                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.mipmap.music_icon)
                            .show();

                    return;

                }

                Intent playListIntent = new Intent(SubCategoryActivity.this, PlayListSongActivity.class);
                playListIntent.putExtra("song_details", new Gson().toJson(categoryViewModelClass));
                playListIntent.putExtra("category_name", categoryName);
                playListIntent.putExtra("all_songs",new Gson().toJson(categoryViewHelperClasses));
                //playListIntent.putExtra("category_id", mCategoryid);
                startActivity(playListIntent);

            }

            @Override
            public void onFailure(String error) {

                new AlertDialog.Builder(SubCategoryActivity.this)
                        .setTitle("Paid Content")
                        .setMessage("You need to subscribe to view this content.")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {

                            // Continue with delete operation
                            Intent alertIntent = new Intent(SubCategoryActivity.this, PaymentActivity.class);
                            alertIntent.putExtra("category_name", categoryName);
                            //    alertIntent.putExtra("category_id", mCategoryid);
                            startActivity(alertIntent);

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

        mSubPlayListLayout.setVisibility(View.GONE);
        playSongSharedPreference.isSongPlaying("is_song_playing",false);

    }

    private void setTheam() {

        if (playSongSharedPreference.getIsSongPlaying("is_song_playing")){

            try {

                mSubPlayListLayout.setVisibility(View.VISIBLE);

                String songDetails = playSongSharedPreference.getPlayingSongDetails("playing_song_details");
                CategoryViewModelClass categoryViewModelClass = new Gson().fromJson(songDetails,CategoryViewModelClass.class);

                mSubViewSongSongName.setText(categoryViewModelClass.getTitle());
                mSubViewSongSongArtist.setText(categoryViewModelClass.getDescription());

                if (!categoryViewModelClass.getImg_url().equalsIgnoreCase("")) {

                    Picasso.get().load(categoryViewModelClass.getImg_url()).into(mSubViewSongImage);

                }

            } catch (Exception e) {
                Log.d("Error", e.getLocalizedMessage());
            }

        } else {

            mSubPlayListLayout.setVisibility(View.GONE);
        }

    }


    @Override
    public void notificationEvents(int play, int pause) {

    }

}