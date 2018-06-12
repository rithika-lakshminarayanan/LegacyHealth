package com.photon.legacyhealth;

import com.photon.legacyhealth.pojo.FeelingTips;
import com.photon.legacyhealth.pojo.FeelingsData;
import com.photon.legacyhealth.pojo.MetaDataResponse;
import com.photon.legacyhealth.pojo.NeighborhoodData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface APIInterface {

   @Headers({"X-App-DeviceType:android",
             "X-App-Locale:en-US",
             "X-App-Version:2.0",
             "Content-Type:application/json",
             "X-App-Id:LH_PORTLAND"})
    @GET("metadata")
    Call<MetaDataResponse> metadata();
 @Headers({"X-App-DeviceType:android",
         "X-App-Locale:en-US",
         "X-App-Version:2.0",
         "Content-Type:application/json",
         "X-App-Id:LH_PORTLAND"})
    @GET("neighborhood")
    Call<NeighborhoodData> neighborhood(@Query("lat") String latitude, @Query("lon") String longitude);
 @Headers({"X-App-DeviceType:android",
         "X-App-Locale:en-US",
         "X-App-Version:2.0",
         "Content-Type:application/json",
         "X-App-Id:LH_PORTLAND"})
    @GET("feelings")
    Call<FeelingsData> feelings(@Query("lat") String latitude, @Query("lon") String longitude, @Query("feelingIds") int id);
 @Headers({"X-App-DeviceType:android",
         "X-App-Locale:en-US",
         "X-App-Version:2.0",
         "Content-Type:application/json",
         "X-App-Id:LH_PORTLAND"})
    @GET("feelings/{feelingId}")
    Call<FeelingTips> feelingTips(@Query("feelingId") int id);
}
