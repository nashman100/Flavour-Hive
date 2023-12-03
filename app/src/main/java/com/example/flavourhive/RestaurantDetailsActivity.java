package com.example.flavourhive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RestaurantDetailsActivity extends AppCompatActivity {
// Prakash just add a button to the restaurant details layout then uncomment this code
 private Button navigateButton;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        navigateButton = findViewById(R.id.detailstomap_button);
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the NavigationActivity
                Intent intent = new Intent(RestaurantDetailsActivity.this, RestaurantDetailsOnMapActivity.class);
                startActivity(intent);
            }
        });

        if (getIntent() != null) {
            String restaurantName = getIntent().getStringExtra("restaurant_name");
            String restaurantType = getIntent().getStringExtra("restaurant_type");
            String restaurantRating = getIntent().getStringExtra("restaurant_rating");

            // Display the restaurant details in the UI
            TextView nameTextView = findViewById(R.id.detailNameTextView);
            TextView typeTextView = findViewById(R.id.detailTypeTextView);
            TextView ratingTextView = findViewById(R.id.detailRatingTextView);

            nameTextView.setText("Name: " + restaurantName);
            typeTextView.setText("Type: " + restaurantType);
            ratingTextView.setText("Rating: " + restaurantRating);
        }
   }
}
