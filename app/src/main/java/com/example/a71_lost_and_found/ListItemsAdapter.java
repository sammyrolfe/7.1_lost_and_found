package com.example.a71_lost_and_found;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.models.ListItem;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListItemsAdapter extends RecyclerView.Adapter<ListItemsAdapter.ViewHolder> {

    private Context context;

    Activity activity;
    ArrayList advert_id, advert_name, advert_phone, advert_description, advert_date, advert_latitude, advert_longitude, advert_type;

    public ListItemsAdapter(@NonNull Context context, ArrayList advert_id, ArrayList advert_name, ArrayList advert_phone, ArrayList advert_description, ArrayList advert_date, ArrayList advert_latitude, ArrayList advert_longitude, ArrayList advert_type) {
        this.context = context;
        this.advert_id = advert_id;
        this.advert_name = advert_name;
        this.advert_phone = advert_phone;
        this.advert_description = advert_description;
        this.advert_date = advert_date;
        this.advert_latitude = advert_latitude;
        this.advert_longitude = advert_longitude;
        this.advert_type = advert_type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_list_items_adapter, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ListItemsAdapter.ViewHolder holder, int position) {
        holder.adverts.setText(String.valueOf((advert_description.get(position))));
        holder.types.setText((String.valueOf(advert_type.get(position))));

        holder.mainLayout.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, AdvertDetailActivity.class);
                intent.putExtra ("id", String.valueOf(advert_id.get(currentPosition)));
                intent.putExtra("name", String.valueOf(advert_name.get(currentPosition)));
                intent.putExtra("phone", String.valueOf(advert_phone.get(currentPosition)));
                intent.putExtra("description", String.valueOf(advert_description.get(currentPosition)));
                intent.putExtra("date", String.valueOf(advert_date.get(currentPosition)));
                intent.putExtra("latitude", String.valueOf(advert_latitude.get(currentPosition)));
                intent.putExtra("longitude", String.valueOf(advert_longitude.get(currentPosition)));
                intent.putExtra("type", String.valueOf(advert_type.get(currentPosition)));

                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return advert_name.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout mainLayout;
        TextView adverts, types;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            adverts = itemView.findViewById(R.id.adverts);
            types = itemView.findViewById(R.id.advertType);


            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }


}