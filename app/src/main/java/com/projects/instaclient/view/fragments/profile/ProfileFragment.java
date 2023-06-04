package com.projects.instaclient.view.fragments.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.projects.instaclient.R;
import com.projects.instaclient.adapters.ProfileRecAdapter;
import com.projects.instaclient.api.PostAPI;
import com.projects.instaclient.databinding.FragmentProfileBinding;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.model.Post;
import com.projects.instaclient.model.User;
import com.projects.instaclient.model.response.ResponseWrapper;
import com.projects.instaclient.service.RetrofitService;
import com.projects.instaclient.view.MainActivity;
import com.projects.instaclient.view.fragments.HomeFragment;
import com.projects.instaclient.viewmodel.ProfileViewModel;

import java.util.ArrayList;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());

        // ON CLICKS
        binding.settingsImageView.setOnClickListener(v -> {
            authAndGoToSettingsFragment();
        });

        // GET VIEW MODEL
        profileViewModel = MainActivity.profileViewModel;

        // PROFILE IMAGE
        if (profileViewModel.getObservedProfile().getValue() != null) {
            binding.setProfileViewModel(profileViewModel);

            User user = profileViewModel.getObservedProfile().getValue();
            int postsNumber = user.getPosts().size();

            binding.profileLikesSumTextView.setText("0 likes");


            // GET POSTS
            getPostsOfUser(user.getId());

            // SET PROFILE IMAGE
            if (profileViewModel.getObservedProfile().getValue().getProfileImage() != null) {
                Glide.with(binding.profileImageProfileImageView.getContext())
                        .load("http://" + RetrofitService.getServerHost() + "/api/getfile/" + user.getProfileImage().getId())
                        .error(R.drawable.profile)
                        .into(binding.profileImageProfileImageView);
            }
        }
        else {
            Helpers.replaceNavigationFragment(getParentFragmentManager(), new HomeFragment());
        }


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
                            binding.noPostsProfileLinearLayout.setVisibility(View.GONE);
                        }
                        int cols = response.body().getData().size() < 3 ? 1 : (response.body().getData().size() < 9 ? 2 : 3);
                        ProfileRecAdapter adapter = new ProfileRecAdapter(response.body().getData(), getLayoutInflater(), getActivity(), cols);

                        // SET RECYCLER VIEW
                        RecyclerView recyclerView = binding.imagesRecyclerView;

                        StaggeredGridLayoutManager staggeredGridLayoutManager
                                = new StaggeredGridLayoutManager(cols, LinearLayout.VERTICAL);
                        recyclerView.setLayoutManager(staggeredGridLayoutManager);
                        recyclerView.setAdapter(adapter);

                        // SET LIKES SUM
                        int likes = response.body().getData().stream().flatMapToInt(post -> IntStream.of(post.getLikes())).sum();
                        binding.profileLikesSumTextView.setText(Helpers.convertLikesToText(likes));
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

    private void authAndGoToSettingsFragment() {
        RetrofitService retrofitService = RetrofitService.getInstance();
        PostAPI postAPI = retrofitService.getPostAPI();

        Call<ResponseWrapper<String>> call = postAPI.postAuthorization(retrofitService.getAuthToken());

        call.enqueue(new Callback<ResponseWrapper<String>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<String>> call, Response<ResponseWrapper<String>> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                }
                else {
                    if (response.body().getSuccess()) {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.mainFrameLayout, new SettingsFragment())
                                .commit();
                    }
                    else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<String>> call, Throwable t) {
                Log.d("xxx", t.getMessage());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}