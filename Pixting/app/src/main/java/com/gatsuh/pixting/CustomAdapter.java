package com.gatsuh.pixting;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import org.w3c.dom.Text;

/**
 * Created by mark on 12/14/2015.
 */
public class CustomAdapter extends ParseQueryAdapter<ParseObject> {
    public CustomAdapter(Context context){
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>(){
            public ParseQuery create(){
                ParseQuery query = new ParseQuery("Gallery");
                query.whereEqualTo("Test", "Mark");
                return query;
            }
        });
    }

    public View getItemView(ParseObject object, View v, ViewGroup parent){
        if (v == null){
            v = View.inflate(getContext(), R.layout.activity_gallery, null);
        }

        super.getItemView(object, v, parent);

        ParseImageView galleryImage = (ParseImageView)v.findViewById(R.id.icon);
        ParseFile imageFile = object.getParseFile("Picture");
        if (imageFile != null){
            galleryImage.setParseFile(imageFile);
            galleryImage.loadInBackground();
        }

        TextView nameTextView = (TextView)v.findViewById(R.id.text1);
        nameTextView.setText(object.getString("Test"));

        TextView timeStampView = (TextView)v.findViewById(R.id.timeStamp);
        timeStampView.setText(object.getCreatedAt().toString());
        return v;
    }
}
