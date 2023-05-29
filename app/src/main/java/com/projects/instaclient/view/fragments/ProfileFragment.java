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
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.model.User;
import com.projects.instaclient.model.response.ResponseWrapper;
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
        Call<ResponseWrapper<User>> call = postAPI.getProfile(retrofitService.getAuthToken());
        call.enqueue(new Callback<ResponseWrapper<User>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<User>> call, Response<ResponseWrapper<User>> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                }
                else {
                    if (response.body().getSuccess()) {
                        User responseUser = response.body().getData();
                        profileViewModel.setupProfile(responseUser);

                        int postsNumber = responseUser.getPosts().size();
                        if (postsNumber == 0) {
                            return;
                        }

                        // GET POSTS
//
//                        // SET USER POSTS
//                        int cols = postsNumber < 3 ? 1 : (postsNumber < 9 ? 2 : 3);
//                        ProfileRecAdapter adapter = new ProfileRecAdapter(responseUser.getPosts(), getLayoutInflater(), getActivity(), cols);
//
//                        // SET RECYCLER VIEW
//                        RecyclerView recyclerView = binding.imagesRecyclerView;
//
//                        StaggeredGridLayoutManager staggeredGridLayoutManager
//                                = new StaggeredGridLayoutManager(cols, LinearLayout.VERTICAL);
//                        recyclerView.setLayoutManager(staggeredGridLayoutManager);
//                        recyclerView.setAdapter(adapter);
                    }
                    else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<User>> call, Throwable t) {
                Log.d("xxx", t.getMessage());
            }
        });

        return binding.getRoot();
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
                                .replace(R.id.mainFrameLayout, new SettingsFragment(profileViewModel))
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