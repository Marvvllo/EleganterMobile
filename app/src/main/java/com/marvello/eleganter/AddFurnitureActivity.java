package com.marvello.eleganter;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFurnitureActivity extends AppCompatActivity {
    private EditText etCode, etName, etBrand, etSpecs;
    private AppCompatButton btnPickImage;
    private AppCompatButton btnSubmit;
    private ImageView imgPreview;
    private DataHelper DB;

    DatabaseReference database = new FirebaseHelper().getReference();

//    Database fields
    private String code;
    private String name;
    private Uri imageUri;
    private String brand;
    private String specs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_furniture);

        DB = new DataHelper(this);

        etCode = findViewById(R.id.et_code);
        etName = findViewById(R.id.et_name);
        etBrand = findViewById(R.id.et_brand);
        etSpecs = findViewById(R.id.et_specs);
        btnPickImage = findViewById(R.id.btn_pick_image);
        btnSubmit = findViewById(R.id.btn_submit);

        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage.launch("image/*");
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etName.getText().toString();
                brand = etBrand.getText().toString();
                specs = etSpecs.getText().toString();

                if (name.isEmpty()) {
                        Toast.makeText(AddFurnitureActivity.this, "Nama kosong"
                                            , Toast.LENGTH_SHORT).show();
                } else if (brand.isEmpty()) {
                        Toast.makeText(AddFurnitureActivity.this, "Brand Kosong"
                                            , Toast.LENGTH_SHORT).show();
                } else if (specs.isEmpty()) {
                        Toast.makeText(AddFurnitureActivity.this, "Specs kosong"
                                            , Toast.LENGTH_SHORT).show();
                } else {
                    Furniture furniture = new Furniture(name, "null", brand, specs);

                    database.child("furniture").push().setValue(furniture)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AddFurnitureActivity.this, "Data Berhasil Disimpan"
                                            , Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(AddFurnitureActivity.this, MainActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddFurnitureActivity.this, "Gagal Menyimpan"
                                    , Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    ActivityResultLauncher<String> pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    imageUri = uri;
                    imgPreview = findViewById(R.id.img_preview);
                    imgPreview.setImageURI(uri);
                }
            });



}