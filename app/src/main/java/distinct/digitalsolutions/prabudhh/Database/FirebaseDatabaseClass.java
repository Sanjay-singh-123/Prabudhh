package distinct.digitalsolutions.prabudhh.Database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import distinct.digitalsolutions.prabudhh.Activities.MainActivity;
import distinct.digitalsolutions.prabudhh.HelperClasses.PlayListHelperClass;
import distinct.digitalsolutions.prabudhh.Interfaces.CategoryFirebaseInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.DatabaseInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.FirebaseDatabaseInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.MostPlayedSongInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.PaymentInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.SongPostFirebaseInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.UserDatailsFetchingInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.UserUploadDataInterface;
import distinct.digitalsolutions.prabudhh.Interfaces.WhichAreInterface;
import distinct.digitalsolutions.prabudhh.Model.CategoryViewModelClass;
import distinct.digitalsolutions.prabudhh.Model.HomeModelClass;
import distinct.digitalsolutions.prabudhh.Model.MostlyPlayed;
import distinct.digitalsolutions.prabudhh.Model.UserDeatilsModelClass;
import distinct.digitalsolutions.prabudhh.Model.WhichAreModelClass;

public class FirebaseDatabaseClass implements DatabaseInterface {

    private FirebaseAuth mUserAuth;
    private DatabaseReference mFirebaseDatabase;
    private StorageReference mFirebaseStorageReference;
    private String mUserId;
    private Context context;

    public FirebaseDatabaseClass(Context context) {

        mUserAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseStorageReference = FirebaseStorage.getInstance().getReference();

        mUserId = mUserAuth.getCurrentUser().getUid();
        this.context = context;


    }

    @Override
    public String getCurrentUserId() {

        return mUserId;

    }

    @Override
    public boolean isUserLoggedIn() {

        return mUserId != null;

    }

