package com.example.a71_lost_and_found;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateNewAdvertActivity extends AppCompatActivity {

    Button SaveButton;
    Items itemsDB;
    EditText nameInput, phoneInput, descriptionInput, dateInput, locationInput;

    RadioGroup typeRadioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_new_advert);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
                itemsDB.addItem(nameInput.getText().toString(), phoneInput.getText().toString(), descriptionInput.getText().toString(), dateInput.getText().toString(), locationInput.getText().toString(), itemType);
                Intent intent = new Intent(CreateNewAdvertActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(CreateNewAdvertActivity.this, "Item added", Toast.LENGTH_SHORT).show();
            }
        });
        itemsDB = new Items(CreateNewAdvertActivity.this);



        nameInput = findViewById(R.id.NameInput);
        phoneInput = findViewById(R.id.PhoneInput);
        descriptionInput = findViewById(R.id.DescriptionInput);
        dateInput = findViewById(R.id.DateInput);
        locationInput = findViewById(R.id.LocationInput);
        typeRadioGroup = findViewById(R.id.radioGroup);




    }
}