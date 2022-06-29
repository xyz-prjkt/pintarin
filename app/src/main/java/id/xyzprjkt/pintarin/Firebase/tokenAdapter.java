package id.xyzprjkt.pintarin.Firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class tokenAdapter extends FirebaseMessagingService {

    private static final String TAG = "FCM";

    @Override
    public void onNewToken(String mToken) {
        super.onNewToken(mToken);
        Log.e("TOKEN",mToken);
    }

}
