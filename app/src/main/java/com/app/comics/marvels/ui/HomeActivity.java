package com.app.comics.marvels.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.comics.marvels.BuildConfig;
import com.app.comics.marvels.R;
import com.app.comics.marvels.data.network.MarvelApi;
import com.app.comics.marvels.data.network.model.Characters;
import com.app.comics.marvels.data.network.model.Result;
import com.app.comics.marvels.ui.home.characters.CharacterAdapter;
import com.app.comics.marvels.util.Constants;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity implements CharacterAdapter.ItemClickListener {

    private RecyclerView characterRV;
    private String heroId = "";
    private CharacterAdapter adapter;
    private List<Result> resultList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        characterRV = findViewById(R.id.characterRV);
        setUp();
        getCharacters();
    }

    //method for recyclerview
    private void setUp() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        characterRV.setHasFixedSize(true);
        adapter = new CharacterAdapter(resultList);
        characterRV.setLayoutManager(gridLayoutManager);
        characterRV.setAdapter(adapter);
        adapter.setOnItemClicked(this);
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
        Call<Characters> call = marvelApi.getCharacters(timeStamp, Constants.API_KEY, md5, "80");
        call.enqueue(new Callback<Characters>() {
            @Override
            public void onResponse(Call<Characters> call, Response<Characters> response) {
                resultList.addAll(response.body().getData().getResults());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Characters> call, Throwable t) {
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

    @Override
    public void onItemClicked(int position) {
        Result result = resultList.get(position);
        heroId = String.valueOf(result.getId());
        Intent intent = new Intent(this, HeroDetails.class);
        intent.putExtra("heroId", heroId);
        startActivity(intent);
    }
}
