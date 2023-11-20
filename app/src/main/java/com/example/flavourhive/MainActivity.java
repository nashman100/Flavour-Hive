package com.example.flavourhive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RestaurantAdapter restaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // Initialize Spinner
        Spinner dropdownSpinner = findViewById(R.id.dropdownSpinner);
        initializeSpinner(dropdownSpinner);

        Button addBtn = findViewById(R.id.addBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManageRestaurantsActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantAdapter = new RestaurantAdapter(new ArrayList<>());
        recyclerView.setAdapter(restaurantAdapter);

        getRestaurants();

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                restaurantAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                restaurantAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void getRestaurants() {
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        RestaurantDao restaurantDao = db.restaurantDao();
        restaurantDao.getAllRestaurants().observe(this, new Observer<List<RestaurantEntity>>() {
            @Override
            public void onChanged(List<RestaurantEntity> restaurantEntities) {
                List<Restaurant> restaurants = convertToRestaurantList(restaurantEntities);
                restaurantAdapter.setRestaurants(restaurants);
            }
        });
    }

    private List<Restaurant> convertToRestaurantList(List<RestaurantEntity> restaurantEntities) {
        List<Restaurant> restaurants = new ArrayList<>();
        for (RestaurantEntity entity : restaurantEntities) {
            restaurants.add(new Restaurant(entity.getName(), entity.getType(), Float.parseFloat(entity.getRating())));
        }
        return restaurants;
    }

    private void initializeSpinner(Spinner spinner) {
        List<String> dropdownItems = new ArrayList<>();
        dropdownItems.add("\u2630");
        dropdownItems.add("About");


        // Create an ArrayAdapter using the custom layout for the string array
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_layout, dropdownItems);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the Spinner
        spinner.setAdapter(adapter);

        // Set a listener for item selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item only if it's not the default "Select an Option"
                if (position > 0) {
                    String selectedItem = dropdownItems.get(position);
                    Toast.makeText(MainActivity.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();

                    // Navigate to a different activity based on the selected item
                    switch (position) {
                        case 1:
                            // About Activity
                            Intent intentAbout = new Intent(MainActivity.this, AboutActivity.class);
                            startActivity(intentAbout);
                            break;

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }
}
