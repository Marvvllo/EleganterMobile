package com.marvello.eleganter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ShowFurnitureActivity extends AppCompatActivity {
    private ImageView imgFurniture;
    private TextView tvName, tvBrand, tvSpecs;
    private AppCompatButton btnEdit;
    private String key;
    DatabaseReference database = new FirebaseHelper().getDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_furniture);

        imgFurniture = findViewById(R.id.img_furniture);
        tvName = findViewById(R.id.tv_name);
        tvBrand = findViewById(R.id.tv_brand);
        tvSpecs = findViewById(R.id.tv_specs);

        key = getIntent().getStringExtra("key");

        database.child("furniture").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Furniture furniture = snapshot.getValue(Furniture.class);
                if (furniture != null) {
                    tvName.setText(furniture.getName());
                    tvBrand.setText(furniture.getSeller());
                    tvSpecs.setText(furniture.getSpecs());

                    StorageReference storageRef = new FirebaseHelper().getStorage();
                    storageRef.child(furniture.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri.toString()).into(imgFurniture);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Toast.makeText(ShowFurnitureActivity.this, "Error while fetching image",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    startActivity(new Intent(ShowFurnitureActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnEdit = findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(view -> {
            Intent intent = new Intent(ShowFurnitureActivity.this, EditFurnitureActivity.class);
            intent.putExtra("key", key);
            startActivity(intent);
        });



    }
}