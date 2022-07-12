package id.xyzprjkt.pintarin.VideoController.Sponsored;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.xyzprjkt.pintarin.R;

public class SponsoredAdapter extends RecyclerView.Adapter<SponsoredAdapter.ViewHolder> {
    private final List<SponsoredVideo> allSponsoredVideos;
    private final Context context;

    public SponsoredAdapter(Context ctx, List<SponsoredVideo> sponsoredVideos){
        this.allSponsoredVideos = sponsoredVideos;
        this.context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_view,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.title.setText(allSponsoredVideos.get(position).getTitle());
        holder.author.setText(allSponsoredVideos.get(position).getAuthor());
        Picasso.get().load(allSponsoredVideos.get(position).getImageUrl()).into(holder.videoImage);

        holder.vv.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putSerializable("videoData", allSponsoredVideos.get(position));
            Intent i = new Intent(context, SponsoredPlayer.class);
            i.putExtras(b);
            v.getContext().startActivity(i);

        });
    }

    @Override
    public int getItemCount() {
        return allSponsoredVideos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView videoImage;
        TextView title;
        TextView author;
        View vv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            videoImage = itemView.findViewById(R.id.videoThumbnail);
            title = itemView.findViewById(R.id.videoTitle);
            author = itemView.findViewById(R.id.videoAuthor);
            vv = itemView;

        }
    }
}
