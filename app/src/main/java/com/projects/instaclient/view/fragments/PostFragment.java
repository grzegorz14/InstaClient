package com.projects.instaclient.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
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
                .into(binding.profileImagePostImageView);

        Glide.with(binding.postImagePostImageView.getContext())
                .load("http://" + RetrofitService.getServerHost() + "/api/" + post.getImage().getUrl())
                .into(binding.postImagePostImageView);

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