package com.marvello.eleganter;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class EditFurnitureActivity extends AppCompatActivity {
    private EditText etName, etBrand, etSpecs;
    private AppCompatButton btnPickImage;
    private AppCompatButton btnSubmit, btnDelete;
    private ImageView imgPreview;

    //    Database fields
    private String key, name, brand, specs, imagePath;
    private Uri imageUri;

    DatabaseReference database = new FirebaseHelper().getDatabase();
    StorageReference storageRef = new FirebaseHelper().getStorage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_furniture);

        etName = findViewById(R.id.et_name);
        etBrand = findViewById(R.id.et_brand);
        etSpecs = findViewById(R.id.et_specs);
        imgPreview = findViewById(R.id.img_preview);

        key = getIntent().getStringExtra("key");

        database.child("furniture").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Furniture furniture = snapshot.getValue(Furniture.class);
                if (furniture != null) {
                    etName.setText(furniture.getName());
                    etBrand.setText(furniture.getBrand());
                    etSpecs.setText(furniture.getSpecs());

                    StorageReference storageRef = new FirebaseHelper().getStorage();
                    storageRef.child(furniture.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri.toString()).into(imgPreview);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Toast.makeText(EditFurnitureActivity.this, "Error while fetching image",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    imagePath = furniture.getImage();
                } else {
                    startActivity(new Intent(EditFurnitureActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnPickImage = findViewById(R.id.btn_pick_image);
        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage.launch("image/*");
            }
        });

        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etName.getText().toString();
                brand = etBrand.getText().toString();
                specs = etSpecs.getText().toString();
                if (imageUri != null) {
                    StorageReference imageRef = storageRef.child(imagePath);
                    Object uploadTask = imageRef.putFile(imageUri);
                }

                database.child("furniture").child(key).setValue(new Furniture(name, imagePath, brand, specs)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        MainActivity.staticMainActivity.displayData();

                        startActivity(new Intent(EditFurnitureActivity.this, MainActivity.class));
                        finish();
                        Toast.makeText(view.getContext(), name + imagePath + brand + specs, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), "Gagal diupdate", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
            dialog.setTitle("Are you sure you want to delete this item?");
            final CharSequence[] dialogItems = {"Yes, delete this item", "No, don't delete this item"};
            dialog.setItems(dialogItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int item) {
                    switch (item) {
                        case 0:
                            startActivity(new Intent(EditFurnitureActivity.this, MainActivity.class));
                            finish();
                            database.child("furniture").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(EditFurnitureActivity.this, "Data berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditFurnitureActivity.this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        case 1:
                            break;
                    }
                }
            });
            dialog.create().show();
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