package com.projects.instaclient.api;

import com.projects.instaclient.model.Post;
import com.projects.instaclient.model.User;
import com.projects.instaclient.model.request.AddPostDataRequest;
import com.projects.instaclient.model.request.CreateTagRequest;
import com.projects.instaclient.model.response.AddPostResponse;
import com.projects.instaclient.model.response.CreateTagResponse;
import com.projects.instaclient.model.response.RegisterResponse;
import com.projects.instaclient.model.response.ResponseWrapper;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface PostAPI {

    @POST("api/user/register")
    Call<ResponseWrapper<RegisterResponse>> postRegisterData(@Body User user);

    @POST("api/user/login")
    Call<ResponseWrapper<String>> postLoginData(@Body User user);

    @GET("api/user/logout")
    Call<ResponseWrapper<String>> getLogout(@Header("Authorization") String token);

    @GET("api/user/profile")
    Call<ResponseWrapper<User>> getProfile(@Header("Authorization") String token);

    @PATCH("api/user/profile")
    Call<ResponseWrapper<User>> patchProfileData(@Header("Authorization") String token,
                                                 @Body User user);

    @POST("api/user/auth")
    Call<ResponseWrapper<String>> postAuthorization(@Header("Authorization") String token);

    @GET("api/user/confirm/{token}")
    Call<ResponseWrapper<User>> getConfirmAccount(@Path("token") String token);

    @Multipart
    @POST("api/posts")
    Call<AddPostResponse> postNewPost(@Header("Authorization") String token,
                                      @Part("data") AddPostDataRequest data,
                                      @Part MultipartBody.Part file);

    @GET("api/posts")
    Call<ArrayList<Post>> getAllPosts();

    @GET("api/users")
    Call<ResponseWrapper<ArrayList<User>>> getAllUsers();

    @GET("api/getuser/{id}")
    Call<ResponseWrapper<User>> getUserById(@Path("id") String token);

    @GET("api/tags/raw")
    Call<ArrayList<String>> getAllTagsRaw();

    @POST("api/tags")
    Call<CreateTagResponse> postNewTag(@Body CreateTagRequest tagRequest);

}
