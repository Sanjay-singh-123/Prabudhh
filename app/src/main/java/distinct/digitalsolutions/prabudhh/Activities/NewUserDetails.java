package distinct.digitalsolutions.prabudhh.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import distinct.digitalsolutions.prabudhh.HelperClasses.NewUserDetailsHelperClass;

public class NewUserDetails extends AppCompatActivity {

    private NewUserDetailsHelperClass mNewUserDetailsHelperClass;
    private String mValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mValue = getIntent().getStringExtra("value");
        mNewUserDetailsHelperClass = new NewUserDetailsHelperClass(NewUserDetails.this,null,mValue);
        setContentView(mNewUserDetailsHelperClass.getRootView());

    }

    @Override
    public void onStart(){
        super.onStart();
        mNewUserDetailsHelperClass.initView();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        mNewUserDetailsHelperClass.BackMethod();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        mNewUserDetailsHelperClass.cameraPermissions(requestCode,permissions,grantResults);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        mNewUserDetailsHelperClass.imagePlacing(requestCode,resultCode,imageReturnedIntent);

    }

}