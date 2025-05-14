package com.example.a71_lost_and_found;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ItemsListActivity extends AppCompatActivity {

    RecyclerView advertsList;

    ListItemsAdapter listItemsAdapter;

    ArrayList advert_id, advert_name, advert_phone, advert_description, advert_date, advert_latitude, advert_longitude, advert_type;
    Items itemsDB;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_items_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        advert_id = new ArrayList<>();
        advert_name = new ArrayList<>();
        advert_phone = new ArrayList<>();
        advert_description = new ArrayList<>();
        advert_date = new ArrayList<>();
        advert_latitude = new ArrayList<>();
        advert_longitude = new ArrayList<>();
        advert_type = new ArrayList<>();

        listItemsAdapter = new ListItemsAdapter(this, advert_id, advert_name, advert_phone, advert_description, advert_date, advert_latitude, advert_longitude, advert_type);
        itemsDB = new Items(ItemsListActivity.this);
        advertsList = findViewById(R.id.advertsRecyclerView);

        advertsList.setAdapter(listItemsAdapter);
        advertsList.setLayoutManager(new LinearLayoutManager(ItemsListActivity.this));
        displayAdvertsData();
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemsListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        if(requestCode == 1) {
            recreate();
        }
    }

    void displayAdvertsData(){
        Cursor cursor = itemsDB.readAllData();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                advert_id.add(cursor.getString(0));
                advert_name.add(cursor.getString(1));
                advert_phone.add(cursor.getString(2));
                advert_description.add(cursor.getString(3));
                long dateTimeStamp = cursor.getLong(4);
                String date = convertTimestampToDate(dateTimeStamp);
                advert_date.add(date);
                advert_latitude.add(cursor.getString(5));
                advert_longitude.add(cursor.getString(6));
                advert_type.add(cursor.getString(7));
            }
            cursor.close();
        }
    }
    private String convertTimestampToDate(Long timestamp) {
        if (timestamp == null || timestamp < 1) {
            return "N/A";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}