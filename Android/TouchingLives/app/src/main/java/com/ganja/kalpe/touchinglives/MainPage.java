package com.ganja.kalpe.touchinglives;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ganja.kalpe.touchinglives.CustomAdapters.CustomAdapter1;
import com.ganja.kalpe.touchinglives.Utils.BaseActivity;
import com.ganja.kalpe.touchinglives.Utils.MyTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainPage extends BaseActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    ArrayList<String> tasks, centres;

    String taskTitle, userRole;

    MyTextView taskTitleTV;

    DatabaseReference userRoleDb, centresDb;
    ListView tasksList;

    final String ADD_PROGRAM = "Add a program",
            ASSIGN_MENTOR = "Assign a mentor",
            ADD_CENTER = "Add learning/community center",
            VIEW_REPORT = "View Reports",
            LOGOUT = "Logout";

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        firebaseAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progbar);


        tasksList = findViewById(R.id.taskList);
        taskTitleTV = findViewById(R.id.taskTitle);
        tasks = new ArrayList<String>();
        centres = new ArrayList<>();

        progressBar.setVisibility(View.VISIBLE);
        tasksList.setVisibility(View.GONE);
        taskTitleTV.setVisibility(View.GONE);

        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(MainPage.this, LoginActivity.class));
            finish();
        } else {
            userRoleDb = FirebaseDatabase.getInstance().getReference("userRoles");
            centresDb = FirebaseDatabase.getInstance().getReference("centres");

            firebaseUser = firebaseAuth.getCurrentUser();

            userRoleDb.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userRole = dataSnapshot.getValue(String.class);

                    setupToolbar();
                    setTitle("Hello " + userRole);

                    if (userRole == null) {
                        Toast.makeText(MainPage.this, "Not approved by admin yet or You dont have access to this app", Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        startActivity(new Intent(MainPage.this, LoginActivity.class));
                        finish();
                    } else {
                        if (userRole.equals("admin")) {
                            taskTitleTV.setText("Select an action");
                            tasks.add(ADD_PROGRAM);
                            tasks.add(ASSIGN_MENTOR);
                            tasks.add(ADD_CENTER);
                            tasks.add(VIEW_REPORT);
                            tasks.add(LOGOUT);


                            progressBar.setVisibility(View.GONE);
                            tasksList.setVisibility(View.VISIBLE);
                            taskTitleTV.setVisibility(View.VISIBLE);

                            tasksList.setAdapter(new CustomAdapter1(MainPage.this, tasks));

                            tasksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    switch (tasks.get(i)){
                                        case ADD_PROGRAM:
                                            startActivity(new Intent(MainPage.this, AddProgram.class));
                                            break;

                                        case ASSIGN_MENTOR:
                                            startActivity(new Intent(MainPage.this, AssignMentor.class));
                                            break;

                                        case ADD_CENTER:
                                            startActivity(new Intent(MainPage.this, AddCentre.class));
                                            break;

                                        case LOGOUT:
                                            firebaseAuth.signOut();
                                            finish();

                                        /*case VIEW_REPORT:
                                            startActivity(new Intent(MainPage.this, ViewReport.class));
                                            break;

                                        case CREATE_ACTIVITY:
                                            startActivity(new Intent(MainPage.this, CreateActivity.class));
                                            break;

                                        case ASSESS_ACTIVITY:
                                            startActivity(new Intent(MainPage.this, AssesActivity.class));
                                            break;

                                        case GIVE_STAR:
                                            startActivity(new Intent(MainPage.this, GiveStar.class));
                                            break;

                                        case ATTENDANCE_SAVING:
                                            startActivity(new Intent(MainPage.this, AddProgram.class));
                                            break;

                                        case RETURN_SAVINGS:
                                            startActivity(new Intent(MainPage.this, AddProgram.class));
                                            break;*/
                                    }
                                }
                            });

                        } else {
                            taskTitleTV.setText("Select a center");

                            centresDb.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String centreName = snapshot.getKey();
                                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                            centres.add(centreName + "-" + childSnapshot.getValue(String.class));
                                        }
                                    }


                                    progressBar.setVisibility(View.GONE);
                                    tasksList.setVisibility(View.VISIBLE);
                                    taskTitleTV.setVisibility(View.VISIBLE);

                                    tasksList.setAdapter(new CustomAdapter1(MainPage.this, centres));

                                    tasksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            Intent intent = new Intent(MainPage.this,MentorActions.class);
                                            intent.putExtra("centre",centres.get(i));
                                            startActivity(intent);

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        /*tasks.add(CREATE_ACTIVITY);
                        tasks.add(ASSESS_ACTIVITY);
                        tasks.add(GIVE_STAR);
                        tasks.add(ATTENDANCE_SAVING);
                        tasks.add(RETURN_SAVINGS);
                        tasks.add(VIEW_REPORT);*/
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }
}
