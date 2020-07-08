package com.example.openmoviedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "MovieManager";
    public static final int DB_VER = 1;

    public static final String TABLE_NAME = "SavedMovie";
    public static final String TABLE_MOVIE_NAME = "MovieName";
    public static final String TABLE_MOVIE_YEAR = "MovieYear";
    public static final String TABLE_MOVIE_IMDB = "MovieImdb";
    public static final String TABLE_MOVIE_IMG = "MovieImg";


    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                TABLE_MOVIE_NAME + " TEXT, " +
                TABLE_MOVIE_YEAR + " TEXT, " +
                TABLE_MOVIE_IMDB + " TEXT, " +
                TABLE_MOVIE_IMG + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insert(String movieImg, String movieTitle, String movieYear, String movieImdb){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TABLE_MOVIE_NAME, movieTitle);
        cv.put(TABLE_MOVIE_YEAR, movieYear);
        cv.put(TABLE_MOVIE_IMDB, movieImdb);
        cv.put(TABLE_MOVIE_IMG, movieImg);

        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor getID(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + TABLE_MOVIE_IMDB + " FROM " + TABLE_NAME, null);
        return res;
    }

    public Cursor getSavedMovie(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public Integer delete(String movieID){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = TABLE_MOVIE_IMDB + " LIKE ?";
        String[] selArgs = {movieID};
        Integer res = db.delete(TABLE_NAME, selection, selArgs);
        return res;
    }
}