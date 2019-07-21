package com.ganja.kalpe.touchinglives;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ganja.kalpe.touchinglives.CustomAdapters.CustomAdapter2;
import com.ganja.kalpe.touchinglives.Utils.BaseActivity;
import com.ganja.kalpe.touchinglives.Utils.MyTextView;
import com.ganja.kalpe.touchinglives.Utils.NonScrollListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AddCentre extends BaseActivity {

    EditText LcenterET, CcenterET;
    Button submitLcentre, submitCcentre;
    NonScrollListView progsList;
    HashMap<String,String> progNames;
    MyTextView progTitle;

    DatabaseReference programDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_centre);

        setupToolbar();
        setTitle("Add A Centre");

        LcenterET= findViewById(R.id.Lcentre);
        CcenterET= findViewById(R.id.Ccenter);
        submitCcentre = findViewById(R.id.submitCCentre);
        submitLcentre = findViewById(R.id.submitLCentre);
        progsList = findViewById(R.id.all_centres);
        progTitle = findViewById(R.id.centreTitle);

        progNames = new HashMap<>();

        programDb = FirebaseDatabase.getInstance().getReference("/centres");

        submitLcentre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = LcenterET.getText().toString().trim();
                if(TextUtils.isEmpty(name)) {
                    shortToast("Enter a name");
                }
                else{
                    programDb.child("learning").push().setValue(name);
                    LcenterET.setText("");
                    shortToast("Added Learning Centre");
                }
            }
        });

        submitCcentre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = CcenterET.getText().toString().trim();
                if(TextUtils.isEmpty(name)) {
                    shortToast("Enter a name");
                }
                else{
                    programDb.child("community").push().setValue(name);
                    CcenterET.setText("");
                    shortToast("Added Community Centre");
                }
            }
        });

        programDb.child("learning").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null) {
                    if(dataSnapshot.getValue() != null) {
                        progNames = (HashMap<String,String>) dataSnapshot.getValue();

                        progsList.setAdapter(new CustomAdapter2(AddCentre.this, new ArrayList<String>(progNames.values()) ));
                    }

                    else{
                        progTitle.setText("No Centres to Display");
                    }
                }
                else{
                    progTitle.setText("No Centres to Display");
                }

                programDb.child("community").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot!=null) {
                            if(dataSnapshot.getValue() != null) {
                                progNames = (HashMap<String,String>) dataSnapshot.getValue();

                                progsList.setAdapter(new CustomAdapter2(AddCentre.this, new ArrayList<String>(progNames.values()) ));
                            }

                            else{
                                progTitle.setText("No Programs to Display");
                            }
                        }
                        else{
                            progTitle.setText("No Programs to Display");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
