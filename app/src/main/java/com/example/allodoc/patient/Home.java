package com.example.allodoc.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.allodoc.R;
import com.example.allodoc.User;
import com.example.allodoc.patient.FoldersFragment;
import com.example.allodoc.patient.HomeFragment;
import com.example.allodoc.patient.ProfileFragment;
import com.example.allodoc.patient.ShareFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    private User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView =  findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.frameLayout);
        loadFragement(new HomeFragment(),true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId == R.id.navHome){
                   loadFragement(new HomeFragment(),false);
                } else if (itemId == R.id.navShare) {
                    loadFragement(new ShareFragment(),false);
                } else if (itemId == R.id.navFolders) {
                    loadFragement(new FoldersFragment(),false);
                }else{// nav profile
                    loadFragement(new ProfileFragment(),false);
                }
                return true;
            }
        });

    }
    private  void loadFragement(Fragment fragment, boolean isApInitialized){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction  fragmentTransaction = fragmentManager.beginTransaction();

        if (isApInitialized){
            fragmentTransaction.add(R.id.frameLayout, fragment);
        }else{
            fragmentTransaction.replace(R.id.frameLayout, fragment);
        }


        fragmentTransaction.commit();

    }
}