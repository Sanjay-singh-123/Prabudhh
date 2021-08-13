package distinct.digitalsolutions.prabudhh.HelperClasses;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import distinct.digitalsolutions.prabudhh.Activities.MainActivity;
import distinct.digitalsolutions.prabudhh.Database.FirebaseDatabaseClass;
import distinct.digitalsolutions.prabudhh.Interfaces.LoginInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.UserDatailsFetchingInterface;
import distinct.digitalsolutions.prabudhh.Model.UserDeatilsModelClass;
import distinct.digitalsolutions.prabudhh.R;
import distinct.digitalsolutions.prabudhh.SharedPreference.UserSharedPreferences;

public class NewUserDetailsHelperClass implements LoginInterface {

    private View mRootView;
    private Activity mContext;

    private Button mChoosePictureButton, mSaveDetailsButton;
    private TextInputLayout mUserNameInputLayout, mUserEmailAddressInputLayout;
    private TextInputEditText mUserNameEditText, mUserEmailAddressEditText;
    private CircleImageView mUserProfilePicture;
    private TextView mUserDetailsSkip;
    private ImageView mUserBackButton;

    private FirebaseDatabaseClass mUserDetailsFirebaseDatabase;
    private UserSharedPreferences mUserDetailsUserSharedPreference;

    private ProgressBar progressBar;

    private RelativeLayout mProgressBarRelativeLayout;
    private ProgressBarClass mProgressBarClass;

    private Uri mProductUrl;

    private String mProductImage,mProductThumbnail;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private String mValue;

    public NewUserDetailsHelperClass(Activity context, ViewGroup viewGroup, String mValue) {

        mRootView = LayoutInflater.from(context).inflate(R.layout.activity_new_user_details, viewGroup);
        this.mContext = context;

        this.mValue = mValue;
        mUserDetailsFirebaseDatabase = new FirebaseDatabaseClass(mContext);
        mProgressBarClass = new ProgressBarClass(mContext);

        mUserDetailsUserSharedPreference = new UserSharedPreferences(mContext);

    }


    @Override
    public void initView() {

        mProgressBarClass = new ProgressBarClass(mContext);

        mProgressBarRelativeLayout = mContext.findViewById(R.id.progress_bar_layout);

        progressBar = mContext.findViewById(R.id.progress_bar);

        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);

        mUserNameInputLayout = mRootView.findViewById(R.id.user_details_user_name);
        mUserEmailAddressInputLayout = mRootView.findViewById(R.id.user_details_email_address);

        mUserNameEditText = mRootView.findViewById(R.id.user_details_user_name_text);
        mUserEmailAddressEditText = mRootView.findViewById(R.id.user_details_email_address_text);

        mChoosePictureButton = mRootView.findViewById(R.id.choose_profile);
        mSaveDetailsButton = mRootView.findViewById(R.id.login_send_otp_button);

        mUserBackButton = mRootView.findViewById(R.id.back_button);
        mUserDetailsSkip = mRootView.findViewById(R.id.skip_button);
        mUserProfilePicture = mRootView.findViewById(R.id.user_details_profile_picture);

        if (mValue != null) {

            mUserDetailsSkip.setVisibility(View.GONE);
            mUserBackButton.setVisibility(View.VISIBLE);

            mUserDetailsFirebaseDatabase.getUserDetails(new UserDatailsFetchingInterface() {
                @Override
                public void onSuccess(UserDeatilsModelClass userDeatilsModelClass) {

                    if (mProductUrl == null) {


                        if (userDeatilsModelClass.getImage() != null) {

                            Picasso.get().load(userDeatilsModelClass.getThumbnail()).placeholder(R.mipmap.user_profile).into(mUserProfilePicture);
                        }

                        try {

                            mProductImage = userDeatilsModelClass.getImage();
                            mProductThumbnail = userDeatilsModelClass.getThumbnail();

                            mUserNameEditText.setText(userDeatilsModelClass.getUser_name());
                            mUserEmailAddressEditText.setText(userDeatilsModelClass.getEmail_address());

                        } catch (Exception e) {

                            Log.d("Error", e.getLocalizedMessage());

                        }

                    }


                }

                @Override
                public void onFailure(String error) {

                }
            });

        } else {

            mUserDetailsSkip.setVisibility(View.VISIBLE);
            mUserBackButton.setVisibility(View.GONE);

        }

