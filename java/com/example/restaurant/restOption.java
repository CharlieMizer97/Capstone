package com.example.restaurant;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class restOption extends AppCompatActivity {

    home_page homePage;
    String rest;
    private DatabaseReference mDataBase;

    private Double userLong = 0.0;
    private Double userLat = 0.0;

    double tempLat = 0.0;
    double tempLot = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_option);

        TextView resT = (TextView) findViewById(R.id.RestN);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                userLong = location.getLongitude();
                userLat = location.getLatitude();
                Log.i("", "onLocationChanged: " + userLong);
                Log.i("", "onLocationChanged: " + userLat);

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        homePage = new home_page();
        this.rest = homePage.rest;
        callDataRest();

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uri = "http://maps.google.com/maps?saddr=" + userLat + "," + userLong + "&daddr=" + tempLat + "," + tempLot;
                //String uri = String.format(Locale.ENGLISH, "geo:0,0?q=1265+W+Colton+Ave%2CRedlands");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);

            }
        });


    }

    public void callDataRest(){
        List<Double> ratingName = new ArrayList<>();
        List<Double> reviewName = new ArrayList<>();
        List<Double> longName = new ArrayList<>();
        List<Double> latName = new ArrayList<>();
        List<String> priceName = new ArrayList<>();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dataRest = mDataBase.child("Restaurants");
        ValueEventListener evntList = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){

                    Double temp1 = ds.child("ratings").getValue(Double.class);
                    ratingName.add(temp1);

                    Double temp2 = ds.child("reviews").getValue(Double.class);
                    reviewName.add(temp2);

                    Double temp3 = ds.child("longitude").getValue(Double.class);
                    longName.add(temp3);

                    Double temp4 = ds.child("latitude").getValue(Double.class);
                    latName.add(temp4);

                    String temp5 = ds.child("price").getValue(String.class);
                    priceName.add(temp5);

                }

                setRestInfo(ratingName, reviewName, longName, latName, priceName);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        dataRest.addListenerForSingleValueEvent(evntList);
    }

    private void setRestInfo(List<Double> ratingName, List<Double> reviewName, List<Double> longName, List<Double> latName, List<String> priceName) {

        TextView rest01, ratings, review, dist, pcn;
        rest01 = findViewById(R.id.RestN);
        ratings = findViewById(R.id.Rtng);
        review = findViewById(R.id.Rview);
        dist = findViewById(R.id.Mles);
        pcn = findViewById(R.id.Prce);

        tempLat = latName.get(0);
        tempLot = longName.get(0);

        Log.i("", this.rest + "");
        ratings.setText(String.valueOf(ratingName.get(0)));
        review.setText(String.valueOf(reviewName.get(0)));
        dist.setText(String.valueOf(distance(this.userLat, this.userLong, latName.get(0), longName.get(0))));
        pcn.setText(priceName.get(0));


    }

    private double distance(double lat1, double lon1, double lat2, double lon2){

        double theta = lon1 - lon2;
        double dist = Math.sin(degRad(lat1))
                *Math.sin(degRad(lat2))
                +Math.cos(degRad(lat1))
                *Math.cos(degRad(lat2))
                *Math.cos(degRad(theta));
        dist = Math.acos(dist);
        dist = radDeg(dist);
        dist = dist * 60 * 1.1515;


        return Math.round((dist / 1609.344) * 100.0) / 100.0;

    }
    private double radDeg(double dist) {
        return (dist * 180.0 / Math.PI);
    }

    private double degRad(double latlon) {
        return (latlon * 180.0 / Math.PI);
    }
}