package id.xyzprjkt.pintarin.VideoController.BasicProgramming;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

import id.xyzprjkt.pintarin.R;

public class BasicProgramingPlayer extends Activity {

    FrameLayout frameLayout;
    ExoPlayer exoPlayer;
    PlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        frameLayout = findViewById(R.id.frameLayout);
        Intent i = getIntent();
        Bundle data = i.getExtras();
        BasicProgrammingVideo v = (BasicProgrammingVideo) data.getSerializable("videoData");
        TextView title = findViewById(R.id.videoTitle);
        TextView desc = findViewById(R.id.videoDesc);
        title.setText(v.getTitle());
        desc.setText(v.getDescription());
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView = findViewById(R.id.player_view);
        playerView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(v.getVideoUrl()));
        exoPlayer.addMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoPlayer.release();
    }
}