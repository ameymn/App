package com.example.jeeevandan;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 123;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private boolean isLocationTrackingStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        // Request location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            // Permission already granted, start location tracking
            startLocationTracking();
        }

        Button startButton = findViewById(R.id.button2);
        Button stopButton = findViewById(R.id.button3);

        startButton.setOnClickListener(v -> {
            startLocationTracking();
        });

        stopButton.setOnClickListener(v -> {
            stopLocationTracking();
        });
    }

    private void startLocationTracking() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        Double latitude = location.getLatitude();
                        Double longitude = location.getLongitude();
                        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                        Call<com.example.jeeevandan.Location> call = apiService.location(latitude, longitude);
                        call.enqueue(new Callback<com.example.jeeevandan.Location>() {
                            @Override
                            public void onResponse(Call<com.example.jeeevandan.Location> call, Response<com.example.jeeevandan.Location> response){
                                Log.d(TAG, "Response code: " + response.code());

                            }

                            @Override
                            public void onFailure(Call<com.example.jeeevandan.Location> call, Throwable t) {
                                Toast.makeText(com.example.jeeevandan.NewActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                            }


                        });


                            // Handle the location updates here
                        Log.d(TAG, "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
                    }
                }
            }
        };

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(2000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            isLocationTrackingStarted = true;
            Toast.makeText(this, "Location tracking started", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopLocationTracking() {
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
            isLocationTrackingStarted = false;
            Toast.makeText(this, "Location tracking stopped", Toast.LENGTH_SHORT).show();
        }
    }
}