package com.projects.instaclient.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

import com.bumptech.glide.Glide;
import com.projects.instaclient.R;
import com.projects.instaclient.databinding.FragmentPostBinding;
import com.projects.instaclient.model.Post;
import com.projects.instaclient.service.RetrofitService;

public class PostFragment extends Fragment {

    private FragmentPostBinding binding;
    private Post post;

    public PostFragment(Post post) {
        this.post = post;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostBinding.inflate(getLayoutInflater());

        binding.setPost(post);

        Glide.with(binding.profileImagePostImageView.getContext())
                .load("http://" + RetrofitService.getServerHost() + "/api/uploads/" + post.getSimpleUser().getId() + "/profile_image.jpg")
                .error(R.drawable.profile)
                .into(binding.profileImagePostImageView);

        // video or image
        if (post.getImage().getUrl().contains("mp4")) {
            ExoPlayer player = new ExoPlayer.Builder(getLayoutInflater().getContext()).build();
            binding.postVideoPostPlayerView.setPlayer(player);
            MediaItem firstItem = MediaItem.fromUri("http://" + RetrofitService.getServerHost() + "/api/" + post.getImage().getUrl());
            player.addMediaItem(firstItem);
            player.prepare();
            player.setPlayWhenReady(false);

            binding.postImagePostImageView.setVisibility(View.GONE);
        }
        else {
            Glide.with(binding.postImagePostImageView.getContext())
                    .load("http://" + RetrofitService.getServerHost() + "/api/" + post.getImage().getUrl())
                    .into(binding.postImagePostImageView);
            binding.postVideoPostPlayerView.setVisibility(View.GONE);
        }

        String tags = "";
        for (String tag : post.getTags()) {
            if (!tag.isEmpty()) {
                tags += "#" + tag + "  ";
            }
        }
        binding.tagsPostTextView.setText(tags);

        return binding.getRoot();
    }
}