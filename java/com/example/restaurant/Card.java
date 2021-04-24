package com.example.restaurant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

@Layout(R.layout.card_view)

public class Card {

    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.nameratings)
    private TextView nameRatings;

    private DatabaseReference mDatabase;

    private final Profile mProfile;
    private final Context mContext;
    private final SwipePlaceHolderView mSwipeView;

    public Card(Context mContext, Profile profile, SwipePlaceHolderView mSwipeView) {

        this.mContext = mContext;
        mProfile = profile;
        this.mSwipeView = mSwipeView;

    }

    @SuppressLint("SetTextI18n")
    @Resolve
    private void onResolved(){
        Glide.with(mContext).load(mProfile.getImageUrl()).into(profileImageView);
        nameRatings.setText(mProfile.getName() + ", " + mProfile.getRatings());
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
    }

    @SwipeIn
    private void onSwipeIn(){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        writeNewUser(mProfile.getName());

    }

    @SwipeInState
    private void onSwipeInState(){
    }

    @SwipeOutState
    private void onSwipeOutState(){
    }

    public void writeNewUser(String rest){

        SetPref setPref = new SetPref(rest);
        mDatabase.child("Pref").child(rest).setValue(setPref);

    }
}

