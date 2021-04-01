package distinct.digitalsolutions.prabudhh.HelperClasses;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import distinct.digitalsolutions.prabudhh.Adapter.HomeRecyclerViewAdapter;
import distinct.digitalsolutions.prabudhh.Database.FirebaseDatabaseClass;
import distinct.digitalsolutions.prabudhh.Interfaces.FirebaseDatabaseInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.LoginInterface;
import distinct.digitalsolutions.prabudhh.Model.HomeModelClass;
import distinct.digitalsolutions.prabudhh.R;

public class CategoryHelperClass implements LoginInterface {

    private View mRootView;

    private Activity mContext;

    private RecyclerView mHomeRecyclerView;
    private HomeRecyclerViewAdapter mHomeRecyclerViewAdapter;
    private List<HomeModelClass> mHomeModelClass = new ArrayList<>();

    private FirebaseDatabaseClass mHomeFirebaseDataClass;

    private EditText mSearchHomeEditText;
    private RelativeLayout mSearchHomeLayout;
    private RelativeLayout mHomeProgressBar;
    private ProgressBarClass progressBarClass;


    public CategoryHelperClass(Activity context, LayoutInflater inflater, ViewGroup viewGroup) {

        mRootView = inflater.inflate(R.layout.fragment_category, viewGroup, false);

        this.mContext = context;

        mHomeFirebaseDataClass = new FirebaseDatabaseClass();

        progressBarClass = new ProgressBarClass(mContext);


    }

    @Override
    public void initView() {

        mHomeProgressBar = mRootView.findViewById(R.id.progress_bar_layout);
        mSearchHomeEditText = mRootView.findViewById(R.id.search_edit_text);
        mSearchHomeLayout = mRootView.findViewById(R.id.search_button);

        mHomeRecyclerView = mRootView.findViewById(R.id.home_recycler_view);
        mHomeRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mHomeRecyclerView.setLayoutManager(layoutManager);

        LoadCategoryPosts();

        mSearchHomeEditText.setText("");

        mSearchHomeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                SearchCategoryMethod();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchHomeLayout.setOnClickListener(v -> {

            SearchCategoryMethod();

        });

    }

    private void SearchCategoryMethod() {

        String searchValue = mSearchHomeEditText.getText().toString().trim();

        if (TextUtils.isEmpty(searchValue)) {

            mHomeModelClass.clear();

            LoadCategoryPosts();

        } else {

            mHomeFirebaseDataClass.getCategories(searchValue.trim(), new FirebaseDatabaseInterface() {
                @Override
                public void onSuccess(List<HomeModelClass> homeModelClasses) {

                    loadDataIntoRecyclerView(homeModelClasses);

                }

                @Override
                public void onFailure(String error) {

                }
            });

        }
    }

    private void LoadCategoryPosts() {


        progressBarClass.setProgressBarVisible(mHomeProgressBar);

        mHomeFirebaseDataClass.getCategories("", new FirebaseDatabaseInterface() {
            @Override
            public void onSuccess(List<HomeModelClass> homeModelClasses) {

                loadDataIntoRecyclerView(homeModelClasses);

                progressBarClass.setProgressBarNotVisible(mHomeProgressBar);

            }

            @Override
            public void onFailure(String error) {

                progressBarClass.setProgressBarNotVisible(mHomeProgressBar);

            }
        });

    }

    private void loadDataIntoRecyclerView(List<HomeModelClass> homeModelClasses) {

        mHomeModelClass.clear();

        mHomeModelClass = homeModelClasses;

        mHomeRecyclerViewAdapter = new HomeRecyclerViewAdapter(mContext, mHomeModelClass);
        mHomeRecyclerView.setAdapter(mHomeRecyclerViewAdapter);
        mHomeRecyclerViewAdapter.notifyDataSetChanged();

    }

    @Override
    public View getRootView() {

        return mRootView;
    }
}
