package com.projects.instaclient.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.SimpleExoPlayer;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.projects.instaclient.R;
import com.projects.instaclient.databinding.PostListItemBinding;
import com.projects.instaclient.model.Image;
import com.projects.instaclient.model.Post;
import com.projects.instaclient.service.RetrofitService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomeRecAdapter extends RecyclerView.Adapter<HomeRecAdapter.HomeViewHolder> {
    private List<Post> list;
    private LayoutInflater layoutInflater;

    public HomeRecAdapter(List<Post> list, LayoutInflater layoutInflater) {
        Collections.shuffle(list);
        this.list = list;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public HomeRecAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PostListItemBinding binding = PostListItemBinding.inflate(layoutInflater, parent, false);
        View view = binding.getRoot();

        return new HomeRecAdapter.HomeViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Post post = list.get(position);

        holder.binding.setPost(post);

        Glide.with(holder.binding.profileImagePostListItemImageView.getContext())
                .load("http://" + RetrofitService.getServerHost() + "/api/uploads/" + post.getSimpleUser().getId() + "/profile_image.jpg")
                .error(R.drawable.profile)
                .into(holder.binding.profileImagePostListItemImageView);

        // video or image
        if (post.getImage().getUrl().contains("mp4")) {
            ExoPlayer player = new ExoPlayer.Builder(layoutInflater.getContext()).build();
            holder.binding.postVideoPostListItemPlayerView.setPlayer(player);
            MediaItem firstItem = MediaItem.fromUri("http://" + RetrofitService.getServerHost() + "/api/" + post.getImage().getUrl());
            player.addMediaItem(firstItem);
            player.prepare();
            player.setPlayWhenReady(false);

            holder.binding.postImagePostListItemImageView.setVisibility(View.GONE);
        }
        else {
            Glide.with(holder.binding.postImagePostListItemImageView.getContext())
                    .load("http://" + RetrofitService.getServerHost() + "/api/" + post.getImage().getUrl())
                    .into(holder.binding.postImagePostListItemImageView);
            holder.binding.postVideoPostListItemPlayerView.setVisibility(View.GONE);
        }

        String tags = "";
        for (String tag : post.getTags()) {
            if (!tag.isEmpty()) {
                tags += "#" + tag + "  ";
            }
        }
        holder.binding.tagsPostListItemTextView.setText(tags);

        holder.binding.heartLottie.setOnClickListener(v -> {
            if (holder.isHeartClicked) {
                post.setLikes(post.getLikes() - 1);
                holder.binding.setPost(post);
                holder.binding.heartLottie.setMinAndMaxProgress(0.5f, 1f);
            }
            else {
                post.setLikes(post.getLikes() + 1);
                holder.binding.setPost(post);
                holder.binding.heartLottie.setMinAndMaxProgress(0f, 0.5f);
            }
            holder.binding.heartLottie.playAnimation();
            holder.isHeartClicked = !holder.isHeartClicked;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {

        private PostListItemBinding binding;
        private boolean isHeartClicked = false;

        public HomeViewHolder(View itemView, PostListItemBinding binding) {
            super(itemView);

            this.binding = binding;

            binding.heartLottie.setSpeed(3);
            binding.heartLottie.setMinAndMaxProgress(0f, 0.5f);
        }
    }
}
