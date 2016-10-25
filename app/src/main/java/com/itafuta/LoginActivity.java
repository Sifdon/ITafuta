package com.itafuta;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.emmasuzuki.easyform.EasyForm;
import com.emmasuzuki.easyform.EasyFormEditText;
import com.emmasuzuki.easyform.EasyTextInputLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    Button btnLogin;

    EasyFormEditText editLoginEmail;
    EasyFormEditText editLoginPassword;
    TextView txtToRegister;
    TextView txtWithoutSignIn;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "LoginActivity");

        mAuth = FirebaseAuth.getInstance();

        editLoginEmail = (EasyFormEditText) findViewById(R.id.edit_login_email);
        editLoginPassword = (EasyFormEditText) findViewById(R.id.edit_login_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        txtToRegister =(TextView) findViewById(R.id.txt_tocreate_account);
        txtWithoutSignIn = (TextView) findViewById(R.id.txt_dontLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        txtToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Going to Register activity!");
                Intent toRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(toRegister);
            }
        });

        txtWithoutSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Going main without signing in!");
                Intent toMainWithNoSignIn = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(toMainWithNoSignIn);
            }
        });
    }

    public void login(){
        Log.d(TAG, "Authenticating login details");

        String email = editLoginEmail.getText().toString();
        String password = editLoginPassword.getText().toString();

        email = email.trim();
        password = password.trim();

        if (email.isEmpty() || password.isEmpty()) {
            Log.d(TAG, "Either password of email field is not filled");
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage("Error")
                    .setTitle("Fill all required fields")
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //If login fails
                            if (!task.isSuccessful()) {
                                Log.d(TAG, "Authentication failed");
                                //Toast.makeText(LoginActivity.this, "Auth failed", Toast.LENGTH_SHORT).show();
                            }
                            //if login succeed
                            else{
                                Log.d(TAG, "Authenticated successfully. Going to Main activity");
                                Intent toMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(toMainActivity);
                            }
                        }
                    });

        }
    }
}
