package com.example.flavourhive;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NavigationActivity extends AppCompatActivity {

    private SearchView searchView;
    private TextView findTextView;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_layout);

        searchView = findViewById(R.id.searchView);
        findTextView = findViewById(R.id.FindTextView);

        // Set up SearchView
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        searchView.setQueryHint("Enter location");

        // Set up OnQueryTextListener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the search here, e.g., initiate the PlacesTask
                new PlacesTask().execute(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text changes if needed
                return false;
            }
        });

        // Load Google Maps Fragment
        loadMapFragment();
    }


    private void loadMapFragment() {
        mapFragment = new SupportMapFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mapFragment, mapFragment);
        transaction.commit();
    }

    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String query = params[0];
            String apiKey = "AIzaSyC55FD2E_Ua7KEB97cQFn9nPowKhfkuiCI";

            try {
                // Encode the query to be used in the URL
                String encodedQuery = java.net.URLEncoder.encode(query, "UTF-8");

                // Create the URL for the textsearch endpoint
                URL url = new URL("https://maps.googleapis.com/maps/api/place/textsearch/json" +
                        "?query=" + encodedQuery +
                        "&key=" + apiKey);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream stream = new BufferedInputStream(connection.getInputStream());
                    return convertStreamToString(stream);
                } finally {
                    connection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    // Check for errors in the API response
                    if (jsonObject.has("error_message")) {
                        String errorMessage = jsonObject.getString("error_message");
                        Log.e("PlacesTask", "API Error: " + errorMessage);
                        return;
                    }

                    JSONArray results = jsonObject.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject place = results.getJSONObject(i);

                        // Extract information about the place
                        String name = place.getString("name");
                        String address = place.getString("formatted_address");

                        // Log the information (you can modify this part based on your requirements)
                        Log.d("PlacesTask", "Place #" + (i + 1) + ":");
                        Log.d("PlacesTask", "Name: " + name);
                        Log.d("PlacesTask", "Address: " + address);
                        Log.d("PlacesTask", "---------------------");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private String convertStreamToString(InputStream stream) {
            java.util.Scanner s = new java.util.Scanner(stream).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }
}
