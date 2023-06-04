package com.projects.instaclient.view.fragments.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.projects.instaclient.R;
import com.projects.instaclient.databinding.FragmentSettingsBinding;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.service.RetrofitService;
import com.projects.instaclient.view.MainActivity;
import com.projects.instaclient.view.fragments.NavigationFragment;
import com.projects.instaclient.viewmodel.ProfileViewModel;

import java.io.InputStream;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private ProfileViewModel profileViewModel;
    private String stringUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(getLayoutInflater());

        // SETUP VIEW MODEL
        profileViewModel = MainActivity.profileViewModel;
        binding.setProfileViewModel(profileViewModel);

        // PROFILE IMAGE
        if (profileViewModel.getObservedProfile().getValue().getProfileImage() != null) {
            Glide.with(binding.profileImageSettingsImageView.getContext())
                    .load("http://" + RetrofitService.getServerHost() + "/api/getfile/" + profileViewModel.getObservedProfile().getValue().getProfileImage().getId())
                    .error(R.drawable.profile)
                    .into(binding.profileImageSettingsImageView);
        }

        // ON CLICKS
        binding.profileImageSettingsImageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 100);
        });

        binding.goBackFromSettingsButton.setOnClickListener(v -> {
            Helpers.replaceMainFragment(getParentFragmentManager(), new NavigationFragment(new ProfileFragment()));
        });

        binding.saveSettingsButton.setOnClickListener(v -> {
            if (!stringUri.isEmpty()) {
                profileViewModel.updateProfileImage(getContext(), getParentFragmentManager(), stringUri);
            }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Uri imageUri;
            InputStream inputStream = null;
            Bitmap bitmap;

            try {
                imageUri = data.getData();
                inputStream = getContext().getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Helpers.replaceMainFragment(getParentFragmentManager(), new SettingsFragment());
                return;
            }
            binding.profileImageSettingsImageView.setImageBitmap(bitmap);
            this.stringUri = imageUri.getPath().substring(5);
        } else {
            Helpers.replaceMainFragment(getParentFragmentManager(), new SettingsFragment());
        }
    }
}