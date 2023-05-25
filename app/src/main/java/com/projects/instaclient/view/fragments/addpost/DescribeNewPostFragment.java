package com.projects.instaclient.view.fragments.addpost;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;
import com.projects.instaclient.R;
import com.projects.instaclient.api.PostAPI;
import com.projects.instaclient.databinding.FragmentDescribeNewPostBinding;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.model.Post;
import com.projects.instaclient.model.request.AddPostDataRequest;
import com.projects.instaclient.model.response.AddPostResponse;
import com.projects.instaclient.service.RetrofitService;
import com.projects.instaclient.view.fragments.HomeFragment;
import com.projects.instaclient.view.fragments.NavigationFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DescribeNewPostFragment extends Fragment {

    private FragmentDescribeNewPostBinding binding;

    private Bitmap image;
    private String imageUri;

    private String description = null;
    private String location = null;
    private ArrayList<String> tags = new ArrayList<>();

    public DescribeNewPostFragment(Bitmap image, String imageUri) {
        this.image = image;
        this.imageUri = imageUri;
    }

    public DescribeNewPostFragment(Bitmap image, String imageUri, String description, String location, ArrayList<String> tags) {
        this.image = image;
        this.imageUri = imageUri;
        this.description = description;
        this.location = location;
        this.tags = tags;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDescribeNewPostBinding.inflate(getLayoutInflater());

        // RETRIEVE DATA
        if (description != null) {
            binding.descriptionNewPostEditText.setText(description);
        }
        if (location != null) {
            binding.locationTextView.setText(location);
        }
        else {
            binding.locationTextView.setVisibility(View.INVISIBLE);
        }
        if (tags != null) {
            tags.forEach(this::addTagChip);
        }

        // ON CLICKS
        binding.chooseLocationButton.setOnClickListener(v -> {
            description = binding.descriptionNewPostEditText.getText().toString();
            Helpers.replaceMainFragment(getParentFragmentManager(), new ChooseLocationFragment(image, imageUri, description, location, tags));
        });

        binding.chooseTagButton.setOnClickListener(v -> {
            chooseTagFromList();
        });

        binding.createTagButton.setOnClickListener(v -> {
            createTag();
        });

        binding.cancelPostButton.setOnClickListener(v -> {
            description = binding.descriptionNewPostEditText.getText().toString();
            Helpers.replaceMainFragment(getParentFragmentManager(), new NavigationFragment(new AddPostCameraFragment()));
        });

        binding.createPostButton.setOnClickListener(v -> {
            createPost();
        });

        return binding.getRoot();
    }

    private void chooseTagFromList() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
        builderSingle.setTitle("Select tag");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item);
        arrayAdapter.add("love");
        arrayAdapter.add("instagood");
        arrayAdapter.add("fashion");
        arrayAdapter.add("photography");
        arrayAdapter.add("art");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedTag = arrayAdapter.getItem(which);
                addTag(selectedTag);
            }
        });
        builderSingle.show();
    }

    private void createTag() {
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.add_tag_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setTitle("Add tag");

        TextInputLayout userInput = promptsView.findViewById(R.id.alertTextInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String name = userInput.getEditText().getText().toString();
                        if (name.length() > 0) {
                            addTag(name);
                        }
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void addTag(String tag) {
        String name = tag.toLowerCase().replaceAll(" ", "");

        if (tags.contains(name)) return;

        tags.add(name);
        addTagChip(name);
    }

    private void addTagChip(String name) {
        Chip chip = new Chip(getContext());
        chip.setText(name);
        chip.setOnClickListener(v -> {
            tags.remove(name);
            binding.tagsChipGroup.removeView(chip);
        });
        binding.tagsChipGroup.addView(chip);
    }

    private void createPost() {
        File file = new File(imageUri);

        Log.d("xxx", String.valueOf(imageUri));
        RequestBody fileRequest = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), fileRequest);

        AddPostDataRequest addPostDataRequest = new AddPostDataRequest(
                binding.descriptionNewPostEditText.getText().toString(),
                location,
                tags);

        RetrofitService retrofitService = RetrofitService.getInstance();
        PostAPI postAPI = retrofitService.getPostAPI();
        Call<AddPostResponse> call = postAPI.postNewPost(retrofitService.getAuthToken(),
                addPostDataRequest,
                filePart);

        call.enqueue(new Callback<AddPostResponse>() {
            @Override
            public void onResponse(Call<AddPostResponse> call, Response<AddPostResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "New post created!", Toast.LENGTH_LONG).show();
                    Helpers.replaceMainFragment(getParentFragmentManager(), new NavigationFragment(new HomeFragment()));
                }
            }

            @Override
            public void onFailure(Call<AddPostResponse> call, Throwable t) {
                Log.d("xxx", t.getMessage());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}