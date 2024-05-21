package com.example.myapplication.nettools;

import com.example.myapplication.models.MyUser;
import com.example.myapplication.models.ReturnData;
import com.example.myapplication.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/QingdaoServerInterface/api/User/AppletUserInfo")
    Call<ReturnData<MyUser>> getUser(@Body User user);
}
