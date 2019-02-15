package com.example.ambit.mytrafficcontrolbee;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.lang.invoke.ConstantCallSite;
import java.nio.charset.MalformedInputException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SubmitAlertActivity extends AppCompatActivity implements View.OnClickListener {

    TextView timeAndDate, gpsAddress;

    EditText alertMessage, alertLocation;
    ImageView capturedImage;
    Button submit;
    ProgressBar alertProgressBar;
    private Uri imageUri;

    DatabaseReference databaseReference;
    StorageReference storageReference;
    StorageTask storageTask;

    private static final int IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_alert);

        this.setTitle("Alert Message");

        timeAndDate = findViewById(R.id.timeAndDate);
        gpsAddress = findViewById(R.id.gpsAddress);
        alertLocation = findViewById(R.id.alertLocation);
        alertMessage = findViewById(R.id.alertEditText);
        capturedImage = findViewById(R.id.capturePicture);
        submit = findViewById(R.id.submitAlert);
        alertProgressBar= findViewById(R.id.alertProgressBar);

        submit.setOnClickListener(this);
        capturedImage.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("AlertMessage");
        storageReference = FirebaseStorage.getInstance().getReference("Uploads");


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm aaa");
        String time = format.format(calendar.getTime());
        timeAndDate.setText(time);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.capturePicture:

                openFileChoser();

                break;

            case R.id.submitAlert:

                if (storageTask!=null && storageTask.isInProgress()){
                    Toast.makeText(getApplicationContext(), "Submit in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    submitData();
                }

                break;
        }

    }


    public String getFileExtention(Uri imageUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }


    private void submitData() {

        final String timeandDate = timeAndDate.getText().toString().trim();
        final String location =  alertLocation.getText().toString().trim();
        final String alertMessages = alertMessage.getText().toString().trim();
        //String alertMessages = alertMessage.getText().toString().trim();

        if (location.isEmpty()){
            alertLocation.setError("Enter Location");
            alertLocation.requestFocus();
            return;
        }
        if (alertMessages.isEmpty()){
            alertMessage.setError("Enter Alert");
            alertMessage.requestFocus();
            return;
        }

        StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExtention(imageUri));

        alertProgressBar.setVisibility(View.VISIBLE);

        reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        alertProgressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Alert Message Updated Successfully", Toast.LENGTH_SHORT).show();

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                        while (!uriTask.isSuccessful());

                        Uri downloadUri = uriTask.getResult();

                        AlertMessage alertMessageClass = new AlertMessage(timeandDate, location, alertMessages, downloadUri.toString());

                        String alertMessageClassId = databaseReference.push().getKey();
                        databaseReference.child(alertMessageClassId).setValue(alertMessageClass);

                        finish();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);




                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(getApplicationContext(), "Alert Message Updated Failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void openFileChoser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(capturedImage);
        }
    }
}
