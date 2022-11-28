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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    Context context;
    private ArrayList<Furniture> furnitures;

    public MainAdapter(ArrayList<Furniture> furnitures, Context context) {
        this.context = context;
        this.furnitures = furnitures;
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
        final Furniture data = furnitures.get(position);
        String name = data.getName();
        String brand = data.getBrand();
        String image = data.getImage();

        StorageReference storageRef = new FirebaseHelper().getStorage();
        storageRef.child(data.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString())
                        .into(holder.getImageView());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        holder.getNameTV().setText(name);
        holder.getBrandTV().setText(brand);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ShowFurnitureActivity.class);
            intent.putExtra("key", data.getKey());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return furnitures.size();
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
