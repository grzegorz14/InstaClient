package com.projects.instaclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.projects.instaclient.api.PostAPI;
import com.projects.instaclient.model.User;

import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static RetrofitService retrofitServiceSingleton = null;

    private final HttpLoggingInterceptor interceptor;
    private final OkHttpClient client;
    private final Gson gson;
    private Retrofit retrofit;
    private static String serverIp = "192.168.119.114";

    private static final String IPV4_PATTERN = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";

    private String authToken = null;

    private RetrofitService() {
        interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        gson = new GsonBuilder()
                .setLenient()
                .create();

        buildRetrofit();
    }

    public static RetrofitService getInstance() {
        if (retrofitServiceSingleton == null) {
            retrofitServiceSingleton = new RetrofitService();
        }

        return retrofitServiceSingleton;
    }

    private void buildRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + getServerHost())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    public PostAPI getPostAPI() {
        return retrofit.create(PostAPI.class);
    }

    public static String getServerIp() {
        return serverIp;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setServerIp(String ip) {
        if (!validateIp(serverIp)) return;

        serverIp = ip;
        buildRetrofit();
    }

    private boolean validateIp(String ip) {
        return Pattern.compile(IPV4_PATTERN)
                .matcher(ip)
                .matches();
    }

    public static String getServerHost() {
        return serverIp + ":3000";
    }
}
