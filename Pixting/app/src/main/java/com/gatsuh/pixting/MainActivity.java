package com.gatsuh.pixting;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {
    final int CAMERA_DATA = 0;
    Button newPix, login, register;
    Bitmap bmp;
    ImageView img;
    ParseObject testObject = new ParseObject("TestObject");
    ParseObject user = new ParseObject("UserCredentials");
    Credentials mCredentials = new Credentials();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, mCredentials.getApplicationId(), mCredentials.getClientKey());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        newPix = (Button)findViewById(R.id.btnnew);
        newPix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "New Pix activity started", Toast.LENGTH_SHORT).show();
                try {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(i, CAMERA_DATA);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    img.setImageResource(R.drawable.sliced_bread_award_test);
                }
            }
        });

        register = (Button)findViewById(R.id.btn_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setTitle("Login");
                dialog.setContentView(R.layout.login);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                final EditText EDT_UserName = (EditText)dialog.findViewById(R.id.edt_userName);
                final EditText EDT_Password = (EditText)dialog.findViewById(R.id.edt_password);
                final EditText EDT_New_UserName = (EditText)dialog.findViewById(R.id.edt_new_username);
                final EditText EDT_New_Password = (EditText)dialog.findViewById(R.id.edt_new_password);
                final EditText EDT_New_UserEmail = (EditText)dialog.findViewById(R.id.edt_new_useremail);

                Button register = (Button)dialog.findViewById(R.id.btn_register);
                Button login = (Button)dialog.findViewById(R.id.btn_login_dialog);

                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!(EDT_UserName.equals(null) && !(EDT_Password.equals(null)))) {
                            ParseUser.logInInBackground(EDT_UserName.getText().toString(),
                                    EDT_Password.getText().toString(), new LogInCallback() {
                                        @Override
                                        public void done(ParseUser user, ParseException e) {
                                            if (user != null){
                                                Toast.makeText(getApplicationContext(),"You are logged in",
                                                        Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(getApplicationContext(),"You must register",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                            );
                        }
                    }
                });

                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!(EDT_New_UserName.equals(null) && !(EDT_New_Password.equals(null)))){
                            ParseUser user = new ParseUser();
                            user.setUsername(EDT_New_UserName.getText().toString());
                            user.setPassword(EDT_New_Password.getText().toString());
                            user.setEmail(EDT_New_UserEmail.getText().toString());
                            user.signUpInBackground(new SignUpCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null){
                                        Toast.makeText(MainActivity.this, "Signed up", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(MainActivity.this, "Sign up was unsuccessful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else if (EDT_New_UserName.equals(null)){
                            Toast.makeText(getApplicationContext(),"Enter a user name",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(),"Enter a password",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        img = (ImageView)findViewById(R.id.mainimage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_DATA && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            bmp = (Bitmap)extras.get("data");
            img.setImageBitmap(bmp);
            testObject.add("Image", bmp);
            testObject.saveInBackground();
        }
    }
}
