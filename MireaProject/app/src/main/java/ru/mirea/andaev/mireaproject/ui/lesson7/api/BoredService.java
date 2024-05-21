package ru.mirea.andaev.mireaproject.ui.lesson7.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BoredService {
    @GET("api/activity/")
    Call<BoredResponse> getActivityToDo();

}
