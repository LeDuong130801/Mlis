package com.leduongw01.mlis.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.models.StringValue;
import com.leduongw01.mlis.utils.Constant;
import com.leduongw01.mlis.utils.DefaultConfig;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
    ApiService apisService = new Retrofit.Builder().baseUrl(DefaultConfig.serverAddress)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiService.class);
    @GET("testapi/getkey")
    Call<StringValue> getApiKey();
    @GET("api/podcast/getpodcastwithsl?page=1&quantity=4")
    Call<ArrayList<Podcast>> getPodcastWithSl();
}
