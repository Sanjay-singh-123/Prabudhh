package distinct.digitalsolutions.prabudhh.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import distinct.digitalsolutions.prabudhh.Activities.MainActivity;
import distinct.digitalsolutions.prabudhh.Activities.NewUserDetails;
import distinct.digitalsolutions.prabudhh.HelperClasses.ProgressBarClass;
import distinct.digitalsolutions.prabudhh.SharedPreference.UserSharedPreferences;
import distinct.digitalsolutions.prabudhh.R;

public class VerifyOtpActivity extends AppCompatActivity {

    private String mVerifyPhoneNumber;

    private String mMerchantOtpVerificationCodeID;

    private FirebaseAuth mFirebaseAuth;
    private PhoneAuthProvider.ForceResendingToken mMerchantOtpResentToken;

    private Button mVerifyOTPButton;

    private TextInputLayout mVerifyOtpLayout;
    private TextInputEditText mVerifyOtpEditText;

    private UserSharedPreferences mUserSharedPreference;

    private ProgressBar progressBar;

    private RelativeLayout mProgressBarRelativeLayout;

    private ProgressBarClass mProgressBarClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        mVerifyPhoneNumber = getIntent().getStringExtra("phone_number");

        mFirebaseAuth = FirebaseAuth.getInstance();

        mProgressBarClass = new ProgressBarClass(VerifyOtpActivity.this);

        mProgressBarRelativeLayout = findViewById(R.id.progress_bar_layout);

        progressBar = findViewById(R.id.progress_bar);

        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);

        sendOtp(mVerifyPhoneNumber);

        mUserSharedPreference = new UserSharedPreferences(this);

        mVerifyOTPButton = findViewById(R.id.login_send_otp_button);
        mVerifyOtpLayout = findViewById(R.id.login_phone_number);
        mVerifyOtpEditText = findViewById(R.id.login_phone_number_edit_text);

        mVerifyOTPButton.setOnClickListener(v -> {

            mProgressBarClass.setProgressBarVisible(mProgressBarRelativeLayout);

            String otpVerificationCode = mVerifyOtpLayout.getEditText().getText().toString().trim();

            if (otpVerificationCode.isEmpty() || otpVerificationCode.length() < 6) {

                mProgressBarClass.setProgressBarNotVisible(mProgressBarRelativeLayout);
                mVerifyOtpEditText.setError("Enter a valid OTP");
                mVerifyOtpEditText.requestFocus();
                return;

            }

            merchantVerifyVerificationCode(otpVerificationCode);

        });

    }

    private void sendOtp(String mPhoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mPhoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                verifyCallBack);


    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verifyCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            mProgressBarClass.setProgressBarVisible(mProgressBarRelativeLayout);

            signInWithPhoneAuthCredential(phoneAuthCredential);

            String merchantOtpVerificationCode = phoneAuthCredential.getSmsCode();

            if (merchantOtpVerificationCode != null) {

                mVerifyOtpEditText.setText(merchantOtpVerificationCode);

            }else {

                Log.d("Error","Main Activity Thing");
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
                Toast.makeText(VerifyOtpActivity.this, "Try again after some time", Toast.LENGTH_SHORT).show();

                Log.d("Error", e.getLocalizedMessage());

            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
                Toast.makeText(VerifyOtpActivity.this, "Try Again After Some time", Toast.LENGTH_SHORT).show();

                Log.d("Error", e.getLocalizedMessage());

            }
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            mMerchantOtpVerificationCodeID = s;
            mMerchantOtpResentToken = forceResendingToken;

        }

    };

    private void merchantVerifyVerificationCode(String merchantOTP) {

        try {

            PhoneAuthCredential merchantOtpVerificationCredentials =
                    PhoneAuthProvider.getCredential(mMerchantOtpVerificationCodeID,
                            merchantOTP);

            signInWithPhoneAuthCredential(merchantOtpVerificationCredentials);

        }catch (Exception e){

            Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();

        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential merchantOtpVerificationCredentials) {

        mFirebaseAuth.signInWithCredential(merchantOtpVerificationCredentials)
                .addOnCompleteListener(VerifyOtpActivity.this, task -> {

                    if (task.isSuccessful()) {

                        final String merchantUserId = mFirebaseAuth.getCurrentUser().getUid();

                        mUserSharedPreference.setUserId("user_id", merchantUserId);

                        if (!mUserSharedPreference.getIsFirstUser("isNewUser")) {

                            Intent intent = new Intent(VerifyOtpActivity.this, NewUserDetails.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            return;

                        }

                        Intent intent = new Intent(VerifyOtpActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                        mProgressBarClass.setProgressBarNotVisible(mProgressBarRelativeLayout);


                    } else {

                        mProgressBarClass.setProgressBarNotVisible(mProgressBarRelativeLayout);
                        Toast.makeText(VerifyOtpActivity.this, "OTP incorrect", Toast.LENGTH_SHORT).show();

                    }
                });
    }

}