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
		final Intent myIntent = new Intent(this, MainActivity.class);

		Hawk.init(getApplicationContext()).build();
		if (!Hawk.contains("segundoAcesso")) { // send user to search screen in case of first time using the app
			Hawk.put("segundoAcesso", new Object());
			startActivity(myIntent);
			finish();
		}

		//get movies in cache
		moviesCache = Hawk.get("movies");
		movies = new ArrayList<>();
		if (moviesCache != null) {
			for (String movie : moviesCache) {
				Result r = Hawk.get(movie);
				movies.add(r);
			}
		}

		//set recyclerview
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview);
		recyclerView.setAdapter(new MoviesAdapter(movies, this));
		RecyclerView.LayoutManager layout = new LinearLayoutManager(this,
				LinearLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(layout);

		//set floating button
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id._floatingBtn);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(myIntent);
				finish();
			}
		});
	}
}
