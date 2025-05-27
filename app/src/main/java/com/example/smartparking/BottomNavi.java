package com.example.smartparking;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.smartparking.databinding.ActivityBottomNaviBinding;

public class BottomNavi extends BaseActivity {
    private ActivityBottomNaviBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BottomNavi", "onCreate");
        binding = ActivityBottomNaviBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.framelayout);

        if (navHostFragment == null) {
            Log.e("BottomNaviActivity", "NavHostFragment not found! Check your FragmentContainerView and nav_graph.xml");
            return;
        }
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavi, navController);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
