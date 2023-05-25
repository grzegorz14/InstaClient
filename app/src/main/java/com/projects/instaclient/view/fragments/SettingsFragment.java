package com.projects.instaclient.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.instaclient.R;
import com.projects.instaclient.databinding.FragmentSettingsBinding;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.viewmodel.ProfileViewModel;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private ProfileViewModel profileViewModel;

    public SettingsFragment(ProfileViewModel profileViewModel) {
        this.profileViewModel = profileViewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(getLayoutInflater());

        // SETUP VIEW MODEL
        binding.setProfileViewModel(profileViewModel);

        // ON CLICKS
        binding.goBackFromSettingsButton.setOnClickListener(v -> {
            Helpers.replaceMainFragment(getParentFragmentManager(), new NavigationFragment(new ProfileFragment()));
        });

        binding.saveSettingsButton.setOnClickListener(v -> {
            profileViewModel.updateProfile(
                    getContext(),
                    getParentFragmentManager(),
                    binding.changeFirstNameTextInput.getEditText().getText().toString(),
                    binding.changeLastNameTextInput.getEditText().getText().toString(),
                    binding.changeEmailTextInput.getEditText().getText().toString());
        });

        binding.logOutButton.setOnClickListener(v -> {
            profileViewModel.logOut(getContext(), getParentFragmentManager());
        });

        return binding.getRoot();
    }
}