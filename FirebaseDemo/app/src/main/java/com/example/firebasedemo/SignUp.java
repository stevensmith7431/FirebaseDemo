package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText emailid;
    EditText password;
    Button signup;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        emailid = findViewById(R.id.emailid);
        password = findViewById(R.id.passwordid);
        signup = findViewById(R.id.signupid);

        progressDialog = new ProgressDialog(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s_email = emailid.getText().toString();
                String s_password = password.getText().toString();

                if (!TextUtils.isEmpty(s_email) && !TextUtils.isEmpty(s_password)){

                    progressDialog.setMessage("Registering please wait....");
                    progressDialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(s_email,s_password)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()){

                                        firebaseAuth.getCurrentUser().sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()){

                                                            Intent intent = new Intent(SignUp.this,Login.class);
                                                            Toast.makeText(SignUp.this, "User Successfully Created" +
                                                                    "link send to your mailid", Toast.LENGTH_SHORT).show();
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                        else {

                                                            Toast.makeText(SignUp.this, "Error in sending link..try again later", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        firebaseAuth.signOut();
                                    }
                                    else {
                                        Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    progressDialog.dismiss();

                                }
                            });

                } else {

                    Toast.makeText(SignUp.this, "Username and Password should not empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
