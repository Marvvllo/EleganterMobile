package com.marvello.eleganter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowFurnitureActivity extends AppCompatActivity {
    private ImageView imgFurniture;
    private TextView tvName, tvBrand, tvSpecs;
    private AppCompatButton btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_furniture);

        imgFurniture = findViewById(R.id.img_furniture);
        tvName = findViewById(R.id.tv_name);
        tvBrand = findViewById(R.id.tv_brand);
        tvSpecs = findViewById(R.id.tv_specs);

        DataHelper DB = new DataHelper(this);
        Cursor cursor = DB.getFurniture(getIntent().getStringExtra("code"));
        Uri imageURI = Uri.parse(cursor.getString(2));
        imgFurniture.setImageURI(imageURI);
        tvName.setText(cursor.getString(1));
        tvBrand.setText(cursor.getString(3));
        tvSpecs.setText(cursor.getString(4));

        btnEdit = findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(view -> {
            Intent intent = new Intent(ShowFurnitureActivity.this, EditFurnitureActivity.class);
            intent.putExtra("code", cursor.getString(0));
            startActivity(intent);
        });



    }
}