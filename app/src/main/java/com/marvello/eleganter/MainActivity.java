package com.marvello.eleganter;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String searchQuery;
    private EditText et_search;
    private AppCompatButton btn_add, btn_auth;
    private RecyclerView recyclerView;
    private ArrayList<String> codes, names, images, brands;
    private ArrayList<Furniture> listFurniture;
    public static MainActivity staticMainActivity;
    MainAdapter adapter;
    DatabaseReference database = new FirebaseHelper().getDatabase();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        searchQuery = "";

        staticMainActivity = this;

        displayData();

        FirebaseUser user = mAuth.getCurrentUser();
        btn_auth = findViewById(R.id.btn_auth);
        if (user != null) {
            Drawable ic_signout = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_signout);
            btn_auth.setCompoundDrawablesWithIntrinsicBounds(ic_signout, null, null, null);
            btn_auth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signOut();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
            });
        } else {
            btn_auth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginActivity);
                }
            });
        }

        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFurnitureIntent = new Intent(getApplicationContext(), AddFurnitureActivity.class);
                startActivity(addFurnitureIntent);
            }
        });

        et_search = findViewById(R.id.et_search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchQuery = et_search.getText().toString();
                displayData();
            }
        });
    }

    public void displayData() {
        recyclerView = findViewById(R.id.rv_main);

        if (searchQuery.isEmpty()) {
        database.child("furniture").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listFurniture = new ArrayList<Furniture>();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Furniture furniture = item.getValue(Furniture.class);
                    assert furniture != null;
                    furniture.setKey(item.getKey());
                    listFurniture.add(furniture);
                }
                adapter = new MainAdapter(listFurniture, MainActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        } else {
            database.child("furniture").orderByChild("name").startAt(searchQuery)
                    .endAt(searchQuery+"\uf8ff").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            listFurniture = new ArrayList<Furniture>();
                            for (DataSnapshot item : snapshot.getChildren()) {
                                Furniture furniture = item.getValue(Furniture.class);
                                assert furniture != null;
                                furniture.setKey(item.getKey());
                                listFurniture.add(furniture);
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


}