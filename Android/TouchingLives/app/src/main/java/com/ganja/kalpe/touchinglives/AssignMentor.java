package com.ganja.kalpe.touchinglives;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Spinner;

import com.ganja.kalpe.touchinglives.CustomAdapters.CustomAdapter2;
import com.ganja.kalpe.touchinglives.CustomAdapters.CustomAdapter3;
import com.ganja.kalpe.touchinglives.Utils.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AssignMentor extends BaseActivity {

    String[] yearArr={"FE", "SE", "TE", "BE"};

    Spinner mentor;
    ListView mentorsList;

    DatabaseReference mentorsDb;

    ArrayList<String> mentorNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_mentor);

        setupToolbar();
        setTitle("Assign Mentor");

        mentorsDb = FirebaseDatabase.getInstance().getReference("/mentor");

        mentorsList = findViewById(R.id.mentorNames);

        mentorNames = new ArrayList<>();

        mentorsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){

                    for(DataSnapshot snapshot : childSnapshot.getChildren()){

                        if((snapshot.getKey()).equals("name"))
                        mentorNames.add(snapshot.getValue(String.class));
                    }
                }

                mentorsList.setAdapter(new CustomAdapter3(AssignMentor.this, mentorNames));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
