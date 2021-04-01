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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import distinct.digitalsolutions.prabudhh.Activities.CategoryViewActivity;
import distinct.digitalsolutions.prabudhh.Model.HomeModelClass;
import distinct.digitalsolutions.prabudhh.Model.WhichAreModelClass;
import distinct.digitalsolutions.prabudhh.R;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.HomeRecyclerViewHolder> {

    public List<HomeModelClass> mHomeModelClass;
    public Activity mHomeActivity;

    public HomeRecyclerViewAdapter(Activity mHomeActivity, List<HomeModelClass> mHomeModelClass) {

        this.mHomeModelClass = mHomeModelClass;
        this.mHomeActivity = mHomeActivity;
    }

    public static class HomeRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView mCategoryName;
        private ImageView mCategoryImage;
        private CardView mCategoryLayout;

        public HomeRecyclerViewHolder(View view) {
            super(view);

            mCategoryImage = view.findViewById(R.id.category_image);
            mCategoryLayout = view.findViewById(R.id.category_layout);
        }
    }

    @NonNull
    @Override
    public HomeRecyclerViewAdapter.HomeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View customerHistoryViewAdapter = LayoutInflater.from(parent.getContext()).inflate(R.layout.categroy_single_view, parent, false);

        return new HomeRecyclerViewAdapter.HomeRecyclerViewHolder(customerHistoryViewAdapter);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeRecyclerViewAdapter.HomeRecyclerViewHolder homeRecyclerViewHolder, final int position) {

          HomeModelClass homeModelClass = mHomeModelClass.get(position);

        if (!homeModelClass.getThumbnail().equalsIgnoreCase("")){

            Picasso.get().load(homeModelClass.getThumbnail()).into(homeRecyclerViewHolder.mCategoryImage);
        }

        homeRecyclerViewHolder.mCategoryLayout.setOnClickListener(v -> {

            Intent homeIntent = new Intent(mHomeActivity, CategoryViewActivity.class);
            homeIntent.putExtra("category_name",homeModelClass.getCategory_name());
         //   homeIntent.putExtra("category_id",homeModelClass.getCategory_id());
            mHomeActivity.startActivity(homeIntent);

        });
    }

    @Override
    public int getItemCount() {

        return mHomeModelClass.size();

    }

}
