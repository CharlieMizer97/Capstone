package com.example.restaurant;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationManager locationManager;
    String provider;
    private EditText eUser, pUser;
    private Login send;
    private ListView obj;
    private DatabaseReference mDataBase;
    public Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);


        eUser = findViewById(R.id.userName);
        pUser = findViewById(R.id.passUser);
        Button loginBtn = findViewById(R.id.btnLogin);
        intent = new Intent(this, home_page.class);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkIfValid(eUser, pUser);
            }
        });

    }

    private void checkIfValid(EditText eUser, EditText pUser) {

        String user = eUser.getText().toString();
        String pass = pUser.getText().toString();

        mDataBase = FirebaseDatabase.getInstance().getReference("users/");

        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();
                for (String key : dataMap.keySet()){

                    Object data = dataMap.get(key);

                    if (key.equals(user)){

                        try{

                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            Log.i("", userData.get("password") + ":");
                            if (String.valueOf(userData.get("password")).equals(pass)){

                                startActivity(intent);


                            }

                        }catch (ClassCastException cce){

                        }

                    }
                    else{

                        User temp = new User(user, pass);
                        mDataBase.child(user).setValue(temp);

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}