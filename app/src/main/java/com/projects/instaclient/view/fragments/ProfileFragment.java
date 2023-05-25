package com.projects.instaclient.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.projects.instaclient.R;
import com.projects.instaclient.adapters.ProfileRecAdapter;
import com.projects.instaclient.api.PostAPI;
import com.projects.instaclient.databinding.FragmentProfileBinding;
import com.projects.instaclient.model.Post;
import com.projects.instaclient.model.User;
import com.projects.instaclient.model.response.AuthResponse;
import com.projects.instaclient.service.RetrofitService;
import com.projects.instaclient.viewmodel.ProfileViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        // SET RECYCLER VIEW
        RecyclerView recyclerView = binding.imagesRecyclerView;

        StaggeredGridLayoutManager staggeredGridLayoutManager
                = new StaggeredGridLayoutManager(3, LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        ProfileRecAdapter adapter = new ProfileRecAdapter(getInitialPosts(), binding.profileFragment.getContext(), getActivity());
        recyclerView.setAdapter(adapter);

        // ON CLICKS
        binding.settingsImageView.setOnClickListener(v -> {
            authAndGoToSettingsFragment();
        });

        // SETUP VIEW MODEL
        profileViewModel = new ViewModelProvider(requireActivity())
                .get(ProfileViewModel.class);
        binding.setProfileViewModel(profileViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

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

    private List<Post> getInitialPosts() {
        User basicUser = new User("Amelia", "Cardano", "acarano@gmail.com", "Qwerty123!", null, null);

        return new ArrayList<>(Arrays.asList(
                new Post(basicUser, R.drawable.z1, "Happy holiday!"),
                new Post(basicUser, R.drawable.z2, "good vibes"),
                new Post(basicUser, R.drawable.z3, "There is no beauty without some strangeness..."),
                new Post(basicUser, R.drawable.z4, "Euphoria"),
                new Post(basicUser, R.drawable.z5, ":)"),
                new Post(basicUser, R.drawable.z6, "Any tips and hits?"),
                new Post(basicUser, R.drawable.z7, "France trip with family and friends")));
    }
}