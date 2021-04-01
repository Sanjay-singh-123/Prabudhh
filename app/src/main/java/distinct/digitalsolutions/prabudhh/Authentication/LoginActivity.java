package distinct.digitalsolutions.prabudhh.Authentication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import distinct.digitalsolutions.prabudhh.HelperClasses.LoginHelperClass;

public class LoginActivity extends AppCompatActivity {

    private LoginHelperClass mLoginHelperClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginHelperClass = new LoginHelperClass(LoginActivity.this,null);
        setContentView(mLoginHelperClass.getRootView());

    }

    @Override
    protected void onStart(){
        super.onStart();
        mLoginHelperClass.initView();

    }

}