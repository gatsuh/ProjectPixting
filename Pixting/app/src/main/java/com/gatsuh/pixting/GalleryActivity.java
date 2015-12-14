package com.gatsuh.pixting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class GalleryActivity extends AppCompatActivity {
    ParseQueryAdapter<ParseObject> mainAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mainAdapter = new ParseQueryAdapter<>(this, "Gallery");
        mainAdapter.setTextKey("Test");
        mainAdapter.setImageKey("Picture");

        //mainAdapter = new CustomAdapter(this);

        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(mainAdapter);
        mainAdapter.loadObjects();

    }

}
