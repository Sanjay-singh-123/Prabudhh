package distinct.digitalsolutions.prabudhh.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import distinct.digitalsolutions.prabudhh.R;

public class ImportantDetailsActivity extends AppCompatActivity {

    private TextView mImportantDetailsHeader;
    private int mImportantDetailsValue;
    private LinearLayout mImportantContactUsLayout;
    private RelativeLayout mImportantAboutUSLayout,mImportantTermsConditionsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_details);

        mImportantDetailsValue = getIntent().getIntExtra("details", -1);

        mImportantDetailsHeader = findViewById(R.id.important_details_text);

        mImportantContactUsLayout = findViewById(R.id.contact_us_layout);
        mImportantAboutUSLayout = findViewById(R.id.about_us_layout);
        mImportantTermsConditionsLayout = findViewById(R.id.terms_conditions_layout);

        if (mImportantDetailsValue != -1) {

            switch (mImportantDetailsValue) {
                case 1:

                    mImportantContactUsLayout.setVisibility(View.GONE);
                    mImportantAboutUSLayout.setVisibility(View.VISIBLE);
                    mImportantTermsConditionsLayout.setVisibility(View.GONE);

                    mImportantDetailsHeader.setText(R.string.about_us);
                    break;

                case 2:

                    mImportantContactUsLayout.setVisibility(View.GONE);
                    mImportantAboutUSLayout.setVisibility(View.GONE);
                    mImportantTermsConditionsLayout.setVisibility(View.VISIBLE);
                    mImportantDetailsHeader.setText(R.string.terms_conditions);
                    break;

                case 3:

                    mImportantContactUsLayout.setVisibility(View.VISIBLE);
                    mImportantAboutUSLayout.setVisibility(View.GONE);
                    mImportantTermsConditionsLayout.setVisibility(View.GONE);

                    mImportantDetailsHeader.setText(getResources().getString(R.string.contact_us));
                    break;
            }

        } else {

            backMethod();
        }

    }

    @Override
    public void onBackPressed() {

        backMethod();
    }

    public void ImportantDetailsBackButton(View view) {

        backMethod();

    }

    public void backMethod() {
        Intent intent = new Intent(ImportantDetailsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}