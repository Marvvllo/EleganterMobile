package com.marvello.eleganter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseHelper {

    public FirebaseHelper() {
    }

    public DatabaseReference getDatabase() {
        DatabaseReference database;
        database = FirebaseDatabase.getInstance("https://eleganter-e0e24-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();
        return database;
    }

    public StorageReference getStorage() {
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        return storage;
    }

}
