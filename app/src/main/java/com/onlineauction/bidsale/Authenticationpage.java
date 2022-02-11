package com.onlineauction.bidsale;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Authenticationpage extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser userid;
    Button rsendemail,verifybt;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticationpage);
        auth = FirebaseAuth.getInstance();


        user = auth.getCurrentUser().getUid();
        userid = auth.getCurrentUser();
        verifybt=findViewById(R.id.verifybtn);
        verifybt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean r =userid.isEmailVerified();

                if(r==false){
                    Toast.makeText(Authenticationpage.this, "email verification is not done", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i = new Intent(Authenticationpage.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                }

            }
        });
        rsendemail = findViewById(R.id.resendbtn);

        rsendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userid.isEmailVerified()) {
                    userid.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Authenticationpage.this, "email verification has been sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: email not sent " + e.getMessage());
                        }

                    });

                }
            }
        });


    }
}