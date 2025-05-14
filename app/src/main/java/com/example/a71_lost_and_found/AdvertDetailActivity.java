package com.example.a71_lost_and_found;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import org.w3c.dom.Text;

public class AdvertDetailActivity extends AppCompatActivity {

    TextView description, date, location, type, phone;
    Button deleteButton;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_advert_detail);
        setContentView(R.layout.activity_advert_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        description = findViewById(R.id.description);
        date = findViewById(R.id.date);
        location = findViewById(R.id.location);
        type = findViewById(R.id.type);
        phone = findViewById(R.id.phone);
        deleteButton = findViewById(R.id.deletebutton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });

        getAndSetIntentData();
    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("description") && getIntent().hasExtra("date") && getIntent().hasExtra("phone") && getIntent().hasExtra("location") && getIntent().hasExtra("type")) {
            // Set the data to the TextViews
            id = getIntent().getStringExtra("id");
            description.setText(getIntent().getStringExtra("description"));
            date.setText(getIntent().getStringExtra("date"));
            location.setText(getIntent().getStringExtra("location"));
            type.setText(getIntent().getStringExtra("type"));
            phone.setText(getIntent().getStringExtra("phone"));
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete?");
        builder.setMessage("Are you sure you want to delete this task?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Items myDB = new Items(AdvertDetailActivity.this);
                myDB.deleteOneRow(String.valueOf(Integer.parseInt(id)));
                Intent intent = new Intent(AdvertDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();

    }

}