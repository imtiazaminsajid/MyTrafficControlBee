package com.example.ambit.mytrafficcontrolbee;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button postAlert;
    private RecyclerView recyclerView;
    private MyAdapterForAlertShow myAdapterForAlertShow;
    private List<AlertMessage> alertMessageList;
    private DatabaseReference databaseReference;
    private TextView seeTraffic, totalPostCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postAlert = findViewById(R.id.postAlertButton);
        seeTraffic = findViewById(R.id.seeTraffic);
        seeTraffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TrafficWebView.class);
                startActivity(intent);
            }
        });


        alertMessageList = new ArrayList<>();
        //Collections.reverse(alertMessageList);

        databaseReference = FirebaseDatabase.getInstance().getReference("AlertMessage");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    AlertMessage alertMessage = dataSnapshot1.getValue(AlertMessage.class);
                    Collections.reverse(alertMessageList);
                    alertMessageList.add(alertMessage);
                    Collections.reverse(alertMessageList);

                }

                myAdapterForAlertShow = new MyAdapterForAlertShow(getApplicationContext(),alertMessageList);
                recyclerView.setAdapter(myAdapterForAlertShow);

                int TotalPost = alertMessageList.size();
                totalPostCount = findViewById(R.id.totalPostCount);
                totalPostCount.setText("Total Alert: "+TotalPost);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });

        postAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder =  new AlertDialog.Builder(this);

        builder.setMessage("Do you want to Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();

                    }
                })
                .setNegativeButton("Cencel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                })
                .create().show();


    }




}
