package distinct.digitalsolutions.prabudhh.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import distinct.digitalsolutions.prabudhh.Activities.MainActivity;
import distinct.digitalsolutions.prabudhh.HelperClasses.CategoryHelperClass;

public class CategoryFragment extends Fragment {

    private CategoryHelperClass mCategoryHelperClass;

    private MainActivity mMainActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mCategoryHelperClass = new CategoryHelperClass(mMainActivity,inflater,container);

        return mCategoryHelperClass.getRootView();

    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        mMainActivity = (MainActivity) activity;

    }

    @Override
    public void onStart(){
        super.onStart();

        mCategoryHelperClass.initView();

    }
}