package com.example.flavourhive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ManageRestaurantsActivity extends AppCompatActivity {

    private Executor databaseExecutor = Executors.newSingleThreadExecutor();

    private EditText editTextName, editTextType, editTextRating, editTextAddress;
    private Button buttonAdd, buttonRemove;
    private RestaurantDao restaurantDao;
    private RestaurantAdapter restaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_restaurants);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editTextName = findViewById(R.id.editTextRestaurantName);
        editTextType = findViewById(R.id.editTextRestaurantType);
        editTextRating = findViewById(R.id.editTextRestaurantRating);
        editTextAddress = findViewById(R.id.editTextRestaurantAddress);
        buttonAdd = findViewById(R.id.btnAddRestaurant);
        buttonRemove = findViewById(R.id.btnRemoveRestaurant);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewManageRestaurants);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantAdapter = new RestaurantAdapter(new ArrayList<>());
        recyclerView.setAdapter(restaurantAdapter);

        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        restaurantDao = db.restaurantDao();
        getRestaurants();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRestaurant();
            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeRestaurant();
            }
        });
    }

    private void getRestaurants() {
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
            restaurants.add(new Restaurant(entity.getName(), entity.getType(), Float.parseFloat(entity.getRating()), entity.getAddress()));
        }
        return restaurants;
    }

    private void addRestaurant() {
        String name = editTextName.getText().toString().trim();
        String type = editTextType.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        float rating;

        try {
            rating = Float.parseFloat(editTextRating.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid rating", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rating < 1 || rating > 5) {
            Toast.makeText(this, "Please enter a rating between 1 and 5", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.isEmpty() || type.isEmpty()) {
            Toast.makeText(this, "Please enter valid details", Toast.LENGTH_SHORT).show();
            return;
        }

        RestaurantEntity newRestaurantEntity = new RestaurantEntity(0, name, type, String.valueOf(rating), address);

        databaseExecutor.execute(() -> {

            restaurantDao.insert(newRestaurantEntity);

            runOnUiThread(this::getRestaurants);

            runOnUiThread(() -> Toast.makeText(this, "Restaurant added successfully", Toast.LENGTH_SHORT).show());

            clearEditTexts();
        });
    }


    private void removeRestaurant() {
        String name = editTextName.getText().toString();

        // Input Validation
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter a restaurant name", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseExecutor.execute(() -> {

            restaurantDao.deleteByName(name);

            runOnUiThread(this::getRestaurants);

            runOnUiThread(() -> Toast.makeText(this, "Restaurant removed successfully", Toast.LENGTH_SHORT).show());

            clearEditTexts();
        });
    }

    private void clearEditTexts() {
        editTextName.setText("");
        editTextType.setText("");
        editTextRating.setText("");
        editTextAddress.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}


