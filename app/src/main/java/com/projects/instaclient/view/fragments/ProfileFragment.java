package com.projects.instaclient.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.projects.instaclient.R;
import com.projects.instaclient.adapters.ProfileRecAdapter;
import com.projects.instaclient.api.PostAPI;
import com.projects.instaclient.databinding.FragmentProfileBinding;
import com.projects.instaclient.model.User;
import com.projects.instaclient.model.response.AuthResponse;
import com.projects.instaclient.service.RetrofitService;
import com.projects.instaclient.viewmodel.ProfileViewModel;

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

        // SETUP VIEW MODEL
        profileViewModel = new ViewModelProvider(requireActivity())
                .get(ProfileViewModel.class);
        binding.setProfileViewModel(profileViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        RetrofitService retrofitService = RetrofitService.getInstance();
        PostAPI postAPI = retrofitService.getPostAPI();
        Call<User> call = postAPI.getProfile(retrofitService.getAuthToken());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                }
                else {
                    User responseUser = response.body();
                    profileViewModel.setupProfile(responseUser);

                    int postsNumber = responseUser.getPosts().size();
                    if (postsNumber == 0) {
                        return;
                    }

                    // SET USER POSTS
                    int cols = postsNumber < 3 ? 1 : (postsNumber < 9 ? 2 : 3);
                    ProfileRecAdapter adapter = new ProfileRecAdapter(responseUser.getPosts(), getLayoutInflater(), getActivity(), cols);

                    // SET RECYCLER VIEW
                    RecyclerView recyclerView = binding.imagesRecyclerView;

                    StaggeredGridLayoutManager staggeredGridLayoutManager
                            = new StaggeredGridLayoutManager(cols, LinearLayout.VERTICAL);
                    recyclerView.setLayoutManager(staggeredGridLayoutManager);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("xxx", t.getMessage());
            }
        });

        return binding.getRoot();
    }

    private void authAndGoToSettingsFragment() {
        RetrofitService retrofitService = RetrofitService.getInstance();
        PostAPI postAPI = retrofitService.getPostAPI();

        Call<AuthResponse> call = postAPI.postAuthorization(retrofitService.getAuthToken());

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getContext(), "Authorization successful", Toast.LENGTH_SHORT).show();

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainFrameLayout, new SettingsFragment(profileViewModel))
                            .commit();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.d("xxx", t.getMessage());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}