package com.emrey.a4pro;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private TextInputEditText email_text_jav;
    private TextInputEditText pass_text_jav;
    private Button button_validate_jav;
    private int counter = 4;

    private FirebaseAuth dblog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_text_jav = findViewById(R.id.email_text);
        pass_text_jav = findViewById(R.id.pass_text);
        button_validate_jav = findViewById(R.id.button_validate);

        email_text_jav.addTextChangedListener(ConfirmLogin);
        pass_text_jav.addTextChangedListener(ConfirmLogin);


        dblog = FirebaseAuth.getInstance();


        button_validate_jav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_text_jav.getText().toString();
                String password = pass_text_jav.getText().toString();
                dblog.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Login.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                                    counter=4;
                                    Intent intent = new Intent(Login.this, User.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                                else {
                                    counter --;
                                    Toast.makeText(Login.this, "Attempts remaining : " + String.valueOf(counter), Toast.LENGTH_SHORT).show();

                                    if (counter == 0)
                                    {
                                        button_validate_jav.setEnabled(false);
                                    }
                                }

                            }
                        });
            }
        });
    }

    private TextWatcher ConfirmLogin = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String emailINPUT = email_text_jav.getText().toString().trim();
            String passINPUT = pass_text_jav.getText().toString().trim();
            button_validate_jav.setEnabled(!emailINPUT.isEmpty() && !passINPUT.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}

