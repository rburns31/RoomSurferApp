package com.mycompany.wearapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * This activity is the main list screen for RoomSurfer's Android application
 * @author Ryan Burns
 */
public class MainActivity extends ActionBarActivity {
    private Intent filterIntent;
    private Context mainContext;
    private ArrayList<ListEntry> entries;

    /**
     * Sets the activity's layout, clears past filters on the list, filters the list if there
     *     was a passed in intent to do so, adds a listener to the list's elements, and creates
     *     a dialog to tell the user if there are no available rooms which meet the criteria
     * @param savedInstanceState I'm assuming this opens the activity in the state it was last in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainContext = this;

        // Clear past filters
        setupEntries();

        // Filter the list if applicable
        filterIntent = getIntent();
        if (filterIntent.hasExtra("Capacity Choice")) {
            filterEntries(filterIntent.getIntExtra("Capacity Choice", 0),
                    filterIntent.getStringExtra("Floor Choice"),
                    filterIntent.getBooleanExtra("Video Conferencing Choice", false),
                    filterIntent.getBooleanExtra("Projector Choice", false));
        }

        setContentView(R.layout.activity_main);

        // Get the list component from the layout of the activity
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new ListEntryClickListener());

        // Create a dialog for the user if there are no rooms for the given criteria
        if (entries.size() == 0) {
            Toast toast = Toast.makeText(
                    getApplicationContext(), "No such rooms available!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
        }

        // Assign an adapter to the list
        listView.setAdapter(new RoomsArrayAdapter(this, R.layout.list_item, entries));
    }

    /**
     * Add the map, filter and refresh buttons to the ActionBar
     * @param menu The menu of the ActionBar
     * @return true to denote this has been handled
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Map").setIcon(android.R.drawable.ic_menu_mapmode)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, 1, 0, "Filter").setIcon(R.drawable.ic_menu_filter)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, 2, 0, "Refresh").setIcon(R.drawable.ic_menu_refresh)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    /**
     * Handles the response to the map, filter, and refresh buttons on the ActionBar being clicked
     * @param item The item in the menu which was selected
     * @return true to denote this has been handled
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            // Open the map activity and highlight all currently valid rooms
            Intent intent = new Intent(mainContext, MapActivity.class);
            ArrayList<Integer> validRooms = new ArrayList<>();
            for (ListEntry entry : entries) {
                validRooms.add(entry.getRoom());
            }
            intent.putExtra("Valid Rooms", validRooms);
            startActivity(intent);
        } else if (item.getItemId() == 1) {
            // Open the filter activity (with the last filters if there are any)
            Intent intent = new Intent(mainContext, FilterActivity.class);
            if (filterIntent.hasExtra("Capacity Choice")) {
                intent.putExtras(filterIntent.getExtras());
            }
            startActivity(intent);
        }
        // TODO: Update the data set from the API call

        return true;
    }

    /**
     * This simulates the API call, will eventually be deleted
     */
    private void setupEntries() {
        entries = new ArrayList<>();
        entries.add(new ListEntry(2, 2000, 12, true, true));
        entries.add(new ListEntry(2, 2001, 1, false, true));
        entries.add(new ListEntry(2, 2002, 3, false, false));
        entries.add(new ListEntry(2, 2003, 2, false, false));
        entries.add(new ListEntry(2, 2004, 5, true, false));
        entries.add(new ListEntry(2, 2005, 3, false, false));
        entries.add(new ListEntry(2, 2006, 2, false, false));
        entries.add(new ListEntry(2, 2007, 5, false, true));
        entries.add(new ListEntry(2, 2008, 3, true, false));
        entries.add(new ListEntry(3, 1002, 2, false, false));
        entries.add(new ListEntry(3, 1018, 5, true, true));
        entries.add(new ListEntry(3, 1025, 3, false, false));
        entries.add(new ListEntry(3, 1026, 5, false, false));
        entries.add(new ListEntry(3, 1030, 5, false, true));
        entries.add(new ListEntry(3, 1031, 2, false, false));
    }

    /**
     * Filters the entries based on the user's inputted parameters
     * @param capacity The minimum capacity of a valid room
     * @param floor The floor of a valid room
     * @param hasVideoConf The video conferencing abilities of a valid room
     * @param hasProjector The projector status of a valid room
     */
    private void filterEntries(int capacity, String floor,
                               boolean hasVideoConf, boolean hasProjector) {

        for (int i = 0; i < entries.size(); i++) {
            boolean keep = true;
            // Mark any entries that do not meet the minimum capacity specified for deletion
            if (entries.get(i).getCapacity() < capacity) {
                keep = false;
            }

            if (!floor.equals("Any") && keep) {
                // Mark any remaining entries that are not on the floor specified for deletion
                if (entries.get(i).getFloor() != Integer.parseInt(floor)) {
                    keep = false;
                }
            }

            if (hasVideoConf && keep) {
                // Mark any remaining entries that do not have video conferencing for deletion
                if (!entries.get(i).getHasVideoConf()) {
                    keep = false;
                }
            }

            if (hasProjector && keep) {
                // Mark any remaining entries that do not have a projector for deletion
                if (!entries.get(i).getHasProjector()) {
                    keep = false;
                }
            }

            // Delete the item if it was marked for deletion
            if (!keep) {
                entries.remove(i);
                i--;
            }
        }
    }

    /**
     * A listener for any time that an entry in the rooms list is clicked
     */
    private class ListEntryClickListener implements AdapterView.OnItemClickListener {

        /**
         * Open this entry in more detail in EntryDetailsActivity
         * @param arg0 Not used
         * @param arg1 Not used
         * @param position The index into the entries ArrayLst of the clicked entry
         * @param arg3 Not used
         */
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            Intent intent = new Intent(mainContext, EntryDetailsActivity.class);

            intent.putExtra("Room", entries.get(position).getRoom());
            intent.putExtra("Floor", entries.get(position).getFloor());
            intent.putExtra("Capacity", entries.get(position).getCapacity());
            intent.putExtra("VideoConf", entries.get(position).getHasVideoConf());
            intent.putExtra("Projector", entries.get(position).getHasProjector());

            startActivity(intent);
        }
    }
}