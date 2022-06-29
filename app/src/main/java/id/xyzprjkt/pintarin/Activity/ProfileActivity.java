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

    // Profile Picture Variable
    ImageView profileImageView;
    CardView editProfilePicBtn;

    // Account Information Variable
    EditText profileFullName,
             profilePhone,
             profileEmail,
             profileUniv,
             profileDepart;
    Button saveBtn;

    // Google Firebase
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

        String fullName = data.getStringExtra("fullName");
        String phone = data.getStringExtra("phone");
        String email = data.getStringExtra("email");

        String univ = data.getStringExtra("univ");
        String depart = data.getStringExtra("depart");

        // Firebase initializations
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        storageReference = FirebaseStorage.getInstance().getReference();

        /*  User information initializations
            This ID values will be replaced from Firebase Firestore database
        */
        // Main User informations ID
        profileImageView = findViewById(R.id.profileImageView);
        editProfilePicBtn = findViewById(R.id.editProfilePic);
        profileFullName = findViewById(R.id.profileFullName);
        profilePhone = findViewById(R.id.profileTelp);
        profileEmail = findViewById(R.id.profileEmailAddress);

        // Education informations ID
        profileUniv = findViewById(R.id.profileUniversity);
        profileDepart  = findViewById(R.id.profileDepartment);

        saveBtn = findViewById(R.id.saveProfileInfo);

        // Fetch User Profile Pic from Firebase Storage
        StorageReference profileRef = storageReference.child("users/"+ Objects.requireNonNull(fAuth.getCurrentUser()).getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImageView));

        // Fetch User information from Firebase Storage
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, (documentSnapshot, e) -> {
            if(Objects.requireNonNull(documentSnapshot).exists()){
                // Set Main User Informations
                profileFullName.setText(documentSnapshot.getString( "fName"));
                profilePhone.setText(documentSnapshot.getString( "phone"));
                profileEmail.setText(documentSnapshot.getString( "email"));
                // Set User Education Informations
                profileUniv.setText(documentSnapshot.getString( "univ"));
                profileDepart.setText(documentSnapshot.getString( "depart"));
            } else {
                Log.d("tag", "onEvent: Document do not exists");
            }
        });

        editProfilePicBtn.setOnClickListener(v -> {
            Intent pickProfilePicIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickProfilePicIntent,1000);
        });

        saveBtn.setOnClickListener(v -> {
            if(profileFullName.getText().toString().isEmpty() || profileEmail.getText().toString().isEmpty()){
                Toast.makeText(ProfileActivity.this, "Name and Email are empty", Toast.LENGTH_SHORT).show();
                return;
            }

            final String email1 = profileEmail.getText().toString();
            user.updateEmail(email1).addOnSuccessListener(aVoid -> {
                DocumentReference docRef = fStore.collection("users").document(user.getUid());
                Map<String,Object> edited = new HashMap<>();

                edited.put("email", email1);
                edited.put("phone",profilePhone.getText().toString());
                edited.put("fName",profileFullName.getText().toString());

                edited.put("univ",profileUniv.getText().toString());
                edited.put("depart",profileDepart.getText().toString());

                docRef.update(edited).addOnSuccessListener(aVoid1 -> {
                    startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                    finish();
                });
            }).addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        });
        profileEmail.setText(email);
        profilePhone.setText(phone);
        profileFullName.setText(fullName);
        profileUniv.setText(univ);
        profileDepart.setText(depart);
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
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }

}