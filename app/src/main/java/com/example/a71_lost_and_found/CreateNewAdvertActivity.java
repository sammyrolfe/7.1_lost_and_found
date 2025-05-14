package com.example.a71_lost_and_found;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import android.location.Geocoder;
import android.location.Address;


import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CreateNewAdvertActivity extends AppCompatActivity {

    Button SaveButton, CancelButton;
    Items itemsDB;
    EditText nameInput, phoneInput, descriptionInput, dateInput;
    TextView locationInput;
    RadioGroup typeRadioGroup;
    private FusedLocationProviderClient fusedLocationClient;
    private Button currentLocationButton;

    private double selectedLatitude, selectedLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_new_advert);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        currentLocationButton = findViewById(R.id.currentLocationButton);

        currentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });

        CancelButton = findViewById(R.id.CancelButton);
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewAdvertActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Places.initialize(getApplicationContext(), "AIzaSyBZ2H_6Jds6czn0fHDTl0FD7UmXybYR2vY");

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.LocationFragment);


        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng location = place.getLatLng();
                String placeName = place.getName();

                locationInput.setText(placeName);

                selectedLatitude = location.latitude;
                selectedLongitude = location.longitude;
                locationInput.setVisibility(View.VISIBLE); // Show input field
                autocompleteFragment.getView().setVisibility(View.GONE); // Hide fragment
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e("PlacesAPI", "Error selecting location: " + status.getStatusMessage());
            }

        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        itemsDB = new Items(CreateNewAdvertActivity.this);
        SaveButton = findViewById(R.id.SaveButton);
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = typeRadioGroup.getCheckedRadioButtonId();
                String itemType = "Lost";
                if (selectedId == R.id.lostRadio) {
                    itemType = "Lost";
                } else if (selectedId == R.id.foundRadio){
                    itemType = "Found";
                }
                itemsDB.addItem(nameInput.getText().toString(), phoneInput.getText().toString(), descriptionInput.getText().toString(), dateInput.getText().toString(), String.valueOf(selectedLatitude),String.valueOf(selectedLongitude), itemType);
                Intent intent = new Intent(CreateNewAdvertActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(CreateNewAdvertActivity.this, "Item added", Toast.LENGTH_SHORT).show();
            }
        });
        nameInput = findViewById(R.id.NameInput);
        phoneInput = findViewById(R.id.PhoneInput);
        descriptionInput = findViewById(R.id.DescriptionInput);
        dateInput = findViewById(R.id.DateInput);
        locationInput = findViewById(R.id.LocationInput);
        typeRadioGroup = findViewById(R.id.radioGroup);
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                selectedLatitude = location.getLatitude();
                                selectedLongitude = location.getLongitude();

                                // Fetch the place name immediately after getting location
                                getPlaceNameFromCoordinates(selectedLatitude, selectedLongitude);
                            } else {
                                Toast.makeText(CreateNewAdvertActivity.this, "No location found, trying again...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            Toast.makeText(this, "Location permission required!", Toast.LENGTH_SHORT).show();
        }
    }


    private void getPlaceNameFromCoordinates(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            Log.d("LocationDebug", "Fetching location name for: Lat " + latitude + ", Lon " + longitude);
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String placeName = address.getLocality();

                Log.d("LocationDebug", "Place name found: " + placeName);

                // Check if activity is still active before updating UI
                if (!isFinishing() && !isDestroyed()) {
                    Log.d("LocationDebug", "locationInput instance: " + locationInput);
                    runOnUiThread(() -> {
                        locationInput.setVisibility(View.VISIBLE);
                        locationInput.setText(placeName != null ? placeName : "Unknown Location");
                        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                                getSupportFragmentManager().findFragmentById(R.id.LocationFragment);

                        if (autocompleteFragment != null && autocompleteFragment.getView() != null) {
                            runOnUiThread(() -> autocompleteFragment.getView().setVisibility(View.GONE));
                        }
                    });
                } else {
                    Log.e("LocationDebug", "Activity is no longer active, UI update skipped.");
                }
            } else {
                Log.e("LocationDebug", "No place name found, Geocoder returned empty list.");
                runOnUiThread(() -> locationInput.setText("Unknown Location"));
            }
        } catch (Exception e) {
            Log.e("LocationDebug", "Geocoder failed: " + e.getMessage());
            runOnUiThread(() -> locationInput.setText("Location unavailable"));
        }
    }

}