package distinct.digitalsolutions.prabudhh.HelperClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import distinct.digitalsolutions.prabudhh.Activities.PostActivity;
import distinct.digitalsolutions.prabudhh.Adapter.WhichAreRecyclerViewAdapter;
import distinct.digitalsolutions.prabudhh.Database.FirebaseDatabaseClass;
import distinct.digitalsolutions.prabudhh.Interfaces.LoginInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.WhichAreInterface;
import distinct.digitalsolutions.prabudhh.Model.WhichAreModelClass;
import distinct.digitalsolutions.prabudhh.R;

public class WhichAreHelperClass implements LoginInterface {

    private View mRootView;

    private RelativeLayout mPostFeedLayout;
    private Activity mContext;

    private RecyclerView mWhichAreRecyclerview;
    private WhichAreRecyclerViewAdapter mWhichAreRecyclerViewAdapter;
    private List<WhichAreModelClass> mWhichAreRecyclerViewModelClass = new ArrayList<>();

    private FirebaseDatabaseClass mWhichAreFirebaseDatabase;

    public WhichAreHelperClass(Activity context, LayoutInflater inflater, ViewGroup viewGroup) {

        mRootView = inflater.inflate(R.layout.fragment_which_are, viewGroup, false);

        this.mContext = context;

        mWhichAreFirebaseDatabase = new FirebaseDatabaseClass();

    }

    @Override
    public void initView() {

        mWhichAreRecyclerview = mRootView.findViewById(R.id.which_are_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mWhichAreRecyclerview.setLayoutManager(layoutManager);

        LoadSongPosts();

    }

    private void LoadSongPosts() {

        //mWhichAreRecyclerViewModelClass.clear();

        mWhichAreFirebaseDatabase.getPostedSongs(new WhichAreInterface() {
            @Override
            public void onSuccess(List<WhichAreModelClass> categoryViewModelClasses) {

                mWhichAreRecyclerViewModelClass = categoryViewModelClasses;

                mWhichAreRecyclerViewAdapter = new WhichAreRecyclerViewAdapter(mContext, mWhichAreRecyclerViewModelClass);
                mWhichAreRecyclerview.setAdapter(mWhichAreRecyclerViewAdapter);

                mWhichAreRecyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    @Override
    public View getRootView() {

        return mRootView;

    }
}
