package com.example.igorb.mymovieproject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by igorb on 08/11/2017.
 */

public interface GetData {

	@GET("/")
	Call<Result> get(@Query("t") String title,
					 @Query("apikey") String apikey);

}
