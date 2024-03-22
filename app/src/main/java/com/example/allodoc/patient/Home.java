package com.example.allodoc.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.allodoc.R;
import com.example.allodoc.Auth.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    private User user ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView =  findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.frameLayout);
        loadFragement(new HomeFragment(),true);
        user = User.getInstance(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                int userId = user.getId();

                if(itemId == R.id.navHome){
                   loadFragement(new HomeFragment(),false);
                } else if (itemId == R.id.navShare) {
                    loadFragement(new ShareFragment(),false);
                } else if (itemId == R.id.navFolders) {
                    loadFragement(new FoldersFragment(),false);
                }else{// nav profile
                    user.getPatientInformation(userId, new User.UserCallback() {
                        @Override
                        public void onUserReceived() {
                        }

                        @Override
                        public void onPatientNotFound() {
                        }

                        @Override
                        public void onPatientError(String errorMessage) {
                            Log.d("Patient error", "Error getting a patient");
                        }
                        @Override
                        public void onPatientReceived(int patientId, String mobile, String birthday, String address, String weight) {
                            user.setIdp(patientId);
                            if (mobile == null || birthday == null || address == null || weight == null) {
                                loadFragement(new CompletInfo(),false);
                            }else{
                                loadFragement(new ProfileFragment(),false);
                            }
                        }

                        @Override
                        public void onMedecinReceived(int medecinId,String mobile,String fax,String siteweb,String location) {

                        }

                        @Override
                        public void onMedecinError(String errorMessage) {

                        }
                    });
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