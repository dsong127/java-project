package edu.pdx.cs410j.dsong.androidairline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import edu.pdx.cs410J.AirportNames;

public class NewFlightActivity extends AppCompatActivity {

    Spinner fromSpinner;
    Spinner toSpinner;
    EditText airlineInput;
    EditText flightNumInput;
    EditText deptDatePicker;
    EditText arrDatePicker;

    Flight newFlight;
    Airline newAirline;

    /**
     * Reads input from each field and creates a new Flight, and adds it to the specified Airline.
     * If specified Airline doesn't exists, a new one is created.
     * It'll then fetch the specified Airline from the DataStore, and update it accordingly.
     *  (Or add if it doesn't exist)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_flight);
        setTitle("Add a new flight");

        fromSpinner = (Spinner) findViewById(R.id.newFromSpinner);
        toSpinner = (Spinner) findViewById(R.id.newToSpinner);
        airlineInput = (EditText) findViewById(R.id.airlineInput);
        flightNumInput = (EditText) findViewById(R.id.flightNumberInput);
        deptDatePicker = (EditText) findViewById(R.id.departureDatePicker);
        arrDatePicker = (EditText) findViewById(R.id.arrivalDatePicker);
        final Button newFlightButton = (Button) findViewById(R.id.newFlightButton);

        final Map<String, String> airportMap = AirportNames.getNamesMap();

        String[] airportList = airportMap.keySet().toArray(new String[airportMap.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, airportList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        airlineInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        flightNumInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        deptDatePicker.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        arrDatePicker.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard(v);
                }
            }
        });

        newFlightButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String airlineName = null;
                String flightNumber = null;
                String src;
                String dDate = null;
                String dest;
                String aDate = null;

                if(TextUtils.isEmpty(airlineInput.getText())){
                    showMessage(v, "Error: No airline");
                    return;
                } else {
                    airlineName = airlineInput.getText().toString();
                }

                if(TextUtils.isEmpty(flightNumInput.getText())){
                    showMessage(v, "Error: No flight number");
                    return;
                } else {
                    flightNumber = flightNumInput.getText().toString();
                }

                if(TextUtils.isEmpty(deptDatePicker.getText())){
                    showMessage(v, "Error: No departure date");
                    return;
                } else {
                    dDate = deptDatePicker.getText().toString();
                }

                if(TextUtils.isEmpty(arrDatePicker.getText())){
                    showMessage(v, "Error: No arrival date");
                    return;
                } else {
                    aDate = arrDatePicker.getText().toString();
                }

                src = fromSpinner.getSelectedItem().toString();
                dest = toSpinner.getSelectedItem().toString();

                try {
                    newFlight = new Flight(flightNumber, src, dDate, dest, aDate);
                    Map<String, Airline> airlineMap = DataStore.readData(NewFlightActivity.this);
                    Airline tempAirline;

                    if (airlineMap.containsKey(airlineName)) {
                        tempAirline = airlineMap.get(airlineName);
                        tempAirline.addFlight(newFlight);
                    } else {
                        tempAirline = new Airline(airlineName);
                        tempAirline.addFlight(newFlight);
                    }

                    airlineMap.put(airlineName, tempAirline);

                    DataStore.writeData(airlineMap, NewFlightActivity.this);

                    Toast.makeText(getApplicationContext(), "New flight added", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NewFlightActivity.this, MainActivity.class);
                    intent.putExtra("newAirline", newAirline);
                    startActivityForResult(intent, 1);

                } catch (IllegalArgumentException | IllegalStateException e){
                    showMessage(v, e.getMessage());
                }
            }
        });
    }

    /**
     * Dismiss keyboard by tapping outside outside of TextEdit
     *
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Show Snackbar containing a message. Mostly used for displaying error message in this program
     * @param view View to find parent layout from
     * @param msg Message to display
     */
    public void showMessage(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }
}
