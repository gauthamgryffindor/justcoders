package com.onlineauction.bidsale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginpageActivity2 extends AppCompatActivity {
    EditText email,passwords;
    Button login,register;
    String memail,mpass;
    FirebaseAuth authsm;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage2);
        email=findViewById(R.id.Email);
        passwords=findViewById(R.id.password);
        authsm= FirebaseAuth.getInstance();
        login=findViewById(R.id.Login);
        register=findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memail = email.getText().toString();
                mpass = passwords.getText().toString();
                if (mpass.isEmpty() || memail.isEmpty()) {
                    Toast.makeText(LoginpageActivity2.this, "enter all the fields", Toast.LENGTH_SHORT).show();
                } else {

                    authsm.signInWithEmailAndPassword(memail, mpass)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    onAuthSuccess(task.getResult().getUser());


                                } else {
                                    Toast.makeText(LoginpageActivity2.this, "invalid password or email id", Toast.LENGTH_SHORT).show();
                                }

                            });


                }


            }

        });

        register.setOnClickListener(view -> {
            Intent i =new Intent(LoginpageActivity2.this,registerpage.class);
            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

        });


}

    private void onAuthSuccess(FirebaseUser user) {
        if (user != null) {
            ref = FirebaseDatabase.getInstance().getReference("Userdata").child(user.getUid());
            System.out.println(ref);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    startActivity(new Intent(LoginpageActivity2.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

                    finish();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
    }