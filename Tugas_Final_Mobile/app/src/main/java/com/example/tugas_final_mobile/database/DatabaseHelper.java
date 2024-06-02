package com.example.tugas_final_mobile.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tugas_final_mobile.response.MenuResponse;
import com.example.tugas_final_mobile.response.NutritionResponse;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tes.db";
    private static final int DATABASE_VERSION = 16;

    public static final String TABLE_NAME = "favorit";
    public static final String COLUMN_ID_MENU = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_PROTEIN = "protein";
    public static final String COLUMN_FAT = "fat";
    public static final String COLUMN_CARBS = "carbs";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID_MENU + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_IMAGE_URL + " TEXT," +
                COLUMN_CALORIES + " TEXT," +
                COLUMN_PROTEIN + " TEXT," +
                COLUMN_FAT + " TEXT," +
                COLUMN_CARBS + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method untuk menambahkan data favorit baru
    public long addFavorite(String title, String imageUrl, String calories, String protein, String fat, String carbs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_IMAGE_URL, imageUrl);
        values.put(COLUMN_CALORIES, calories);
        values.put(COLUMN_PROTEIN, protein);
        values.put(COLUMN_FAT, fat);
        values.put(COLUMN_CARBS, carbs);
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    // Method untuk mengambil semua data favorit dan mengonversinya ke dalam List<MenuResponse>
    @SuppressLint("Range")
    public List<MenuResponse> getAllFavoriteItems() {
        List<MenuResponse> favoriteItems = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                MenuResponse menuResponse = new MenuResponse();
                menuResponse.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_MENU)))); // Set ID
                menuResponse.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                menuResponse.setImageUrl(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URL)));

                // Initialize NutritionResponse and set its properties
                NutritionResponse nutritionResponse = new NutritionResponse(
                        cursor.getString(cursor.getColumnIndex(COLUMN_CALORIES)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PROTEIN)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_FAT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CARBS))
                );

                // Set the initialized NutritionResponse to the MenuResponse
                menuResponse.setNutrition(nutritionResponse);

                favoriteItems.add(menuResponse);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return favoriteItems;
    }

    // Method untuk menghapus item dari database berdasarkan ID
    public void deleteItem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID_MENU + "=?", new String[]{id});
        db.close();
    }
}
