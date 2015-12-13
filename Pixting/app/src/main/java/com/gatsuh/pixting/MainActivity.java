package com.gatsuh.pixting;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity
                          implements SetTextDialogFragment.SetTextDialogListener{
    final int CAMERA_DATA = 0;
    Button newPix, viewGallery;
    Bitmap bmp;
    ImageView img;
    ParseObject testObject = new ParseObject("TestObject");
    ParseObject user = new ParseObject("UserCredentials");
    Credentials mCredentials = new Credentials();
    ParseFile picture;
    Bitmap temp;
    BitmapFactory.Options options = new BitmapFactory.Options();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable Local Datastore.
        //Parse.enableLocalDatastore(this);

        //Parse.initialize(this, mCredentials.getApplicationId(), mCredentials.getClientKey());

        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.main_image);
        options.inJustDecodeBounds = true;

        newPix = (Button) findViewById(R.id.btn_new);
        newPix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, CAMERA_DATA);
                DialogFragment dialog = new SetTextDialogFragment();
                dialog.show(getFragmentManager(), "SetImageTextDialogFragment");
                Log.d("test", "test1");
            }
        });

        viewGallery = (Button) findViewById(R.id.btn_gallery);
        viewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query =
                        ParseQuery.getQuery("Gallery");
                query.getInBackground("QufPpUoHUj", new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), "ParseObject created",
                                    Toast.LENGTH_SHORT).show();
                            picture = (ParseFile) object.get("Picture");
                            picture.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getApplicationContext(), "Life is good",
                                                Toast.LENGTH_SHORT).show();
//First attempt=========================>>temp = BitmapFactory.decodeByteArray(data,0,data.length);
//Second attempt========================>>img.setImageBitmap(decodeSampledBitmapFromData(data, 0, data.length,
                                        //100, 100 ));
                                        img.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length, options));
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Life sucks",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "ParseObject not created",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
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
        if (requestCode == CAMERA_DATA && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            bmp = (Bitmap) extras.get("data");
        }
    }

    public void SendToParse() {
        int bytes = bmp.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        bmp.copyPixelsToBuffer(buffer);
        byte[] array = buffer.array();

        ParseFile file = new ParseFile("Picture", array);
        file.saveInBackground();

        ParseObject object = new ParseObject("Gallery");
        object.put("Test", "Mark");
        object.put("Picture", file);
        object.saveInBackground();
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    @Override
    public void onDialogPositiveClick(String imageText) {
        // Stuff to write on Bitmap goes here
        Toast.makeText(MainActivity.this, "Text to be added to Bitmap is " + imageText,
                Toast.LENGTH_SHORT).show();
        Bitmap newBmp = (writeTextOnBitmap(bmp, imageText).getBitmap());
        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //newBmp .compress(Bitmap.CompressFormat.PNG, 100, stream);
        //byte[] byteArray = stream.toByteArray();

        Bundle b = new Bundle();
        b.putParcelable("Image", newBmp);

        DialogFragment dialog = new ConfirmImageDialogFragment();
        dialog.setArguments(b);
        dialog.show(getFragmentManager(), "Confirm Image Text");

        //SendToParse();
    }

    public BitmapDrawable writeTextOnBitmap(Bitmap bmp, String text){
        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(getApplicationContext(), 11));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0 , text.length(), textRect);

        Canvas canvas = new Canvas();
        canvas.drawBitmap(bmp, 0f, 0f, null);

        if (textRect.width() >= (canvas.getWidth() - 4)) {
            paint.setTextSize(convertToPixels(getApplicationContext(), 7));
        }

        int xPos = (canvas.getWidth() / 2) - 2;

        int yPos = (int)((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

        canvas.drawText(text, xPos, yPos, paint);

        return new BitmapDrawable(getResources(), bmp);
    }

    public static int convertToPixels(Context context, int nDP){
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int)((nDP * conversionScale) + 0.5f);
    }

    /*public static Bitmap decodeSampledBitmapFromData(byte[] data, int offset,
                                                     int lenght, int reqHeight, int reqWidth){
        /*final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, offset, lenght, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, offset, lenght, options);
    }*/
}

//I HATE GITHUB
//Me too
