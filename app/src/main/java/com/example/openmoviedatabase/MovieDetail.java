package com.example.openmoviedatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class MovieDetail extends AppCompatActivity {
    ImageView imageView;
    TextView title, year, imdbID;
    Button saveDelete;
    DBHelper mydb;
    public ArrayList<String> tempID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getSupportActionBar().setTitle("Movie Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView = findViewById(R.id.ivImage);
        title = findViewById(R.id.titleMovie);
        year = findViewById(R.id.yearMovie);
        imdbID = findViewById(R.id.imdbMovie);
        saveDelete = findViewById(R.id.btnSaveDelete);
        mydb = new DBHelper(this);
        tempID = new ArrayList<>();
        Intent fromSearch = getIntent();
        String pos = fromSearch.getStringExtra("arr");
        int moviePos = Integer.parseInt(pos);
        final String movieTitle = MovieDataDB.movieDataDefaults.get(moviePos).getMovieName();
        final String movieYear = MovieDataDB.movieDataDefaults.get(moviePos).getMovieYear();
        final String movieImdbID = MovieDataDB.movieDataDefaults.get(moviePos).getMovieImdb();
        final String movieImage = MovieDataDB.movieDataDefaults.get(moviePos).getMovieImg();
        Glide.with(MovieDetail.this).load(movieImage).placeholder(R.drawable.ic_launcher_foreground).into(imageView);
        title.setText(movieTitle);
        year.setText(movieYear);
        imdbID.setText(movieImdbID);
        checkID();
        boolean isSaved = false;
        if(tempID.size() > 0){
            for (int i = 0; i < tempID.size(); i++) {
                if(movieImdbID.equals(tempID.get(i))){
                    isSaved = true;
                    break;
                }
            }
        }
        if(tempID.size() == 0 || !isSaved){
            saveDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isAdded = mydb.insert(movieImage, movieTitle, movieYear, movieImdbID);
                    if(isAdded){
                        Toast.makeText(MovieDetail.this, "Movie saved :)", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(MovieDetail.this, "Failed to save movie :(", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            saveDelete.setText("Delete movie");
            Glide.with(MovieDetail.this).load(movieImage).placeholder(R.drawable.ic_launcher_foreground).into(imageView);
            saveDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int del = mydb.delete(movieImdbID);
                    if(del > 0){
                        Toast.makeText(MovieDetail.this, "Movie deleted!", Toast.LENGTH_LONG).show();
                        Intent toSavedMovie = new Intent(MovieDetail.this, SavedMovie.class);
                        startActivity(toSavedMovie);
                        finish();
                    }
                    else{
                        Toast.makeText(MovieDetail.this, "Failed to delete movie", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void checkID(){
        Cursor c = mydb.getID();
        while(c.moveToNext()){
            String id = c.getString(c.getColumnIndex(mydb.TABLE_MOVIE_IMDB));
            tempID.add(id);
        }
    }
}
