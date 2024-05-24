package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.models.MyUser;
import com.example.myapplication.models.ReturnData;
import com.example.myapplication.models.User;
import com.example.myapplication.nettools.AutoUpdater;
import com.example.myapplication.nettools.WebAPI;
import com.example.myapplication.ui.main.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity{

    ActivityMainBinding binding;
    private MainViewModel mViewModel;
    private static final String Tag="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());//R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.button.setOnClickListener(v->myButton_click(v));
        binding.button1.setOnClickListener(v->myButton1_click(v));
        binding.button2.setOnClickListener(v->myButton2_click(v));
        binding.button3.setOnClickListener(v->myButton3_click(v));
        binding.button4.setOnClickListener(v->myButton4_click(v));
        binding.button5.setOnClickListener(v->myButton5_click(v));
        mViewModel.getLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                TextView mtextview=findViewById(R.id.mtextview);
                mtextview.setText(s);
            }
        });
        //软件更新
        //MyUpdate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Thread thread=new Thread(()-> {
            while (true)
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Date date=new Date();
                mViewModel.getLiveData().postValue(String.valueOf(date.getTime()));
        }
        });
        thread.start();
    }

    public void myButton_click(View view)
    {
        //if(myButton.getText().toString().equals("Button")){
        //myButton.setEnabled(false);
        Button myButton=(Button)view;
        myButton.setText("按钮被单击了");

        User user=new User();
        user.setAccount("admin");
        user.setPassWord("qdnyncj9690");
        Call<ReturnData<MyUser>> call= WebAPI.GetApiService().getUser(user);
        call.enqueue(new Callback<ReturnData<MyUser>>() {
            @Override
            public void onResponse(@NonNull Call<ReturnData<MyUser>> call, @NonNull Response<ReturnData<MyUser>> response) {
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
    }
    public void myButton1_click(View view)
    {
        Intent intent = new Intent(this, MainActivityTest.class);
        startActivity(intent);
    }
    public void myButton2_click(View view)
    {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
    public void myButton3_click(View view)
    {
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);
    }
    public void myButton4_click(View view)
    {
        Intent intent = new Intent(this, MainActivity3.class);
        intent.putExtra("mykey",1);
        startActivity(intent);
    }
    public void myButton5_click(View view)
    {
        Intent intent = new Intent(this, MainActivity4.class);
        startActivity(intent);
    }
    private void MyUpdate()
    {
        //检查更新
        try {
            //6.0才用动态权限
            if (Build.VERSION.SDK_INT >= 23) {
                String[] permissions = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.INTERNET};
                List<String> permissionList = new ArrayList<>();
                for (int i = 0; i < permissions.length; i++) {
                    if (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                        permissionList.add(permissions[i]);
                    }
                }
                if (permissionList.size() <= 0) {
                    //说明权限都已经通过，可以做你想做的事情去
                    //自动更新
                    AutoUpdater manager = new AutoUpdater(MainActivity.this);
                    manager.CheckUpdate();
                } else {
                    //存在未允许的权限
                    ActivityCompat.requestPermissions(this, permissions, 100);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, "自动更新异常：" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean haspermission = false;
        if (100 == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    haspermission = true;
                }
            }
            if (haspermission) {
                //跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
                permissionDialog();
            } else {
                //全部权限通过，可以进行下一步操作
                AutoUpdater manager = new AutoUpdater(MainActivity.this);
                manager.CheckUpdate();
            }
        }
    }

    AlertDialog alertDialog;

    //打开手动设置应用权限
    private void permissionDialog() {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this)
                    .setTitle("提示信息")
                    .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();
                            Uri packageURI = Uri.parse("package:" + getPackageName());
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();
                        }
                    })
                    .create();
        }
        alertDialog.show();
    }

    private void cancelPermissionDialog() {
        alertDialog.cancel();
    }
}