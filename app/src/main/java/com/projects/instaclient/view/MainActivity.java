package com.projects.instaclient.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

import com.projects.instaclient.R;
import com.projects.instaclient.databinding.ActivityMainBinding;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.view.fragments.LoginFragment;
import com.projects.instaclient.view.fragments.ProfileFragment;
import com.projects.instaclient.viewmodel.ProfileViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SET VIEW
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // SET STARTING FRAGMENT
        Helpers.replaceMainFragment(getSupportFragmentManager(), new LoginFragment());
    }
}