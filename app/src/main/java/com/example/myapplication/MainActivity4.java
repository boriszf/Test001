package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityMain4Binding;

public class MainActivity4 extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMain4Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain4Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setSupportActionBar(binding.appBarMain.toolbar);

        //DrawerLayout drawer = binding.drawerLayout;

        BottomNavigationView navigationView = binding.appBarMain.navView;


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //mAppBarConfiguration = new AppBarConfiguration.Builder(
        ////        R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
        //        .setOpenableLayout(drawer)
        //        .build();

        //NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        NavigationUI.setupWithNavController(navigationView, navController);
    }
}