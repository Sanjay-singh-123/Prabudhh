package distinct.digitalsolutions.prabudhh.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.razorpay.PaymentResultListener;

import distinct.digitalsolutions.prabudhh.HelperClasses.PaymentHelperClass;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private PaymentHelperClass mPaymentHelperClass;

    private String mCategoryName,mCategoryId;
    private int mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCategoryName = getIntent().getStringExtra("category_name");
        mCategoryId = getIntent().getStringExtra("category_id");
        mBackButton = getIntent().getIntExtra("back_button",0);

        mPaymentHelperClass = new PaymentHelperClass(PaymentActivity.this,null,mCategoryName,mCategoryId,mBackButton);
        setContentView(mPaymentHelperClass.getRootView());

    }

    @Override
    public void onStart(){
        super.onStart();

        mPaymentHelperClass.initView();

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        mPaymentHelperClass.backMethod();

    }

    @Override
    public void onPaymentSuccess(String s) {

        mPaymentHelperClass.onPaymentSuccess(s);

    }

    @Override
    public void onPaymentError(int i, String s) {

        mPaymentHelperClass.onPaymentError(i,s);
    }
}