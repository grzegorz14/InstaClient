package com.projects.instaclient.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.projects.instaclient.api.PostAPI;
import com.projects.instaclient.model.User;
import com.projects.instaclient.model.response.ResponseWrapper;
import com.projects.instaclient.view.fragments.profile.OtherUserProfileFragment;
import com.projects.instaclient.R;
import com.projects.instaclient.databinding.PostListItemBinding;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.model.Post;
import com.projects.instaclient.service.RetrofitService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRecAdapter extends RecyclerView.Adapter<HomeRecAdapter.HomeViewHolder> {
    private final List<Post> list;
    private LayoutInflater layoutInflater;
    private FragmentManager fragmentManager;

    public HomeRecAdapter(List<Post> list, LayoutInflater layoutInflater, FragmentManager fragmentManager) {
        list.sort(Comparator.comparing(Post::getId)); // sort by creation date
        Collections.reverse(list);
        this.list = list;
        this.layoutInflater = layoutInflater;
        this.fragmentManager = fragmentManager;
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
                    .error(R.drawable.empty_image)
                    .into(holder.binding.postImagePostListItemImageView);
            holder.binding.postVideoPostListItemPlayerView.setVisibility(View.GONE);
        }

        StringBuilder tags = new StringBuilder();
        for (String tag : post.getTags()) {
            if (!tag.isEmpty()) {
                tags.append("#").append(tag).append("  ");
            }
        }
        holder.binding.tagsPostListItemTextView.setText(tags.toString());

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

        holder.binding.profileImagePostListItemImageView.setOnClickListener(v -> {
            goToOtherUserProfileFragment(post.getSimpleUser().getId());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {

        private final PostListItemBinding binding;
        private boolean isHeartClicked = false;

        public HomeViewHolder(View itemView, PostListItemBinding binding) {
            super(itemView);

            this.binding = binding;

            binding.heartLottie.setSpeed(3);
            binding.heartLottie.setMinAndMaxProgress(0f, 0.5f);
        }
    }

    private void goToOtherUserProfileFragment(String id) {
        RetrofitService retrofitService = RetrofitService.getInstance();
        Call<ResponseWrapper<User>> call = retrofitService.getPostAPI().getUserById(id);
        call.enqueue(new Callback<ResponseWrapper<User>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<User>> call, Response<ResponseWrapper<User>> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                }
                else {
                    if (response.body().getSuccess()) {
                        User responseUser = response.body().getData();
                        Helpers.replaceNavigationFragment(fragmentManager, new OtherUserProfileFragment(responseUser));
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<User>> call, Throwable t) {
                Log.d("xxx", t.getMessage());
            }
        });
    }
}
