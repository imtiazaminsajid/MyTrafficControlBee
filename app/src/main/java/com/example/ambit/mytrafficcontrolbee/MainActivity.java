package com.example.ambit.mytrafficcontrolbee;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
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

    ImageView postAlert;
    private RecyclerView recyclerView;
    private GridView gridView;
    private MyAdapterForAlertShow myAdapterForAlertShow;
    private List<AlertMessage> alertMessageList;
    private DatabaseReference databaseReference;
    private TextView seeTraffic, totalPostCount;

    private ModelClassLocation modelClassLocation;
    private CustomAdapterLocation customAdapterLocation;
    private ArrayList<ModelClassLocation> modelClassLocationArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("TCB | Home");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        gridView = findViewById(R.id.gridViewForHomeActivity);
//        gridView.setHasFixedSize(true);
//        gridView.setLayoutManager(new LinearLayoutManager(this));



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


        modelClassLocationArrayList = new ArrayList<>();
        customAdapterLocation = new CustomAdapterLocation(MainActivity.this, modelClassLocationArrayList);

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

        DatabaseReference databaseReferenceForLocation = FirebaseDatabase.getInstance().getReference("AlertMessage");
        databaseReferenceForLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                modelClassLocationArrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ModelClassLocation locationName = snapshot.getValue(ModelClassLocation.class);
                    Collections.reverse(modelClassLocationArrayList);
                    modelClassLocationArrayList.add(locationName);
                    Collections.reverse(modelClassLocationArrayList);
                }


                gridView.setAdapter(customAdapterLocation);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        DatabaseReference databaseReferenceForLocation = FirebaseDatabase.getInstance().getReference("AlertMessage").child("location");
//
//        databaseReferenceForLocation.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                modelClassLocationArrayList.clear();
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    ModelClassLocation locationName = snapshot.getValue(ModelClassLocation.class);
//                    modelClassLocationArrayList.add(locationName);
//                }
//
//                customAdapterLocation = new CustomAdapterLocation(MainActivity.this, modelClassLocationArrayList);
//
//                gridView.setAdapter(customAdapterLocation);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        databaseReferenceForLocation.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                modelClassLocationArrayList.clear();
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    ModelClassLocation locationName = snapshot.getValue(ModelClassLocation.class);
//                    modelClassLocationArrayList.add(locationName);
//                }
//
//                gridView.setAdapter(customAdapterLocation);
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });




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

//    @Override
//    protected void onStart() {
//
//        DatabaseReference databaseReferenceForLocation = FirebaseDatabase.getInstance().getReference("AlertMessage");
//        databaseReferenceForLocation.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                modelClassLocationArrayList.clear();
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    ModelClassLocation locationName = snapshot.getValue(ModelClassLocation.class);
//                    modelClassLocationArrayList.add(locationName);
//                }
//
//
//                gridView.setAdapter(customAdapterLocation);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        super.onStart();
//    }
}
