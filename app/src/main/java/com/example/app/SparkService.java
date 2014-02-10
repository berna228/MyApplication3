package com.example.app;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface SparkService {
    @GET("/v1/devices/{device_id}/{SensorType}")
    void getSensorData(@Path("device_id") String DeviceID,
                       @Path("SensorType") String SensorType,
                       @Query("access_token") String token,
                       Callback<SparkCoreData> sensorData);
}
