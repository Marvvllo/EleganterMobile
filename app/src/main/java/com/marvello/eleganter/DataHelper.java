package com.marvello.eleganter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "eleganter.db";
    private static final int DATABASE_VERSION = 1;
    public DataHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table furniture (code text primary key, name text null, image text null, brand text null, specs text null);";
        Log.d("Data","onCreate: "+sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }

    public boolean insertFurniture(String code, String name, String imageUri, String brand, String specs) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("name", name);
        contentValues.put("image", imageUri);
        contentValues.put("brand", brand);
        contentValues.put("specs", specs);

        long result = DB.insert("furniture", null, contentValues);
        return result == -1;
    }

    public boolean updateFurniture(String code, String name, String imageUri, String brand, String specs) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("image", imageUri);
        contentValues.put("brand", brand);
        contentValues.put("specs", specs);

        long result = DB.update("furniture", contentValues, "code = ?", new String[]{code});
        return !(result == -1);
    }

    public Cursor getAllFurniture() {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM furniture", null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getFurniture(String code) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM furniture WHERE code='" + code + "'", null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor searchFurniture(String search) {
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM furniture WHERE name LIKE '%" + search + "%'" +
                " OR brand LIKE '%" + search + "%'", null);
        cursor.moveToFirst();
        return cursor;
    }

    public void deleteFurniture(String code) {
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.delete("furniture", "code = ?", new String[]{code});
    }

}