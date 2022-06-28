package id.xyzprjkt.pintarin.Activity;

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

import id.xyzprjkt.pintarin.R;
import id.xyzprjkt.pintarin.VideoController.Programming.ProgrammingVideo;
import id.xyzprjkt.pintarin.VideoController.Programming.ProgrammingAdapter;

import id.xyzprjkt.pintarin.VideoController.Sponsored.SponsoredVideo;
import id.xyzprjkt.pintarin.VideoController.Sponsored.SponsoredAdapter;


public class CourseActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    RecyclerView coursePrograming, courseSponsored;

    ProgrammingAdapter adapterPrograming;
    SponsoredAdapter adapterSponsored;

    List<ProgrammingVideo> videoPrograming;
    List<SponsoredVideo> videoSponsored;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Stored Video
        videoPrograming = new ArrayList<>();
        videoSponsored = new ArrayList<>();

        // Programming
        coursePrograming = findViewById(R.id.coursePrograming);
        coursePrograming.setHasFixedSize(true);
        coursePrograming.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL , false));
        adapterPrograming = new ProgrammingAdapter(this, videoPrograming);
        coursePrograming.setAdapter(adapterPrograming);

        // Sponsored
        courseSponsored = findViewById(R.id.courseSponsored);
        courseSponsored.setHasFixedSize(true);
        courseSponsored.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL , false));
        adapterSponsored = new SponsoredAdapter(this, videoSponsored);
        courseSponsored.setAdapter(adapterSponsored);

        getJsonData();
    }

    private void getJsonData() {
        String URL = "https://firebasestorage.googleapis.com/v0/b/pintarin-labit2022.appspot.com/o/data.json?alt=media&token=6027c6f3-34a2-4978-af61-74a14a9a25ba";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        @SuppressLint("NotifyDataSetChanged") JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, response -> {
            try {
                JSONArray categories = response.getJSONArray("categories");
                JSONObject categoriesData = categories.getJSONObject(0);

                // Categories
                JSONArray categoriesPrograming = categoriesData.getJSONArray("programingVideos");
                JSONArray categoriesSponsored = categoriesData.getJSONArray("sponsoredVideos");

                for (int i = 0; i < categoriesPrograming.length();i++){
                    JSONObject video = categoriesPrograming.getJSONObject(i);

                    ProgrammingVideo fetchedVid = new ProgrammingVideo();

                    fetchedVid.setTitle(video.getString("title"));
                    fetchedVid.setDescription(video.getString("description"));
                    fetchedVid.setAuthor(video.getString("author"));
                    fetchedVid.setImageUrl(video.getString("thumb"));
                    JSONArray videoUrl = video.getJSONArray("sources");
                    fetchedVid.setVideoUrl(videoUrl.getString(0));
                    videoPrograming.add(fetchedVid);
                    adapterPrograming.notifyDataSetChanged();
                }

                for (int i = 0; i < categoriesSponsored.length();i++){
                    JSONObject video = categoriesSponsored.getJSONObject(i);

                    SponsoredVideo fetchedVid = new SponsoredVideo();

                    fetchedVid.setTitle(video.getString("title"));
                    fetchedVid.setDescription(video.getString("description"));
                    fetchedVid.setAuthor(video.getString("author"));
                    fetchedVid.setImageUrl(video.getString("thumb"));
                    JSONArray videoUrl = video.getJSONArray("sources");
                    fetchedVid.setVideoUrl(videoUrl.getString(0));
                    videoSponsored.add(fetchedVid);
                    adapterSponsored.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.d(TAG, "onErrorResponse: " + error.getMessage()));
        requestQueue.add(objectRequest);
    }
}