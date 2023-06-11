package com.projects.instaclient.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.projects.instaclient.R;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.model.Post;
import com.projects.instaclient.service.RetrofitService;
import com.projects.instaclient.view.fragments.profile.PostFragment;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class ProfileRecAdapter extends RecyclerView.Adapter<ProfileRecAdapter.ProfileViewHolder>{
    private List<Post> list;
    private LayoutInflater layoutInflater;
    private FragmentActivity activity;
    private int cols;

    public ProfileRecAdapter(List<Post> list, LayoutInflater layoutInflater, FragmentActivity activity, int cols) {
        list.sort(Comparator.comparing(Post::getDate));
        Collections.reverse(list);
        this.list = list;
        this.layoutInflater = layoutInflater;
        this.activity = activity;
        this.cols = cols;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_list_item, parent, false);
        return new ProfileViewHolder(v, activity, cols);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Post post = list.get(position);

        holder.setPost(post);

        // video or image
        // after changes only play video image is visible on mp4 file
        holder.playerView.setVisibility(View.GONE);
        if (post.getImage().getUrl().contains("mp4")) {
            Glide.with(holder.imageView.getContext())
                    .load(R.drawable.play_video)
                    .into(holder.imageView);
//            holder.imageView.setOnClickListener(v -> {
//                ExoPlayer player = new ExoPlayer.Builder(layoutInflater.getContext()).build();
//                holder.playerView.setPlayer(player);
//                MediaItem firstItem = MediaItem.fromUri("http://" + RetrofitService.getServerHost() + "/api/" + post.getImage().getUrl());
//                player.addMediaItem(firstItem);
//                player.prepare();
//                player.setPlayWhenReady(false);
//
//                holder.imageView.setVisibility(View.GONE);
//                holder.playerView.setVisibility(View.VISIBLE);
//            });
        }
        else {
            Glide.with(holder.imageView.getContext())
                    .load("http://" + RetrofitService.getServerHost() + "/api/" + post.getImage().getUrl())
                    .into(holder.imageView);
        }
    }

    @Override
    public void onViewRecycled(@NonNull ProfileViewHolder holder) {
        if (holder.playerView.getPlayer() != null) {
            holder.playerView.getPlayer().stop();
            holder.playerView.getPlayer().release();
            holder.playerView.setPlayer(null);
        }
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private PlayerView playerView;
        private Random random = new Random();
        private Post post;

        public ProfileViewHolder(View itemView, FragmentActivity activity, int cols) {
            super(itemView);

            this.imageView = itemView.findViewById(R.id.imageListItemImageView);
            this.playerView = itemView.findViewById(R.id.postVideoListItemPlayerView);

            int height = random.nextInt(300) + 600 - 100 * cols;
            this.imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
            this.playerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
            this.imageView.setOnClickListener(v -> {
                Helpers.replaceNavigationFragment(activity.getSupportFragmentManager(), new PostFragment(post));
            });
            this.playerView.setOnClickListener(v -> {
                Helpers.replaceNavigationFragment(activity.getSupportFragmentManager(), new PostFragment(post));
            });
        }

        public Post getPost() {
            return post;
        }

        public void setPost(Post post) {
            this.post = post;
        }
    }
}
