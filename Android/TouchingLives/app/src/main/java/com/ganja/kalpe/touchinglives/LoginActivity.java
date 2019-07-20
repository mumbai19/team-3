package com.ganja.kalpe.touchinglives;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ganja.kalpe.touchinglives.Utils.BaseActivity;
import com.ganja.kalpe.touchinglives.Utils.BaseFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class LoginActivity extends BaseActivity {

    public static final String TITLE="Login";

    public static boolean mShowingBack;

    public static class LoginCard extends BaseFragment {

        EditText un,pass;
        Button login;
        TextView reg, forgotpass;


        String unstr, passstr, uidstr, emailstr;
        String emailfinal, passwordfinal;
        String catpath, categories;

        ProgressDialog pd;

        private FirebaseAuth firebaseAuth;
        FirebaseUser firebaseUser;
        DatabaseReference usrdet, dbUserPref;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            mShowingBack=false;
            View view=inflater.inflate(R.layout.logincard, container, false);

            un=(EditText)view.findViewById(R.id.login_emailid);
            pass=(EditText)view.findViewById(R.id.login_password);
            login=(Button)view.findViewById(R.id.loginBtn);
            reg=(TextView) view.findViewById(R.id.createAccount);
            forgotpass=(TextView)view.findViewById(R.id.forgotPass);

            firebaseAuth = FirebaseAuth.getInstance();

            if(firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()) {
                pd = ProgressDialog.show(getBaseActivity(), "TouchingLives", "Logging you in...");
                    firebaseUser = firebaseAuth.getCurrentUser();
                    uidstr = firebaseUser.getUid();

                    Intent i1 = new Intent(getBaseActivity(), MainPage.class);
                    getBaseActivity().startActivity(i1);
                    getBaseActivity().finish();


            }

            reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flipCard((Activity) getBaseContext());
                }
            });

            loginClick();
            forgotClick();
            return view;
        }

        public void forgotClick(){
            forgotpass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unstr=un.getText().toString().trim();
                    if(TextUtils.isEmpty(unstr))
                        Toast.makeText(getBaseActivity(), "Enter Username/Email", Toast.LENGTH_SHORT).show();
                    else if(!TextUtils.isEmpty(unstr)){
                            //  Check if it is an email or not
                            if (android.util.Patterns.EMAIL_ADDRESS.matcher(unstr).matches())
                                performResetPassword(unstr);
                            else {
                                Toast.makeText(getBaseActivity(), "Invalid Username/Email or Not Registered", Toast.LENGTH_LONG).show();
                            }
                    }
                }
            });
        }

        public void performResetPassword(String email){
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getBaseActivity(), "Password reset link sent to registered email address", Toast.LENGTH_LONG).show();
                                un.setText("");
                                pass.setText("");
                            }
                            else{
                                Toast.makeText(getBaseActivity(), "Couldn't send password reset link, Contact developers", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
        }

        public void loginClick() {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //pd=new ProgressDialog(LoginActivity.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                    pd=ProgressDialog.show(getBaseActivity(),"NewsAppUS","Logging you in...");
                    unstr = un.getText().toString().trim();
                    passstr = pass.getText().toString().trim();

                    if(TextUtils.isEmpty(unstr) || TextUtils.isEmpty(passstr))
                    {
                        pd.dismiss();
                        if(TextUtils.isEmpty(unstr))
                            Toast.makeText(getBaseActivity(), "Enter Username/Email", Toast.LENGTH_SHORT).show();
                        else if(TextUtils.isEmpty(passstr)) {
                            Toast.makeText(getBaseActivity(), "Enter Password", Toast.LENGTH_SHORT).show();
                            forgotpass.setVisibility(View.VISIBLE);
                        }
                    }
                    else if(!TextUtils.isEmpty(unstr) && !TextUtils.isEmpty(passstr))
                    {
                            //  Check if it is an email or not
                            if (android.util.Patterns.EMAIL_ADDRESS.matcher(unstr).matches())
                                performLogin(unstr, passstr);

                    }
                }
            });
        }

        public void performLogin(String email, String password)
        {
            emailfinal=email;
            passwordfinal=password;

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getBaseActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                        if(firebaseUser.isEmailVerified())
                        {
                            //update verified field in db start
                            usrdet.child("details").child(firebaseUser.getUid()).child("verified").setValue("YES");
                            //update verified field in db end

                            Intent i1 = new Intent(getBaseActivity(), MainPage.class);
                            getBaseActivity().startActivity(i1);
                            getBaseActivity().finish();
                        }
                        else
                        {
                            pd.dismiss();
                            Toast.makeText(getBaseActivity(),"Email Not Verified",Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        pd.dismiss();
                        Toast.makeText(getBaseActivity(),"Incorrect username or password",Toast.LENGTH_LONG).show();
                        forgotpass.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
    }

    public static class SignupCard extends BaseFragment {
        private FirebaseAuth firebaseAuth;
        EditText reg_email, reg_pass, reg_repass, reg_un;
        Button submit_form;
        String emailstr, passstr, repassstr;
        int passlen, repasslen, flag2;

        static boolean verificationEmailSent;
        private Context context;
        private Activity activity;


        ProgressDialog pd;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.signupcard, container, false);

            context = container.getContext();
            activity = getBaseActivity();
            reg_email = (EditText) view.findViewById(R.id.userEmailId);
            reg_pass = (EditText) view.findViewById(R.id.password);
            reg_repass = (EditText) view.findViewById(R.id.repassword);
            submit_form = (Button) view.findViewById(R.id.signupBtn);

            firebaseAuth = FirebaseAuth.getInstance();

            onSubmit();

            return view;
        }

        public void onSubmit() {
            submit_form.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registerUser();
                }
            });
        }

        private void registerUser() {
            pd = ProgressDialog.show(context, "TouchingLives", "Signing You Up..", true);
            emailstr = reg_email.getText().toString().trim();
            passstr = reg_pass.getText().toString().trim();
            repassstr = reg_repass.getText().toString().trim();

            passlen = passstr.length();
            repasslen = repassstr.length();

            if (TextUtils.isEmpty(emailstr) || TextUtils.isEmpty(passstr) || TextUtils.isEmpty(repassstr)) {
                if (TextUtils.isEmpty(emailstr)) {
                    pd.dismiss();
                    Toast.makeText(context, "Please enter email", Toast.LENGTH_LONG).show();
                }

                if (TextUtils.isEmpty(passstr)) {
                    pd.dismiss();
                    Toast.makeText(context, "Please enter password", Toast.LENGTH_LONG).show();
                }

                if (TextUtils.isEmpty(repassstr)) {
                    pd.dismiss();
                    Toast.makeText(context, "Please re-enter password", Toast.LENGTH_LONG).show();
                }
            } else if (!TextUtils.isEmpty(passstr) && !TextUtils.isEmpty(repassstr) ) {
                if (passstr.equals(repassstr) && passlen == repasslen) {
                    if (passlen < 8) {
                        pd.dismiss();
                        Toast.makeText(context, "Password should have minimum 8 characters", Toast.LENGTH_LONG).show();
                    } else {

                        //add user
                        firebaseAuth.createUserWithEmailAndPassword(emailstr, passstr)
                                .addOnCompleteListener(getBaseActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                pd.dismiss();
                                                Toast.makeText(context, "User with this email id already exists", Toast.LENGTH_LONG).show();
                                            } else {
                                                pd.dismiss();
                                                Toast.makeText(context, "Registration Error, Contact Developer(1)", Toast.LENGTH_LONG).show();
                                            }

                                        } else if (task.isSuccessful()) {
                                            //getBaseActivity().finish();
                                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                            if (user != null) {
                                                user.sendEmailVerification()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    if (pd != null && pd.isShowing())
                                                                        pd.dismiss();
                                                                    Toast.makeText(context, "Signup successful.Verification email sent", Toast.LENGTH_LONG).show();


                                                                    flipCard(activity);
                                                                }
                                                            }
                                                        });
                                            } else {
                                                pd.dismiss();
                                                Toast.makeText(context, "Registration Error, Contact Developer(2)", Toast.LENGTH_LONG).show();
                                            }


                                        } else {
                                            pd.dismiss();
                                            Toast.makeText(context, "Registration Error, Contact Developer(3)", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });

                    }

                }
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupToolbar();
        setTitle(LoginActivity.TITLE);

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.loginContainer, new LoginCard())
                    .commit();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setTitle(LoginActivity.TITLE);
    }

    private static void flipCard(Activity activity) {
        if (mShowingBack) {
            activity.getFragmentManager().popBackStack();
            return;
        }

        // Flip to the back.

        mShowingBack = true;

        // Create and commit a new fragment transaction that adds the fragment for
        // the back of the card, uses custom animations, and is part of the fragment
        // manager's back stack.

        activity.getFragmentManager()
                .beginTransaction()

                // Replace the default fragment animations with animator resources
                // representing rotations when switching to the back of the card, as
                // well as animator resources representing rotations when flipping
                // back to the front (e.g. when the system Back button is pressed).
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)

                // Replace any fragments currently in the container view with a
                // fragment representing the next page (indicated by the
                // just-incremented currentPage variable).
                .replace(R.id.loginContainer, new SignupCard())

                // Add this transaction to the back stack, allowing users to press
                // Back to get to the front of the card.
                .addToBackStack(null)

                // Commit the transaction.
                .commit();

    }
}
