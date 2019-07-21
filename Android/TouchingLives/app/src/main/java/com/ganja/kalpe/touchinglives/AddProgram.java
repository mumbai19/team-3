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

public class AddProgram extends BaseActivity {

    EditText progNameET;
    Button submitButton;
    NonScrollListView progsList;
    HashMap<String,String> progNames;
    MyTextView progTitle;

    DatabaseReference programDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_program);

        setupToolbar();
        setTitle("Add a program");

        progNameET = findViewById(R.id.prog_name);
        submitButton = findViewById(R.id.submitProg);
        progsList = findViewById(R.id.all_progs);
        progTitle = findViewById(R.id.progTitle);

        progNames = new HashMap<>();

        programDb = FirebaseDatabase.getInstance().getReference("/programs");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = progNameET.getText().toString().trim();
                if(TextUtils.isEmpty(name)) {
                    shortToast("Enter a name");
                }
                else{
                    programDb.push().setValue(name);
                    progNameET.setText("");
                    shortToast("Added Program");
                }
            }
        });

        programDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null) {
                    if(dataSnapshot.getValue() != null) {
                        progNames = (HashMap<String,String>) dataSnapshot.getValue();

                        progsList.setAdapter(new CustomAdapter2(AddProgram.this, new ArrayList<String>(progNames.values()) ));
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
}
