package com.example.allodoc.Medecin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.allodoc.Medecin.Mfolder;
import com.example.allodoc.Medecin.Scaner;
import com.example.allodoc.Medecin.fragment_mhome;
import com.example.allodoc.R;
import com.example.allodoc.Auth.User;
import com.example.allodoc.patient.CompletInfo;
import com.example.allodoc.patient.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MHome extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private User user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhome);
        bottomNavigationView = findViewById(R.id.mbottomNavView);
        frameLayout = findViewById(R.id.mframeLayout);
        loadFragment(new fragment_mhome(), true);
        user = User.getInstance(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                int userId = user.getId();

                if (itemId == R.id.MnavHome) {
                    loadFragment(new fragment_mhome(), false);
                } else if (itemId == R.id.Mnavscan) {
                    loadFragment(new Scaner(), false);
                } else if (itemId == R.id.MnavFolders) {
                    loadFragment(new Mfolder(), false);
                }else if (itemId == R.id.Mfoldershared) {
                    loadFragment(new foldereshared(), false);
                }else{
                    user.getMedecinInformation(userId, new User.UserCallback() {
                        @Override
                        public void onUserReceived() {}
                        @Override
                        public void onPatientNotFound() {}
                        @Override
                        public void onPatientError(String errorMessage) {}
                        @Override
                        public void onPatientReceived(int patientId, String mobile, String birthday, String address, String weight) {}

                        @Override
                        public void onMedecinReceived(int medecinId,String reviews,String fax,String siteweb,String location) {
                            user.setIdm(medecinId);
                            if (reviews == null || fax == null || location == null || siteweb == null) {
                                loadFragment(new MCompletInfo(),false);
                            }else{
                                loadFragment(new MProfileFragment(),false);
                            }
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

    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (isAppInitialized) {
            transaction.add(R.id.mframeLayout, fragment);
        } else {
            transaction.replace(R.id.mframeLayout, fragment);
        }

        transaction.commit();
    }
}
