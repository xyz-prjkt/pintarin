package id.xyzprjkt.pintarin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

import id.xyzprjkt.pintarin.Activity.DashboardActivity;
import id.xyzprjkt.pintarin.Activity.LoginActivity;
import id.xyzprjkt.pintarin.infotechAPI.service.APIClient;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends Activity {

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        APIClient apiClient = new APIClient();
        fAuth = FirebaseAuth.getInstance();
        int delay = 2000;
        new Handler().postDelayed(() -> {

            if(fAuth.getCurrentUser() != null){
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            } else {
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            }
            finish();
        }, delay);
    }
}
