package distinct.digitalsolutions.prabudhh.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import distinct.digitalsolutions.prabudhh.Authentication.LoginActivity;
import distinct.digitalsolutions.prabudhh.Database.FirebaseDatabaseClass;
import distinct.digitalsolutions.prabudhh.Fragments.CategoryFragment;
import distinct.digitalsolutions.prabudhh.Fragments.HomeFragment;
import distinct.digitalsolutions.prabudhh.Fragments.WhichAreFragment;
import distinct.digitalsolutions.prabudhh.HelperClasses.MainActivityHelperClass;
import distinct.digitalsolutions.prabudhh.SharedPreference.UserSharedPreferences;
import distinct.digitalsolutions.prabudhh.Interfaces.NotificationInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.UserDatailsFetchingInterface;
import distinct.digitalsolutions.prabudhh.Model.UserDeatilsModelClass;
import distinct.digitalsolutions.prabudhh.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NotificationInterface {

    private boolean doubleBackToExitPressedOnce = false;

    private UserSharedPreferences mUserSharedPreference;

    private MainActivityHelperClass mMainActivityHelperClass;

    private Fragment mMainFragment = null;
    private FragmentManager mMainFragmentManager;

    private DrawerLayout mMainPageDrawerLayout;
    private BottomNavigationView mMainBottomNavigationView;

    private FirebaseDatabaseClass mMainFirebaseDatabase;

    private ImageView mAddStoryImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserSharedPreference = new UserSharedPreferences(this);

        mMainActivityHelperClass = new MainActivityHelperClass(MainActivity.this,this);

        mMainFirebaseDatabase = new FirebaseDatabaseClass();

        mAddStoryImage = findViewById(R.id.add_post_icon);

        mMainBottomNavigationView = findViewById(R.id.main_bottom_navigation_layout);

        mMainBottomNavigationView.setOnNavigationItemSelectedListener(mMainActivityBottomNavigationSelectListener);

        mMainFragmentManager = getSupportFragmentManager();

        mMainBottomNavigationView.setSelectedItemId(R.id.home_screen);

        mMainPageDrawerLayout = findViewById(R.id.main_page_drawer_layout);
        NavigationView mMainPageNavigationView = findViewById(R.id.main_side_navigation);

        mMainPageNavigationView.bringToFront();
        ActionBarDrawerToggle mainActionBarToggle = new ActionBarDrawerToggle(MainActivity.this, mMainPageDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mMainPageDrawerLayout.addDrawerListener(mainActionBarToggle);
        mainActionBarToggle.syncState();

        mMainPageNavigationView.setNavigationItemSelectedListener(this);

        mMainActivityHelperClass.initView();

        mMainFirebaseDatabase.getUserDetails(new UserDatailsFetchingInterface() {
            @Override
            public void onSuccess(UserDeatilsModelClass userDeatilsModelClass) {

                View headerView = mMainPageNavigationView.getHeaderView(0);
                TextView userName = headerView.findViewById(R.id.user_name);
                TextView emailAddress = headerView.findViewById(R.id.user_email_address);
                CircleImageView userProfile = headerView.findViewById(R.id.user_profile_picture);

                try {

                    userName.setText(userDeatilsModelClass.getUser_name());
                    emailAddress.setText(userDeatilsModelClass.getEmail_address());
                    Picasso.get().load(userDeatilsModelClass.getThumbnail()).placeholder(R.mipmap.navigation_user).into(userProfile);

                }catch (Exception e){

                    Log.d("Error",e.getLocalizedMessage());
                }


            }

            @Override
            public void onFailure(String error) {

                Log.d("Error",error);
            }
        });


        mMainBottomNavigationView.setOnNavigationItemReselectedListener(onNavigationItemReselectedListener);

    }

    @Override
    public void onBackPressed() {

        int selectedItemId = mMainBottomNavigationView.getSelectedItemId();

        if (mMainPageDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {

            mMainPageDrawerLayout.closeDrawer(GravityCompat.END);

        } else if (R.id.home_screen != selectedItemId) {

            mMainBottomNavigationView.setSelectedItemId(R.id.home_screen);

        } else {

            if (doubleBackToExitPressedOnce) {

                super.onBackPressed();

                finish();
                return;
            }

            this.doubleBackToExitPressedOnce = true;

            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    doubleBackToExitPressedOnce = false;

                }

            }, 2000);

        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mMainActivityBottomNavigationSelectListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {

                case R.id.home_screen:

                    mAddStoryImage.setVisibility(View.GONE);
                    mMainFragment = new HomeFragment();

                    break;

                case R.id.category_screen:

                    mAddStoryImage.setVisibility(View.GONE);
                    mMainFragment = new CategoryFragment();

                    break;

                case R.id.which_are_screen:

                    mAddStoryImage.setVisibility(View.VISIBLE);
                    mMainFragment = new WhichAreFragment();

                    break;

            }

            mMainFragmentManager.beginTransaction().replace(R.id.main_frame_layout, mMainFragment).commit();
            return true;

        }
    };

    private BottomNavigationView.OnNavigationItemReselectedListener onNavigationItemReselectedListener = item -> {

    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.about_us:

                startActivity(1);
                break;

            case R.id.terms_conditions:

                startActivity(2);
                break;

            case R.id.contact_us:

                startActivity(3);
                break;

            case R.id.log_out:

               mMainActivityHelperClass.clearData();

                mMainFirebaseDatabase.signOut();
                mUserSharedPreference.setUserId("user_id","");
                Intent signOutIntent = new Intent(MainActivity.this, LoginActivity.class);
                signOutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signOutIntent);
                finish();
                break;

            case R.id.settings:
                Intent settingsIntent = new Intent(MainActivity.this,NewUserDetails.class);
                settingsIntent.putExtra("value","yes");
                startActivity(settingsIntent);

                break;

        }

        return true;

    }

    private void startActivity(int details) {

        Intent intent = new Intent(MainActivity.this, ImportantDetailsActivity.class);
        intent.putExtra("details", details);
        startActivity(intent);

    }

    public void openSideNavigation(View view) {

        mMainPageDrawerLayout.openDrawer(GravityCompat.END);

    }

    public void addPostButton(View view) {

        Intent whichAreIntent = new Intent(MainActivity.this, PostActivity.class);
        startActivity(whichAreIntent);

    }

    @Override
    public void notificationEvents(int play, int pause) {

        if (play == 1) {
            mMainActivityHelperClass.playMethod();

        } else if (pause == 1) {

          mMainActivityHelperClass.pauseMethod();

        }

    }

}