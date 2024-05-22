package com.example.myapplication.nettools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebAPI {
    private static final OkHttpClient client=new OkHttpClient.Builder().build();

    private static Gson GetMyGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> {
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
            try {
                return f.parse(json.getAsJsonPrimitive().getAsString());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        });
        return builder.create();
    }
    private static final Retrofit retrofit=new Retrofit.Builder()
            .baseUrl("http://www.miliotech.com:8886/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GetMyGson()))
            //.addConverterFactory(GsonConverterFactory.create())
            .build();
    private static final ApiService apiService = retrofit.create(ApiService.class);

    public static ApiService GetApiService()
    {
        return apiService;
    }
}
