package com.mycompany.wearapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Displays the available rooms list for RoomSurfer's Wear application
 * @author Ryan Burns
 */
public class WearActivity extends Activity {
    /**
     * The list view of the layout which holds the available rooms list
     */
    private WearableListView listView;

    /**
     * Room data held in the list
     */
    private static ArrayList<ListEntry> entries;

    /**
     * Sets the activity's layout, clears past filters on the list, filters the list if there
     *     was a passed in intent to do so, adds a listener to the list's elements, and creates
     *     a dialog to tell the user if there are no available rooms which meet the criteria
     * @param savedInstanceState I'm assuming this opens the activity in the state it was last in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Clear past filters
        setupEntries();

        // Filter the list if applicable
        Intent intent = getIntent();
        String[] response = intent.getStringArrayExtra("Choices");
        if (response != null) {
            filterEntries(response[0], response[1]);
        }

        setContentView(R.layout.activity_main);

        // Get the list component from the layout of the activity
        listView = (WearableListView) findViewById(R.id.wearable_list);
        listView.scrollToPosition(1);

        // Create a dialog for the user if there are no rooms for the given criteria
        if (entries.size() == 0) {
            Context context = getApplicationContext();
            CharSequence text = "No such rooms available!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
        }

        // Assign an adapter to the list
        listView.setAdapter(new Adapter(
                entries.toArray(new ListEntry[entries.size()]), LayoutInflater.from(this)));
    }

    /**
     * This simulates the API call, will eventually be deleted
     */
    private void setupEntries() {
        entries = new ArrayList<>();
        entries.add(new ListEntry(2, 2000, 12));
        entries.add(new ListEntry(2, 2001, 1));
        entries.add(new ListEntry(2, 2002, 3));
        entries.add(new ListEntry(2, 2003, 2));
        entries.add(new ListEntry(2, 2004, 5));
        entries.add(new ListEntry(2, 2005, 3));
        entries.add(new ListEntry(2, 2006, 2));
        entries.add(new ListEntry(2, 2007, 5));
        entries.add(new ListEntry(2, 2008, 3));
        entries.add(new ListEntry(3, 1002, 2));
        entries.add(new ListEntry(3, 1018, 5));
        entries.add(new ListEntry(3, 1025, 3));
        entries.add(new ListEntry(3, 1026, 5));
        entries.add(new ListEntry(3, 1030, 5));
        entries.add(new ListEntry(3, 1031, 2));
    }

    /**
     * Filters the entries based on the user's inputted parameters
     * @param capacity The minimum capacity of a valid room
     * @param floor The floor of a valid room
     */
    private void filterEntries(String capacity, String floor) {
        for (int i = 0; i < entries.size(); i++) {
            boolean keep = true;
            if (!capacity.equals("N/A")) {
                // Remove any entries that do not meet the minimum capacity specified
                if (entries.get(i).getCapacity() < Integer.parseInt(capacity)) {
                    keep = false;
                }
            }
            if (!floor.equals("N/A") && keep) {
                // Remove any entries that are not on the floor specified
                if (entries.get(i).getFloor() != Integer.parseInt(floor)) {
                    keep = false;
                }
            }
            if (!keep) {
                entries.remove(i);
                i--;
            }
        }
    }

    /**
     * Called when the user selects the refresh button, refreshes the list of rooms from an API call
     *     and then reloads the main screen of the app
     * @param view Not used
     */
    public void onRefresh(View view) {
        // TODO: Update the data set from the API call
        setupEntries();

        // Reload the list
        listView.setAdapter(new Adapter(
                entries.toArray(new ListEntry[entries.size()]), LayoutInflater.from(this)));
        listView.scrollToPosition(1);
    }

    /**
     * Called when the user selects the filter button, should switch activities to provide filtering
     *    options to the user (by capacity and/or by floor)
     * @param view Not used
     */
    public void onFilter(View view) {
        Intent intent = new Intent(this, FilterActivity.class);
        startActivity(intent);
    }
}