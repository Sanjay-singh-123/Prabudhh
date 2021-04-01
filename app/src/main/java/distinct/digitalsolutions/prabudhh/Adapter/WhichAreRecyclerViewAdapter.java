package distinct.digitalsolutions.prabudhh.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import distinct.digitalsolutions.prabudhh.Activities.SinglePostViewActivity;
import distinct.digitalsolutions.prabudhh.Model.WhichAreModelClass;
import distinct.digitalsolutions.prabudhh.R;

public class WhichAreRecyclerViewAdapter extends RecyclerView.Adapter<WhichAreRecyclerViewAdapter.WhichAreRecyclerViewHolder> {

    public List<WhichAreModelClass> mWhichAreModelClass;
    public Activity mWhichAreActivity;

    public WhichAreRecyclerViewAdapter(Activity mWhichAreActivity, List<WhichAreModelClass> mWhichAreModelClass) {

        this.mWhichAreModelClass = mWhichAreModelClass;
        this.mWhichAreActivity = mWhichAreActivity;
    }

    public static class WhichAreRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView mSongUserName, mSongFullText;
        private CircleImageView mSongUserProfile;
        private LinearLayout mCompleteView;

        public WhichAreRecyclerViewHolder(View view) {
            super(view);

            mSongUserName = view.findViewById(R.id.post_user_name);
            mSongFullText = view.findViewById(R.id.song_edit_text);

            mSongUserProfile = view.findViewById(R.id.post_user_profile_picture);

            mCompleteView = view.findViewById(R.id.complete_view);

        }
    }

    @NonNull
    @Override
    public WhichAreRecyclerViewAdapter.WhichAreRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View customerHistoryViewAdapter = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_view_single_layout, parent, false);

        return new WhichAreRecyclerViewAdapter.WhichAreRecyclerViewHolder(customerHistoryViewAdapter);
    }

    @Override
    public void onBindViewHolder(@NonNull final WhichAreRecyclerViewAdapter.WhichAreRecyclerViewHolder whichAreRecyclerViewHolder, final int position) {

        WhichAreModelClass whichAreModelClass = mWhichAreModelClass.get(position);

        if (!whichAreModelClass.getThumbNail().equalsIgnoreCase("")){

            Picasso.get().load(whichAreModelClass.getThumbNail()).placeholder(R.mipmap.user_image).into(whichAreRecyclerViewHolder.mSongUserProfile);
        }

        whichAreRecyclerViewHolder.mSongUserName.setText(whichAreModelClass.getUserName());

        whichAreRecyclerViewHolder.mSongFullText.setText(whichAreModelClass.getSong());

        whichAreRecyclerViewHolder.mCompleteView.setOnClickListener(v -> {

            Intent songIntent = new Intent(mWhichAreActivity, SinglePostViewActivity.class);
            songIntent.putExtra("single_song_value",new Gson().toJson(whichAreModelClass));
            songIntent.putExtra("user_profile",whichAreModelClass.getThumbNail());
            mWhichAreActivity.startActivity(songIntent);


        });

    }

    @Override
    public int getItemCount() {

        return mWhichAreModelClass.size();

    }

}
