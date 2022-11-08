package com.marvello.eleganter;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class EditFurnitureActivity extends AppCompatActivity {
    private EditText etCode, etName, etBrand, etSpecs;
    private AppCompatButton btnPickImage;
    private AppCompatButton btnSubmit, btnDelete;
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
        setContentView(R.layout.activity_edit_furniture);

        DB = new DataHelper(this);
        Cursor cursor = DB.getFurniture(getIntent().getStringExtra("code"));

        etCode = findViewById(R.id.et_code);
        etName = findViewById(R.id.et_name);
        etBrand = findViewById(R.id.et_brand);
        etSpecs = findViewById(R.id.et_specs);
        imgPreview = findViewById(R.id.img_preview);

//      Set EditTexts & ImageView to current DB value
        etCode.setEnabled(false);
        etCode.setText(cursor.getString(0));
        etName.setText(cursor.getString(1));
        etBrand.setText(cursor.getString(3));
        etSpecs.setText(cursor.getString(4));

        Bitmap imageBitmap = new ImageHelper().loadImageFromStorage(cursor.getString(2));
        imgPreview.setImageBitmap(imageBitmap);

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
                String imagePath;
                if (imageUri != null) {
                    imagePath = new ImageHelper().saveImageToInternalStorage(
                            getApplicationContext(), imageUri, etCode.getText().toString()
                    );
                } else {
                    imagePath = cursor.getString(2);
                }
                boolean checkUpdateData = DB.updateFurniture(code, name, imagePath, brand, specs);
                if (checkUpdateData) {
                    Toast.makeText(getApplicationContext(), "Furniture added successfully", Toast.LENGTH_LONG).show();
                    MainActivity.staticMainActivity.displayData();
                    Intent intent = new Intent(EditFurnitureActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), imagePath, Toast.LENGTH_LONG).show();
                }
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
                            DB.deleteFurniture(cursor.getString(0));
                            MainActivity.staticMainActivity.displayData();
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