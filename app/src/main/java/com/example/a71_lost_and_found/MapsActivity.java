package com.example.a71_lost_and_found;

import androidx.fragment.app.FragmentActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.a71_lost_and_found.databinding.ActivityMapsBinding;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    ArrayList<Double> advert_latitude, advert_longitude;
    ArrayList<String> advert_name;
    Items itemsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        advert_latitude = new ArrayList<>();
        advert_longitude = new ArrayList<>();
        advert_name = new ArrayList<>();


        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        itemsDB = new Items(this); // Initialize database before using it
        loadAdvertsFromDB(); // Load data from the database

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Loop through stored item locations and add markers
        for (int i = 0; i < advert_latitude.size(); i++) {
            double latitude = advert_latitude.get(i);
            double longitude = advert_longitude.get(i);
            LatLng itemLocation = new LatLng(latitude, longitude);

            mMap.addMarker(new MarkerOptions().position(itemLocation).title(advert_name.get(i)));
        }

        // Move the camera to the first item's location if available
        if (!advert_latitude.isEmpty() && !advert_longitude.isEmpty()) {
            LatLng firstItemLocation = new LatLng(advert_latitude.get(0), advert_longitude.get(0));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstItemLocation, 15));
        }
    }
    void loadAdvertsFromDB() {
        if (itemsDB == null) {
            Log.e("MapsActivity", "Database instance is null!");
            return;
        }

        Cursor cursor = itemsDB.readAllData();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                advert_name.add(cursor.getString(1));
                advert_latitude.add(cursor.getDouble(5));
                advert_longitude.add(cursor.getDouble(6));
            }
            cursor.close();
        } else {
            Log.e("MapsActivity", "Database returned no records!");
        }
    }
}