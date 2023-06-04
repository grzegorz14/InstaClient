package com.projects.instaclient.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.projects.instaclient.adapters.HomeRecAdapter;
import com.projects.instaclient.databinding.FragmentHomeBinding;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.model.Post;
import com.projects.instaclient.model.response.ResponseWrapper;
import com.projects.instaclient.service.RetrofitService;

import java.util.ArrayList;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        // GET POSTS FROM SERVER
        Call<ResponseWrapper<ArrayList<Post>>> call = RetrofitService.getInstance().getPostAPI().getAllPosts();
        call.enqueue(new Callback<ResponseWrapper<ArrayList<Post>>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<ArrayList<Post>>> call, Response<ResponseWrapper<ArrayList<Post>>> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                } else {
                    if (response.body().getSuccess()) {
                        ArrayList<Post> posts = response.body().getData();
                        HomeRecAdapter adapter = new HomeRecAdapter(posts, getLayoutInflater(), getParentFragmentManager());

                        // SET RECYCLER VIEW
                        RecyclerView recyclerView = binding.postsRecyclerView;

                        StaggeredGridLayoutManager staggeredGridLayoutManager
                                = new StaggeredGridLayoutManager(1, LinearLayout.VERTICAL);
                        recyclerView.setLayoutManager(staggeredGridLayoutManager);
                        recyclerView.setAdapter(adapter);
                    }
                    else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<ArrayList<Post>>> call, Throwable t) {
                Log.d("xxx", t.getMessage());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return binding.getRoot();
    }
}