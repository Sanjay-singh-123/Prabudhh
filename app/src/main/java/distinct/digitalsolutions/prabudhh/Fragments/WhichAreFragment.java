package distinct.digitalsolutions.prabudhh.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import distinct.digitalsolutions.prabudhh.Activities.MainActivity;
import distinct.digitalsolutions.prabudhh.HelperClasses.WhichAreHelperClass;

public class WhichAreFragment extends Fragment {

    private View mWhichAreView;
    private WhichAreHelperClass mWhichAreHelperClass;

    private MainActivity mMainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mWhichAreHelperClass = new WhichAreHelperClass(mMainActivity,inflater,container);

        return mWhichAreHelperClass.getRootView();

    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        mMainActivity = (MainActivity) activity;

    }

    @Override
    public void onStart(){
        super.onStart();

        mWhichAreHelperClass.initView();

    }
}