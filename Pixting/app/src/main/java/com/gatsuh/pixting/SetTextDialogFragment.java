package com.gatsuh.pixting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by mark on 12/13/2015.
 */
public class SetTextDialogFragment extends DialogFragment {
    SetTextDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (SetTextDialogListener)activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() +
                    " must implement SetTextDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setMessage("Set Image Text");
        View v = inflater.inflate(R.layout.set_image_text, null);
        builder.setView(v);
        final EditText EDT_Set_Image_Text = (EditText)v.findViewById(R.id.edt_image_text);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogPositiveClick(EDT_Set_Image_Text.getText().toString());

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    public interface SetTextDialogListener {
        public void onDialogPositiveClick(String imageText);
    }
}
