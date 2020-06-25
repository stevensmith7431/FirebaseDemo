package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    TextView signup;
    EditText emailid;
    EditText password;
    Button login;
    Button resend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailid = findViewById(R.id.userid);
        password = findViewById(R.id.passid);
        login = findViewById(R.id.loginid);
        resend = findViewById(R.id.resentid);

        firebaseAuth = FirebaseAuth.getInstance();
        signup = findViewById(R.id.signupid);

        if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()){

            Intent intent = new Intent(Login.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Login.this,SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s_email = emailid.getText().toString();
                String s_pass = password.getText().toString();

                if (!TextUtils.isEmpty(s_email) && !TextUtils.isEmpty(s_pass)){

                    firebaseAuth.signInWithEmailAndPassword(s_email,s_pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()){

                                        if (firebaseAuth.getCurrentUser().isEmailVerified()){

                                            Intent intent = new Intent(Login.this,MainActivity.class);
                                            startActivity(intent);
                                            finish();

                                        } else {

                                            resend.setVisibility(View.VISIBLE);
                                            resend.setClickable(true);
                                            Toast.makeText(Login.this, "Please verify your email and continue", Toast.LENGTH_SHORT).show();

                                        }
                                    } else {

                                        Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else {

                    Toast.makeText(Login.this, "email and password should not empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (firebaseAuth != null){

                    if (firebaseAuth.getCurrentUser() != null){

                        firebaseAuth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){

                                            Toast.makeText(Login.this, "Link has sent to your email" +
                                                    "please verify your email and continue", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }

            }
        });
    }
}
