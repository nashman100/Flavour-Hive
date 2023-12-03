package com.example.flavourhive;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RestaurantDetailsOnMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurantonmap);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Get restaurant details from the intent or wherever you have them
        double restaurantLatitude = 43.67384;  // Test - Replace with actual latitude
        double restaurantLongitude = -79.39636; // Test - Replace with actual longitude
        String restaurantName = "Sotto Sotto"; // Test - Replace with actual restaurant name

        // Add a marker for the restaurant and move the camera
        LatLng restaurantLatLng = new LatLng(restaurantLatitude, restaurantLongitude);
        googleMap.addMarker(new MarkerOptions().position(restaurantLatLng).title(restaurantName));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLatLng, 15)); // Adjust the zoom level as needed
    }
}

