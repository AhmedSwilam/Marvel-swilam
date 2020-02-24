package com.app.comics.marvels.data.network;

import com.app.comics.marvels.data.network.model.Characters;
import com.app.comics.marvels.data.network.model.Comics;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by arif on 22/12/17.
 */

public interface MarvelApi {

    //interface for calling api with parameter

    @GET("/v1/public/characters")
    Call<Characters> getCharacters(@Query("ts") String timestamp, @Query("apikey") String apiKey,
                                   @Query("hash") String hash, @Query("limit") String limit);

    @GET("/v1/public/characters")
    Call<Comics> getComics(@Query("ts") String timestamp, @Query("apikey") String apiKey,
                               @Query("hash") String hash, @Query("limit") String limit);
}
