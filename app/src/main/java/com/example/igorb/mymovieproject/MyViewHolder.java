package com.example.igorb.mymovieproject;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by igorb on 10/11/2017.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

	final TextView movieTitle;
	final TextView movieData;
	final ImageView moviePoster;

	public MyViewHolder(View itemView) {
		super(itemView);
		movieData = (TextView) itemView.findViewById(R.id.movieData);
		movieTitle = (TextView) itemView.findViewById(R.id.movieTitle);
		moviePoster = (ImageView) itemView.findViewById(R.id.moviePoster);
	}
}
