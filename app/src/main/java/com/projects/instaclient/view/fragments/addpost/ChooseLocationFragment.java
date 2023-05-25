package com.projects.instaclient.view.fragments.addpost;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.projects.instaclient.R;
import com.projects.instaclient.databinding.FragmentChooseLocationBinding;
import com.projects.instaclient.helpers.Helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChooseLocationFragment extends Fragment implements OnMapReadyCallback {

    private FragmentChooseLocationBinding binding;

    private final String API_KEY = "AIzaSyAwu6FO-Vb-ITp39cSydpdr7e6yYjdHP5k";
    private GoogleMap map;
    private List<Address> list;
    private Geocoder geocoder;
    private PlacesClient placesClient;
    private String chosenLocation;

    // DESCRIBE POST FIELDS
    private Bitmap image;
    private String imageUri;
    private String description;
    private String location;
    private ArrayList<String> tags;

    public ChooseLocationFragment(Bitmap image, String imageUri, String description, String location, ArrayList<String> tags) {
        this.image = image;
        this.imageUri = imageUri;
        this.description = description;
        this.location = location;
        this.tags = tags;
    }

    private String[] REQUIRED_PERMISSIONS = new String[]{
            "android.permission.ACCESS_FINE_LOCATION"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChooseLocationBinding.inflate(getLayoutInflater());

        // ON CLICKS
        binding.cancelLocationImageButton.setOnClickListener(v -> {
            Helpers.replaceMainFragment(getParentFragmentManager(), new DescribeNewPostFragment(image, imageUri, description, location, tags));
        });

        binding.acceptLocationImageButton.setOnClickListener(v -> {
            Helpers.replaceMainFragment(getParentFragmentManager(), new DescribeNewPostFragment(image, imageUri, description, chosenLocation, tags));
        });

        // SETUP MAPS
        if (!checkIfPermissionsGranted()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int PERMISSIONS_REQUEST_CODE = 100;
                requestPermissions(REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
        else {
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getChildFragmentManager()
                            .findFragmentById(R.id.map_fragment);

            mapFragment.getMapAsync(this);

            geocoder = new Geocoder(getContext());

            if (!Places.isInitialized()) {
                Places.initialize(getContext().getApplicationContext(), API_KEY);
                placesClient = Places.createClient(getContext());
            }

            AutocompleteSupportFragment autocompleteFragment =
                    (AutocompleteSupportFragment) getChildFragmentManager()
                            .findFragmentById(R.id.autocomplete_fragment);

            autocompleteFragment.setActivityMode(AutocompleteActivityMode.FULLSCREEN);
            autocompleteFragment.getView().setBackgroundColor(0x0000ff);
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    Log.i("xxx", "Place: " + place.getName() + ", " + place.getId());
                    try {
                        geocode(place.getName());
                        chosenLocation = place.getName();
                        binding.acceptLocationImageButton.setEnabled(true);
                    } catch (IOException e) {
                        Log.i("xxx", "error: " + e.getMessage());
                    }
                }

                @Override
                public void onError(@NonNull Status status) {
                    Log.i("xxx", "error: " + status);
                }
            });
        }

        return binding.getRoot();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
    }

    private void geocode(String locationName) throws IOException {

        list = geocoder.getFromLocationName(locationName, 1);

        double latitude = list.get(0).getLatitude();
        double longitude = list.get(0).getLongitude();

        LatLng latLng = new LatLng(latitude, longitude);

        map.addMarker(new MarkerOptions().position(latLng).title("Marker"));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng , 10);
        map.animateCamera(cameraUpdate);
    }

    private boolean checkIfPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}