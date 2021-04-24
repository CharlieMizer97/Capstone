package com.example.restaurant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class home_page extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Context context;
    private DatabaseReference mDataBase;
    public String rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Intent intent = new Intent(this, restOption.class);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.navi_guide);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        callDataRest();

        Button generate = findViewById(R.id.gen);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callDataRest();

            }
        });

        TextView rest01 = (TextView) findViewById(R.id.Rest01);
        TextView rest02 = (TextView) findViewById(R.id.Rest02);
        TextView rest03 = (TextView) findViewById(R.id.Rest03);
        TextView restN = (TextView) findViewById(R.id.RestN);
        rest01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    setRest(rest01.getText().toString());
                    startActivity(intent);
            }
        });

        rest02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRest(rest02.getText().toString());
                    startActivity(intent);
            }
        });

        rest03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    setRest(rest03.getText().toString());
                    startActivity(intent);
            }
        });

    }

    public void setRestInfo(List<String> restList){

        List<Integer> num = new ArrayList<Integer>();

        TextView rest01 = findViewById(R.id.Rest01);

        TextView rest02 = findViewById(R.id.Rest02);

        TextView rest03 = findViewById(R.id.Rest03);

        for (int i = 0; i < restList.size(); i++){

            num.add(i);

        }
        Collections.shuffle(num);

        if (restList.isEmpty()){

            Log.i("", "List is Empty");

        }else {

            rest01.setText(restList.get(num.get(0)));

            rest02.setText(restList.get(num.get(1)));

            rest03.setText(restList.get(num.get(2)));
        }

    }

    public void callDataRest(){
        List<String> restName = new ArrayList<String>();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dataRest = mDataBase.child("Restaurants");
        ValueEventListener evntList = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){

                    String rest = ds.child("name").getValue(String.class);
                    restName.add(rest);
                }

                setRestInfo(restName);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        dataRest.addListenerForSingleValueEvent(evntList);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.navi_guide);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.naviview_prfle){

            Intent intent = new Intent(this, UserProfile.class);
            startActivity(intent);

        }else if (id == R.id.naviview_prefen){

            Intent intent = new Intent(this, MainActivityPref.class);
            startActivity(intent);

        }else if (id == R.id.naviview_faq){

        }else if (id == R.id.naviview_set){

        }else if (id == R.id.naviview_logout){

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }else if (id == R.id.naviview_exit){

            System.exit(0);

        }
        DrawerLayout drawer = findViewById(R.id.navi_guide);
        drawer.closeDrawer(GravityCompat.START);

        return false;
    }

    public void setRest(String rest){

        this.rest = rest;


    }

}