package id.xyzprjkt.pintarin.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import id.xyzprjkt.pintarin.R;

public class ProfileActivity extends Activity {

    public static final String TAG = "TAG";
    CardView editProfilePicBtn;
    EditText profileFullName,profileEmail;
    ImageView profileImageView;
    Button saveBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent data = getIntent();
        final String fullName = data.getStringExtra("fullName");
        String email = data.getStringExtra("email");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        storageReference = FirebaseStorage.getInstance().getReference();

        profileFullName = findViewById(R.id.profileFullName);
        profileEmail = findViewById(R.id.profileEmailAddress);
        profileImageView = findViewById(R.id.profileImageView);
        editProfilePicBtn = findViewById(R.id.editProfilePic);
        saveBtn = findViewById(R.id.saveProfileInfo);

        StorageReference profileRef = storageReference.child("users/"+ Objects.requireNonNull(fAuth.getCurrentUser()).getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImageView));

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, (documentSnapshot, e) -> {
            if(Objects.requireNonNull(documentSnapshot).exists()){
                profileFullName.setHint(documentSnapshot.getString( "fName"));
                profileEmail.setHint(documentSnapshot.getString( "email"));
            } else {
                Log.d("tag", "onEvent: Document do not exists");
            }
        });

        editProfilePicBtn.setOnClickListener(v -> {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent,1000);
        });

        saveBtn.setOnClickListener(v -> {
            if(profileFullName.getText().toString().isEmpty() || profileEmail.getText().toString().isEmpty()){
                Toast.makeText(ProfileActivity.this, "One or Many fields are empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            final String email1 = profileEmail.getText().toString();
            user.updateEmail(email1).addOnSuccessListener(aVoid -> {
                DocumentReference docRef = fStore.collection("users").document(user.getUid());
                Map<String,Object> edited = new HashMap<>();
                edited.put("email", email1);
                edited.put("fName",profileFullName.getText().toString());
                docRef.update(edited).addOnSuccessListener(aVoid1 -> {
                    Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                    finish();
                });
                Toast.makeText(ProfileActivity.this, "Email is changed.", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> Toast.makeText(ProfileActivity.this,   e.getMessage(), Toast.LENGTH_SHORT).show());
        });
        profileEmail.setText(email);
        profileFullName.setText(fullName);
        Log.d(TAG, "onCreate: " + fullName + " " + email);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = Objects.requireNonNull(data).getData();
                uploadImageToFirebase(imageUri);
            }
        }

    }

    private void uploadImageToFirebase(Uri imageUri) {
        final StorageReference fileRef = storageReference.child("users/"+ Objects.requireNonNull(fAuth.getCurrentUser()).getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImageView))).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show());

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
}