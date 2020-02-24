package com.app.comics.marvels.ui.details;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.comics.marvels.BuildConfig;
import com.app.comics.marvels.R;
import com.app.comics.marvels.data.network.MarvelApi;
import com.app.comics.marvels.data.network.model.Comics;
import com.app.comics.marvels.util.Constants;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity {

    //this is second activity for marvel hero details

    private RecyclerView characterRV;
    private String heroId = "";
    private DetailsAdapter adapter;
    private List<Comics> resultList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        characterRV = findViewById(R.id.rec_comics);
        setUp();
        getCharacters();
    }

    //method for recyclerview
    private void setUp() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3,
                RecyclerView.HORIZONTAL, false);
        characterRV.setHasFixedSize(true);
        adapter = new DetailsAdapter(resultList);
        characterRV.setLayoutManager(gridLayoutManager);
        characterRV.setAdapter(adapter);
    }

    //calling api using okhttp
    private void getCharacters() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(new StethoInterceptor());
        }

        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String timeStamp = new Date().getTime() + "";
        String md5 = md5(timeStamp + Constants.PRIVATE_KEY + Constants.API_KEY);
        MarvelApi marvelApi = retrofit.create(MarvelApi.class);
        Call<Comics> call = marvelApi.getComics(timeStamp, Constants.API_KEY, md5, "80");
        call.enqueue(new Callback<Comics>() {
            @Override
            public void onResponse(Call<Comics> call, Response<Comics> response) {
                resultList.addAll(Collections.singleton(response.body()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Comics> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //method to get parameter hash for api
    public String md5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }


}
