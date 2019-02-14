package com.example.ambit.mytrafficcontrolbee;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText registrationEmail, registrationPassword, registrationName;
    Button registrationButton;
    TextView haveAnAccount;
    ProgressBar registrationprogressBar;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        this.setTitle("Registration");

        registrationEmail = findViewById(R.id.registrationEmailId);
        registrationPassword = findViewById(R.id.registrationPassword);
        registrationName = findViewById(R.id.registrationName);

        registrationButton  = findViewById(R.id.registrationButton);

        haveAnAccount = findViewById(R.id.registrationHaveAccount);
        registrationprogressBar = findViewById(R.id.registrationProgressBar);

        registrationButton.setOnClickListener(this);
        haveAnAccount.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(this);

        if (mAuth.getCurrentUser()!=null){

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.registrationButton:

                registration();

                break;

            case R.id.registrationHaveAccount:
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void registration() {

        final String email = registrationEmail.getText().toString().trim();
        final String password = registrationPassword.getText().toString().trim();
        final String name = registrationName.getText().toString().trim();

        if(email.isEmpty())
        {
            registrationEmail.setError("Enter an email address");
            registrationEmail.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            registrationEmail.setError("Enter a valid email address");
            registrationEmail.requestFocus();
            return;
        }

        //checking the validity of the password
        if(password.isEmpty())
        {
            registrationPassword.setError("Enter a password");
            registrationPassword.requestFocus();
            return;
        }

        if(password.length()<6)
        {
            registrationPassword.setError("Minimum length of a password should be 6");
            registrationPassword.requestFocus();
            return;
        }

        registrationprogressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Users users = new Users(name, email, password);

                    FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            registrationprogressBar.setVisibility(View.GONE);

                            if (task.isSuccessful()){
                                finish();

                                Toast.makeText(RegistrationActivity.this, "Registation Is Successfull", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), SubmitAlertActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });



                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "User Email is Already Registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }
}
