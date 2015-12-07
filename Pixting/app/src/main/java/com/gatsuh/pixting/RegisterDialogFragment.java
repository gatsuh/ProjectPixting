package com.gatsuh.pixting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by mark on 12/7/2015.
 */
public class RegisterDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText EDT_New_UserName = (EditText)getDialog().findViewById(R.id.edt_new_username);
        final EditText EDT_New_Password = (EditText)getDialog().findViewById(R.id.edt_new_password);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setMessage("Login")
                .setView(inflater.inflate(R.layout.register_dialog, null))
                .setPositiveButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!(EDT_New_UserName.equals(null) && !(EDT_New_Password.equals(null)))){
                                    ParseUser user = new ParseUser();
                                    user.setUsername(EDT_New_UserName.getText().toString());
                                    user.setPassword(EDT_New_Password.getText().toString());
                                    //user.setEmail(EDT_New_UserEmail.getText().toString());
                                    user.signUpInBackground(new SignUpCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null){
                                                Log.d("Test","It works");
                                                //Toast.makeText(MainActivity.this, "Signed up", Toast.LENGTH_SHORT).show();
                                            }else {
                                                //Toast.makeText(MainActivity.this, "Sign up was unsuccessful", Toast.LENGTH_SHORT).show();
                                                Log.d("problem", e.toString());
                                            }
                                        }
                                    });
                                }else if (EDT_New_UserName.equals(null)){
                                    //Toast.makeText(getApplicationContext(),"Enter a user name",Toast.LENGTH_SHORT).show();
                                }else {
                                    //Toast.makeText(getApplicationContext(),"Enter a password",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}
