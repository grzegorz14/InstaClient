package com.projects.instaclient.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.instaclient.R;
import com.projects.instaclient.databinding.FragmentNavigationBinding;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.view.fragments.addpost.AddPostCameraFragment;
import com.projects.instaclient.viewmodel.PostViewModel;
import com.projects.instaclient.viewmodel.ProfileViewModel;

public class NavigationFragment extends Fragment {

    private FragmentNavigationBinding binding;
    private Fragment startingFragment = new HomeFragment();

    public NavigationFragment() {
    }

    public NavigationFragment(Fragment fragment) {
        this.startingFragment = fragment;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNavigationBinding.inflate(getLayoutInflater());

        // ON CLICK WITH ANIMATIONS
        binding.homeNavigationLottie.setSpeed(2);
        binding.homeNavigationLottie.setOnClickListener(v -> {
            binding.homeNavigationLottie.playAnimation();
            Helpers.replaceNavigationFragment(getParentFragmentManager(), new HomeFragment());
        });

        binding.addPostNavigationLottie.setProgress(0.5f);
        binding.addPostNavigationLottie.setOnClickListener(v -> {
            binding.addPostNavigationLottie.setMinAndMaxProgress(0f, 0.5f);
            binding.addPostNavigationLottie.playAnimation();
            Helpers.replaceNavigationFragment(getParentFragmentManager(), new AddPostCameraFragment());
        });

        binding.profileNavigationLottie.setSpeed(1.5f);
        binding.profileNavigationLottie.setOnClickListener(v -> {
            binding.profileNavigationLottie.playAnimation();
            Helpers.replaceNavigationFragment(getParentFragmentManager(), new ProfileFragment());
        });

        Helpers.replaceNavigationFragment(getParentFragmentManager(), this.startingFragment);

//        binding.bottomNavigation.setSelectedItemId(R.id.profileMenuItem);
//        binding.bottomNavigation.setOnItemSelectedListener(v -> {
//            switch (v.getItemId()) {
//                case R.id.homeMenuItem:
//                    replaceFragment(new HomeFragment());
//                    break;
//                case R.id.addPostMenuItem:
//                    replaceFragment(new AddPostCameraFragment());
//                    break;
//                case R.id.profileMenuItem:
//                    replaceFragment(new ProfileFragment());
//                    break;
//            }
//            return true;
//        });
//                <com.google.android.material.bottomnavigation.BottomNavigationView
//        android:background="@color/white"
//        app:itemRippleColor="@color/white"
//        app:itemTextColor="@color/black"
//        app:itemTextAppearanceActive="@color/black"
//        app:itemTextAppearanceInactive="@color/white"
//        app:itemIconTint="@color/black"
//        android:id="@+id/bottomNavigation"
//        android:layout_width="match_parent"
//        android:layout_height="0dp"
//        app:menu="@menu/navigation_menu"/>


        return binding.getRoot();
    }
}