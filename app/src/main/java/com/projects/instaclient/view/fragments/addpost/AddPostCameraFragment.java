package com.projects.instaclient.view.fragments.addpost;

import static com.airbnb.lottie.LottieDrawable.INFINITE;
import static com.airbnb.lottie.LottieDrawable.RESTART;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.common.util.concurrent.ListenableFuture;
import com.projects.instaclient.databinding.FragmentAddPostCameraBinding;
import com.projects.instaclient.helpers.Helpers;

import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

public class AddPostCameraFragment extends Fragment {

    private FragmentAddPostCameraBinding binding;

    private String[] REQUIRED_PERMISSIONS = new String[]{
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.READ_MEDIA_IMAGES"};
    private int PERMISSIONS_REQUEST_CODE = 100;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    private VideoCapture videoCapture;
    private PreviewView previewView;
    private CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

    private boolean isRecording = false;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddPostCameraBinding.inflate(getLayoutInflater());

        previewView = binding.previewView;

        if (!checkIfPermissionsGranted()) {
            requestPermissions(REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
        } else {
            startCamera();
        }

        binding.flipLottie.setOnClickListener(v -> {
            binding.flipLottie.setMinAndMaxProgress(0f, 0.1111f);
            binding.flipLottie.setSpeed(2);
            binding.flipLottie.playAnimation();
            flipCamera();
        });

        binding.cameraLottie.setOnClickListener(v -> {
            takePhoto();
        });

        binding.videoLottie.setSpeed(2f);
        binding.videoLottie.setMinAndMaxProgress(0f, 0.82f);
        binding.videoLottie.setRepeatMode(RESTART);
        binding.videoLottie.setRepeatCount(INFINITE);
        binding.videoLottie.setOnClickListener(v -> {
            if (isRecording) {
                videoCapture.stopRecording();
                binding.videoLottie.pauseAnimation();
            }
            else {
                recordVideo();
                binding.videoLottie.playAnimation();
            }
            isRecording = !isRecording;
        });

        binding.galleryLottie.setOnClickListener(v -> {
            binding.galleryLottie.setSpeed(6);
            binding.galleryLottie.playAnimation();

            binding.galleryLottie.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(@NonNull Animator animation) {

                }

                @Override
                public void onAnimationEnd(@NonNull Animator animation) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/* video/*");
                    startActivityForResult(intent, 100);
                }

                @Override
                public void onAnimationCancel(@NonNull Animator animation) {

                }

                @Override
                public void onAnimationRepeat(@NonNull Animator animation) {

                }
            });
        });

        return binding.getRoot();
    }

    private boolean checkIfPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //tak
                    startCamera();
                } else {
                    //nie
                }
                break;
            case 101:

                break;
        }
    }

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (InterruptedException | ExecutionException e) {
                // No errors need to be handled for this Future. This should never be reached.
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }

    @SuppressLint("RestrictedApi")
    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();

        Preview preview = new Preview.Builder().build();

        imageCapture = new ImageCapture.Builder()
                .setTargetRotation(Surface.ROTATION_0)
                .build();

        int flashMode = ImageCapture.FLASH_MODE_OFF;
        imageCapture.setFlashMode(flashMode);

        videoCapture = new VideoCapture.Builder()
                .setTargetRotation(Surface.ROTATION_0)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture, videoCapture, preview);
    }

    private void takePhoto() {
        String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());

        ContentValues contentValues = new ContentValues();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
        }
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(
                        getContext().getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues)
                        .build();

        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(getContext()),
                new ImageCapture.OnImageSavedCallback() {

                    final Bitmap bitmap = previewView.getBitmap();

                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        String uri = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + timestamp + ".jpg";
                        Helpers.replaceMainFragment(getParentFragmentManager(), new AcceptImagePostFragment(bitmap, uri));
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @SuppressLint("RestrictedApi")
    private void recordVideo() {
        String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());

        ContentValues contentValues = new ContentValues();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
        }
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");

        VideoCapture.OutputFileOptions outputFileOptions =
                new VideoCapture.OutputFileOptions.Builder(
                        getContext().getContentResolver(),
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        contentValues)
                        .build();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        videoCapture.startRecording(
                outputFileOptions,
                ContextCompat.getMainExecutor(getContext()),
                new VideoCapture.OnVideoSavedCallback() {

                    final Bitmap bitmap = previewView.getBitmap();

                    @Override
                    public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {
                        String uri = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/" + timestamp + ".mp4";
                        Helpers.replaceMainFragment(getParentFragmentManager(), new AcceptImagePostFragment(bitmap, uri));
                    }

                    @Override
                    public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void flipCamera() {
        if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
        } else {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
        }
        startCamera();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("xxx", String.valueOf(requestCode));
        if (requestCode == 100) {
            InputStream inputStream = null;
            Bitmap bitmap;
            Uri imageUri;

            try {
                imageUri = data.getData();
                inputStream = getContext().getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Helpers.replaceNavigationFragment(getParentFragmentManager(), new AddPostCameraFragment());
                return;
            }
            String stringUri = imageUri.getPath().substring(5);

            Helpers.replaceMainFragment(getParentFragmentManager(), new AcceptImagePostFragment(bitmap, stringUri));
        } else {
            Helpers.replaceNavigationFragment(getParentFragmentManager(), new AddPostCameraFragment());
        }
    }
}