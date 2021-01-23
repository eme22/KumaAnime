package com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.io;


import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_details.AnimeDetails;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_list.AnimeList;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_ranking.AnimeRanking;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_season.AnimeSeason;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_sugestions.AnimeSugestions;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.anime_update.AnimeUpdate;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.authentication.AuthenticationResponse;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.userdata.Userdata;
import com.eme22.kumaanime.AppUtils.AnimeList_Integration.api.data.userlist.UserList;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyAnimeListAPIService {

    @GET("v2/anime")
    Call<AnimeList> getanimelist(
            @Query("q") String query,
            @Query("limit") Integer limit,
            @Query("offset") Integer offset

    );

    @GET("v2/anime/{anime_id}")
    Call<AnimeDetails> getanimedetails(
            @Path("anime_id") Integer id,
            @Query("fields") String fields
    );

    @GET("v2/anime/ranking")
    Call<AnimeRanking> getranking(
            @Query("ranking_type") String type,
            @Query("limit") Integer limit,
            @Query("offset") Integer offset,
            @Query("fields") String fields
    );


    @GET("v2/anime/season/{year}/{season}")
    Call<AnimeSeason> getseasonalanime(
            @Path("year") Integer year,
            @Path("season") String season,
            @Query("sort") String sort,
            @Query("limit") Integer limit,
            @Query("offset") Integer offset,
            @Query("fields") String fields
    );

    @GET("v2/anime/suggestions")
    Call<AnimeSugestions> getsugestedanime(
            @Query("limit") Integer limit,
            @Query("offset") Integer offset,
            @Query("fields") String fields
    );

    @FormUrlEncoded
    @PATCH("v2/anime/{anime_id}/my_list_status")
    Call<AnimeUpdate> updatemyanime(
            @Path("anime_id") Integer id,
            @Field("status") String status,
            @Field("is_rewatching") Boolean isRewatching,
            @Field("score") Integer score,
            @Field("num_watched_episodes") Integer watched,
            @Field("priority") Integer priority,
            @Field("num_times_rewatched") Integer times,
            @Field("rewatch_value") Integer rewatch,
            @Field("tags") String tags,
            @Field("comments") String comments
    );

    @DELETE("v2/anime/{anime_id}/my_list_status")
    Call<Void> deletemyanime(@Path("anime_id") int animeid);

    @GET("v2/users/{user_name}/animelist")
    Call<UserList> getuseranimelist(
            @Path("user_name") String user,
            @Query("status") String status,
            @Query("sort") String sort,
            @Query("limit") Integer limit,
            @Query("offset") Integer offset
    );

    
    @GET("v2/users/{user_name}")
    Call<Userdata> getuserinfo(
            @Path("user_name") String user,
            @Query("fields") String fields
    );

    /*
    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "accept-encoding: gzip, deflate",
    })
    @POST("v1/oauth2/token")
    Call<AuthenticationResponse> login(
            @Query("client_id") String clientid,
            @Query("grant_type") String grant_type,
            @Query("code") String OAuth,
            @Query("code_verifier") String verifier2
    );

    @Headers({
            "Content-Type: application/x-www-form-urlencoded",
            "accept-encoding: gzip, deflate",
    })
    @POST("v1/oauth2/token")
    Call<AuthenticationResponse> refresh(
            @Query("client_id") String clientid,
            @Query("grant_type") String grant_type,
            @Query("refresh_token") String refresh_token
    );
    */

    @FormUrlEncoded
    @Headers("Cache-Control: no-cache")
    @POST("v1/oauth2/token")
    Call<AuthenticationResponse> login(@Header("Accept") String accept,
                                         @Header("Content-Type") String contentType,
                                         @Field("client_id") String clientId,
                                         @Field("grant_type") String grantType,
                                         @Field("code") String OAuth,
                                       @Field("code_verifier") String verifier2);

    @FormUrlEncoded
    @Headers("Cache-Control: no-cache")
    @POST("v1/oauth2/token")
    Call<AuthenticationResponse> refresh(@Header("Accept") String accept,
                                         @Header("Content-Type") String contentType,
                                         @Field("client_id") String clientId,
                                         @Field("grant_type") String grantType,
                                         @Field("refresh_token") String refreshToken);
}



