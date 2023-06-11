package com.projects.instaclient.view.fragments.editpost;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.projects.instaclient.R;
import com.projects.instaclient.api.PostAPI;
import com.projects.instaclient.databinding.FragmentEditImageBinding;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.model.Image;
import com.projects.instaclient.model.Post;
import com.projects.instaclient.model.request.EditImageRequest;
import com.projects.instaclient.model.response.ResponseWrapper;
import com.projects.instaclient.service.RetrofitService;
import com.projects.instaclient.view.fragments.NavigationFragment;
import com.projects.instaclient.view.fragments.profile.PostFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditImageFragment extends Fragment {

    private FragmentEditImageBinding binding;

    private Post post;

    public EditImageFragment(Post post) {
        this.post = post;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditImageBinding.inflate(getLayoutInflater());

        // SET IMAGE
        Glide.with(binding.editImageImageView.getContext())
                .load("http://" + RetrofitService.getServerHost() + "/api/" + post.getImage().getUrl())
                .into(binding.editImageImageView);

        // ON CLICKS
        binding.rotateLeftButton.setOnClickListener(v -> {
            transformImage(new EditImageRequest(post.getId(), "rotate", -90));
        });

        binding.rotateRightButton.setOnClickListener(v -> {
            transformImage(new EditImageRequest(post.getId(), "rotate", 90));
        });

        binding.grayscaleButton.setOnClickListener(v -> {
            transformImage(new EditImageRequest(post.getId(), "grayscale", 0));
        });

        binding.negateButton.setOnClickListener(v -> {
            transformImage(new EditImageRequest(post.getId(), "negate", 0));
        });

        binding.acceptEditImageButton.setOnClickListener(v -> {
            Helpers.replaceMainFragment(getParentFragmentManager(), new NavigationFragment(new PostFragment(post)));
        });
        binding.cancelEditImageButton.setOnClickListener(v -> {
            setBasicImage();
        });

        return binding.getRoot();
    }

    private void transformImage(EditImageRequest editImageRequest) {

        RetrofitService retrofitService = RetrofitService.getInstance();
        PostAPI postAPI = retrofitService.getPostAPI();
        Call<ResponseWrapper<Image>> call = postAPI.patchEditImage(editImageRequest);

        call.enqueue(new Callback<ResponseWrapper<Image>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<Image>> call, Response<ResponseWrapper<Image>> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                } else {
                    if (response.body().getSuccess()) {
                        post.getImage().setUrl(response.body().getData().getUrl());
                        Glide.with(binding.editImageImageView.getContext())
                                .load("http://" + RetrofitService.getServerHost() + "/api/" + post.getImage().getUrl())
                                .error(R.drawable.empty_image)
                                .into(binding.editImageImageView);
                    }
                    else {
                        Log.d("xxx", response.body().getMessage());
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<Image>> call, Throwable t) {
            }
        });
    }

    private void setBasicImage() {

        RetrofitService retrofitService = RetrofitService.getInstance();
        PostAPI postAPI = retrofitService.getPostAPI();
        Call<ResponseWrapper<Image>> call = postAPI.patchBasicImage(post.getImage().getId());

        call.enqueue(new Callback<ResponseWrapper<Image>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<Image>> call, Response<ResponseWrapper<Image>> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                } else {
                    if (response.body().getSuccess()) {
                        post.getImage().setUrl(response.body().getData().getUrl());
                        Helpers.replaceMainFragment(getParentFragmentManager(), new NavigationFragment(new PostFragment(post)));
                    }
                    else {
                        Log.d("xxx", response.body().getMessage());
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<Image>> call, Throwable t) {
            }
        });
    }
}