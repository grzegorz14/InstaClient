package com.projects.instaclient.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.projects.instaclient.databinding.FragmentPostBinding;
import com.projects.instaclient.model.Post;

public class PostFragment extends Fragment {

    private FragmentPostBinding binding;
    private ImageView imageView;
    private Post post;

    public PostFragment(ImageView imageView, Post post) {
        this.imageView = imageView;
        this.post = post;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostBinding.inflate(getLayoutInflater());

        binding.setPost(post);
        binding.setImageView(imageView);

        return binding.getRoot();
    }
}