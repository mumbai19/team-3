package com.ganja.kalpe.touchinglives;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ganja.kalpe.touchinglives.CustomAdapters.CustomAdapter1;
import com.ganja.kalpe.touchinglives.Utils.BaseActivity;
import com.ganja.kalpe.touchinglives.Utils.MyTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MentorActions extends BaseActivity {
    final String CREATE_ACTIVITY = "Create activity",
    ASSESS_ACTIVITY = "Asses Activity",
    GIVE_STAR = "Give stars",
    ATTENDANCE_SAVING = "Record Attendance/Savings",
    RETURN_SAVINGS = "Return savings", ADD_STUDENT = "Add a student",VIEW_REPORT = "View Reports", ADD_TO_CONTACTS="Add Students to Contact", LOGOUT="Logout";

    String centreName, centre_id;

    ArrayList<String> tasks;

    MyTextView taskTitleTV;

    DatabaseReference phonebookDb, centreDb, studDb;
    ListView tasksList;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_actions);

        centreName = getIntent().getStringExtra("center");

        setupToolbar();
        setTitle("Hello, Mentor");

        tasks = new ArrayList<String>();


        tasks.add(ADD_STUDENT);
        tasks.add(ADD_TO_CONTACTS);
        tasks.add(CREATE_ACTIVITY);
        tasks.add(ASSESS_ACTIVITY);
        tasks.add(ATTENDANCE_SAVING);
        tasks.add(RETURN_SAVINGS);
        tasks.add(VIEW_REPORT);
        tasks.add(GIVE_STAR);
        tasks.add(LOGOUT);


        tasksList = findViewById(R.id.mentorTaskList);
        taskTitleTV = findViewById(R.id.mentorTaskTitle);
        progressBar = findViewById(R.id.mentorProgbar);

        taskTitleTV.setText("Select a task");
        tasksList.setAdapter(new CustomAdapter1(MentorActions.this, tasks));

        studDb = FirebaseDatabase.getInstance().getReference("studentInfo");
        centreDb = FirebaseDatabase.getInstance().getReference("centres");
        phonebookDb = FirebaseDatabase.getInstance().getReference("studentContact");

        tasksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (tasks.get(i)){
                    case ADD_STUDENT:
                        Intent intent = new Intent(MentorActions.this, AddStudent.class);
                        intent.putExtra("centre",centreName);
                        startActivity(intent);
                        break;

                    case ADD_TO_CONTACTS:
                        phonebookDb.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                                    addContact(childSnapshot.child("name").getValue(String.class),
                                            childSnapshot.child("number").getValue(String.class));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        break;

                    case LOGOUT:
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.signOut();
                        startActivity(new Intent(MentorActions.this,LoginActivity.class));
                    /*case VIEW_REPORT:
                                            startActivity(new Intent(MentorActions.this, ViewReport.class));
                                            break;

                                        case CREATE_ACTIVITY:
                                            startActivity(new Intent(MentorActions.this, CreateActivity.class));
                                            break;

                                        case ASSESS_ACTIVITY:
                                            startActivity(new Intent(MentorActions.this, AssesActivity.class));
                                            break;

                                        case GIVE_STAR:
                                            startActivity(new Intent(MentorActions.this, GiveStar.class));
                                            break;

                                        case ATTENDANCE_SAVING:
                                            startActivity(new Intent(MentorActions.this, AddProgram.class));
                                            break;

                                        case RETURN_SAVINGS:
                                            startActivity(new Intent(MentorActions.this, AddProgram.class));
                                            break;*/
                }
            }
        });

    }

    private void addContact(String name, String phone) {
        /*ContentValues values = new ContentValues();
        shortToast(name+" x "+phone);
        values.put(ContactsContract.Data.RAW_CONTACT_ID, 001);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM);
        values.put(ContactsContract.CommonDataKinds.Phone.LABEL, name);
        Uri dataUri = getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);*/

        /*ArrayList<ContentProviderOperation> ops =
                new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValue(ContactsContract.Data.RAW_CONTACT_ID, name )
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM)
                .withValue(ContactsContract.CommonDataKinds.Phone.LABEL, name)
                .build());*/


        /*try {
            ContentResolver cr = this.getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, "New Name");
            cv.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "1234567890");
            cv.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            cr.insert(ContactsContract.RawContacts.CONTENT_URI, cv);

            Toast.makeText(this, "Contact added", Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            TextView tv = new TextView(this);
            tv.setText(e.toString());
            setContentView(tv);
        }*/


    }
}
