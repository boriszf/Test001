package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.databinding.ActivityMain3Binding;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.ui.main.BlankFragment;
import com.example.myapplication.ui.main.ItemFragment;
import com.example.myapplication.ui.main.MainFragment;

public class MainActivity3 extends AppCompatActivity {

    ActivityMain3Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain3Binding.inflate(getLayoutInflater());
        Intent intent = getIntent();
        int data = intent.getIntExtra("mykey",0);
        if(data == 1)
        {
            binding.container.removeAllViews();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            ItemFragment itemFragment = new ItemFragment();
            transaction.add(R.id.container, itemFragment);
            transaction.commit();
        }
        else {
            binding.btnShowFragment1.setOnClickListener(view -> {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                MainFragment mainFragment = new MainFragment();
                transaction.add(R.id.fragment_container, mainFragment);
                transaction.commit();
            });
            binding.btnShowFragment2.setOnClickListener(view -> {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                BlankFragment mainFragment = new BlankFragment();
                transaction.add(R.id.fragment_container, mainFragment);
                transaction.commit();
            });
        }
        setContentView(binding.getRoot());//setContentView(R.layout.activity_main3);
    }
}