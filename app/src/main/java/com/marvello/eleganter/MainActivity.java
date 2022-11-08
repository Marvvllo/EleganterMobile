package com.marvello.eleganter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String searchQuery;
    private EditText etSearch;
    private AppCompatButton btnAdd;
    private RecyclerView recyclerView;
    private ArrayList<String> codes, names, images, brands;
    private ArrayList<Furniture> listFurniture;
    public static MainActivity staticMainActivity;
    MainAdapter adapter;
    DatabaseReference database = new FirebaseHelper().getDatabase();

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
        database.child("furniture").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listFurniture = new ArrayList<Furniture>();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Furniture furniture = item.getValue(Furniture.class);
                    assert furniture != null;
                    furniture.setKey(item.getKey());
                    listFurniture.add(furniture);
                    Toast.makeText(MainActivity.this, furniture.getName(), Toast.LENGTH_SHORT).show();
                }
                adapter = new MainAdapter(listFurniture, MainActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}