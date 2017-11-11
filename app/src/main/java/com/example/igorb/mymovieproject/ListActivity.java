package com.example.igorb.mymovieproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igorb on 09/11/2017.
 */

public class ListActivity extends AppCompatActivity {

	private static List<String> moviesCache;
	private static List<Result> movies;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recyclerview);
		setTitle("My IMDB Movie List");

		Hawk.init(getApplicationContext()).build();
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id._floatingBtn);
		moviesCache = Hawk.get("filmes");
		movies = new ArrayList<>();

		//get movies in cache
		if (moviesCache != null) {
			for (String movie : moviesCache) {
				Result r = Hawk.get(movie);
				movies.add(r);
			}
		}

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview);
		recyclerView.setAdapter(new MoviesAdapter(movies, this));
		RecyclerView.LayoutManager layout = new LinearLayoutManager(this,
				LinearLayoutManager.VERTICAL, false);

		recyclerView.setLayoutManager(layout);


		final Intent myIntent = new Intent(this, MainActivity.class);

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(myIntent);
				finish();
			}
		});
	}
}
