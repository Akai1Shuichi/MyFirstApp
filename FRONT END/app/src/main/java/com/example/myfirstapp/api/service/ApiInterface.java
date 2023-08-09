package com.example.myfirstapp.api.service;

import com.example.myfirstapp.api.model.Message;
import com.example.myfirstapp.api.model.Token;
import com.example.myfirstapp.api.model.User;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("/user")
    Call<Message> createUser(@Field("name") String name,
                           @Field("email") String email,
                           @Field("password") String password) ;

    @FormUrlEncoded
    @POST("/user/login")
    Call<Token> loginUser(@Field("email") String email,
                         @Field("password") String password);

    @FormUrlEncoded
    @POST("/user/checkpass")
    Call<Message> checkPass(@Header("Authorization") String authToken,
                            @Field("password") String password);

    @GET("/user/you")
    Call<User> getUser(@Header("Authorization") String authToken);


    @PATCH("/user/you")
    Call<Message> updateUser(@Header("Authorization") String authToken,
                             @Body User user);



    @POST("/user/logout")
    Call<Message> logoutUser(@Header("Authorization") String authToken);
}
