package com.marvello.eleganter;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class AddFurnitureActivity extends AppCompatActivity {
    private EditText etCode, etName, etBrand, etSpecs;
    private AppCompatButton btnPickImage;
    private AppCompatButton btnSubmit;
    private ImageView imgPreview;

    DatabaseReference database = new FirebaseHelper().getDatabase();
    StorageReference storageRef = new FirebaseHelper().getStorage();


    //    Database fields
    private String name;
    private Uri imageUri;
    private String brand;
    private String specs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_furniture);

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
                    String imageFileName = new ImageHelper().getFileName(imageUri);

                    Furniture furniture = new Furniture(name, "images/"+imageFileName, brand, specs);

                    database.child("furniture").push().setValue(furniture)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AddFurnitureActivity.this, "Data Berhasil Disimpan"
                                            , Toast.LENGTH_SHORT).show();

                                    StorageReference imageRef = storageRef.child("images/" + imageFileName);
                                    Object uploadTask = imageRef.putFile(imageUri);


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