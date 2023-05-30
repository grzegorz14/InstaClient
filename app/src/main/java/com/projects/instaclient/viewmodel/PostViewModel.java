package com.projects.instaclient.viewmodel;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.projects.instaclient.R;
import com.projects.instaclient.adapters.HomeRecAdapter;
import com.projects.instaclient.model.Post;
import com.projects.instaclient.model.User;
import com.projects.instaclient.model.response.ResponseWrapper;
import com.projects.instaclient.service.RetrofitService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostViewModel extends ViewModel {

    private MutableLiveData<List<Post>> postsMutableLiveData;
    private List<Post> posts;

    public PostViewModel() {
        this.postsMutableLiveData = new MutableLiveData<>();
        getPosts();
    }

    public MutableLiveData<List<Post>> getObservedPosts() {
        return postsMutableLiveData;
    }

    public void setupPosts() {
        this.postsMutableLiveData.setValue(this.posts);
    }

    public void addPost(Post post) {
        this.posts.add(post);
        this.postsMutableLiveData.setValue(this.posts);
    }

    private void getPosts() {
        Call<ResponseWrapper<ArrayList<Post>>> call = RetrofitService.getInstance().getPostAPI().getAllPosts();

        call.enqueue(new Callback<ResponseWrapper<ArrayList<Post>>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<ArrayList<Post>>> call, Response<ResponseWrapper<ArrayList<Post>>> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                } else {
                    if (response.body().getSuccess()) {
                        posts = response.body().getData();
                        setupPosts();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<ArrayList<Post>>> call, Throwable t) {
                Log.d("xxx", t.getMessage());
            }
        });
    }

    // currently on server
//    private List<Post> getInitialPosts() {
//        User userZendaya = new User("Zendaya", "", "acarano@gmail.com", "Qwerty123!", null, null);
//        User userTomasz = new User("Tomasz", "Marczyński", "marczyńskiKolarz@gmail.com", "Qwerty123!", null, null);
//        User userSergio = new User("Sergio", "Calvo Miniatures", "sgminiatures@gmail.com", "Qwerty123!", null, null);
//        User userHbo = new User("HBO", "Poland", "hbo@gmail.com", "Qwerty123!", null, null);
//
//        return new ArrayList<>(Arrays.asList(
//                new Post(userZendaya, R.drawable.z1, "session session session", 35022),
//                new Post(userZendaya, R.drawable.z2, "good vibes", 24211),
//                new Post(userZendaya, R.drawable.z3, "OMG! Dune part 1 is finally out! Can't wait to see the final effect in the cinema :)))", 11943),
//
//                new Post(userTomasz, R.drawable.initial_bike1, "This S-WORKS is extremely stiff! And it looks gorgeous...", 147),
//                new Post(userTomasz, R.drawable.initial_bike2, "Pinarello Dogma F in the most beautiful color option", 64),
//                new Post(userTomasz, R.drawable.initial_bike3, "TIME MACHINE 01:00:00", 23),
//
//                new Post(userHbo, R.drawable.initial_dune, "Can't wait? It's worth waiting for...", 7544),
//                new Post(userHbo, R.drawable.initial_euphoria, "With extraordinary music of Labrinth, check it out on Spotify", 3523),
//                new Post(userHbo, R.drawable.initial_hotd, "TOMORROW 17:00", 5643),
//
//                new Post(userSergio, R.drawable.initial_miniature1, "For sell only for 1000$ :)", 632),
//                new Post(userSergio, R.drawable.initial_miniature2, "Any tips what to do better?",153),
//                new Post(userSergio, R.drawable.initial_miniature3, "5 hours of hard work", 836)));
//    }
}
