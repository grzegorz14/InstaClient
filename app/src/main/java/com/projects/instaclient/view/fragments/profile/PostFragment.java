package com.projects.instaclient.view.fragments.profile;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

import com.bumptech.glide.Glide;
import com.projects.instaclient.R;
import com.projects.instaclient.databinding.FragmentPostBinding;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.model.Post;
import com.projects.instaclient.model.response.ResponseWrapper;
import com.projects.instaclient.service.RetrofitService;
import com.projects.instaclient.view.MainActivity;
import com.projects.instaclient.view.fragments.HomeFragment;
import com.projects.instaclient.view.fragments.NavigationFragment;
import com.projects.instaclient.view.fragments.editpost.EditImageFragment;
import com.projects.instaclient.view.fragments.editpost.EditPostFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostFragment extends Fragment {

    private FragmentPostBinding binding;
    private Post post;

    public PostFragment(Post post) {
        this.post = post;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        binding = FragmentPostBinding.inflate(getLayoutInflater());

        binding.setPost(post);
        Log.d("xxx", "location: "  + post.getLocation());

        // SET IMAGE
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

        // ADD TAGS
        String tags = "";
        for (String tag : post.getTags()) {
            if (!tag.isEmpty()) {
                tags += "#" + tag + "  ";
            }
        }
        binding.tagsPostTextView.setText(tags);

        // ON CLICKS
        binding.editPostButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getContext(), v);

            // video or image menu
            if (post.getImage().getUrl().contains("mp4")) {
                popup.inflate(R.menu.post_edit_video_menu);
            }
            else {
                popup.inflate(R.menu.post_edit_menu);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popup.setForceShowIcon(true);
            }
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getTitle().toString()) {
                    case "Edit image":
                        Helpers.replaceMainFragment(getParentFragmentManager(), new EditImageFragment(post));
                        break;
                    case "Edit post data":
                        Helpers.replaceMainFragment(getParentFragmentManager(), new EditPostFragment(post));
                        break;
                    case "Delete post":
                        deletePost();
                        break;
                }
                return true;
            });
            popup.show();
        });

        // HIDE POST MENU FOR OTHER USERS
        if (!MainActivity.profileViewModel.getObservedProfile().getValue().getEmail().equals(post.getSimpleUser().getEmail())) {
            binding.editPostButton.setVisibility(View.GONE);
        }

        return binding.getRoot();
    }

    private void deletePost() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder
                .setCancelable(false)
                .setTitle("Delete post")
                .setMessage("Are you sure to delete this post?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Helpers.replaceMainFragment(getParentFragmentManager(), new NavigationFragment(new HomeFragment()));

                        RetrofitService retrofitService = RetrofitService.getInstance();
                        Call<ResponseWrapper<Post>> call = retrofitService.getPostAPI().deletePostById(post.getId());

                        call.enqueue(new Callback<ResponseWrapper<Post>>() {
                            @Override
                            public void onResponse(Call<ResponseWrapper<Post>> call, Response<ResponseWrapper<Post>> response) {
                                if (!response.isSuccessful()) {
                                    Log.d("xxx", String.valueOf(response.code()));
                                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                                } else {
                                    if (response.body().getSuccess()) {
                                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseWrapper<Post>> call, Throwable t) {
                                Log.d("xxx", t.getMessage());
                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}