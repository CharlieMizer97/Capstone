package com.example.restaurant;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {

    private ListView listView;
    ArrayList<String> arrayList;
    DatabaseReference ref;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ImageView rndImg = (ImageView) findViewById(R.id.roundedimag);
        Picasso.with(getApplicationContext())
                .load("https://static.turbosquid.com/Preview/2019/10/10__15_03_28/miniature.png0573B418-C497-4BA4-B13D-7DA3A4504A2DLarge.jpg")
                .into(rndImg);

        listView = findViewById(R.id.dtaPref);
        arrayList = new ArrayList<String>();
        callPref();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ref = FirebaseDatabase.getInstance().getReference().child("Pref").child(arrayList.get(position));
                ref.removeValue();
                arrayList.remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });


    }

    private void callPref(){

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayList);
        listView.setAdapter(adapter);
        ref = FirebaseDatabase.getInstance().getReference().child("Pref");
        ValueEventListener evntList = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){

                   sender(ds.child("Name").getValue(String.class));

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        ref.addListenerForSingleValueEvent(evntList);

    }

    private void sender(String temp){

        Log.i("", String.valueOf(arrayList));

        arrayList.add(temp);
        adapter.notifyDataSetChanged();

    }
}