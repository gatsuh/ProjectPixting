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
        //Parse.enableLocalDatastore(this);

        //Parse.initialize(this, mCredentials.getApplicationId(), mCredentials.getClientKey());

        setContentView(R.layout.activity_main);


        newPix = (Button)findViewById(R.id.btnnew);
        newPix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "New Pix activity started", Toast.LENGTH_SHORT).show();
                try {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, CAMERA_DATA);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    img.setImageResource(R.drawable.sliced_bread_award_test);
                }
            }
        }); */


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

//I HATE GITHUB
//Me too
