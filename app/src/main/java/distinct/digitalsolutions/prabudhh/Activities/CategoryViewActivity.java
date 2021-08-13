package distinct.digitalsolutions.prabudhh.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import distinct.digitalsolutions.prabudhh.HelperClasses.CategoryViewHelperClass;
import distinct.digitalsolutions.prabudhh.Interfaces.NotificationInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.PaymentAlertInterface;
import distinct.digitalsolutions.prabudhh.Model.CategoryViewModelClass;

public class CategoryViewActivity extends AppCompatActivity implements PaymentAlertInterface, NotificationInterface {

    private CategoryViewHelperClass mCategoryViewHolderClass;
    private String mCategoryName;
    // private String mCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCategoryName = getIntent().getStringExtra("category_name");
        // mCategoryId = getIntent().getStringExtra("category_id");

        mCategoryViewHolderClass = new CategoryViewHelperClass(CategoryViewActivity.this, null, mCategoryName, this, this);
        setContentView(mCategoryViewHolderClass.getRootView());

    }

    @Override
    public void onStart() {
        super.onStart();
        mCategoryViewHolderClass.initView();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(CategoryViewActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    @Override
    public void showAlertDialog(CategoryViewModelClass categoryViewModelClass, String categoryName, List<CategoryViewModelClass> categoryViewModelClasses
                                //,String categoryId
    ) {

        mCategoryViewHolderClass.showAlertDialog(categoryViewModelClass, categoryName, categoryViewModelClasses
                //,categoryId
        );

    }

    @Override
    public void notificationEvents(int play, int pause,int next,int previous) {

        if (play == 1) {
            mCategoryViewHolderClass.playMethod();

        } else if (pause == 1) {

            mCategoryViewHolderClass.pauseMethod();

        }else if (next == 1){

            mCategoryViewHolderClass.nextMethod();

        }else if (previous == 1){

            mCategoryViewHolderClass.previousMethod();
        }

    }
}