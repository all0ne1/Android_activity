package ru.mirea.andaev.mireaproject.ui.lesson8.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OSRMService {
    @GET("route/v1/car/{startLon},{startLat};{endLon},{endLat}?overview=full&geometries=geojson")
    Call<OSRMResponse> getRoute(
            @Path("startLon") double startLon, @Path("startLat") double startLat,
            @Path("endLon") double endLon, @Path("endLat") double endLat
    );
}