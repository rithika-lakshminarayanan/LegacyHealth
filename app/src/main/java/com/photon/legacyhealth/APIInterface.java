package com.photon.legacyhealth;

import com.photon.legacyhealth.pojo.FeelingsData;
import com.photon.legacyhealth.pojo.MetaDataResponse;
import com.photon.legacyhealth.pojo.NeighborhoodData;
import com.photon.legacyhealth.pojo.NeighborhoodDataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("metadata")
    Call<MetaDataResponse> metadata();
    @GET("neighborhood")
    Call<NeighborhoodData> neighborhood(@Query("lat") String latitude, @Query("lon") String longitude);
    @GET("feelings")
    Call<FeelingsData> feelings(@Query("lat") String latitude, @Query("lon") String longitude, @Query("feelingIds") int id);
}
