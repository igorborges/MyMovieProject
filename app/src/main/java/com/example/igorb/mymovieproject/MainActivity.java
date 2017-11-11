package com.example.igorb.mymovieproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

	public static final String URL = "http://www.omdbapi.com/";
	public static final String APIKEY = "90490335";
	public static String movieName;
	public static String data = "";
	private static TextView movieData;
	private static GetData getData;
	private static EditText EditMovieName;
	private static Result result;
	private static List<String> movies = new ArrayList<>();
	private static boolean isList = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Hawk.init(getApplicationContext()).build();

		//Get data from activity
		movieData = (TextView) findViewById(R.id.data);
		ImageButton btnSearch = (ImageButton) findViewById(R.id.btnSearch);
		EditMovieName = (EditText) findViewById(R.id.movieName);
		final FloatingActionButton btnSave = (FloatingActionButton) findViewById(R.id._floatingBtnSave);


		//Retrofit build with basic URL
		Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
		getData = retrofit.create(GetData.class);

		//Button search onClick
		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				closeKeyboard();
				movieName = EditMovieName.getText().toString().replace(" ", "").toLowerCase();
				if (Hawk.contains(movieName)) {
					Result r = Hawk.get(movieName);
					displayResult(r);
				} else
					loadMovieData();
			}
		});

		//EditText onClick
		EditMovieName.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditMovieName.setText("");
			}
		});

		final Intent myIntent = new Intent(this, ListActivity.class);

		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast toast;
				if (isList) {
					startActivity(myIntent);
				} else {
					if (movieName != null) {
						if (!Hawk.contains(movieName)) {
							movies.add(movieName);
							Hawk.put(movieName, result);
							Hawk.put("filmes", movies);
							btnSave.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_format_list_bulleted_black_24dp));
							EditMovieName.setText("Insira o nome do filme");
							toast = Toast.makeText(getApplicationContext(), "Salvo com sucesso!", Toast.LENGTH_SHORT);
						} else {
							toast = Toast.makeText(getApplicationContext(), "Filme já cadastrado!", Toast.LENGTH_SHORT);
						}
					} else {
						toast = Toast.makeText(getApplicationContext(), "Insira o nome do filme!", Toast.LENGTH_SHORT);
					}
					toast.show();
				}
			}
		});
		
	}

	private void loadMovieData() {
		//Send data to the API
		Call<Result> call = getData.get(movieName, APIKEY);
		call.enqueue(new Callback<Result>() {
			@Override
			public void onResponse(Call<Result> call, Response<Result> response) {
				Result result = response.body();
				displayResult(result);
			}

			@Override
			public void onFailure(Call<Result> call, Throwable t) {

			}
		});
	}

	private void displayResult(Result r) {
		if (r != null) {
			result = r;
			data = "";
			data += "<b>Name:</b> " + r.getTitle() + "<br/>";
			data += "<b>Year:</b> " + r.getYear() + "<br/>";
			data += "<b>Director:</b> " + r.getDirector() + "<br/>";
			data += "<b>Genre:</b> " + r.getGenre() + "<br/>";
			data += "<b>IMDB rate:</b> " + r.getImdbRating() + "<br/>";

			new DownloadImageTask((ImageView) findViewById(R.id.moviePoster))
					.execute(r.getPoster());

			movieData.setText(Html.fromHtml(data));
		} else {
			new DownloadImageTask((ImageView) findViewById(R.id.moviePoster)).execute("http://memeshappen.com/download.php?memeid=82985");
			movieData.setText("Wrong Movie Name");
		}
	}

	private void closeKeyboard() {
		InputMethodManager inputManager = (InputMethodManager)
				getSystemService(Context.INPUT_METHOD_SERVICE);

		inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}
}
