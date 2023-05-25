package com.projects.instaclient.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.projects.instaclient.R;
import com.projects.instaclient.adapters.HomeRecAdapter;
import com.projects.instaclient.adapters.ProfileRecAdapter;
import com.projects.instaclient.databinding.FragmentHomeBinding;
import com.projects.instaclient.model.Post;
import com.projects.instaclient.model.User;
import com.projects.instaclient.viewmodel.PostViewModel;
import com.projects.instaclient.viewmodel.ProfileViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private PostViewModel postViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        // SET RECYCLER VIEW
        RecyclerView recyclerView = binding.postsRecyclerView;

        StaggeredGridLayoutManager staggeredGridLayoutManager
                = new StaggeredGridLayoutManager(1, LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        // SETUP VIEW MODEL
        postViewModel = new ViewModelProvider(requireActivity())
                .get(PostViewModel.class);
        postViewModel.setupPosts();
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //HomeRecAdapter adapter = new HomeRecAdapter(postViewModel.getObservedPosts().getValue(), binding.homeFragment.getContext());
        HomeRecAdapter adapter = new HomeRecAdapter(getInitialPosts(), binding.homeFragment.getContext());
        recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    private List<Post> getInitialPosts() {
        User userZendaya = new User("Zendaya", "", "acarano@gmail.com", "Qwerty123!", null, null);
        User userTomasz = new User("Tomasz", "Marczyński", "marczyńskiKolarz@gmail.com", "Qwerty123!", null, null);
        User userSergio = new User("Sergio", "Calvo Miniatures", "sgminiatures@gmail.com", "Qwerty123!", null, null);
        User userHbo = new User("HBO", "Poland", "hbo@gmail.com", "Qwerty123!", null, null);

        return new ArrayList<>(Arrays.asList(
                new Post(userZendaya, R.drawable.z1, "session session session", 35022),
                new Post(userZendaya, R.drawable.z2, "good vibes", 24211),
                new Post(userZendaya, R.drawable.z3, "OMG! Dune part 1 is finally out! Can't wait to see the final effect in the cinema :)))", 11943),

                new Post(userTomasz, R.drawable.initial_bike1, "This S-WORKS is extremely stiff! And it looks gorgeous...", 147),
                new Post(userTomasz, R.drawable.initial_bike2, "Pinarello Dogma F in the most beautiful color option", 64),
                new Post(userTomasz, R.drawable.initial_bike3, "TIME MACHINE 01:00:00", 23),

                new Post(userHbo, R.drawable.initial_dune, "Can't wait? It's worth waiting for...", 7544),
                new Post(userHbo, R.drawable.initial_euphoria, "With extraordinary music of Labrinth, check it out on Spotify", 3523),
                new Post(userHbo, R.drawable.initial_hotd, "TOMORROW 17:00", 5643),

                new Post(userSergio, R.drawable.initial_miniature1, "For sell only for 1000$ :)", 632),
                new Post(userSergio, R.drawable.initial_miniature2, "Any tips what to do better?",153),
                new Post(userSergio, R.drawable.initial_miniature3, "5 hours of hard work", 836)));
    }
}