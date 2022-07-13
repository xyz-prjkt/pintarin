package id.xyzprjkt.pintarin.VideoController.BasicProgramming;

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

public class BasicProgrammingAdapter extends RecyclerView.Adapter<BasicProgrammingAdapter.ViewHolder> {
    private final List<BasicProgrammingVideo> allBasicProgrammingVideos;
    private final Context context;

    public BasicProgrammingAdapter(Context ctx, List<BasicProgrammingVideo> basicProgrammingVideos){
        this.allBasicProgrammingVideos = basicProgrammingVideos;
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
        holder.title.setText(allBasicProgrammingVideos.get(position).getTitle());
        holder.author.setText(allBasicProgrammingVideos.get(position).getAuthor());
        Picasso.get().load(allBasicProgrammingVideos.get(position).getImageUrl()).into(holder.videoImage);

        holder.vv.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putSerializable("videoData", allBasicProgrammingVideos.get(position));
            Intent i = new Intent(context, BasicProgramingPlayer.class);
            i.putExtras(b);
            v.getContext().startActivity(i);

        });
    }

    @Override
    public int getItemCount() {
        return allBasicProgrammingVideos.size();
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