        mUserBackButton.setOnClickListener(v -> {

            BackMethod();

        });

        mUserDetailsSkip.setOnClickListener(v -> {


            gotoHomeScreen();

        });

        mSaveDetailsButton.setOnClickListener(v -> {

            String userName = mUserNameInputLayout.getEditText().getText().toString();
            String emailId = mUserEmailAddressInputLayout.getEditText().getText().toString();

            if (TextUtils.isEmpty(userName) || userName.length() < 1) {

                mUserNameInputLayout.setHelperText("Enter User Name");
                mUserNameInputLayout.setHelperTextColor(ColorStateList.valueOf(mContext.getResources().getColor(R.color.colorBlue)));

                return;
            }

            if (TextUtils.isEmpty(emailId) || emailId.length() < 1) {

                mUserEmailAddressInputLayout.setHelperText("Enter Email Address");
                mUserEmailAddressInputLayout.setHelperTextColor(ColorStateList.valueOf(mContext.getResources().getColor(R.color.colorBlue)));

                return;
            }

            mProgressBarClass.setProgressBarVisible(mProgressBarRelativeLayout);

            mUserDetailsFirebaseDatabase.saveUserDetails(mContext, mProductUrl, userName, emailId, mProductImage,mProductThumbnail,value -> {

                if (value) {

                    gotoHomeScreen();

                } else {

                    Toast.makeText(mContext, "Please Try again", Toast.LENGTH_SHORT).show();

                }
                mProgressBarRelativeLayout.setVisibility(View.GONE);

            });

        });

        mChoosePictureButton.setOnClickListener(v -> {

            openOptionsMenu();

        });

    }

    private void gotoHomeScreen() {

        mUserDetailsUserSharedPreference.isFirstUser("isNewUser", true);

        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        mContext.finish();

    }

    private void openOptionsMenu() {

        if (mContext != null) {

            try {

                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    //show dialog
                    builder.setTitle("Select Options").setPositiveButton("Gallery", mAddProductImageInterface)
                            .setNegativeButton("Remove", mAddProductImageInterface).show();


                }

            } catch (Exception e) {

                e.printStackTrace();

            }


        }

    }

    @Override
    public View getRootView() {

        return mRootView;
    }

    public void BackMethod() {

        if (mValue != null) {

            Intent intent = new Intent(mContext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(intent);
            mContext.finish();
            return;
        }
        mContext.finish();

    }

    DialogInterface.OnClickListener mAddProductImageInterface = (dialog, which) -> {

        switch (which) {

            case DialogInterface.BUTTON_POSITIVE:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (mContext.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        mContext.requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);

                    } else {

                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        mContext.startActivityForResult(intent, 0);

                    }
                }

                break;

            case DialogInterface.BUTTON_NEGATIVE:

                mProductUrl = null;
                mUserProfilePicture.setImageResource(R.mipmap.user_profile);
                break;

        }
    };

    DialogInterface.OnClickListener permissionsInterface = (dialog, which) -> {

        switch (which) {

            case DialogInterface.BUTTON_POSITIVE:

                Toast.makeText(mContext, "camera permission denied", Toast.LENGTH_LONG).show();

                break;

            case DialogInterface.BUTTON_NEGATIVE:

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                intent.setData(uri);
                mContext.startActivity(intent);

                break;

        }
    };


    public void cameraPermissions(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 0) {// If request is cancelled, the result arrays are empty.

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                mContext.startActivityForResult(intent, 0);

            } else {

                Toast.makeText(mContext, "permission denied", Toast.LENGTH_LONG).show();
                openOptionsMenu();

            }

        } else {


            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            //show dialog
            builder.setTitle("Permissions").setMessage("This app need storage permissions").setPositiveButton("Deny", permissionsInterface)
                    .setNegativeButton("Allow", permissionsInterface).show();

        }

    }

    public void imagePlacing(int requestCode, int resultCode, Intent imageReturnedIntent) {

        if (resultCode == Activity.RESULT_OK) {

            if (imageReturnedIntent == null) {

                Toast.makeText(mContext, "Unable to choose image.", Toast.LENGTH_SHORT).show();
                return;

            }

            mProductUrl = imageReturnedIntent.getData();
            mUserProfilePicture.setImageURI(mProductUrl);

        } else {

            Toast.makeText(mContext, "Unable to choose image.", Toast.LENGTH_SHORT).show();

        }

    }
}
