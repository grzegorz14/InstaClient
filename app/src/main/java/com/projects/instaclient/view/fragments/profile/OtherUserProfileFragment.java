package com.projects.instaclient.view.fragments.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.projects.instaclient.R;
import com.projects.instaclient.adapters.ProfileRecAdapter;
import com.projects.instaclient.databinding.FragmentOtherUserProfileBinding;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.model.Post;
import com.projects.instaclient.model.User;
import com.projects.instaclient.model.response.ResponseWrapper;
import com.projects.instaclient.service.RetrofitService;
import com.projects.instaclient.view.fragments.HomeFragment;

import java.util.ArrayList;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherUserProfileFragment extends Fragment {

    private FragmentOtherUserProfileBinding binding;
    private User user;

    public OtherUserProfileFragment(User user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOtherUserProfileBinding.inflate(getLayoutInflater());

        binding.setUser(user);

        int postsNumber = user.getPosts().size();
        if (postsNumber == 0) {
            binding.otherProfileLikesSumTextView.setText("0 likes");
        }
        else {
            // GET POSTS
            getPostsOfUser(user.getId());
        }

        // SET PROFILE IMAGE
        Glide.with(binding.otherProfileImageProfileImageView.getContext())
                .load("http://" + RetrofitService.getServerHost() + "/api/" + user.getProfileImage().getUrl())
                .error(R.drawable.profile)
                .into(binding.otherProfileImageProfileImageView);


        return binding.getRoot();
    }

    private void getPostsOfUser(String userId) {
        Call<ResponseWrapper<ArrayList<Post>>> call = RetrofitService.getInstance().getPostAPI().getAllPostOfUserById(userId);
        call.enqueue(new Callback<ResponseWrapper<ArrayList<Post>>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<ArrayList<Post>>> call, Response<ResponseWrapper<ArrayList<Post>>> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                } else {
                    if (response.body().getSuccess()) {
                        // SET USER POSTS
                        if (response.body().getData().size() > 0) {
                            binding.otherProfileNoPostsProfileLinearLayout.setVisibility(View.GONE);
                        }
                        int cols = response.body().getData().size() < 3 ? 1 : (response.body().getData().size() < 9 ? 2 : 3);
                        ProfileRecAdapter adapter = new ProfileRecAdapter(response.body().getData(), getLayoutInflater(), getActivity(), cols);

                        // SET RECYCLER VIEW
                        RecyclerView recyclerView = binding.otherProfileImagesRecyclerView;

                        StaggeredGridLayoutManager staggeredGridLayoutManager
                                = new StaggeredGridLayoutManager(cols, LinearLayout.VERTICAL);
                        recyclerView.setLayoutManager(staggeredGridLayoutManager);
                        recyclerView.setAdapter(adapter);

                        // SET LIKES SUM
                        int likes = response.body().getData().stream().flatMapToInt(post -> IntStream.of(post.getLikes())).sum();
                        binding.otherProfileLikesSumTextView.setText(Helpers.convertLikesToText(likes));
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<ArrayList<Post>>> call, Throwable t) {
                Log.d("xxx", t.getMessage());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}