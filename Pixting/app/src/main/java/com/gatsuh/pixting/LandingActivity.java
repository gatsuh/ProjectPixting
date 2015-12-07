package com.gatsuh.pixting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;

public class LandingActivity extends AppCompatActivity {
    Credentials mCredentials = new Credentials();
    EditText EDT_Login, EDT_Password;
    Button BTN_Login, BTN_Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, mCredentials.getApplicationId(), mCredentials.getClientKey());

        EDT_Login = (EditText)findViewById(R.id.edt_login);
        EDT_Password = (EditText)findViewById(R.id.edt_password);
        BTN_Login = (Button)findViewById(R.id.btn_login);
        BTN_Register = (Button)findViewById(R.id.btn_register);

        BTN_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(EDT_Login.getText().toString(),
                        EDT_Password.getText().toString(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if (user != null){
                                    Toast.makeText(LandingActivity.this, "Redirecting",
                                            Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                }else{
                                    Toast.makeText(LandingActivity.this, "Login failed",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        BTN_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new RegisterDialogFragment();
                dialog.show(getFragmentManager(), "RegisterDialogFragment");
            }
        });
    }
}
