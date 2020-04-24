package com.tptien.tstudy.Service;

import com.tptien.tstudy.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DataService {

    @GET("register.php")
    Call<User> Register(@Query("username")String username, @Query("password") String password,@Query("displayname")String displayName);
    @GET("login.php")
    Call<User> Login(@Query("username") String username, @Query("password") String password);

}
