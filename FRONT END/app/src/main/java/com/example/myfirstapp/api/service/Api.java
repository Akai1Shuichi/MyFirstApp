package com.example.myfirstapp.api.service;

import com.example.myfirstapp.api.model.Message;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static ApiInterface getClient() {
        ApiInterface api = retrofit.create(ApiInterface.class);
        return api ;
    }

    public static Message parserError(Response<?> response) {
        Converter<ResponseBody, Message> converter =
                retrofit.responseBodyConverter(Message.class,new Annotation[0]);
        Message error ;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new Message();
        }
        return error;
    }
}
