package com.ganja.kalpe.touchinglives;

import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.ganja.kalpe.touchinglives.Utils.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AddStudent extends BaseActivity {

    EditText nameET, ageET, standardET, phoneET, addressET, mediumET;
    Spinner oldNewSpinner, modeSpinner, batchSpinner, programSpinner;

    String [] oldNew={"Old", "New", "Transfer"}, mode={"Private", "Regular"}, batch ={"Morning", "Evening"}, progArr;

    String name, age, standard, phone, address, centreName, centre_id, medium, batchSelected, modeSelected, oldNewSelected, programSelected;

    Button submit;

    DatabaseReference studDb, programDb, phonebookDb, centreDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        centreName = getIntent().getStringExtra("centre");

        setupToolbar();
        setTitle("Add a Student");

        nameET = findViewById(R.id.studName);
        ageET = findViewById(R.id.studAge);
        standardET = findViewById(R.id.studStd);
        phoneET = findViewById(R.id.studPhone);
        addressET = findViewById(R.id.studAdd);
        mediumET = findViewById(R.id.studMed);
        submit = findViewById(R.id.submitStud);

        oldNewSpinner = findViewById(R.id.oldNew);
        modeSpinner = findViewById(R.id.mode);
        batchSpinner = findViewById(R.id.batch);
        programSpinner = findViewById(R.id.studProgram);


        studDb = FirebaseDatabase.getInstance().getReference("/studInfo");
        programDb = FirebaseDatabase.getInstance().getReference("/programs");
        centreDb = FirebaseDatabase.getInstance().getReference("/centres");
        phonebookDb = FirebaseDatabase.getInstance().getReference("/studentContact");



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameET.getText().toString().trim();
                age = ageET.getText().toString().trim();
                standard = standardET.getText().toString().trim();
                phone = phoneET.getText().toString().trim();
                address = addressET.getText().toString().trim();
                medium = mediumET.getText().toString().trim();

                batchSelected = batchSpinner.getSelectedItem().toString();
                modeSelected = modeSpinner.getSelectedItem().toString();
                oldNewSelected = oldNewSpinner.getSelectedItem().toString();
                programSelected = programSpinner.getSelectedItem().toString();

                centreDb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                                if((childSnapshot.getValue(String.class)).equals(centreName))
                                    centre_id = childSnapshot.getKey();
                            }
                        }

                        studDb.push().setValue(new Student(Integer.parseInt(age),Integer.parseInt(phone),0,name,standard,address,medium,oldNewSelected,
                                modeSelected,batchSelected,centreName,programSelected));
                        shortToast("Student Added");

                        ageET.setText("");
                        addressET.setText("");
                        nameET.setText("");
                        mediumET.setText("");
                        phoneET.setText("");
                        standardET.setText("");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone)) {
                    studDb.orderByChild("name").equalTo(name).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                phonebookDb.child(dataSnapshot.getKey()).child("name").setValue(name);
                                phonebookDb.child(dataSnapshot.getKey()).child("number").setValue(phone);
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        programDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,String> map = (HashMap<String, String>) dataSnapshot.getValue();

                progArr = map.values().toArray(new String[0]);

                ArrayAdapter<String> aa1 = new ArrayAdapter<String>(AddStudent.this,android.R.layout.simple_spinner_item, oldNew);
                aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                oldNewSpinner.setAdapter(aa1);

                ArrayAdapter<String> aa2 = new ArrayAdapter<String>(AddStudent.this,android.R.layout.simple_spinner_item, mode);
                aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                modeSpinner.setAdapter(aa2);

                ArrayAdapter<String> aa3 = new ArrayAdapter<String>(AddStudent.this,android.R.layout.simple_spinner_item, batch);
                aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                batchSpinner.setAdapter(aa3);

                ArrayAdapter<String> aa4 = new ArrayAdapter<String>(AddStudent.this,android.R.layout.simple_spinner_item, progArr);
                aa4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                programSpinner.setAdapter(aa4);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
