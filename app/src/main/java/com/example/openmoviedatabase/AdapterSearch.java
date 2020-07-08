package com.example.openmoviedatabase;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.ViewHolder>{
    Context context;
    ArrayList<String> tempID;
    DBHelper mydb;

    public AdapterSearch(Context context) {
        this.context = context;
    }

    private void checkID(){
        Cursor c = mydb.getID();
        while(c.moveToNext()){
            String id = c.getString(c.getColumnIndex(mydb.TABLE_MOVIE_IMDB));
            tempID.add(id);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_menu_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final MovieDataDefault movieDataDefault = MovieDataDB.movieDataDefaults.get(position);
        holder.movieTitle.setText(movieDataDefault.getMovieName());
        Glide.with(context).load(movieDataDefault.movieImg).into(holder.movieImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMovieDetail = new Intent(context, MovieDetail.class);
                String pos = String.valueOf(position);
                toMovieDetail.putExtra("arr", pos);
                context.startActivity(toMovieDetail);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                tempID = new ArrayList<>();
                mydb = new DBHelper(context);
                checkID();
                boolean isSaved = false;
                if(tempID.size() > 0){
                    for (int i = 0; i < tempID.size(); i++) {
                        if(MovieDataDB.movieDataDefaults.get(position).getMovieImdb().equals(tempID.get(i))){
                            isSaved = true;
                            break;
                        }
                    }
                }
                if(tempID.size() == 0 || !isSaved){
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage("Do you want to save this movie?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String movieImage = movieDataDefault.getMovieImg();
                            String movieTitle = movieDataDefault.getMovieName();
                            String movieYear = movieDataDefault.getMovieYear();
                            String movieImdbID = movieDataDefault.getMovieImdb();
                            boolean isAdded = mydb.insert(movieImage, movieTitle, movieYear, movieImdbID);
                            if(isAdded){
                                Toast.makeText(context, "Movie saved :)", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(context, "Failed to save movie :(", Toast.LENGTH_LONG).show();
                            }
                            notifyDataSetChanged();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.setTitle("Save Movie");
                    alertDialog.show();

                }
                else{
                    Toast.makeText(context, "Movie is saved already!", Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return MovieDataDB.movieDataDefaults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView movieTitle;
        ImageView movieImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            movieImage = itemView.findViewById(R.id.movieImage);
        }
    }

}
