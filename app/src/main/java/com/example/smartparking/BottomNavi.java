package com.example.smartparking;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartparking.databinding.ActivityBottomNaviBinding;
import com.example.smartparking.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class BottomNavi extends AppCompatActivity {
ActivityBottomNaviBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BottomNavi","onCreate");
        binding = ActivityBottomNaviBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.bottomNavi.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home){
                replaceFragment(new HomeFragment());
            }
            if(item.getItemId() == R.id.list){
                replaceFragment(new TicketFragment());
            }
        return true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment);
        fragmentTransaction.commit();
    }
}