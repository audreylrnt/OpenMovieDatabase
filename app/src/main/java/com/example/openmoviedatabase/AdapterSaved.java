package com.example.openmoviedatabase;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class AdapterSaved extends RecyclerView.Adapter<AdapterSaved.ViewHolder> {
    Context context;
    DBHelper mydb;

    public AdapterSaved(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.main_menu_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        MovieDataDefault movieDataDefault = AdapterDB.movieDataDefaults.get(position);
        final String title = movieDataDefault.getMovieName();
        final String year = movieDataDefault.getMovieYear();
        final String imdb = movieDataDefault.getMovieImdb();
        final String img = movieDataDefault.getMovieImg();
        holder.movieTitle.setText(movieDataDefault.getMovieName());
        Glide.with(context).load(img).placeholder(R.drawable.ic_launcher_foreground).into(holder.movieImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDataDB.movieDataDefaults.clear();
                MovieDataDefault movieDataDefault = new MovieDataDefault(title, year, imdb, img);
                MovieDataDB.movieDataDefaults.add(movieDataDefault);
                Intent toMovieDetail = new Intent(context, MovieDetail.class);
                String pos = String.valueOf(0);
                toMovieDetail.putExtra("arr", pos);
                context.startActivity(toMovieDetail);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mydb = new DBHelper(context);
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("Do you want to delete your saved movie?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int del = mydb.delete(imdb);
                        if(del > 0){
                            notifyDataSetChanged();
                            Toast.makeText(context, "Movie deleted", Toast.LENGTH_LONG).show();
                            AdapterDB.movieDataDefaults.remove(position);
                            notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(context, "Failed to delete movie", Toast.LENGTH_LONG).show();
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
                alertDialog.setTitle("Delete Movie");
                alertDialog.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return AdapterDB.movieDataDefaults.size();
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