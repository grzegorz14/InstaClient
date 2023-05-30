package com.projects.instaclient.view.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.projects.instaclient.R;
import com.projects.instaclient.api.PostAPI;
import com.projects.instaclient.databinding.FragmentLoginBinding;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.model.User;
import com.projects.instaclient.model.response.ResponseWrapper;
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

        // set server ip from application level - hidden function
        binding.instagramSetServerIpTextView.setOnClickListener(v -> {
            setServerIp();
        });

        return binding.getRoot();
    }

    private void loginUser() {
        User userData = new User(
                binding.emailLoginTextInput.getEditText().getText().toString(),
                binding.passwordLoginTextInput.getEditText().getText().toString());

        RetrofitService retrofitService = RetrofitService.getInstance();
        PostAPI postAPI = retrofitService.getPostAPI();
        Call<ResponseWrapper<String>> call = postAPI.postLoginData(userData);

        call.enqueue(new Callback<ResponseWrapper<String>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<String>> call, Response<ResponseWrapper<String>> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                }
                else {
                    if (response.body().getSuccess()) {
                        String authToken = response.body().getData();
                        retrofitService.setAuthToken(authToken);
                        Helpers.replaceMainFragment(getParentFragmentManager(), new NavigationFragment());
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

    private void setServerIp() {
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.edit_text_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setView(promptsView);

        TextInputLayout userInput = promptsView.findViewById(R.id.alertTextInput);
        userInput.getEditText().setText(RetrofitService.getServerIp());

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String url = userInput.getEditText().getText().toString();
                        RetrofitService.getInstance().setServerIp(url);
                        Toast.makeText(getContext(), "Url set: "+ url, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}