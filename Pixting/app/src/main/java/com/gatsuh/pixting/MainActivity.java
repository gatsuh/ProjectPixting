package com.gatsuh.pixting;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.Resources;
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
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
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
import com.parse.ParseQueryAdapter;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity
                          implements SetTextDialogFragment.SetTextDialogListener{
    final int CAMERA_DATA = 0;
    Button newPix, viewGallery;
    Bitmap bmp;
    ImageView img;
    Bitmap newBmp;
    BitmapFactory.Options options = new BitmapFactory.Options();
    private ParseQueryAdapter<ParseObject> mainAdapter;
    private ListView listView;

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
                Intent g = new Intent(MainActivity.this, GalleryActivity.class);
                startActivity(g);
            }
        });

        mainAdapter = new ParseQueryAdapter<ParseObject>(this, "Gallery");
        mainAdapter.setTextKey("title");
        mainAdapter.setImageKey("image");

        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(mainAdapter);
        mainAdapter.loadObjects();
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

    public void SendToParse(Bitmap bmp) {
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

    @Override
    public void onDialogPositiveClick(String imageText) {
        // Stuff to write on Bitmap goes here
        Toast.makeText(MainActivity.this, "Text to be added to Bitmap is " + imageText,
                Toast.LENGTH_SHORT).show();
        newBmp = writeTextOnBitmap(getApplicationContext(),
                bmp, imageText);
        img.setImageBitmap(newBmp);

        Bundle b = new Bundle();
        b.putParcelable("Image", newBmp);

        DialogFragment dialog = new ConfirmImageDialogFragment();
        dialog.setArguments(b);

        dialog.show(getFragmentManager(), "Confirm Image Text");

        SendToParse(newBmp);
    }

    public Bitmap writeTextOnBitmap(Context gContext, Bitmap bmp, String gText){
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = bmp;
        android.graphics.Bitmap.Config bitmapConfig =
                bitmap.getConfig();

        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }

        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setTextSize( 10 * scale);
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width())/2;
        int y = (bitmap.getHeight() + bounds.height())/2;

        canvas.drawText(gText, x, y, paint);

        return bitmap;
    }
}