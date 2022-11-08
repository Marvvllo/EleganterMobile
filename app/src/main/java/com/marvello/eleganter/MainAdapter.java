package com.marvello.eleganter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    Context context;
    private SQLiteDatabase DB;
    private ArrayList<String> codes, names, images, brands;

    public MainAdapter(Context context, ArrayList<String> codes, ArrayList<String> names, ArrayList<String> images, ArrayList<String> brands) {
        this.context = context;
        this.codes = codes;
        this.names = names;
        this.images = images;
        this.brands = brands;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.furniture_item, parent, false);

        return new MainAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = names.get(position);
        String brand = brands.get(position);
        Bitmap imageBitmap = new ImageHelper().loadImageFromStorage(images.get(position));

        if(imageBitmap != null) {
            holder.getImageView().setImageBitmap(imageBitmap);
        }
        holder.getNameTV().setText(name);
        holder.getBrandTV().setText(brand);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ShowFurnitureActivity.class);
            intent.putExtra("code", codes.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvBrand;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvBrand = itemView.findViewById(R.id.tv_brand);
            imageView = itemView.findViewById(R.id.img_furniture);
        }

        public TextView getNameTV() {
            return tvName;
        }

        public TextView getBrandTV() {
            return tvBrand;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
