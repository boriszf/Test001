package com.example.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.InvalidScannerNameException;

public class MainActivityTest extends AppCompatActivity {
    private static BarcodeReader barcodeReader;
    private AidcManager manager;
    private Button btnAutomaticBarcode;
    private Button btnClientBarcode;
    private Button btnScannerSelectBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(Build.MODEL.startsWith("VM1A")) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        // create the AidcManager providing a Context and a
        // CreatedCallback implementation.
        AidcManager.create(this, aidcManager -> {
            manager = aidcManager;
            try{
                barcodeReader = manager.createBarcodeReader();
            }
            catch (InvalidScannerNameException e){
                Toast.makeText(MainActivityTest.this, "不正确的扫描器名称异常: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                Toast.makeText(MainActivityTest.this, "异常: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ActivitySetting();
    }

    public void ActivitySetting() {
        btnAutomaticBarcode = findViewById(R.id.buttonAutomaticBarcode);
        btnAutomaticBarcode.setOnClickListener(v -> {
            // get the intent action string from AndroidManifest.xml
            Intent barcodeIntent = new Intent("android.intent.action.AUTOMATICBARCODEACTIVITY");
            startActivity(barcodeIntent);
        });

        btnClientBarcode = findViewById(R.id.buttonClientBarcode);
        btnClientBarcode.setOnClickListener(v -> {
            // get the intent action string from AndroidManifest.xml
            Intent barcodeIntent = new Intent("android.intent.action.CLIENTBARCODEACTIVITY");
            startActivity(barcodeIntent);
        });

        btnScannerSelectBarcode = findViewById(R.id.buttonScannerSelectBarcode);
        btnScannerSelectBarcode.setOnClickListener(v -> {
            // get the intent action string from AndroidManifest.xml
            Intent barcodeIntent = new Intent(
                    "android.intent.action.SCANNERSELECTBARCODEACTIVITY");
            startActivity(barcodeIntent);
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (barcodeReader != null) {
            // close BarcodeReader to clean up resources.
            barcodeReader.close();
            barcodeReader = null;
        }

        if (manager != null) {
            // close AidcManager to disconnect from the scanner service.
            // once closed, the object can no longer be used.
            manager.close();
        }
    }
}