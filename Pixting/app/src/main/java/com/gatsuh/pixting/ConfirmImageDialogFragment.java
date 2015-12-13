package com.gatsuh.pixting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by mark on 12/7/2015.
 */
public class ConfirmImageDialogFragment extends DialogFragment {

    public ConfirmImageDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setMessage("Confirm Image Text");
        View v = inflater.inflate(R.layout.confirm_image_text, null);
        builder.setView(v);
        Bitmap bitmapImage = this.getArguments().getParcelable("Image");
        ImageView IMG_Confirm_Image_Text = (ImageView)v.findViewById(R.id.img_confirm_image_text);
        IMG_Confirm_Image_Text.setImageBitmap(bitmapImage);
        builder.setPositiveButton("Looks Good", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
