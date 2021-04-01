package distinct.digitalsolutions.prabudhh.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import distinct.digitalsolutions.prabudhh.Model.WhichAreModelClass;
import distinct.digitalsolutions.prabudhh.R;

public class SinglePostViewActivity extends AppCompatActivity {

    private String mSingleViewValue,mUserProfilePicture;
    private WhichAreModelClass mWhichAreModelClass;
    private TextView mSingleSongViewText;
    private ImageView mProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_song_view);

        mUserProfilePicture = getIntent().getStringExtra("user_profile");
        mSingleViewValue = getIntent().getStringExtra("single_song_value");

        mWhichAreModelClass = new Gson().fromJson(mSingleViewValue,WhichAreModelClass.class);

        mSingleSongViewText = findViewById(R.id.song_edit_text);
        mProfilePicture = findViewById(R.id.post_user_profile_picture);

        mSingleSongViewText.setText(mWhichAreModelClass.getSong());

        if (!mUserProfilePicture.equalsIgnoreCase("")){

            Picasso.get().load(mUserProfilePicture).placeholder(R.mipmap.user_image).into(mProfilePicture);
        }


    }

    public void ViewSingleSongBackButton(View view) {

        Intent intent = new Intent(SinglePostViewActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}