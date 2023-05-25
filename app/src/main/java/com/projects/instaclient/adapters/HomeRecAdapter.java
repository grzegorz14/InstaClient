package com.projects.instaclient.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.projects.instaclient.R;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.model.Post;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HomeRecAdapter extends RecyclerView.Adapter<HomeRecAdapter.HomeViewHolder> {
    private List<Post> list;
    private Context context;
    private Random random = new Random();

    public HomeRecAdapter(List<Post> list, Context context) {
//        List<Post> imagesLong = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            imagesLong.add(list.get(random.nextInt(list.size())));
//        }
//        this.list = imagesLong;
        Collections.shuffle(list);
        list.addAll(list);
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeRecAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        binding = PostListItemBinding.inflate(layoutInflater, parent, false);
//        View view = binding.getRoot();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_list_item, parent, false);

        return new HomeRecAdapter.HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Post post = list.get(position);

        @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = context.getDrawable(post.getImage());

        String username = post.getUser().getFirstName() + " " + post.getUser().getLastName();

        holder.profileImageView.setImageDrawable(drawable);
        holder.usernameTextView.setText(username);
        holder.postImageView.setImageDrawable(drawable);
        holder.likesTextView.setText(Helpers.convertLikesToText(post.getLikes()));
        holder.descriptionTextView.setText(post.getUser().getFirstName() + " - " + post.getDescription());

        holder.heartLottie.setOnClickListener(v -> {
            if (holder.isHeartClicked) {
                holder.likesTextView.setText(Helpers.convertLikesToText(post.getLikes()));
                holder.heartLottie.setMinAndMaxProgress(0.5f, 1f);
            }
            else {
                holder.likesTextView.setText(Helpers.convertLikesToText(post.getLikes() + 1));
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

        private ImageView profileImageView;
        private ImageView postImageView;
        private TextView usernameTextView;
        private TextView likesTextView;
        private TextView descriptionTextView;
        private LottieAnimationView heartLottie;
        private boolean isHeartClicked = false;

        private Random random = new Random();

        public HomeViewHolder(View itemView) {
            super(itemView);

            this.profileImageView = itemView.findViewById(R.id.profileImagePostListItemImageView);
            this.postImageView = itemView.findViewById(R.id.postImagePostListItemImageView);
            this.usernameTextView = itemView.findViewById(R.id.userNamePostListItemTextView);
            this.likesTextView = itemView.findViewById(R.id.likesCounterPostListItemTextView);
            this.descriptionTextView = itemView.findViewById(R.id.descriptionPostListItemTextView);

            this.heartLottie = itemView.findViewById(R.id.heartLottie);
            this.heartLottie.setSpeed(3);
            this.heartLottie.setMinAndMaxProgress(0f, 0.5f);
        }
    }
}
