package distinct.digitalsolutions.prabudhh.HelperClasses;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.razorpay.Checkout;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import distinct.digitalsolutions.prabudhh.Activities.CategoryViewActivity;
import distinct.digitalsolutions.prabudhh.Activities.MainActivity;
import distinct.digitalsolutions.prabudhh.Activities.SuccessFailedPaymentStatusScreen;
import distinct.digitalsolutions.prabudhh.Database.FirebaseDatabaseClass;
import distinct.digitalsolutions.prabudhh.Interfaces.LoginInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.PaymentInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.SongPostFirebaseInterface;
import distinct.digitalsolutions.prabudhh.R;

public class PaymentHelperClass implements LoginInterface {

    private static final String TAG = "Payment";
    private View mRootView;
    private Activity mContext;

    private TextView mMonthlyBill, mYearlyBill;
    private Button mMonthlyBillButton, mYearlyBillButton;

    private String mMonthlyPrice, mYearlyPrice;

    private FirebaseAuth mFirebaseAuth;
    private String PhoneNumber;

    private ImageView mBackButton;

    private FirebaseDatabaseClass mPaymentFirebaseClass;
    private String categoryName, categoryId;

    private String currentDate, expiryDate;
    private String mPaymentType;
    private int mBackButtonValue;

    public PaymentHelperClass(Activity mContext, ViewGroup viewGroup, String categoryName, String categoryId,int mBackButtonValue) {

        mRootView = LayoutInflater.from(mContext).inflate(R.layout.activity_payment, viewGroup);

        mPaymentFirebaseClass = new FirebaseDatabaseClass();

        this.mContext = mContext;

        this.categoryName = categoryName;
        this.categoryId = categoryId;

        this.mBackButtonValue = mBackButtonValue;
    }

    @Override
    public void initView() {

        mMonthlyBill = mRootView.findViewById(R.id.month_plan_price);
        mYearlyBill = mRootView.findViewById(R.id.year_plan_price);

        mBackButton = mRootView.findViewById(R.id.payment_header_icon);

        mMonthlyBillButton = mRootView.findViewById(R.id.buy_month_plan_button);
        mYearlyBillButton = mRootView.findViewById(R.id.buy_year_plan_button);

        PhoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        mPaymentFirebaseClass.getPaymentPrices(new PaymentInterface() {
            @Override
            public void payment(String monthlyPayment, String yearlyPayment) {

                try {

                    mMonthlyBill.setText(String.format("RS : %s/-", monthlyPayment));
                    mYearlyBill.setText(String.format("RS : %s/-", yearlyPayment));

                    mMonthlyPrice = monthlyPayment;
                    mYearlyPrice = yearlyPayment;

                } catch (Exception e) {

                    Log.d("Error", e.getLocalizedMessage());
                }

            }

            @Override
            public void dataNotFound(String error) {

                Toast.makeText(mContext, "Please try again later", Toast.LENGTH_SHORT).show();
                backMethod();

            }
        });

        mMonthlyBillButton.setOnClickListener(v -> {

            currentDate = getCurrentTime();
            expiryDate = getExpiryDate(30);

            doPayment("4900");
            mPaymentType = "0";

        });

        mYearlyBillButton.setOnClickListener(v -> {

            currentDate = getCurrentTime();
            expiryDate = getExpiryDate(365);

            doPayment("9900");
            mPaymentType = "1";

        });

        mBackButton.setOnClickListener(v -> {

            backMethod();

        });

    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    public void backMethod() {

        Intent intent;

        if (mBackButtonValue == 1){

            intent = new Intent(mContext, MainActivity.class);

        }else {

            intent = new Intent(mContext, CategoryViewActivity.class);
            intent.putExtra("category_name", categoryName);
            intent.putExtra("category_id", categoryId);

        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

    }

    public void doPayment(String amount) {

        Activity activity = mContext;

        Checkout checkout = new Checkout();

        //checkout.setKeyID("rzp_test_fnBQYSxUcVFFYU");

        checkout.setImage(R.mipmap.app_logo_foreground);

        try {

            JSONObject options = new JSONObject();

            options.put("name", "Prabudhh");
            options.put("description", "Subscription Plan");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#9a2843");
            options.put("currency", "INR");
            options.put("amount", amount);//pass amount in currency subunits
            //options.put("prefill.email", "prabuddha.audiobooks@gmail.com");
            options.put("prefill.contact", PhoneNumber);
            checkout.open(activity, options);

        } catch (Exception e) {

            Log.e(TAG, "Error in starting Razorpay Checkout", e);

        }
    }

    public void onPaymentSuccess(String s) {

        mPaymentFirebaseClass.savePaymentDetails(currentDate, expiryDate, mPaymentType, new SongPostFirebaseInterface() {
            @Override
            public void onSuccess(String success, String date, String expiry_date) {

                paymentMethod(1);

            }

            @Override
            public void onFailure(String error) {

                paymentMethod(1);

            }
        });

    }

    public void onPaymentError(int i, String s) {

        paymentMethod(0);

    }

    public String getCurrentTime() {

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return df.format(c);

    }

    public String getExpiryDate(int validDate) {

        Date currentDate = Calendar.getInstance().getTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, validDate);
        Date newDate = calendar.getTime();

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String output = sdf1.format(newDate.getTime());

        return output;

    }

    private void paymentMethod(int value) {

        Intent intent = new Intent(mContext, SuccessFailedPaymentStatusScreen.class);
        intent.putExtra("value", value);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

    }
}
