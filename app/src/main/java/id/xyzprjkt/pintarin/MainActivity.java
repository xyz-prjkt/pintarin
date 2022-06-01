package id.xyzprjkt.pintarin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    RecyclerView videoList;
    VideoAdapter adapter;
    List<Video> all_videos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        all_videos = new ArrayList<>();
        videoList = findViewById(R.id.videoList);
        videoList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideoAdapter(this,all_videos);
        videoList.setAdapter(adapter);
        getJsonData();

    }

    private void getJsonData() {
        String URL = "https://gitlab.com/ixyzuan/pintarin_db/-/raw/main/data.json";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        @SuppressLint("NotifyDataSetChanged") JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, response -> {
            try {
                JSONArray categories = response.getJSONArray("categories");
                JSONObject categoriesData = categories.getJSONObject(0);
                JSONArray videos = categoriesData.getJSONArray("videos");

                for (int i = 0; i< videos.length();i++){
                    JSONObject video = videos.getJSONObject(i);

                    Video v = new Video();

                    v.setTitle(video.getString("title"));
                    v.setDescription(video.getString("description"));
                    v.setAuthor(video.getString("author"));
                    v.setImageUrl(video.getString("thumb"));
                    JSONArray videoUrl = video.getJSONArray("sources");
                    v.setVideoUrl(videoUrl.getString(0));

                    all_videos.add(v);
                    adapter.notifyDataSetChanged();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> Log.d(TAG, "onErrorResponse: " + error.getMessage()));

        requestQueue.add(objectRequest);
    }
}