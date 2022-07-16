package id.xyzprjkt.pintarin.VideoController.Sponsored;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.faltenreich.skeletonlayout.Skeleton;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.xyzprjkt.pintarin.R;

public class SponsoredPlayer extends Activity {

    ExoPlayer exoPlayer;
    StyledPlayerView playerView;

    MaterialCardView authorContainer;
    TextView title, desc, author, authorMajor;
    ImageView authorPic;

    List<SponsoredVideo> videoSponsored;
    SponsoredAdapter adapterSponsored;
    RecyclerView recommend;
    Skeleton loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent i = getIntent();
        Bundle data = i.getExtras();
        SponsoredVideo v = (SponsoredVideo) data.getSerializable("videoData");

        title = findViewById(R.id.videoTitle);
        desc = findViewById(R.id.videoDesc);

        authorContainer = findViewById(R.id.videoAuthorContainer);
        author = findViewById(R.id.videoAuthorName);
        authorMajor = findViewById(R.id.videoAuthorMajor);
        authorPic = findViewById(R.id.videoAuthorPic);

        title.setText(v.getTitle());
        desc.setText(v.getDescription());
        author.setText(v.getAuthor());

        if(v.getAuthor().equals("Kiara Azzahra") || v.getAuthor().equals("Rosydan Amru") || v.getAuthor().equals("xyzuan")) {
            authorContainer.setVisibility(View.VISIBLE);
            if (v.getAuthor().equals("Kiara Azzahra") || v.getAuthor().equals("Rosydan Amru")) {
                authorMajor.setText("Speaker");
            } else if (v.getAuthor().equals("xyzuan")){
                authorMajor.setText("xyzscape Developer");
            }
            switch (v.getAuthor()) {
                case "Kiara Azzahra":
                    authorPic.setImageResource(R.drawable.about_zara);
                    break;
                case "Rosydan Amru":
                    authorPic.setImageResource(R.drawable.about_rosydan);
                    break;
                case "xyzuan":
                    authorPic.setImageResource(R.drawable.about_xyzuan);
                    break;
            }
        }

        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView = findViewById(R.id.player_view);
        playerView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(v.getVideoUrl()));
        exoPlayer.addMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);

        getRecommended();
    }

    private void getRecommended() {
        TextView sponsoredCategory;
        LinearLayout includedSponsored;

        loading = findViewById(R.id.skeletonLayout);
        sponsoredCategory = findViewById(R.id.sponsoredCategory);
        recommend = findViewById(R.id.courseSponsored);
        includedSponsored = findViewById(R.id.includedSponsored);

        includedSponsored.setVisibility(View.VISIBLE);
        videoSponsored = new ArrayList<>();
        adapterSponsored = new SponsoredAdapter(this, videoSponsored);
        recommend.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL , false));
        recommend.setAdapter(adapterSponsored);
        sponsoredCategory.setText("Recommended video");
        sponsoredCategory.setPadding(24,0,0,0);
        sponsoredCategory.setVisibility(View.VISIBLE);
        loading.showSkeleton();
        getJsonData();
    }

    private void getJsonData() {
        String URL = "https://raw.githubusercontent.com/xyz-prjkt/pintar.in/master/videoDB.json";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        @SuppressLint("NotifyDataSetChanged") JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, response -> {
            try {
                JSONArray categories = response.getJSONArray("categories");
                JSONObject categoriesData = categories.getJSONObject(0);
                JSONArray categoriesPrograming = categoriesData.getJSONArray("sponsoredVideos");

                for (int i = 0; i < categoriesPrograming.length();i++){
                    JSONObject video = categoriesPrograming.getJSONObject(i);

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
        }, error -> Log.d("pintarin: ", "onErrorResponse: " + error.getMessage()));
        requestQueue.add(objectRequest);
        new Handler(Looper.getMainLooper()).postDelayed(() -> loading.showOriginal(), 2000);
    }

    @Override
    public void onStop() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.stop();
            exoPlayer.seekTo(0);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.stop();
            exoPlayer.seekTo(0);
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(true);
        }
        super.onResume();
    }
}