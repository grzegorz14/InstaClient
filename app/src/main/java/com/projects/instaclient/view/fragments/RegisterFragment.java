package com.projects.instaclient.view.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.projects.instaclient.api.PostAPI;
import com.projects.instaclient.databinding.FragmentRegisterBinding;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.model.User;
import com.projects.instaclient.model.response.RegisterResponse;
import com.projects.instaclient.model.response.ResponseWrapper;
import com.projects.instaclient.service.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(getLayoutInflater());

        binding.registerButton.setOnClickListener(v -> {
            registerUser();
        });

        binding.goToLoginPageLink.setOnClickListener(v -> {
            Helpers.replaceMainFragment(getParentFragmentManager(), new LoginFragment());
        });

        return binding.getRoot();
    }

    private void registerUser() {
        User newUser = new User(
                binding.firstNameRegisterTextInput.getEditText().getText().toString(),
                binding.lastNameRegisterTextInput.getEditText().getText().toString(),
                binding.emailRegisterTextInput.getEditText().getText().toString(),
                binding.passwordRegisterTextInput.getEditText().getText().toString(),
                null,
                null);

        PostAPI postAPI = RetrofitService.getInstance().getPostAPI();
        Call<ResponseWrapper<RegisterResponse>> call = postAPI.postRegisterData(newUser);

        call.enqueue(new Callback<ResponseWrapper<RegisterResponse>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<RegisterResponse>> call, Response<ResponseWrapper<RegisterResponse>> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                }
                else {
                    if (response.body().getSuccess()) {
                        String confirmToken = response.body().getData().getToken();

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

                        alertDialogBuilder
                                .setCancelable(false)
                                .setTitle("Confirm account")
                                .setMessage("App will send GET with received token to address: " + RetrofitService.getInstance().getServerIp())
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        sendConfirm(confirmToken);
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                    else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<RegisterResponse>> call, Throwable t) {
                Log.d("xxx", t.getMessage());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendConfirm(String token) {
        PostAPI postAPI = RetrofitService.getInstance().getPostAPI();

        Call<ResponseWrapper<User>> call = postAPI.getConfirmAccount(token);
        call.enqueue(new Callback<ResponseWrapper<User>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<User>> call, Response<ResponseWrapper<User>> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    if (response.body().getSuccess()) {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        Helpers.replaceMainFragment(getParentFragmentManager(), new LoginFragment());
                    }
                    else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<User>> call, Throwable t) {
                Log.d("xxx", t.getMessage());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}