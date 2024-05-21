package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.models.MyUser;
import com.example.myapplication.models.ReturnData;
import com.example.myapplication.models.User;
import com.example.myapplication.nettools.WebAPI;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String Tag="MainActivity";
    Button myButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        myButton = findViewById(R.id.button);
        myButton.setOnClickListener(v -> {
            //if(myButton.getText().toString().equals("Button")){
                //myButton.setEnabled(false);
                myButton.setText("按钮被单击了");

                User user=new User();
                user.setAccount("admin");
                user.setPassWord("qdnyncj9690");
                Call<ReturnData<MyUser>> call= WebAPI.GetApiService().getUser(user);
                call.enqueue(new Callback<ReturnData<MyUser>>() {
                    @Override
                    public void onResponse(Call<ReturnData<MyUser>> call, Response<ReturnData<MyUser>> response) {
                        if(response.isSuccessful())
                        {
                            ReturnData<MyUser> data=response.body();
                            if(data != null) {
                                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
                                myButton.setText(f.format(data.getData().get(0).getFScTime()));
                                return;
                            }
                        }
                        Toast.makeText(getApplicationContext(),"Toast",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ReturnData<MyUser>> call, Throwable throwable) {
                        Log.e(Tag,throwable.getMessage());
                    }
                });
            //}
        });
    }
}