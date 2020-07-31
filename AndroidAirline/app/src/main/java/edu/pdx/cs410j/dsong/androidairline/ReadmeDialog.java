package edu.pdx.cs410j.dsong.androidairline;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ReadmeDialog extends AppCompatDialogFragment {

    /**
     *  Displays a README by using Dialog class
     *
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        String readmeText = "Airline android app for creating, adding, viewing, and searching flights in an Airline.\n\n" +
                "To add a new flight use the + New Flight button at the bottom in the main page\n\n" +
                "To view all flights in an Airline, enter Airline name and press submit. \n\n" +
                "To search flights, enter Airline name, src, and destination airport. \n\n" +
                "To dismiss the keyboard just tap anywhere outside the EditText box";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("README")
                .setMessage(readmeText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}
