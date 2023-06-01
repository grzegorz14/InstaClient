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
import com.projects.instaclient.view.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(getLayoutInflater());

        // ON CLICKS
        binding.loginButton.setOnClickListener(v -> {
            MainActivity.profileViewModel.logIn(
                    getContext(),
                    getParentFragmentManager(),
                    binding.emailLoginTextInput.getEditText().getText().toString(),
                    binding.passwordLoginTextInput.getEditText().getText().toString());
        });

        binding.goToRegisterPageLink.setOnClickListener(v -> {
            Helpers.replaceMainFragment(getParentFragmentManager(), new RegisterFragment());
        });

        binding.instagramSetServerIpTextView.setOnClickListener(v -> {
            setServerIp(); // set server ip from application level - hidden function
        });

        return binding.getRoot();
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