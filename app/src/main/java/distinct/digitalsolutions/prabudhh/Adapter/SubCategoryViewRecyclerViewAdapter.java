package distinct.digitalsolutions.prabudhh.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import distinct.digitalsolutions.prabudhh.Activities.PlayListSongActivity;
import distinct.digitalsolutions.prabudhh.Interfaces.PaymentAlertInterface;
import distinct.digitalsolutions.prabudhh.Model.CategoryViewModelClass;
import distinct.digitalsolutions.prabudhh.R;
import distinct.digitalsolutions.prabudhh.SharedPreference.UserSharedPreferences;

public class SubCategoryViewRecyclerViewAdapter extends RecyclerView.Adapter<SubCategoryViewRecyclerViewAdapter.CategoryViewHolder> {

    public List<CategoryViewModelClass> mCategoryModelClass;
    public Activity mCategoryViewActivity;
    public String categoryName;
    private UserSharedPreferences mUserSharedPreference;
    private PaymentAlertInterface paymentAlertInterface;

    public SubCategoryViewRecyclerViewAdapter(
            //String categoryId,
            String categoryname, Activity mCategoryViewActivity,
            List<CategoryViewModelClass> mCategoryModelClass, PaymentAlertInterface paymentAlertInterface) {

        this.mCategoryModelClass = mCategoryModelClass;
        this.mCategoryViewActivity = mCategoryViewActivity;
        this.categoryName = categoryname;
        //this.categoryId = categoryId;

        this.paymentAlertInterface = paymentAlertInterface;
        mUserSharedPreference = new UserSharedPreferences(mCategoryViewActivity);


    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mViewSongLayout;
        private TextView mSongName;
        private ImageView mSongImage;

        public CategoryViewHolder(View view) {
            super(view);

            mViewSongLayout = view.findViewById(R.id.song_layout);
            mSongName = view.findViewById(R.id.song_name);
            mSongImage = view.findViewById(R.id.song_image);

        }
    }

    @NonNull
    @Override
    public SubCategoryViewRecyclerViewAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View customerHistoryViewAdapter;

        if (categoryName.equalsIgnoreCase("Recommended Play List") || categoryName.equalsIgnoreCase("Mostly Played")) {

            customerHistoryViewAdapter = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_recommended_layout, parent, false);

        } else {

            customerHistoryViewAdapter = LayoutInflater.from(parent.getContext()).inflate(R.layout.songs_view_layout, parent, false);

        }

        return new SubCategoryViewRecyclerViewAdapter.CategoryViewHolder(customerHistoryViewAdapter);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubCategoryViewRecyclerViewAdapter.CategoryViewHolder categoryViewHolder, final int position) {

        CategoryViewModelClass categoryViewModelClass = mCategoryModelClass.get(position);

        String songName = categoryViewModelClass.getTitle().substring(0, 1).toUpperCase() + categoryViewModelClass.getTitle().substring(1);

        if (!categoryViewModelClass.getImg_url().equalsIgnoreCase("")) {

            Picasso.get().load(categoryViewModelClass.getImg_url()).into(categoryViewHolder.mSongImage);

        }

        categoryViewHolder.mSongName.setText(songName);

        categoryViewHolder.mViewSongLayout.setOnClickListener(v -> {

            if (categoryViewModelClass.getPaid_content().equalsIgnoreCase("1")) {

                paymentAlertInterface.showAlertDialog(categoryViewModelClass, categoryName, mCategoryModelClass
                        //        categoryId,
                );

            } else {


                Intent playListIntent = new Intent(mCategoryViewActivity, PlayListSongActivity.class);
                playListIntent.putExtra("song_details", new Gson().toJson(categoryViewModelClass));
                playListIntent.putExtra("category_name", categoryName);

                if (categoryName.equalsIgnoreCase("Mostly Played") || categoryName.equalsIgnoreCase("Recommended Play List")) {

                    playListIntent.putExtra("back_button", 1);
                }

                playListIntent.putExtra("all_songs", new Gson().toJson(mCategoryModelClass));
                mCategoryViewActivity.startActivity(playListIntent);

            }

        });

    }

    @Override
    public int getItemCount() {

        return mCategoryModelClass.size();

    }

}
