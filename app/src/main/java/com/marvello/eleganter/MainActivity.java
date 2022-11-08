package com.marvello.eleganter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String searchQuery;
    private EditText etSearch;
    private AppCompatButton btnAdd;
    private RecyclerView recyclerView;
    private ArrayList<String> codes, names, images, brands;
    public static MainActivity staticMainActivity;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchQuery = "";

        staticMainActivity = this;

        displayData();

        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFurnitureIntent = new Intent(getApplicationContext(), AddFurnitureActivity.class);
                startActivity(addFurnitureIntent);
            }
        });

        etSearch = findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchQuery = etSearch.getText().toString();
                displayData();
            }
        });
    }

    public void displayData() {
        recyclerView = findViewById(R.id.rv_main);
        DataHelper DB = new DataHelper(this);
        Cursor cursor = null;
        if (searchQuery.equals("")) {
            cursor = DB.getAllFurniture();
        } else {
            cursor = DB.searchFurniture(searchQuery);
        }
        cursor.moveToFirst();

        codes = new ArrayList<String>();
        names = new ArrayList<String>();
        images = new ArrayList<String>();
        brands = new ArrayList<String>();

        for (int position = 0; position < cursor.getCount(); position++) {
            cursor.moveToPosition(position);
                codes.add(cursor.getString(0));
                names.add(cursor.getString(1));
                images.add(cursor.getString(2));
                brands.add(cursor.getString(3));
        }
        cursor.close();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MainAdapter(this, codes, names, images, brands);

        // Set adapter to recycler view
        if(!(names.size() < 1))
        {
        recyclerView.setAdapter(adapter);
        }
    }


}