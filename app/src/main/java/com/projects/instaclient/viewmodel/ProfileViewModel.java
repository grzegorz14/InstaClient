package com.projects.instaclient.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.projects.instaclient.api.PostAPI;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.model.Post;
import com.projects.instaclient.model.User;
import com.projects.instaclient.model.response.LoginResponse;
import com.projects.instaclient.model.response.LogoutResponse;
import com.projects.instaclient.model.response.UpdateProfileResponse;
import com.projects.instaclient.service.RetrofitService;
import com.projects.instaclient.view.fragments.LoginFragment;
import com.projects.instaclient.view.fragments.NavigationFragment;
import com.projects.instaclient.view.fragments.ProfileFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {

    public MutableLiveData<User> profileMutableLiveData;
    public User profile;

    public ProfileViewModel() {
        this.profileMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<User> getObservedProfile() {
        return profileMutableLiveData;
    }

    public void setupProfile() {
        this.profileMutableLiveData.setValue(this.profile);
    }
    public void setupProfile(User user) {
        this.profile = user;
        setupProfile();
    }

    private void getProfile() {
        RetrofitService retrofitService = RetrofitService.getInstance();
        PostAPI postAPI = retrofitService.getPostAPI();

        Call<User> call = postAPI.getProfile(retrofitService.getAuthToken());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                }
                else {
                    User responseUser = response.body();
                    setupProfile(responseUser);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("xxx", t.getMessage());
            }
        });
    }

    public void updateProfile(Context context, FragmentManager fragmentManager, String newFirstName, String newLastName, String newEmail) {
        User newUserData = new User(
                newFirstName,
                newLastName,
                newEmail);

        RetrofitService retrofitService = RetrofitService.getInstance();
        PostAPI postAPI = retrofitService.getPostAPI();

        Call<User> call = postAPI.patchProfileData(retrofitService.getAuthToken(), newUserData);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                }
                else {
                    User responseUser = response.body();
                    setupProfile(responseUser);
                    Toast.makeText(context, "Profile is updated", Toast.LENGTH_LONG).show();
                    Helpers.replaceMainFragment(fragmentManager, new NavigationFragment(new ProfileFragment()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("xxx", t.getMessage());
            }
        });
    }

    public void logIn(Context context, FragmentManager fragmentManager, String email, String password) {
        User userData = new User(email, password);

        RetrofitService retrofitService = RetrofitService.getInstance();
        PostAPI postAPI = retrofitService.getPostAPI();
        Call<LoginResponse> call = postAPI.postLoginData(userData);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                    Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show();
                }
                else {
                    String authToken = response.body().getToken();
                    retrofitService.setAuthToken(authToken);

                    Helpers.replaceMainFragment(fragmentManager, new NavigationFragment());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("xxx", t.getMessage());
            }
        });
    }

    public void logOut(Context context, FragmentManager fragmentManager) {
        RetrofitService retrofitService = RetrofitService.getInstance();
        PostAPI postAPI = retrofitService.getPostAPI();

        Call<LogoutResponse> call = postAPI.getLogout(retrofitService.getAuthToken());

        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                }
                else {
                    setupProfile(null);

                    String message = response.body().getMessage();
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    Helpers.replaceMainFragment(fragmentManager, new LoginFragment());
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                Log.d("xxx", t.getMessage());
            }
        });
    }
}
