package distinct.digitalsolutions.prabudhh.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import distinct.digitalsolutions.prabudhh.HelperClasses.CategoryViewHelperClass;
import distinct.digitalsolutions.prabudhh.HelperClasses.PostHelperClass;
import distinct.digitalsolutions.prabudhh.HelperClasses.WhichAreHelperClass;
import distinct.digitalsolutions.prabudhh.R;

public class PostActivity extends AppCompatActivity {

    private PostHelperClass mPostHelperClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPostHelperClass = new PostHelperClass(PostActivity.this,null);
        setContentView(mPostHelperClass.getRootView());

    }

    @Override
    public void onStart(){
        super.onStart();
        mPostHelperClass.initView();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        mPostHelperClass.BackMethod();

    }
}