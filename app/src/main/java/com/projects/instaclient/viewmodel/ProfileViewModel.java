package com.projects.instaclient.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.projects.instaclient.api.PostAPI;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.model.User;
import com.projects.instaclient.model.response.ResponseWrapper;
import com.projects.instaclient.service.RetrofitService;
import com.projects.instaclient.view.MainActivity;
import com.projects.instaclient.view.fragments.login.LoginFragment;
import com.projects.instaclient.view.fragments.NavigationFragment;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    public void setProfileFromCall() {
        RetrofitService retrofitService = RetrofitService.getInstance();
        PostAPI postAPI = retrofitService.getPostAPI();

        Call<ResponseWrapper<User>> call = postAPI.getProfile(retrofitService.getAuthToken());

        call.enqueue(new Callback<ResponseWrapper<User>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<User>> call, Response<ResponseWrapper<User>> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                }
                else {
                    if (response.body().getSuccess()) {
                        User responseUser = response.body().getData();
                        setupProfile(responseUser);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<User>> call, Throwable t) {
                Log.d("xxx", t.getMessage());
            }
        });
    }

    public void updateProfileImage(Context context, FragmentManager fragmentManager, String imageUri) {
        File file = new File(imageUri);

        RequestBody fileRequest = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), fileRequest);

        RetrofitService retrofitService = RetrofitService.getInstance();
        PostAPI postAPI = retrofitService.getPostAPI();
        Call<ResponseWrapper<User>> call = postAPI.postProfileImage(retrofitService.getAuthToken(), filePart);

        call.enqueue(new Callback<ResponseWrapper<User>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<User>> call, Response<ResponseWrapper<User>> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                    Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show();
                } else {
                    if (response.body().getSuccess()) {
                        User responseUser = response.body().getData();
                        setupProfile(responseUser);
                    }
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<User>> call, Throwable t) {
                Log.d("xxx", t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
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

        Call<ResponseWrapper<User>> call = postAPI.patchProfileData(retrofitService.getAuthToken(), newUserData);

        call.enqueue(new Callback<ResponseWrapper<User>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<User>> call, Response<ResponseWrapper<User>> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                }
                else {
                    if (response.body().getSuccess()) {
                        User responseUser = response.body().getData();
                        setupProfile(responseUser);
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<User>> call, Throwable t) {
                Log.d("xxx", t.getMessage());
            }
        });
    }

    public void logIn(Context context, FragmentManager fragmentManager, String email, String password) {
        User userData = new User(email, password);

        RetrofitService retrofitService = RetrofitService.getInstance();
        PostAPI postAPI = retrofitService.getPostAPI();
        Call<ResponseWrapper<String>> call = postAPI.postLoginData(userData);

        call.enqueue(new Callback<ResponseWrapper<String>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<String>> call, Response<ResponseWrapper<String>> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                    Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show();
                }
                else {
                    if (response.body().getSuccess()) {
                        String authToken = response.body().getData();
                        retrofitService.setAuthToken(authToken);
                        MainActivity.profileViewModel.setProfileFromCall();
                        Helpers.replaceMainFragment(fragmentManager, new NavigationFragment());
                    }
                    else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<String>> call, Throwable t) {
                Log.d("xxx", t.getMessage());
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void logOut(Context context, FragmentManager fragmentManager) {
        RetrofitService retrofitService = RetrofitService.getInstance();
        PostAPI postAPI = retrofitService.getPostAPI();

        Call<ResponseWrapper<String>> call = postAPI.getLogout(retrofitService.getAuthToken());

        call.enqueue(new Callback<ResponseWrapper<String>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<String>> call, Response<ResponseWrapper<String>> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                }
                else {
                    if (response.body().getSuccess()) {
                        setupProfile(null);

                        String message = response.body().getData();
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        Helpers.replaceMainFragment(fragmentManager, new LoginFragment());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<String>> call, Throwable t) {
                Log.d("xxx", t.getMessage());
            }
        });
    }
}
