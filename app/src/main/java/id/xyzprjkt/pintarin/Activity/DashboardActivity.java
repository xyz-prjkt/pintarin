package id.xyzprjkt.pintarin.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import javax.annotation.Nullable;

import id.xyzprjkt.pintarin.R;

public class DashboardActivity extends Activity {

    // Firebase Variable
    TextView fullName;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseUser user;
    StorageReference storageReference;

    // General Variable
    ImageView profilePic;
    CardView courseCard, aboutCard, profileCard;
    Intent course, about, profile;
    ScrollView rootLayout;
    Snackbar snackBar;

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

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        user = fAuth.getCurrentUser();

        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profilePic));

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, (documentSnapshot, e) -> {
            if(Objects.requireNonNull(documentSnapshot).exists()){
                fullName.setText(getString(R.string.user_name_welcome ) + ", " + documentSnapshot.getString( "fName"));

            } else {
                Log.d("tag", "onEvent: Document do not exists");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (snackBar.isShown()) {
            this.finishAffinity();
        } else {
            snackBar.show();
        }
    }
}
