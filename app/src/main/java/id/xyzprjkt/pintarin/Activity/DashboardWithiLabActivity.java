package id.xyzprjkt.pintarin.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.faltenreich.skeletonlayout.Skeleton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

import id.xyzprjkt.pintarin.R;
import id.xyzprjkt.pintarin.infotechAPI.service.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardWithiLabActivity extends Activity
        implements Callback<APIService> {

    // iLab Variable
    public String accountName;

    // Main Variable
    TextView fullName;
    ImageView profilePic;
    CardView courseCard, aboutCard, profileCard;
    Intent course, about, profile;
    ScrollView rootLayout;
    Snackbar snackBar;

    // News Variable
    Skeleton foryouSkeleton;
    ImageView newsThumb1, newsThumb2;
    TextView newsTitle1, newsTitle2;
    TextView newsAuthor1, newsAuthor2;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        rootLayout = findViewById(R.id.pintarinRoot);
        snackBar = Snackbar.make(rootLayout,"Press again to exit", Snackbar.LENGTH_SHORT);

        courseCard = findViewById(R.id.pintarinCourse);
        course = new Intent(this, CourseActivity.class);

        aboutCard = findViewById(R.id.pintarinAbout);
        about = new Intent(this, AboutActivity.class);

        profileCard = findViewById(R.id.pintarinProfile);
        profile = new Intent(this, ProfileActivity.class);

        profilePic = findViewById(R.id.pintarinProfileImage);

        courseCard.setOnClickListener(v -> startActivity(course));

        aboutCard.setOnClickListener(v -> startActivity(about));

        profileCard.setOnClickListener(v -> startActivity(profile));

        fullName = findViewById(R.id.profileName);
        fullName.setText(getString(R.string.user_name_welcome ) + ", " + accountName);

        /*
            News content ( TO-DO )
            Create the News Adapter instead static layout content
        */
        foryouSkeleton = findViewById(R.id.foryouSkeleton);
        newsThumb1 = findViewById(R.id.newsThumnail);
        newsTitle1 = findViewById(R.id.newsTitle);
        newsAuthor1 = findViewById(R.id.newsAuthor);
        newsThumb2 = findViewById(R.id.newsThumnail2);
        newsTitle2 = findViewById(R.id.newsTitle2);
        newsAuthor2 = findViewById(R.id.newsAuthor2);

        foryouSkeleton.showSkeleton();

        // WS OOP Java News
        String oopThumb = "https://infotech.umm.ac.id/uploads/info_img/e310598aabb8871824ef2c5310a1fb54395.jpg";
        Picasso.get().load(oopThumb).into(newsThumb1, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                foryouSkeleton.showOriginal();
            }

            @Override
            public void onError(Exception e) {
            }
        });
        newsTitle1.setText("Workshop Build Mini System With Java OOP");
        newsAuthor1.setText("By Fildzah Lathifah");

        // WS Python News
        String pythonThumb = "https://infotech.umm.ac.id/uploads/info_img/e291f8fabfa5b1f304c25ef54183ffff371.jpg";
        Picasso.get().load(pythonThumb).into(newsThumb2, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                foryouSkeleton.showOriginal();
            }

            @Override
            public void onError(Exception e) {
            }
        });
        newsTitle2.setText("Workshop Python Basic");
        newsAuthor2.setText("By Dinda Arinawati Wiyono");
    }

    @Override
    public void onBackPressed() {
        if (snackBar.isShown()) {
            this.finishAffinity();
        } else {
            snackBar.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResponse(@NonNull Call<APIService> call, @NonNull Response<APIService> response) {
    }

    @Override
    public void onFailure(@NonNull Call<APIService> call, @NonNull Throwable t) {

    }

}
