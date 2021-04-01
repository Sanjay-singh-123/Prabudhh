package distinct.digitalsolutions.prabudhh.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

import distinct.digitalsolutions.prabudhh.Activities.MainActivity;
import distinct.digitalsolutions.prabudhh.HelperClasses.HomeHelperClass;
import distinct.digitalsolutions.prabudhh.Interfaces.NotificationInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.PaymentAlertInterface;
import distinct.digitalsolutions.prabudhh.Model.CategoryViewModelClass;

public class HomeFragment extends Fragment implements PaymentAlertInterface {

    private HomeHelperClass mHomeHelperClass;

    private MainActivity mMainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mHomeHelperClass = new HomeHelperClass(mMainActivity,inflater,container,this);

        return mHomeHelperClass.getRootView();

    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        mMainActivity = (MainActivity) activity;

    }

    @Override
    public void onStart(){
        super.onStart();

        mHomeHelperClass.initView();

    }

    @Override
    public void showAlertDialog(CategoryViewModelClass categoryViewModelClass, String categoryName,List<CategoryViewModelClass> categoryViewModelClasses
            //, String categoryId
    ) {

        mHomeHelperClass.showAlertDialog(categoryViewModelClass,categoryName,categoryViewModelClasses);

    }

}