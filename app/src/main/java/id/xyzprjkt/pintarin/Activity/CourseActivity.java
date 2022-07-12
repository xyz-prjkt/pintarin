package id.xyzprjkt.pintarin.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.faltenreich.skeletonlayout.Skeleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.xyzprjkt.pintarin.R;
import id.xyzprjkt.pintarin.VideoController.BasicProgramming.BasicProgrammingAdapter;
import id.xyzprjkt.pintarin.VideoController.BasicProgramming.BasicProgrammingVideo;
import id.xyzprjkt.pintarin.VideoController.Programming.ProgrammingAdapter;
import id.xyzprjkt.pintarin.VideoController.Programming.ProgrammingVideo;
import id.xyzprjkt.pintarin.VideoController.Sponsored.SponsoredAdapter;
import id.xyzprjkt.pintarin.VideoController.Sponsored.SponsoredVideo;

public class CourseActivity extends Activity {
    public static final String TAG = "TAG";

    TextView programingCategory, basicProgramingCategory, sponsoredCategory;
    RecyclerView coursePrograming, courseBasicPrograming, courseSponsored;

    ProgrammingAdapter adapterPrograming;
    BasicProgrammingAdapter adapterBasicPrograming;
    SponsoredAdapter adapterSponsored;

    List<ProgrammingVideo> videoPrograming;
    List<BasicProgrammingVideo> videoBasicPrograming;
    List<SponsoredVideo> videoSponsored;

    Skeleton loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        loading = findViewById(R.id.skeletonLayout);
        loading.showSkeleton();

        // Stored Video
        videoPrograming = new ArrayList<>();
        videoSponsored = new ArrayList<>();
        videoBasicPrograming = new ArrayList<>();

        // Programming
        programingCategory = findViewById(R.id.programingCategory);
        coursePrograming = findViewById(R.id.coursePrograming);
        coursePrograming.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL , false));
        adapterPrograming = new ProgrammingAdapter(this, videoPrograming);
        coursePrograming.setAdapter(adapterPrograming);

        // Basic Programming
        basicProgramingCategory = findViewById(R.id.basicProgramingCategory);
        courseBasicPrograming = findViewById(R.id.courseBasicPrograming);
        courseBasicPrograming.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL , false));
        adapterBasicPrograming = new BasicProgrammingAdapter(this, videoBasicPrograming);
        courseBasicPrograming.setAdapter(adapterBasicPrograming);

        // Sponsored
        sponsoredCategory = findViewById(R.id.sponsoredCategory);
        courseSponsored = findViewById(R.id.courseSponsored);
        courseSponsored.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL , false));
        adapterSponsored = new SponsoredAdapter(this, videoSponsored);
        courseSponsored.setAdapter(adapterSponsored);

        getJsonData();
    }

    private void getJsonData() {
        String URL = "https://raw.githubusercontent.com/xyz-prjkt/pintar.in/master/videoDB.json";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        @SuppressLint("NotifyDataSetChanged") JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, response -> {
            try {
                JSONArray categories = response.getJSONArray("categories");
                JSONObject categoriesData = categories.getJSONObject(0);

                // Categories
                JSONArray categoriesPrograming = categoriesData.getJSONArray("programingVideos");
                JSONArray categoriesBasicPrograming = categoriesData.getJSONArray("basicProgramingVideos");
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
                    programingCategory.setVisibility(View.VISIBLE);
                    videoPrograming.add(fetchedVid);
                    adapterPrograming.notifyDataSetChanged();
                }

                for (int i = 0; i < categoriesBasicPrograming.length();i++){
                    JSONObject video = categoriesBasicPrograming.getJSONObject(i);

                    BasicProgrammingVideo fetchedVid = new BasicProgrammingVideo();

                    fetchedVid.setTitle(video.getString("title"));
                    fetchedVid.setDescription(video.getString("description"));
                    fetchedVid.setAuthor(video.getString("author"));
                    fetchedVid.setImageUrl(video.getString("thumb"));
                    JSONArray videoUrl = video.getJSONArray("sources");
                    fetchedVid.setVideoUrl(videoUrl.getString(0));
                    basicProgramingCategory.setVisibility(View.VISIBLE);
                    videoBasicPrograming.add(fetchedVid);
                    adapterBasicPrograming.notifyDataSetChanged();
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
                    sponsoredCategory.setVisibility(View.VISIBLE);
                    videoSponsored.add(fetchedVid);
                    adapterSponsored.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.d(TAG, "onErrorResponse: " + error.getMessage()));
        requestQueue.add(objectRequest);
        loading.showOriginal();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DashboardActivity.class));
    }
}