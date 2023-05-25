package com.projects.instaclient.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.projects.instaclient.R;
import com.projects.instaclient.api.PostAPI;
import com.projects.instaclient.databinding.FragmentLoginBinding;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.model.User;
import com.projects.instaclient.model.response.LoginResponse;
import com.projects.instaclient.service.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(getLayoutInflater());

        binding.loginButton.setOnClickListener(v -> {
            loginUser();
        });

        binding.goToRegisterPageLink.setOnClickListener(v -> {
            Helpers.replaceMainFragment(getParentFragmentManager(), new RegisterFragment());
        });

        // bypass only for testing without server
        binding.instagramBypassTextView.setOnClickListener(v -> {
            Helpers.replaceMainFragment(getParentFragmentManager(), new NavigationFragment());
        });

        return binding.getRoot();
    }

    private void loginUser() {
        User userData = new User(
                binding.emailLoginTextInput.getEditText().getText().toString(),
                binding.passwordLoginTextInput.getEditText().getText().toString());

        RetrofitService retrofitService = RetrofitService.getInstance();
        PostAPI postAPI = retrofitService.getPostAPI();
        Call<LoginResponse> call = postAPI.postLoginData(userData);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                }
                else {
                    String authToken = response.body().getToken();
                    retrofitService.setAuthToken(authToken);

                    Helpers.replaceMainFragment(getParentFragmentManager(), new NavigationFragment());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("xxx", t.getMessage());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}