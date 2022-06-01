package id.xyzprjkt.pintarin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_splash);
        int delay = 1000;
        new Handler().postDelayed(() -> {

            Intent home=new Intent(SplashScreen.this, MainActivity.class);
            startActivity(home);
            finish();

        }, delay);
    }
}
