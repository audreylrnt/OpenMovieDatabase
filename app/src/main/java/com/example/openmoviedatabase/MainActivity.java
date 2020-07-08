package com.example.openmoviedatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageButton btnSearch;
    ImageButton btnSaved;
    EditText searchBar;
    RecyclerView recyclerView;
    AdapterSearch adapterSearch;
    TextView tvNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = findViewById(R.id.btnSearch);
        searchBar = findViewById(R.id.searchBar);
        btnSaved = findViewById(R.id.btnSaved);
        recyclerView = findViewById(R.id.recyclerView);
        tvNotes = findViewById(R.id.tvNotes);
        tvNotes.setText("Find your movie!");
        searchBar.setFocusableInTouchMode(true);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MovieDataDB.movieDataDefaults.size() > 0){
                    MovieDataDB.movieDataDefaults.clear();
                }
                String searchQuery = searchBar.getText().toString()+"";
                if(searchQuery.equals("")){
                    Toast.makeText(MainActivity.this, "Please type the movie title!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(searchQuery.length() < 3){
                    Toast.makeText(MainActivity.this, "Please type at least 3 characters!", Toast.LENGTH_LONG).show();
                    return;
                }
                String url = "http://www.omdbapi.com/?s="+ searchQuery + "&apikey=e6b86f21&";
                jsonGet(searchQuery, url);
            }
        });

        btnSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSaved = new Intent(MainActivity.this, SavedMovie.class);
                startActivity(toSaved);
            }
        });
    }

    private void jsonGet(final String movieTitleQuery, String movieURL) {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null && response.length() > 0){
                    try {
                        JSONArray jsonArray = response.getJSONArray("Search");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject movies = jsonArray.getJSONObject(i);
                            String title, year, imdbID, image;
                            title = movies.getString("Title");
                            year = movies.getString("Year");
                            imdbID = movies.getString("imdbID");
                            image = movies.getString("Poster");
                            MovieDataDefault movieDataDefault = new MovieDataDefault(title, year, imdbID, image);
                            MovieDataDB.movieDataDefaults.add(movieDataDefault);
                            tvNotes.setText("Search result for " + movieTitleQuery);
                        }
                    } catch (JSONException e) {
                        String err = null;
                        try {
                            err = response.getString("Error");
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                        tvNotes.setText("Can't find " + movieTitleQuery + "! " + err);
                        e.printStackTrace();
                    }
                    recyclerView.setHasFixedSize(true);
                    adapterSearch = new AdapterSearch(MainActivity.this);
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                    recyclerView.setAdapter(adapterSearch);
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_LONG).show();
            }
        };
        Cache cache = new DiskBasedCache(getCacheDir(), 1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, movieURL, null, listener, errorListener);
        requestQueue.add(request);
    }
}