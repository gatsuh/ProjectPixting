package com.gatsuh.pixting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ParseQuery<ParseObject> query =
                ParseQuery.getQuery("Gallery");
        query.getInBackground("JFH7BfxHmm", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                TextView textView = (TextView)findViewById(R.id.txt_image_owner);
                textView.setText(object.get("Test").toString());
                ParseFile parseFile = object.getParseFile("Picture");
                ParseImageView imageView = (ParseImageView)findViewById(R.id.parse_image_view);
                imageView.setParseFile(parseFile);
                imageView.loadInBackground();
            }
        });

    }

}
