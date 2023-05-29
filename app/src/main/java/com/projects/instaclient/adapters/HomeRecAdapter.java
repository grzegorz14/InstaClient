package com.projects.instaclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.projects.instaclient.R;
import com.projects.instaclient.databinding.PostListItemBinding;
import com.projects.instaclient.model.Image;
import com.projects.instaclient.model.Post;
import com.projects.instaclient.service.RetrofitService;

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

        Glide.with(holder.profileImageView.getContext())
                .load("http://" + RetrofitService.getServerHost() + "/api/uploads/" + post.getSimpleUser().getId() + "/profile_image.jpg")
                .error(R.drawable.profile)
                .into(holder.profileImageView);

        Glide.with(holder.postImageView.getContext())
                .load("http://" + RetrofitService.getServerHost() + "/api/" + post.getImage().getUrl())
                .into(holder.postImageView);

        String tags = "";
        for (String tag : post.getTags()) {
            if (!tag.isEmpty()) {
                tags += "#" + tag + "  ";
            }
        }
        holder.tagsTextView.setText(tags);

        holder.heartLottie.setOnClickListener(v -> {
            if (holder.isHeartClicked) {
                post.setLikes(post.getLikes() - 1);
                holder.binding.setPost(post);
                holder.heartLottie.setMinAndMaxProgress(0.5f, 1f);
            }
            else {
                post.setLikes(post.getLikes() + 1);
                holder.binding.setPost(post);
                holder.heartLottie.setMinAndMaxProgress(0f, 0.5f);
            }
            holder.heartLottie.playAnimation();
            holder.isHeartClicked = !holder.isHeartClicked;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {

        private PostListItemBinding binding;

        private ImageView profileImageView;
        private ImageView postImageView;
        private TextView tagsTextView;
        private LottieAnimationView heartLottie;
        private boolean isHeartClicked = false;

        public HomeViewHolder(View itemView, PostListItemBinding binding) {
            super(itemView);

            this.binding = binding;

            this.profileImageView = itemView.findViewById(R.id.profileImagePostListItemImageView);
            this.postImageView = itemView.findViewById(R.id.postImagePostListItemImageView);
            this.tagsTextView = itemView.findViewById(R.id.tagsPostListItemTextView);

            this.heartLottie = itemView.findViewById(R.id.heartLottie);
            this.heartLottie.setSpeed(3);
            this.heartLottie.setMinAndMaxProgress(0f, 0.5f);
        }
    }
}
