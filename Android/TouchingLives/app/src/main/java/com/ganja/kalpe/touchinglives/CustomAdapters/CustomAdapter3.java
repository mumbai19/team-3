package com.ganja.kalpe.touchinglives.CustomAdapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.ganja.kalpe.touchinglives.AddProgram;
import com.ganja.kalpe.touchinglives.R;
import com.ganja.kalpe.touchinglives.Utils.MyTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomAdapter3 extends ArrayAdapter<String>{

    public ArrayList<String> title;
    public Activity context;

    HashMap<String,String> progNames;
    public CustomAdapter3(Activity context, ArrayList<String> title){
        super(context, R.layout.list3,title);
        this.context=context;
        this.title=title;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View row=inflater.inflate(R.layout.list3, null, true);

        MyTextView t1= row.findViewById(R.id.mentorName);
        final Spinner progSpin = row.findViewById(R.id.progSpinner);

        DatabaseReference progDb= FirebaseDatabase.getInstance().getReference("/programs");
        final DatabaseReference mentorDb = FirebaseDatabase.getInstance().getReference("/mentor");

        t1.setText(title.get(position));

        progNames = new HashMap<>();
        progDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null) {
                    if(dataSnapshot.getValue() != null) {
                        progNames = (HashMap<String,String>) dataSnapshot.getValue();

                        final Object[] obj = (new ArrayList<String>(progNames.values())).toArray();
                        final String[] progArr = Arrays.copyOf(obj, obj.length, String[].class);
                        ArrayAdapter<String> aa2 = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, progArr);
                        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        progSpin.setAdapter(aa2);

                        progSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Toast.makeText(context,title.get(position)+" assigned to "+progArr[i],Toast.LENGTH_SHORT).show();
                                final String ind = progArr[i];
                                mentorDb.orderByChild("name").equalTo(title.get(position)).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                            snapshot.getRef().child("program").setValue(ind);
                                        }
                                        //dataSnapshot.getRef().child(dataSnapshot.getKey()).child("program").setValue(progArr[ind]);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }

                    else{
                        String[] progArr = {"No Programs"};
                        ArrayAdapter<String> aa2 = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, progArr);
                        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        progSpin.setAdapter(aa2);
                    }
                }
                else{
                    String[] progArr = {"No Programs"};
                    ArrayAdapter<String> aa2 = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, progArr);
                    aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    progSpin.setAdapter(aa2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return row;
    }
}

