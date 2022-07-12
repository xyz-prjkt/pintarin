package id.xyzprjkt.pintarin.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import id.xyzprjkt.pintarin.R;
import id.xyzprjkt.pintarin.infotechAPI.service.APIClient;

public class LoginInfotechActivity extends Activity {

    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView welcome;

    APIClient apiClient = new APIClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_infotech);

        welcome = findViewById(R.id.welcome);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.loginBtn);

        Random genmsg = new Random();
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String[] msg = getResources().getStringArray(R.array.welcome);
                int n = genmsg.nextInt(msg.length-1);
                welcome.setText(msg[n]);
                handler.postDelayed(this, 5000);
            }
        };
        handler.post(runnable);

        mLoginBtn.setOnClickListener(v -> {

            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

            if(TextUtils.isEmpty(email)){
                mEmail.setError("Email is Required.");
                return;
            }

            if(TextUtils.isEmpty(password)){
                mPassword.setError("Password is Required.");
                return;
            }

            if(password.length() < 6){
                mPassword.setError("Password Must be >= 6 Characters");
            }

            apiClient.login(email, password);
            if (apiClient.getLoggedWithiLab()) {
                startActivity(new Intent(getApplicationContext(),DashboardWithiLabActivity.class));
            } else {
                Toast.makeText(LoginInfotechActivity.this, "Error ! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

}