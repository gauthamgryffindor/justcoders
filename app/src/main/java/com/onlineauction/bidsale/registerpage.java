package com.onlineauction.bidsale;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class registerpage extends AppCompatActivity {
    EditText username,email,phone,address,pasword,cpass;
    Button reg;
    String uname,mail,phon,addre,mpas,mconf;
    FirebaseAuth auth;
    DatabaseReference myref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpage);
        username = findViewById(R.id.UserName);
        email = findViewById(R.id.EmailAddress);
        phone = findViewById(R.id.Phone);
        auth = FirebaseAuth.getInstance();
        address = findViewById(R.id.Address);
        pasword = findViewById(R.id.cpPassword);
        cpass = findViewById(R.id.cPassword2);
        reg = findViewById(R.id.register1);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uname = username.getText().toString().trim();
                mail = email.getText().toString().trim();
                phon = phone.getText().toString().trim();
                addre = address.getText().toString();
                mpas = pasword.getText().toString().trim();
                mconf = cpass.getText().toString().trim();

                if (uname.isEmpty() || mail.isEmpty() || mpas.isEmpty() || mconf.isEmpty() || phon.isEmpty() || addre.isEmpty()) {
                    Toast.makeText(registerpage.this, "enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean n = mpas.equals(mconf);
                    if (n == false) {
                        Toast.makeText(registerpage.this, "password is not matching", Toast.LENGTH_LONG).show();
                    } else {

                        //Iterables.removeIf(subs, Predicates.isNull());
                        Registernow(uname, mail, phon, addre, mpas);
                    }

                }
            }

        });

    }
        private void Registernow(String uname, String mail, String phon, String addre, String mpas) {
            auth.createUserWithEmailAndPassword(mail,mpas).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    FirebaseUser user=auth.getCurrentUser();
                    user.sendEmailVerification().addOnSuccessListener(aVoid -> Toast.makeText(registerpage.this,"email verification has been sent",Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: email not sent "+e.getMessage());
                        }
                    });
                    //int q=1;
                    String userid=user.getUid();
                    myref=FirebaseDatabase.getInstance().getReference("Userdata")
                            .child(userid);
                    HashMap<String,String> hash=new HashMap<>();
                    hash.put("username",uname);
                    hash.put("email",mail);
                    hash.put("phonenumber",phon);
                    hash.put("address",addre);
                    myref.setValue(hash).addOnCompleteListener(task1 -> {

                        if(task1.isSuccessful()) {
                            Intent i1 = new Intent(registerpage.this, Authenticationpage.class);
                            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(i1);

                            finish();
                        }else{
                            Toast.makeText(registerpage.this,"registeration not done error occurred",Toast.LENGTH_SHORT).show();

                        }
                    });

                }else{
                    Toast.makeText(registerpage.this,"invalid email or password",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
