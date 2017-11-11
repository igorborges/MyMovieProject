package com.example.igorb.mymovieproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.InputStream;
import java.util.List;

import static android.view.View.GONE;
import static com.example.igorb.mymovieproject.MainActivity.data;
import static com.example.igorb.mymovieproject.R.id.progressBar;

/**
 * Created by igorb on 10/11/2017.
 */

class MoviesAdapter extends RecyclerView.Adapter {

	private List<Result> movies;
	private Context context;

	public MoviesAdapter(List<Result> movies, Context context) {
		this.movies = movies;
		this.context = context;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View view = LayoutInflater.from(context).inflate(R.layout.activity_recyclerview_item, parent, false);
		MyViewHolder myViewHolder = new MyViewHolder(view);
		return myViewHolder;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		MyViewHolder mHolder = (MyViewHolder) holder;
		Result movie = movies.get(position);

		String data = "";
		data += "<b>Year:</b> " + movie.getYear() + "<br/>";
		data += "<b>Director:</b> " + movie.getDirector() + "<br/>";
		data += "<b>Genre:</b> " + movie.getGenre() + "<br/>";
		data += "<b>IMDB rate:</b> " + movie.getImdbRating() + "<br/>";

		mHolder.movieTitle.setText(movie.getTitle());
		mHolder.movieData.setText(Html.fromHtml(data));

		new DownloadImageTask(mHolder).execute(movie.getPoster());

	}

	@Override
	public int getItemCount() {
		return movies != null ? movies.size() : 0;
	}

	public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(MyViewHolder holder) {
			this.bmImage = holder.moviePoster;
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
