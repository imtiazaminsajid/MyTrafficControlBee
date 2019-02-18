package com.example.ambit.mytrafficcontrolbee;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    EditText signInEmail, signInPassword;
    Button signInButton;
    TextView dontHaveAccount, TotalAlert;
    private FirebaseAuth mAuth;

    private ProgressBar progressBar;

    private CustomAdapterLocation customAdapterLocation;
    private ArrayList<ModelClassLocation> modelClassLocationArrayList;

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        this.setTitle("SignIn");

        signInEmail = findViewById(R.id.signInEmail);
        signInPassword = findViewById(R.id.signInPassword);
        TotalAlert = findViewById(R.id.TotalAlert);

        signInButton = findViewById(R.id.signInButton);
        progressBar = findViewById(R.id.signInProgressBar);

        dontHaveAccount = findViewById(R.id.signinDontHaveAccount);

        signInButton.setOnClickListener(this);
        dontHaveAccount.setOnClickListener(this);

        gridView = findViewById(R.id.gridViewForSignin);

        modelClassLocationArrayList = new ArrayList<>();
        customAdapterLocation = new CustomAdapterLocation(SignInActivity.this, modelClassLocationArrayList);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.signInButton:

                signIn();


                break;

            case R.id.signinDontHaveAccount:
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void signIn() {

        String email = signInEmail.getText().toString().trim();
        String password = signInPassword.getText().toString().trim();

        if(email.isEmpty())
        {
            signInEmail.setError("Enter an email address");
            signInEmail.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signInEmail.setError("Enter a valid email address");
            signInEmail.requestFocus();
            return;
        }

        //checking the validity of the password
        if(password.isEmpty())
        {
            signInPassword.setError("Enter a password");
            signInPassword.requestFocus();
            return;
        }

        if(password.length()<6)
        {
            signInPassword.setError("Minimum length of a password should be 6");
            signInPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);

            if (task.isSuccessful()){
                finish();
                Intent intent = new Intent(getApplicationContext(), SubmitAlertActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else {

                Toast.makeText(getApplicationContext(), "Sign In Failed", Toast.LENGTH_SHORT).show();

            }
            }
        });
    }


    @Override
    protected void onStart() {

        DatabaseReference databaseReferenceForLocation = FirebaseDatabase.getInstance().getReference("AlertMessage");
        databaseReferenceForLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                modelClassLocationArrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ModelClassLocation locationName = snapshot.getValue(ModelClassLocation.class);
                    modelClassLocationArrayList.add(locationName);
                }


                gridView.setAdapter(customAdapterLocation);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        super.onStart();
    }


}
