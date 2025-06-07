package com.example.kamusotomotif;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity; // ✅ WAJIB
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView nav = findViewById(R.id.bottom_navigation);

        loadFragment(new HomeFragment());

        nav.setOnItemSelectedListener(item -> {
            Fragment f = null;
            if (item.getItemId() == R.id.nav_home) f = new HomeFragment();
            else if (item.getItemId() == R.id.nav_bookmark) f = new BookmarkFragment();
            else if (item.getItemId() == R.id.nav_about) f = new AboutFragment();
            return loadFragment(f);
        });
    }

    private boolean loadFragment(Fragment f) {
        if (f != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, f).commit();
            return true;
        }
        return false;
    }
}
