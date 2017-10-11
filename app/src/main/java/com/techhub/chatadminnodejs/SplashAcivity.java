package com.techhub.chatadminnodejs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SplashAcivity extends AppCompatActivity {
    private DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;
    Button btnlogin;
    EditText edtmail,edtpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_acivity);
        btnlogin=(Button)findViewById(R.id.btnlogin);
        edtmail=(EditText)findViewById(R.id.edtemail);
        edtpass=(EditText)findViewById(R.id.edtpass);
        mAuth=FirebaseAuth.getInstance();












        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




            }
        });
        String usn="thiennhat27031996@gmail.com";//edtmail.getText().toString();
        String pass="1312131213";//edtpass.getText().toString();
        resger( usn, pass);




    }

    private void resger( String email, String pass) {

        mAuth=FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                            Toast.makeText(SplashAcivity.this, "false",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {


                            Toast.makeText(SplashAcivity.this, "ok",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SplashAcivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.


                        // ...
                    }
                });

    }
}