    public void getCategories(String searchValue, FirebaseDatabaseInterface firebaseDatabaseInterface) {

        List<HomeModelClass> homeModelClasses = new ArrayList<>();

        try {


            mFirebaseDatabase.child("category").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        HomeModelClass homeModelClass = dataSnapshot.getValue(HomeModelClass.class);

                        if (TextUtils.isEmpty(searchValue) || searchValue.equalsIgnoreCase("")) {

                            homeModelClasses.add(homeModelClass);

                        } else {

                            if (homeModelClass != null) {

                                if (homeModelClass.getCategory_name().toLowerCase().contains(searchValue)) {

                                    homeModelClasses.add(homeModelClass);

                                }
                            }
                        }

                    }

                    firebaseDatabaseInterface.onSuccess(homeModelClasses);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    firebaseDatabaseInterface.onFailure(error.getMessage());

                }
            });

        } catch (Exception e) {
            Log.d("Erro_value", e.getLocalizedMessage());

            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);


        }


    }

    public void getSubCategoriesList(String categoryName, String searchSubCategory, FirebaseDatabaseInterface firebaseDatabaseInterface) {

        List<HomeModelClass> subCategoryList = new ArrayList<>();

        try {


            mFirebaseDatabase.child("sub_categorys").child(categoryName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        String subCategoryId = dataSnapshot.child("sub_category_id").getValue().toString();
                        String subCategoryName = dataSnapshot.child("sub_category_name").getValue().toString();
                        String thumbNail = dataSnapshot.child("thumbnail").getValue().toString();

                        HomeModelClass homeModelClass = new HomeModelClass(subCategoryId, subCategoryName, thumbNail);

                        if (TextUtils.isEmpty(searchSubCategory) || searchSubCategory.equalsIgnoreCase("")) {

                            subCategoryList.add(homeModelClass);

                        } else {

                            if (subCategoryName != null) {

                                if (subCategoryName.toLowerCase().contains(searchSubCategory)) {

                                    subCategoryList.add(homeModelClass);

                                }
                            }
                        }

                    }

                    firebaseDatabaseInterface.onSuccess(subCategoryList);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    firebaseDatabaseInterface.onFailure("Failed To get Data");

                }
            });

        } catch (Exception e) {
            Log.d("Erro_value", e.getLocalizedMessage());

            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);


        }


    }

    public void getContent(String subCategoryName, String searchSong, CategoryFirebaseInterface categoryFirebaseInterface) {

        List<CategoryViewModelClass> categoryViewModelClasses = new ArrayList<>();
        List<CategoryViewModelClass> allSongsList = new ArrayList<>();

        try {

            mFirebaseDatabase.child("Music").child(subCategoryName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        CategoryViewModelClass categoryViewModelClass = dataSnapshot.getValue(CategoryViewModelClass.class);

                        if (TextUtils.isEmpty(searchSong) || searchSong.equalsIgnoreCase("")) {

                            categoryViewModelClasses.add(categoryViewModelClass);

                        } else {

                            if (categoryViewModelClass != null) {

                                if (categoryViewModelClass.getTitle().toLowerCase().contains(searchSong)) {

                                    categoryViewModelClasses.add(categoryViewModelClass);

                                }
                            }
                        }

                    }

                    categoryFirebaseInterface.onSuccess(categoryViewModelClasses, allSongsList);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    categoryFirebaseInterface.onFailure(error.getMessage());

                }
            });

        } catch (Exception e) {

            Log.d("Erro_value", e.getLocalizedMessage());

            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);


        }

    }

    public void signOut() {

        mUserAuth.signOut();

    }

    public void postSongMethod(String song, String userId, SongPostFirebaseInterface songPostFirebaseInterface) {

        try {

            String postId = mFirebaseDatabase.push().getKey();

            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("Song", song);
            hashMap.put("User", userId);

            if (postId == null) {

                songPostFirebaseInterface.onFailure("Failed");
                return;
            }

            mFirebaseDatabase.child("SongPosts").child(postId).setValue(hashMap).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {

                    songPostFirebaseInterface.onSuccess("Success", "", "");

                } else {

                    songPostFirebaseInterface.onFailure("Failed");

                }

            });

        } catch (Exception e) {

            Log.d("Erro_value", e.getLocalizedMessage());

            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);

        }

    }

    public void getPostedSongs(WhichAreInterface whichAreInterface) {

        List<WhichAreModelClass> whichAreModelClass = new ArrayList<>();

        try {

            mFirebaseDatabase.child("SongPosts").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String total = String.valueOf(snapshot.getChildrenCount());
                    int value = 0;

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        value++;

                        String userId = dataSnapshot.child("User").getValue().toString();
                        String post = dataSnapshot.child("Song").getValue().toString();

                        int finalValue = value;

                        mFirebaseDatabase.child("User_Details").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String thumbNail = "";
                                String image = "";

                                if (snapshot.hasChild("thumbnail")) {

                                    thumbNail = snapshot.child("thumbnail").getValue().toString();
                                    image = snapshot.child("image").getValue().toString();

                                }

                                try {

                                    String userName = snapshot.child("user_name").getValue().toString();

                                    WhichAreModelClass whichAreModelClass1 = new WhichAreModelClass(post, userName, thumbNail, image);
                                    whichAreModelClass.add(whichAreModelClass1);

                                } catch (Exception e) {
                                }


                                if (finalValue >= Integer.parseInt(total)) {

                                    whichAreInterface.onSuccess(whichAreModelClass);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {


                            }
                        });

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    whichAreInterface.onFailure(error.getMessage());
                }
            });

        } catch (Exception e) {

            Log.d("Erro_value", e.getLocalizedMessage());

            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }


    }

    public void saveUserDetails(Activity activity, Uri mProductUrl, String userName, String emailAddress, String productImage, String thumbNail, UserUploadDataInterface userUploadDataInterface) {

        if (mProductUrl == null) {

            Map<String, String> userDetails = new HashMap<>();
            userDetails.put("user_name", userName);
            userDetails.put("email_address", emailAddress);
            userDetails.put("thumbnail", thumbNail);
            userDetails.put("image", productImage);

            mFirebaseDatabase.child("User_Details").child(mUserId)
                    .setValue(userDetails).addOnSuccessListener(aVoid -> userUploadDataInterface.onSuccess(true)).addOnFailureListener(e -> userUploadDataInterface.onSuccess(false));

            return;
        }

        final String pushKey = mFirebaseDatabase.push().getKey();

        final String thumbNailPushKey = UUID.randomUUID().toString();

        Bitmap ediProfileBitMap = null;

        try {

            ediProfileBitMap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), mProductUrl);

            Bitmap imageBitmap = Bitmap.createScaledBitmap(ediProfileBitMap, 230, 230, true);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 85, byteArrayOutputStream);
            final byte[] supplier_byte = byteArrayOutputStream.toByteArray();

            mFirebaseStorageReference.child("User_Images").child(pushKey).putFile(mProductUrl).addOnSuccessListener(taskSnapshot -> mFirebaseStorageReference.child("User_Images").child(pushKey).getDownloadUrl().addOnSuccessListener(uri -> mFirebaseStorageReference.child("User_Images").child("Thumbnails").child(thumbNailPushKey).putBytes(supplier_byte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot1) {

                    mFirebaseStorageReference.child("User_Images").child("Thumbnails").child(thumbNailPushKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(final Uri thumbnailUri) {

                            Map<String, String> userDetails = new HashMap<>();
                            userDetails.put("user_name", userName);
                            userDetails.put("email_address", emailAddress);
                            userDetails.put("thumbnail", thumbnailUri.toString());
                            userDetails.put("image", uri.toString());

                            mFirebaseDatabase.child("User_Details").child(mUserId)
                                    .setValue(userDetails).addOnSuccessListener(aVoid -> userUploadDataInterface.onSuccess(true)).addOnFailureListener(e -> userUploadDataInterface.onSuccess(false));


                        }
                    }).addOnFailureListener(e -> {

                        userUploadDataInterface.onSuccess(false);


                    });

                }
            }).addOnFailureListener(e -> {

                userUploadDataInterface.onSuccess(false);


            })).addOnFailureListener(e -> {

                userUploadDataInterface.onSuccess(false);

            })).addOnFailureListener(e -> {

                userUploadDataInterface.onSuccess(false);


            });

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Erro_value", e.getLocalizedMessage());

            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);

        }
    }

    public void getUserDetails(UserDatailsFetchingInterface userDatailsFetchingInterface) {

        try {

            mFirebaseDatabase.child("User_Details").child(mUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    try {

                        UserDeatilsModelClass userDeatilsModelClass = snapshot.getValue(UserDeatilsModelClass.class);

                        userDatailsFetchingInterface.onSuccess(userDeatilsModelClass);

                    } catch (Exception e) {

                        userDatailsFetchingInterface.onFailure(e.getLocalizedMessage());
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {

            Log.d("Erro_value", e.getLocalizedMessage());

            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);

        }

    }

    public void getSongAndUpdateCount(String song_id, UserUploadDataInterface userUploadDataInterface) {

        try {

            mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.hasChild("SongsCount")) {

                        mFirebaseDatabase.child("SongsCount").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.hasChild(song_id)) {

                                    mFirebaseDatabase.child("SongsCount").child(song_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            try {
                                                int count = Integer.parseInt(snapshot.child("count").getValue().toString()) + 1;

                                                Map<String, String> map = new HashMap<>();
                                                map.put("count", String.valueOf(count));

                                                mFirebaseDatabase.child("SongsCount").child(song_id).setValue(map)
                                                        .addOnSuccessListener(aVoid -> userUploadDataInterface.onSuccess(true)).addOnFailureListener(e -> {
                                                    userUploadDataInterface.onSuccess(false);
                                                });

                                            } catch (Exception e) {

                                                userUploadDataInterface.onSuccess(false);
                                            }


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                            userUploadDataInterface.onSuccess(false);
                                        }
                                    });

                                } else {

                                    Map<String, String> map = new HashMap<>();
                                    map.put("count", "1");

                                    mFirebaseDatabase.child("SongsCount").child(song_id).setValue(map)
                                            .addOnSuccessListener(aVoid -> userUploadDataInterface.onSuccess(true)).addOnFailureListener(e -> {
                                        userUploadDataInterface.onSuccess(false);
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                userUploadDataInterface.onSuccess(false);
                            }
                        });

                    } else {

                        Map<String, String> map = new HashMap<>();
                        map.put("count", "1");

                        mFirebaseDatabase.child("SongsCount").child(song_id).setValue(map)
                                .addOnSuccessListener(aVoid -> userUploadDataInterface.onSuccess(true)).addOnFailureListener(e -> {
                            userUploadDataInterface.onSuccess(false);
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    userUploadDataInterface.onSuccess(false);
                }
            });
        } catch (Exception e) {

            userUploadDataInterface.onSuccess(false);

        }
    }

    public void getPaymentPrices(PaymentInterface paymentInterface) {

        try {

            mFirebaseDatabase.child("PaymentPlans").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String monthlyPayment = snapshot.child("monthly").getValue().toString();
                    String yearlyPayment = snapshot.child("yearly").getValue().toString();

                    paymentInterface.payment(monthlyPayment, yearlyPayment);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    paymentInterface.dataNotFound(error.getMessage());

                }
            });

        } catch (Exception e) {
            Log.d("Erro_value", e.getLocalizedMessage());

            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);

        }


    }

    public void checkUserPaymentStatus(SongPostFirebaseInterface songPostFirebaseInterface) {

        try {

            mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.hasChild("Payment_Status")) {

                        mFirebaseDatabase.child("Payment_Status").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.hasChild(mUserId)) {

                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                        String key = dataSnapshot.getKey();

                                        if (key != null && key.equalsIgnoreCase(mUserId)) {

                                            String currentDate = dataSnapshot.child("date").getValue().toString();
                                            String expiryDate = dataSnapshot.child("expiry_date").getValue().toString();

                                            songPostFirebaseInterface.onSuccess("Success", currentDate, expiryDate);


                                        }


                                    }

                                } else {

                                    songPostFirebaseInterface.onFailure("Failed");

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                songPostFirebaseInterface.onFailure("Failed");
                            }
                        });


                    } else {

                        songPostFirebaseInterface.onFailure("Failed");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    songPostFirebaseInterface.onFailure("Failed");
                }
            });


        } catch (Exception e) {
            Log.d("Erro_value", e.getLocalizedMessage());

            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);


        }


    }

    public void savePaymentDetails(String date, String expiryDate, String planType, SongPostFirebaseInterface songPostFirebaseInterface) {

        try {


            Map<String, String> paymentHashMap = new HashMap<>();
            paymentHashMap.put("date", date);
            paymentHashMap.put("expiry_date", expiryDate);
            paymentHashMap.put("plan_type", planType);

            mFirebaseDatabase.child("Payment_Status").child(mUserId).setValue(paymentHashMap).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {

                    songPostFirebaseInterface.onSuccess("Success", "", "");

                } else {

                    songPostFirebaseInterface.onFailure("Failed");
                }

            });

        } catch (Exception e) {
            Log.d("Erro_value", e.getLocalizedMessage());

            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);


        }


    }

    public void getMostlyPlayedSongs(MostPlayedSongInterface mostPlayedSongInterface) {

        ArrayList<MostlyPlayed> mostlyPlayed = new ArrayList<>();

        try {


            mFirebaseDatabase.child("SongsCount").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                        String key = snapshot1.getKey();
                        String count = snapshot1.child("count").getValue().toString();

                        mostlyPlayed.add(new MostlyPlayed(key, count));

                    }

                    mostPlayedSongInterface.onSuccess(mostlyPlayed);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    mostPlayedSongInterface.onFailure(error.getDetails());

                }
            });
        } catch (Exception e) {
            Log.d("Erro_value", e.getLocalizedMessage());

            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);


        }


    }

    public void getFinalMostlyPlayedSongsData(List<MostlyPlayed> mostlyPlayeds, CategoryFirebaseInterface categoryFirebaseInterface) {

        List<CategoryViewModelClass> categoryViewModelClasses = new ArrayList<>();
        List<CategoryViewModelClass> allSongs = new ArrayList<>();

        try {


            mFirebaseDatabase.child("Music").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            CategoryViewModelClass categoryViewModelClass = dataSnapshot1.getValue(CategoryViewModelClass.class);

                            allSongs.add(categoryViewModelClass);

                            for (MostlyPlayed mostlyPlayed : mostlyPlayeds) {

                                if (mostlyPlayed.getSongId().equalsIgnoreCase(categoryViewModelClass.getSong_id())) {

                                    categoryViewModelClasses.add(categoryViewModelClass);

                                }
                            }

                        }

                    }

                    categoryFirebaseInterface.onSuccess(categoryViewModelClasses, allSongs);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    categoryFirebaseInterface.onFailure(error.getMessage());

                }
            });

        } catch (Exception e) {
            Log.d("Erro_value", e.getLocalizedMessage());

            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);


        }


    }

    public void getRecommendedSongs(CategoryFirebaseInterface categoryFirebaseInterface) {

        List<CategoryViewModelClass> playListHelperClasses = new ArrayList<>();
        List<CategoryViewModelClass> allSongsListClass = new ArrayList<>();

        try {


            mFirebaseDatabase.child("Music").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            Log.d("Recommended", dataSnapshot1.getKey());

                            CategoryViewModelClass playListHelperClass = dataSnapshot1.getValue(CategoryViewModelClass.class);

                            if (playListHelperClass != null) {

                                if (playListHelperClass.getRecommended().equalsIgnoreCase("1")) {

                                    playListHelperClasses.add(playListHelperClass);

                                }

                            }

                        }
                    }

                    categoryFirebaseInterface.onSuccess(playListHelperClasses, allSongsListClass);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    categoryFirebaseInterface.onFailure(error.getMessage());

                }
            });

        } catch (Exception e) {
            Log.d("Erro_value", e.getLocalizedMessage());

            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);


        }

    }

}