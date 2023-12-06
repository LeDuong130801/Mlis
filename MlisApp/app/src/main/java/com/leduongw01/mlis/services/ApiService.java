package com.leduongw01.mlis.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leduongw01.mlis.models.Favorite;
import com.leduongw01.mlis.models.MlisUser;
import com.leduongw01.mlis.models.Playlist;
import com.leduongw01.mlis.models.Podcast;
import com.leduongw01.mlis.models.StringValue;
import com.leduongw01.mlis.utils.Constant;
import com.leduongw01.mlis.utils.DefaultConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
    ApiService apisService = new Retrofit.Builder().baseUrl(DefaultConfig.serverAddress)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiService.class);
    @GET("testapi/getkey")
    Call<StringValue> getApiKey();
    @GET("storage/files/{url}")
    @Streaming
    Call<ResponseBody> getFile(
            @Path("url") String url
    );
    @GET("api/podcast/getall")
    Call<ArrayList<Podcast>> getAllPodcast();
    @GET("api/mlis/login")
    Call<MlisUser> login(
            @Query("username") String username,
            @Query("password") String password
    );
    @GET("api/mlis/loginwithtoken")
    Call<MlisUser> loginwithtoken(
                    @Query("username") String username,
                    @Query("token") String token
            );
    @POST("api/mlis/register")
    Call<String> register(
            @Body MlisUser mlisUser
    );
    //playlist
    @GET("api/playlist/getallbyStatus")
    Call<List<Playlist>> getAllByStatus(
            @Query("contentStatus") String status
    );
    @POST("api/favorite/addpodcasttomainfavorite")
    Call<Favorite> addPodcastToMainFavorite(
            @Query("mlisUserId") String mlisUserId,
            @Body List<String> podcastListId
    );
    @PUT("api/favorite/removepodcasttomainfavorite")
    Call<Favorite> removePodcastToMainFavorite(
            @Query("mlisUserId") String mlisUserId,
            @Body List<String> podcastListId
    );
    @GET("api/favorite/getallu")
    Call<List<Favorite>> getAllFavoriteByUserId(
            @Query("user") String userId
    );
}
