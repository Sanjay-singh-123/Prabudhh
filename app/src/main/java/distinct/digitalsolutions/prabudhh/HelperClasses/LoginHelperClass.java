package distinct.digitalsolutions.prabudhh.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import distinct.digitalsolutions.prabudhh.Authentication.VerifyOtpActivity;
import distinct.digitalsolutions.prabudhh.Interfaces.LoginInterface;
import distinct.digitalsolutions.prabudhh.R;

public class LoginHelperClass implements LoginInterface {

    private View loginRootView;

    private Context mLoginContext;

    private Button mLoginButton;
    private TextInputLayout mPhoneNumberEditText;

    public LoginHelperClass(Context context, ViewGroup viewGroup) {

        mLoginContext = context;

        loginRootView = LayoutInflater.from(context).inflate(R.layout.activity_login, viewGroup);

    }

    @Override
    public View getRootView() {

        return loginRootView;

    }

    @Override
    public void initView() {

        mLoginButton = loginRootView.findViewById(R.id.login_send_otp_button);
        mPhoneNumberEditText = loginRootView.findViewById(R.id.login_phone_number);

        mLoginButton.setOnClickListener(v -> {

            String loginMobileNumber = mPhoneNumberEditText.getEditText().getText().toString();

            if (loginMobileNumber.isEmpty() || loginMobileNumber.length() < 10) {

                mPhoneNumberEditText.setError("Enter a valid number");
                mPhoneNumberEditText.requestFocus();
                return;

            }

            Intent intent = new Intent(mLoginContext, VerifyOtpActivity.class);
            intent.putExtra("phone_number", loginMobileNumber);
            mLoginContext.startActivity(intent);


        });
        
    }

}
