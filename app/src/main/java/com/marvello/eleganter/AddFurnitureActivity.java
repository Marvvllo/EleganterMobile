package com.marvello.eleganter;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

public class AddFurnitureActivity extends AppCompatActivity {
    private EditText etCode, etName, etBrand, etSpecs;
    private AppCompatButton btnPickImage;
    private AppCompatButton btnSubmit;
    private ImageView imgPreview;
    private DataHelper DB;

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
                code = etCode.getText().toString();
                name = etName.getText().toString();
                brand = etBrand.getText().toString();
                specs = etSpecs.getText().toString();
                String imagePath = new ImageHelper().saveImageToInternalStorage(
                        getApplicationContext(), imageUri, etCode.getText().toString()
                );
                boolean checkInsertData = DB.insertFurniture(code, name, imagePath, brand, specs);
                if (checkInsertData) {
                Toast.makeText(getApplicationContext(), "Furniture added successfully", Toast.LENGTH_LONG).show();
                } else {
                finish();
                    Toast.makeText(getApplicationContext(), imagePath, Toast.LENGTH_LONG).show();
                }
                MainActivity.staticMainActivity.displayData();
                finish();
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