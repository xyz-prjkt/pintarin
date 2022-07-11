package id.xyzprjkt.pintarin.infotechAPI.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import id.xyzprjkt.pintarin.infotechAPI.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class APIClient extends User{

    public APIService apiService = APIClient.getClient().create(APIService.class);

    public static final String BASE_URL = "https://api.infotech.umm.ac.id/dotlab/api/v1/auth/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public void login(){
        Call<Object> postCall = apiService.authLogin("202110370311147", "xyzuan2002");
        postCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    JSONObject responseObj = new JSONObject(new Gson().toJson(response.body()));
                    setToken(responseObj.getString("access_token"));
                    Log.d("infotechAPI", "Response : " + getToken());
                    fetchInfo(getToken());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d("infotechAPI", "Failure Response: "+ t);
            }
        });
    }

    public void fetchInfo(String token){
        Thread thread = new Thread(() -> {
            try  {
                URL url = new URL(BASE_URL + "me");
                HttpURLConnection authPOST = (HttpURLConnection) url.openConnection();
                authPOST.setRequestMethod("POST");
                authPOST.setDoOutput(true);
                authPOST.setRequestProperty("Authorization", "Bearer " + token);
                authPOST.setRequestProperty("Content-Type", "application/json");
                authPOST.setRequestProperty("Accept", "application/json");

                BufferedReader in = new BufferedReader(new InputStreamReader(authPOST.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                JSONObject jsonObj = new JSONObject(String.valueOf(content));
                String user_name = jsonObj.getString("user_name");
                String email = jsonObj.getString("email");
                String full_name = jsonObj.getString("full_name");
                Log.d("infotechAPI", "Username\t\t: " + user_name);
                Log.d("infotechAPI", "Email\t\t\t: " + email);
                Log.d("infotechAPI", "Full Name\t\t: " + full_name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}