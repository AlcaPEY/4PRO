package com.emrey.a4pro;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Add extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //         "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    //          "(?=\\S+$)" +           //no white spaces
                    ".{5,}" +               //at least 5 characters
                    "$");

    private TextInputEditText txt_password_jav, txt_password_v_jav,
            txt_mail_jav, txt_lastname_jav, txt_name_jav, txt_age_jav;
    private Button button_validate_jav;

    private FirebaseAuth dblog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        txt_password_jav = findViewById(R.id.txt_pass);
        txt_password_v_jav = findViewById(R.id.txt_pass_v);
        txt_mail_jav = findViewById(R.id.txt_mail);
        txt_lastname_jav = findViewById(R.id.txt_lastname);
        txt_name_jav = findViewById(R.id.txt_name);
        txt_age_jav = findViewById(R.id.txt_age);

        button_validate_jav = findViewById(R.id.button);

        dblog = FirebaseAuth.getInstance();

        txt_password_jav.addTextChangedListener(ConfirmRegistration);
        txt_password_v_jav.addTextChangedListener(ConfirmRegistration);

        txt_mail_jav.addTextChangedListener(ConfirmRegistration);
        txt_lastname_jav.addTextChangedListener(ConfirmRegistration);
        txt_name_jav.addTextChangedListener(ConfirmRegistration);
        txt_age_jav.addTextChangedListener(ConfirmRegistration);
        //    radioButton.addTextChangedListener(ConfirmRegistration);



        button_validate_jav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup);
                final String radiovalue = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();

                final String password = txt_password_jav.getText().toString();
                final String password_v = txt_password_v_jav.getText().toString();
                final String mail = txt_mail_jav.getText().toString();
                final String lastname = txt_lastname_jav.getText().toString();
                final String name= txt_name_jav.getText().toString();
                final String age = txt_age_jav.getText().toString();



                Query usernameQuery = FirebaseDatabase.getInstance().getReference()
                        .child("Users").orderByChild("EMAIL").equalTo(mail);

                usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount()>0){
                            Toast.makeText(Add.this,
                                    "Your email address already exist", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            dblog.createUserWithEmailAndPassword(mail, password)
                                    .addOnCompleteListener(Add.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()){
                                                Toast.makeText(Add.this,"Sign up error",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                String user_id = dblog.getCurrentUser().getUid();
                                                final DatabaseReference current_user_db = FirebaseDatabase.
                                                        getInstance().getReference().child("Users").child(user_id);

                                                Map newPost = new HashMap();
                                                newPost.put("id", user_id);
                                                newPost.put("PASSWORD", password);
                                                newPost.put("EMAIL", mail);
                                                newPost.put("SEXE", radiovalue);
                                                newPost.put("LASTNAME", lastname);
                                                newPost.put("NAME", name);
                                                newPost.put("AGE", age);

                                                current_user_db.setValue(newPost);

                                                Toast.makeText(Add.this,
                                                        "Sign up successful WELCOME", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(Add.this, Login.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);

                                            }

                                        }
                                    });
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });






    }

    private boolean ValidateEmail(){
        String mINPUT = txt_mail_jav.getText().toString().trim();

        if (mINPUT.isEmpty()){
            txt_mail_jav.setError("Field can't be empty");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(mINPUT).matches()){
            txt_mail_jav.setError("Please enter a valid email address");
            return false;
        }
        else {
            txt_mail_jav.setError(null);
            return true;
        }
    }

    private boolean ValidatePassword(){
        String pINPUT= txt_password_jav.getText().toString().trim();
        String pvINPUT= txt_password_v_jav.getText().toString().trim();

        if (pINPUT.isEmpty()){
            txt_password_jav.setError("Field can't be empty");
            return false;
        }
        else if (pvINPUT.isEmpty()){
            txt_password_v_jav.setError("Field can't be empty");
            return false;
        }
        else if (!PASSWORD_PATTERN.matcher(pINPUT).matches()){
            txt_password_jav.setError("Please enter a password with at least one letter and number");
            return false;
        }
        else if (!pvINPUT.equals(pINPUT)){
            txt_password_v_jav.setError("Confirmation password not correct");
            return false;
        }
        else {
            txt_password_jav.setError(null);
            txt_password_v_jav.setError(null);
            return true;
        }
    }

    private boolean ValidateNameAge() {
        String lINPUT = txt_lastname_jav.getText().toString().trim();
        String nINPUT = txt_name_jav.getText().toString().trim();
        String aINPUT = txt_age_jav.getText().toString().trim();

        if (lINPUT.isEmpty()) {
            txt_lastname_jav.setError("Field can't be empty");
            return false;
        } else if (nINPUT.isEmpty()) {
            txt_name_jav.setError("Field can't be empty");
            return false;
        } else if (aINPUT.isEmpty()) {
            txt_age_jav.setError("Field can't be empty");
            return false;
        } else if (Integer.parseInt(aINPUT) <= 17) {
            txt_age_jav.setError("You must be 18 or over 18 to register");
            return false;
        } else {
            txt_lastname_jav.setError(null);
            txt_name_jav.setError(null);
            txt_age_jav.setError(null);
            return true;
        }
    }

    private TextWatcher ConfirmRegistration = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            button_validate_jav.setEnabled(
                    ValidateEmail()
                            && ValidatePassword()
                            && ValidateNameAge());
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            button_validate_jav.setEnabled(
                    ValidateEmail()
                            && ValidatePassword()
                            && ValidateNameAge());
        }

        @Override
        public void afterTextChanged(Editable s) {
            button_validate_jav.setEnabled(
                    ValidateEmail()
                            && ValidatePassword()
                            && ValidateNameAge());
        }
    };
}

