package com.leduongw01.mlis.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leduongw01.mlis.models.StringValue;
import com.leduongw01.mlis.utils.DefaultConfig;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
    ApiService apisService = new Retrofit.Builder().baseUrl(DefaultConfig.serverAddress)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiService.class);
    @GET("testapi/getkey")
    Call<StringValue> getApiKey();
}
