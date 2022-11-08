package com.marvello.eleganter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    public DatabaseReference getReference() {
        DatabaseReference database;
        database = FirebaseDatabase.getInstance("https://eleganter-e0e24-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();
        return database;
    }

    public FirebaseHelper() {
    }
}
