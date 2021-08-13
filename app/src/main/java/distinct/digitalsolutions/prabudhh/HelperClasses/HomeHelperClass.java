package distinct.digitalsolutions.prabudhh.HelperClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import distinct.digitalsolutions.prabudhh.Activities.CreateNotification;
import distinct.digitalsolutions.prabudhh.Activities.PaymentActivity;
import distinct.digitalsolutions.prabudhh.Activities.PlayListSongActivity;
import distinct.digitalsolutions.prabudhh.Adapter.SubCategoryViewRecyclerViewAdapter;
import distinct.digitalsolutions.prabudhh.Database.FirebaseDatabaseClass;
import distinct.digitalsolutions.prabudhh.Interfaces.CategoryFirebaseInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.LoginInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.MostPlayedSongInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.PaymentAlertInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.SongPostFirebaseInterface;
import distinct.digitalsolutions.prabudhh.Model.CategoryViewModelClass;
import distinct.digitalsolutions.prabudhh.Model.MostlyPlayed;
import distinct.digitalsolutions.prabudhh.R;

public class HomeHelperClass implements LoginInterface {

    private View mHomeHelperRecyclerView;

    private FirebaseDatabaseClass firebaseDatabaseClass;

    private RecyclerView mHomeMostlyPlayedRecyclerView;
    private SubCategoryViewRecyclerViewAdapter mHomeMostlyPlayedAdapter;
    private List<CategoryViewModelClass> mHomeCategoryModelClass = new ArrayList<>();

    private List<MostlyPlayed> mMostlyPlayed = new ArrayList<>();

    private RecyclerView mHomeRecommendedRecyclerView;
    private SubCategoryViewRecyclerViewAdapter mHomeRecommendedAdapter;
    private List<CategoryViewModelClass> mHomeRecommendedPlayList = new ArrayList<>();

    private Activity mContext;
    private RelativeLayout mHomeProgressBar;
    private ProgressBarClass progressBarClass;

    private TextView mRecommendedHeader, mMostlyPlayedHeader;
    private PaymentAlertInterface paymentAlertInterface;

    public HomeHelperClass(Activity mContext, LayoutInflater inflater, ViewGroup viewGroup, PaymentAlertInterface paymentAlertInterface) {

        this.paymentAlertInterface = paymentAlertInterface;
        this.mContext = mContext;

        mHomeHelperRecyclerView = inflater.inflate(R.layout.fragment_home, viewGroup, false);

        firebaseDatabaseClass = new FirebaseDatabaseClass(mContext);
        progressBarClass = new ProgressBarClass(mContext);

    }

    @Override
    public void initView() {

        mHomeProgressBar = mContext.findViewById(R.id.progress_bar_layout);

        mRecommendedHeader = mContext.findViewById(R.id.recommended_text);
        mMostlyPlayedHeader = mContext.findViewById(R.id.mostly_played_text);

        //Mostly Played Songs
        mHomeMostlyPlayedRecyclerView = mContext.findViewById(R.id.mostly_played_recycler_view);
        mHomeMostlyPlayedRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager mostlyPlayedLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false);
        mHomeMostlyPlayedRecyclerView.setLayoutManager(mostlyPlayedLayoutManager);

        mRecommendedHeader.setVisibility(View.GONE);

        //Recommended Played Songs
        mHomeRecommendedRecyclerView = mContext.findViewById(R.id.recommended_recycler_view);
        mHomeRecommendedRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager recommendedLayoutManger = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mHomeRecommendedRecyclerView.setLayoutManager(recommendedLayoutManger);

        mMostlyPlayedHeader.setVisibility(View.GONE);

        progressBarClass.setProgressBarVisible(mHomeProgressBar);

        LoadRecommendedSongs();
        LoadMostlyPlayedSongs();

    }

    private void LoadRecommendedSongs() {

        mHomeRecommendedPlayList.clear();

        firebaseDatabaseClass.getRecommendedSongs(new CategoryFirebaseInterface() {
            @Override
            public void onSuccess(List<CategoryViewModelClass> categoryViewModelClasses, List<CategoryViewModelClass> allSongDetails) {

                mHomeRecommendedPlayList = categoryViewModelClasses;

                mHomeRecommendedAdapter = new SubCategoryViewRecyclerViewAdapter("Recommended Play List", mContext, mHomeRecommendedPlayList, paymentAlertInterface,"");
                mHomeRecommendedRecyclerView.setAdapter(mHomeRecommendedAdapter);
                mHomeRecommendedAdapter.notifyDataSetChanged();

                mRecommendedHeader.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(String error) {

                mRecommendedHeader.setVisibility(View.VISIBLE);

            }
        });
    }

    private void LoadMostlyPlayedSongs() {

        mHomeCategoryModelClass.clear();

        firebaseDatabaseClass.getMostlyPlayedSongs(new MostPlayedSongInterface() {
            @Override
            public void onSuccess(List<MostlyPlayed> mostlyPlayeds) {

                mMostlyPlayed = mostlyPlayeds;

                Collections.sort(mMostlyPlayed);

                if (mMostlyPlayed.size() > 10) {

                    mMostlyPlayed.subList(11, mMostlyPlayed.size()).clear();

                }

                firebaseDatabaseClass.getFinalMostlyPlayedSongsData(mMostlyPlayed, new CategoryFirebaseInterface() {
                    @Override
                    public void onSuccess(List<CategoryViewModelClass> categoryViewModelClasses, List<CategoryViewModelClass> allSongDetails) {

                        mHomeCategoryModelClass = categoryViewModelClasses;

                        Collections.sort(mHomeCategoryModelClass);

                        mHomeMostlyPlayedAdapter = new SubCategoryViewRecyclerViewAdapter("Mostly Played", mContext, mHomeCategoryModelClass, paymentAlertInterface,"");
                        mHomeMostlyPlayedRecyclerView.setAdapter(mHomeMostlyPlayedAdapter);
                        mHomeMostlyPlayedAdapter.notifyDataSetChanged();

                        mMostlyPlayedHeader.setVisibility(View.VISIBLE);

                        progressBarClass.setProgressBarNotVisible(mHomeProgressBar);

                    }

                    @Override
                    public void onFailure(String error) {

                        mMostlyPlayedHeader.setVisibility(View.VISIBLE);

                        progressBarClass.setProgressBarNotVisible(mHomeProgressBar);

                    }
                });
            }

            @Override
            public void onFailure(String failed) {

                mMostlyPlayedHeader.setVisibility(View.VISIBLE);

                progressBarClass.setProgressBarNotVisible(mHomeProgressBar);

            }
        });

    }

    @Override
    public View getRootView() {
        return mHomeHelperRecyclerView;
    }

    public void showAlertDialog(CategoryViewModelClass categoryViewModelClass, String categoryName, List<CategoryViewModelClass> categoryViewModelClasses) {

        firebaseDatabaseClass.checkUserPaymentStatus(new SongPostFirebaseInterface() {
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

//                if (TextUtils.isEmpty(mCategoryid) || mCategoryid == null) {
//
//                    Intent intent = new Intent(mContext, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    mContext.startActivity(intent);
//                    return;
//
//                }
//
                Intent playListIntent = new Intent(mContext, PlayListSongActivity.class);
                playListIntent.putExtra("song_details", new Gson().toJson(categoryViewModelClass));
                playListIntent.putExtra("category_name", categoryName);
                playListIntent.putExtra("back_button", 1);
                playListIntent.putExtra("all_songs", new Gson().toJson(categoryViewModelClasses));
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
