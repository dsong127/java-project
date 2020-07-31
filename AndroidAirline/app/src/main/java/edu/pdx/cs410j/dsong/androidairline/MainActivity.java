package edu.pdx.cs410j.dsong.androidairline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import edu.pdx.cs410J.AirportNames;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Map<String, Airline> airlineMap;

    private CustomAdapter adapter;
    private EditText airlineNameInput;
    ArrayList<Flight> flights;
    Spinner fromSpinner;
    Spinner toSpinner;
    ListView flightsList;
    final Map<String, String> airportMap = AirportNames.getNamesMap();


    /**
     * Read existing data from DataStore, and checks if the Submit button is pressed.
     * If pressed, parses data from the input fields, and performs either Airline query, or
     * Flight search.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fromSpinner = (Spinner) findViewById(R.id.from_spinner);
        toSpinner = (Spinner) findViewById(R.id.to_spinner);
        airlineNameInput = (EditText) findViewById(R.id.edit_text_airlineName);
        Button addButton = (Button) findViewById(R.id.addButton);
        Button submitButton = (Button) findViewById(R.id.submitButton);
        flightsList = (ListView) findViewById(R.id.flightsList);

        airlineMap = DataStore.readData(this);


        String[] emptyStringList = {""};
        String[] keysArr = airportMap.keySet().toArray(new String[airportMap.size()]);

        ArrayList<String> airportList = new ArrayList<>(Arrays.asList(emptyStringList));
        airportList.addAll(Arrays.asList(keysArr));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, airportList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        airlineNameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        addButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
    }


    /**
     * Opens a new Activity for adding new Flights
     */
    public void openNewFlightActivity() {
        Intent intent = new Intent(this, NewFlightActivity.class);
        startActivity(intent);
    }

    /**
     * Call to hide the keyboard
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Called when a Button is pressed. If it's "+ New Flight" Button, a new Activity is opened.
     * If it's submitButton, it queries all Flights in an Airline. If source and destination
     * airports are specified, search function is triggered.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.addButton:
                openNewFlightActivity();
                break;
            case R.id.submitButton:
                String airlineName;

                if(!TextUtils.isEmpty(airlineNameInput.getText().toString())){
                    airlineName = airlineNameInput.getText().toString();
                    Airline airline = airlineMap.get(airlineName);
                    hideKeyboard(v);

                    if(!fromSpinner.getSelectedItem().toString().equals("") &&
                            !toSpinner.getSelectedItem().toString().equals("")){

                        String src = fromSpinner.getSelectedItem().toString();
                        String dest = toSpinner.getSelectedItem().toString();

                        // Search
                        Airline dummyAirline = new Airline(airlineName);

                        if(airline != null) {
                            LinkedHashSet<Flight> allFlights = airline.getFlights();
                            // Iterate through each flight from specified Airline.
                            // Find matching flights and add to dummyAirline
                            for (Flight flight : allFlights) {
                                if (flight.getSource().equals(src) && flight.getDestination().equals(dest)) {
                                    dummyAirline.addFlight(flight);
                                }
                            }

                            if(!dummyAirline.getFlights().isEmpty()){
                                flights = dummyAirline.getSortedFlightsAsList();
                                displayFlights(flights);
                            } else {
                                Snackbar.make(v, "No flights from " + src + " to " + dest, Snackbar.LENGTH_LONG).show();
                            }
                        } else {
                            Snackbar.make(v, "Airline " + airlineName + " doesn't exist", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        // Just display all flights in an Airline
                        if (airline != null) {
                            flights = airline.getSortedFlightsAsList();
                            displayFlights(flights);
                        } else {
                            Snackbar.make(v, "Airline " + airlineName + " doesn't exist", Snackbar.LENGTH_LONG).show();

                        }
                    }

                } else {
                    Snackbar.make(v, "Mising airline name", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    /**
     * Takes in a custom Adapter and displays a list of Flights
     * @param flightsData ArrayList of Flights to be displayed
     */
    public void displayFlights(ArrayList<Flight> flightsData){
        adapter = new CustomAdapter();
        flightsList.setAdapter(adapter);
    }

    /**
     * Show the Menu button
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Display the Dialog containing README when the README button is pressed
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.readmeButton) {
            openReadme();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Function for displaying Dialog containing README
     */
    public void openReadme() {
        ReadmeDialog readmeDialog = new ReadmeDialog();
        readmeDialog.show(getSupportFragmentManager(), "test dialog");
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return flights.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        /**
         * Custom view for pretty printing Flights
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.custom_layout, null);

            TextView flightNumber = (TextView) convertView.findViewById(R.id.flightNumberLabel);
            TextView duration = (TextView) convertView.findViewById(R.id.durationLabel);
            TextView dept = (TextView) convertView.findViewById(R.id.depLabel);
            TextView arr = (TextView) convertView.findViewById(R.id.arrLabel);

            String number = String.valueOf(flights.get(position).getNumber());
            long diff = flights.get(position).getArrival().getTime() - flights.get(position).getDeparture().getTime();
            long dur = TimeUnit.MILLISECONDS.toMinutes(diff);
            String deptString = String.format(Locale.US, "%s, %tB %<te, %<tY %tT %Tp",
                    airportMap.get(flights.get(position).getSource().toUpperCase()),
                    flights.get(position).getDeparture(), flights.get(position).getDeparture(), flights.get(position).getDeparture());

            String arrString = String.format(Locale.US, "%s, %tB %<te, %<tY %tT %Tp",
                    airportMap.get(flights.get(position).getDestination().toUpperCase()),
                    flights.get(position).getArrival(), flights.get(position).getArrival(), flights.get(position).getArrival());

            flightNumber.setText(number);
            duration.setText(dur + " Minutes");
            dept.setText(deptString);
            arr.setText(arrString);

            return convertView;
        }
    }

}
