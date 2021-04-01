package distinct.digitalsolutions.prabudhh.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import distinct.digitalsolutions.prabudhh.R;

public class SuccessFailedPaymentStatusScreen extends AppCompatActivity {

    private ImageView mStatusImage;
    private TextView mStatusTitle;
    private Button mContinueButton;

    private int mValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_failed_payment_status_screen);

        mValue = getIntent().getIntExtra("value",-1);

        mStatusImage = findViewById(R.id.payment_success_image);
        mStatusTitle = findViewById(R.id.payment_header);
        mContinueButton = findViewById(R.id.continue_button);

        if (mValue == 1){

            mStatusTitle.setText(getResources().getString(R.string.payment_success));
            mStatusImage.setImageResource(R.mipmap.payment_success);

        }else if (mValue == 0){

            mStatusTitle.setText(getResources().getString(R.string.payment_failed));
            mStatusImage.setImageResource(R.mipmap.payment_failed);

        }

        mContinueButton.setOnClickListener(v -> {

            backButton();

        });


    }

    @Override
    public void onBackPressed(){

        backButton();
        super.onBackPressed();

    }
    public void backButton(){

        Intent intent = new Intent(SuccessFailedPaymentStatusScreen.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
}