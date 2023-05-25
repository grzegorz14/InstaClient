package com.projects.instaclient.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.model.Post;
import com.projects.instaclient.view.fragments.PostFragment;
import com.projects.instaclient.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProfileRecAdapter extends RecyclerView.Adapter<ProfileRecAdapter.ProfileViewHolder>{
    private List<Post> list;
    private Context context;
    private FragmentActivity activity;
    private Random random = new Random();

    public ProfileRecAdapter(List<Post> list, Context context, FragmentActivity activity) {
        List<Post> imagesLong = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            imagesLong.add(list.get(random.nextInt(list.size())));
        }
        this.list = imagesLong;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_list_item, parent, false);
        return new ProfileViewHolder(v, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Post post = list.get(position);
        @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = context.getDrawable(post.getImage());
        holder.imageView.setImageDrawable(drawable);

        holder.setPost(post);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private Random random = new Random();
        private Post post;

        public ProfileViewHolder(View itemView, FragmentActivity activity) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageListItemImageView);
            this.imageView.getBaseline();
            int h = random.nextInt(400) + 300;
            this.imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,h));

            this.imageView.setOnClickListener(v -> {
                Helpers.replaceNavigationFragment(activity.getSupportFragmentManager(), new PostFragment(imageView, post));
            });
        }

        public Post getPost() {
            return post;
        }

        public void setPost(Post post) {
            this.post = post;
        }
    }
}
