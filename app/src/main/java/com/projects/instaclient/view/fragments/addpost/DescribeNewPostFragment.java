package com.projects.instaclient.view.fragments.addpost;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;

import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;
import com.projects.instaclient.R;
import com.projects.instaclient.api.PostAPI;
import com.projects.instaclient.databinding.FragmentDescribeNewPostBinding;
import com.projects.instaclient.helpers.Helpers;
import com.projects.instaclient.model.Post;
import com.projects.instaclient.model.request.AddPostDataRequest;
import com.projects.instaclient.model.request.CreateTagRequest;
import com.projects.instaclient.model.response.ResponseWrapper;
import com.projects.instaclient.service.RetrofitService;
import com.projects.instaclient.view.fragments.HomeFragment;
import com.projects.instaclient.view.fragments.NavigationFragment;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DescribeNewPostFragment extends Fragment {

    private FragmentDescribeNewPostBinding binding;
    private String imageUri;

    private String description = null;
    private String location = null;
    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<String> allTags = new ArrayList<>();

    public DescribeNewPostFragment(String imageUri) {
        this.imageUri = imageUri;
    }

    public DescribeNewPostFragment(String imageUri, String description, String location, ArrayList<String> tags) {
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
        getAllTags();
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
            Helpers.replaceMainFragment(getParentFragmentManager(), new ChooseLocationFragment(imageUri, description, location, tags));
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
            binding.createPostButton.setEnabled(false);
            createPost();
        });

        return binding.getRoot();
    }

    private void chooseTagFromList() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
        builderSingle.setTitle("Select tag");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item);
        allTags.forEach(arrayAdapter::add);

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

    private void postNewTag(String name) {
        Call<ResponseWrapper<String>> call = RetrofitService.getInstance().getPostAPI().postNewTag(new CreateTagRequest(name));
        call.enqueue(new Callback<ResponseWrapper<String>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<String>> call, Response<ResponseWrapper<String>> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    if (response.body().getSuccess()) {
                        getAllTags();
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

    private void getAllTags() {
        Call<ResponseWrapper<ArrayList<String>>> call = RetrofitService.getInstance().getPostAPI().getAllTagsRaw();
        call.enqueue(new Callback<ResponseWrapper<ArrayList<String>>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<ArrayList<String>>> call, Response<ResponseWrapper<ArrayList<String>>> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                } else {
                    if (response.body().getSuccess()) {
                        allTags = response.body().getData();
                    }
                    else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<ArrayList<String>>> call, Throwable t) {
                Log.d("xxx", t.getMessage());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addTag(String tag) {
        String name = tag.toLowerCase().replaceAll(" ", "");

        if (tags.contains(name)) return;

        if (!allTags.contains(name)) {
            postNewTag(name);
        }

        tags.add(name);
        addTagChip(name);
    }

    private void addTagChip(String name) {
        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.tag_chip, null, false);
        chip.setText(name);
        chip.setOnClickListener(v -> {
            tags.remove(name);
            binding.tagsChipGroup.removeView(chip);
        });
        binding.tagsChipGroup.addView(chip);
    }

    private void createPost() {
        Helpers.replaceMainFragment(getParentFragmentManager(), new NavigationFragment(new HomeFragment()));

        File file = new File(imageUri);

        RequestBody fileRequest;
        MultipartBody.Part filePart;
        if (imageUri.contains(".mp4")) {
            fileRequest = RequestBody.create(MediaType.parse("video/*"), file);
            filePart = MultipartBody.Part.createFormData("file", file.getName(), fileRequest);
        }
        else {
            fileRequest = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            filePart = MultipartBody.Part.createFormData("file", file.getName(), fileRequest);
        }

        AddPostDataRequest addPostDataRequest = new AddPostDataRequest(
                binding.descriptionNewPostEditText.getText().toString(),
                location,
                tags);

        RetrofitService retrofitService = RetrofitService.getInstance();
        PostAPI postAPI = retrofitService.getPostAPI();
        Call<ResponseWrapper<Post>> call = postAPI.postNewPost(retrofitService.getAuthToken(),
                addPostDataRequest,
                filePart);

        call.enqueue(new Callback<ResponseWrapper<Post>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<Post>> call, Response<ResponseWrapper<Post>> response) {
                if (!response.isSuccessful()) {
                    Log.d("xxx", String.valueOf(response.code()));
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<Post>> call, Throwable t) {
                Log.d("xxx", t.getMessage());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}