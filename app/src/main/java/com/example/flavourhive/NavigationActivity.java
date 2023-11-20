package com.example.flavourhive;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class NavigationActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private ImageButton map_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_layout);

        map_button = findViewById(R.id.map_button);
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the app has the necessary permissions
                if (ContextCompat.checkSelfPermission(NavigationActivity.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
                    // Request the permission
                    ActivityCompat.requestPermissions(NavigationActivity.this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                    return;
                }

                // Create an intent
                Uri uri = Uri.parse("geo:43.6759374,-79.4111022?q=restaurants");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                // Check if there's an app available to handle the intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    Intent chooser = Intent.createChooser(intent, "How should I open this location?");
                    startActivity(chooser);
                }
            }
        });
    }
}
