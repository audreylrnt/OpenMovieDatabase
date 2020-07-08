package com.example.openmoviedatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class SavedMovie extends AppCompatActivity {
    RecyclerView recyclerViewSaved;
    DBHelper mydb;
    AdapterSaved adapterSaved;
    Button toSearch;
    TextView tvNotesSaved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_movie);
        getSupportActionBar().setTitle("Saved Movies");
        toSearch = findViewById(R.id.btnSearchFromSaved);
        recyclerViewSaved = findViewById(R.id.recyclerViewSaved);
        tvNotesSaved = findViewById(R.id.tvNotesSaved);
        mydb = new DBHelper(this);
        AdapterDB.movieDataDefaults.clear();
        getSavedData();
        if(AdapterDB.movieDataDefaults.size() > 0){
            tvNotesSaved.setVisibility(View.GONE);
        }
        else{
            tvNotesSaved.setVisibility(View.VISIBLE);
        }
        recyclerViewSaved.setLayoutManager(new GridLayoutManager(this, 2));
        adapterSaved = new AdapterSaved(this);
        recyclerViewSaved.setAdapter(adapterSaved);
        adapterSaved.notifyDataSetChanged();

        toSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSearchPage = new Intent(SavedMovie.this, MainActivity.class);
                startActivity(toSearchPage);
                finish();
            }
        });
    }

    private void getSavedData() {
        Cursor c = mydb.getSavedMovie();
        while(c.moveToNext()){
            String title = c.getString(c.getColumnIndex(DBHelper.TABLE_MOVIE_NAME));
            String year = c.getString(c.getColumnIndex(DBHelper.TABLE_MOVIE_YEAR));
            String imdb = c.getString(c.getColumnIndex(DBHelper.TABLE_MOVIE_IMDB));
            String img = c.getString(c.getColumnIndex(DBHelper.TABLE_MOVIE_IMG));

            MovieDataDefault movieDataDefault = new MovieDataDefault(title, year, imdb, img);
            AdapterDB.movieDataDefaults.add(movieDataDefault);
        }
    }

}