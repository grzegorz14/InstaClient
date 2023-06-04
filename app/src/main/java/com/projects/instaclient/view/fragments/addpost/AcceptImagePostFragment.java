package com.projects.instaclient.view.fragments.addpost;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.projects.instaclient.databinding.FragmentAcceptImagePostBinding;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.service.RetrofitService;
import com.projects.instaclient.view.fragments.NavigationFragment;

public class AcceptImagePostFragment extends Fragment {

    private FragmentAcceptImagePostBinding binding;
    private Bitmap image;
    private String fileUri;

    public AcceptImagePostFragment(Bitmap image, String fileUri) {
        this.image = image;
        this.fileUri = fileUri;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAcceptImagePostBinding.inflate(getLayoutInflater());

        // video or image
        if (fileUri.contains("mp4")) {
            ExoPlayer player = new ExoPlayer.Builder(getLayoutInflater().getContext()).build();
            binding.acceptPostVideoPlayerView.setPlayer(player);
            MediaItem firstItem = MediaItem.fromUri(fileUri);
            player.addMediaItem(firstItem);
            player.prepare();
            player.setPlayWhenReady(false);

            binding.acceptPostImageImageView.setVisibility(View.GONE);
        }
        else {
            binding.acceptPostImageImageView.setImageBitmap(image);
            binding.acceptPostVideoPlayerView.setVisibility(View.GONE);
        }

        binding.cancelImageButton.setOnClickListener(v -> {
            Helpers.replaceMainFragment(getParentFragmentManager(), new NavigationFragment(new AddPostCameraFragment()));
        });

        binding.acceptImageButton.setOnClickListener(v -> {
            Helpers.replaceMainFragment(getParentFragmentManager(), new DescribeNewPostFragment(fileUri));
        });

        return binding.getRoot();
    }
}