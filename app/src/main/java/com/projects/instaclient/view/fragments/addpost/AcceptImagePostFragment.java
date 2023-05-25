package com.projects.instaclient.view.fragments.addpost;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.instaclient.R;
import com.projects.instaclient.databinding.FragmentAcceptImagePostBinding;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.view.fragments.NavigationFragment;

public class AcceptImagePostFragment extends Fragment {

    private FragmentAcceptImagePostBinding binding;
    private Bitmap image;
    private String imageUri;

    public AcceptImagePostFragment(Bitmap image, String imageUri) {
        this.image = image;
        this.imageUri = imageUri;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAcceptImagePostBinding.inflate(getLayoutInflater());

        binding.acceptPostImageImageView.setImageBitmap(image);

        // ON CLICKS
        binding.rotateLeftButton.setOnClickListener(v -> {
            image = rotate(image, false);
            binding.acceptPostImageImageView.setImageBitmap(image);
        });
        binding.rotateRightButton.setOnClickListener(v -> {
            image = rotate(image, true);
            binding.acceptPostImageImageView.setImageBitmap(image);
        });
        binding.flipHorizontalButton.setOnClickListener(v -> {
            image = flip(image, true, false);
            binding.acceptPostImageImageView.setImageBitmap(image);
        });
        binding.flipVerticalButton.setOnClickListener(v -> {
            image = flip(image, false, true);
            binding.acceptPostImageImageView.setImageBitmap(image);
        });

        binding.cancelImageButton.setOnClickListener(v -> {
            Helpers.replaceMainFragment(getParentFragmentManager(), new NavigationFragment(new AddPostCameraFragment()));
        });

        binding.acceptImageButton.setOnClickListener(v -> {
            Helpers.replaceMainFragment(getParentFragmentManager(), new DescribeNewPostFragment(image, imageUri));
        });

        return binding.getRoot();
    }

    private static Bitmap rotate(Bitmap bitmap, boolean right) {
        Matrix matrix = new Matrix();
        if (right) {
            matrix.postRotate(90);
        }
        else {
            matrix.postRotate(-90);
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}