package edu.pdx.cs410j.dsong.androidairline;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for persisting data
 * Uses FileInputStream and FileOutputStream to read andwrite to a file within the FilesDir directory
 */
public class DataStore {

    public static final String FILENAME = "airlineList.dat";

    /**
     * Writes a Map of Airlines and writes to file
     */
    public static void writeData(Map<String, Airline> airlineMap, Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream ops = new ObjectOutputStream(fos);
            ops.writeObject(airlineMap);
            ops.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads and returns a Map containing all Airlines.
     * If file is not found, ie, first time running the app, it'll create a new empty Airline Map
     */
    public static Map<String, Airline> readData(Context context) {
        Map<String, Airline> airlineMap = null;
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            airlineMap = (Map<String,Airline>) ois.readObject();
        } catch (FileNotFoundException e) {
            airlineMap = new HashMap<>();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return airlineMap;
    }
}
