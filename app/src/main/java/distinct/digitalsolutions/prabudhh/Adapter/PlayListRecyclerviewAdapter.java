package distinct.digitalsolutions.prabudhh.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import distinct.digitalsolutions.prabudhh.Interfaces.PaymentAlertInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.PlayListInterface;
import distinct.digitalsolutions.prabudhh.Model.CategoryViewModelClass;
import distinct.digitalsolutions.prabudhh.R;

public class PlayListRecyclerviewAdapter extends RecyclerView.Adapter<PlayListRecyclerviewAdapter.PlayListViewHolder> {

    public List<CategoryViewModelClass> mPlayListModelClass;
    public Activity mPlayListAdapter;
    private String mPlayListSubCategoryName;
    private PlayListInterface playListInterface;
    private PaymentAlertInterface paymentAlertInterface;
    private String mCategoryName;

    public PlayListRecyclerviewAdapter(Activity mPlayListAdapter, List<CategoryViewModelClass> mPlayListModelClass,
                                       String mPlayListSubCategoryName, PlayListInterface playListInterface,
                                       PaymentAlertInterface paymentAlertInterface,
                                       String mCategoryName) {

        this.mPlayListModelClass = mPlayListModelClass;
        this.mPlayListAdapter = mPlayListAdapter;
        this.mPlayListSubCategoryName = mPlayListSubCategoryName;
        this.playListInterface = playListInterface;
        this.paymentAlertInterface = paymentAlertInterface;
         this.mCategoryName = mCategoryName;

    }

    public static class PlayListViewHolder extends RecyclerView.ViewHolder {

        private ImageView mSongImage;
        private TextView mSongName;
        private TextView mSongDescription;
        private ImageView mSongPlayButton;

        public PlayListViewHolder(View view) {
            super(view);

            mSongImage = view.findViewById(R.id.play_list_song_image);

            mSongName = view.findViewById(R.id.song_title);
            mSongDescription = view.findViewById(R.id.song_artiest);

            mSongPlayButton = view.findViewById(R.id.song_play_icon);


        }
    }

    @NonNull
    @Override
    public PlayListRecyclerviewAdapter.PlayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View customerHistoryViewAdapter = LayoutInflater.from(parent.getContext()).inflate(R.layout.play_list_song_layout, parent, false);

        return new PlayListRecyclerviewAdapter.PlayListViewHolder(customerHistoryViewAdapter);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlayListRecyclerviewAdapter.PlayListViewHolder playListViewHolder, final int position) {

        CategoryViewModelClass categoryViewModelClass = mPlayListModelClass.get(position);

        if (!categoryViewModelClass.getImg_url().equalsIgnoreCase("")) {

            Picasso.get().load(categoryViewModelClass.getImg_url()).into(playListViewHolder.mSongImage);

        }

        playListViewHolder.mSongName.setText(categoryViewModelClass.getTitle());

        playListViewHolder.mSongDescription.setText(categoryViewModelClass.getDescription());

        playListViewHolder.mSongPlayButton.setOnClickListener(v -> {

            if (categoryViewModelClass.getPaid_content().equalsIgnoreCase("1")) {

                paymentAlertInterface.showAlertDialog(categoryViewModelClass, mPlayListSubCategoryName,mPlayListModelClass
                        //        , categoryId
                );

            } else {

                playListInterface.playListResponse(categoryViewModelClass, mPlayListSubCategoryName);

            }

        });
    }

    @Override
    public int getItemCount() {

        return mPlayListModelClass.size();

    }

}
