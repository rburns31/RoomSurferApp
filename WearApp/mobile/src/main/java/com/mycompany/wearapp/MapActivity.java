package com.mycompany.wearapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This activity is the map screen for RoomSurfer's Android application
 * @author Ryan Burns
 */
public class MapActivity extends ActionBarActivity {
    /**
     * toast holds any dialogs to be popped up and displayed to the user
     */
    private Toast toast;

    /**
     * mainContext holds the context of this activity so that toasts can be added even from inner
     *     classes which have a different context
     */
    private Context mainContext;

    /**
     * menu is the set of items in the action bar at the top of the activity
     */
    private Menu menu;

    /**
     * userLoc holds the image (pin) that displays the user's location on the floor map
     */
    private ImageView userLoc;

    /**
     * currentlyHighlighted holds the room number of the currently highlighted room, or -1 if none
     */
    private int currentlyHighlighted = -1;

    /**
     * roomsHighlightedBatch holds the room numbers of all of the valid rooms to
     *     be highlighted at once if appropriate
     */
    private ArrayList<Integer> roomsHighlightedBatch;

    /**
     * allRooms maps from a room number to an object containing the x, y, and view id of the room
     */
    private final HashMap<Integer, RoomInfo> allRooms = new HashMap<>();

    /**
     * TODO: This should be populated through the API call
     * openRooms holds the room numbers of all of the currently available rooms
     */
    private final ArrayList<Integer> openRooms = new ArrayList<>();

    /**
     * Sets the activity's layout, assigns the view variables, instantiates the backing data
     *     structures (allRooms and openRooms), highlights the passed in room if there was one
     *     specified in in the passed in intent, otherwise all valid and available rooms, and
     *     displays a dialog to the user instructing them to place their location on the map
     * @param savedInstanceState I'm assuming this opens the activity in the state it was last in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainContext = this;

        setContentView(R.layout.activity_map);

        userLoc = (ImageView) findViewById(R.id.userLocation);
        (findViewById(R.id.imageView)).setOnTouchListener(new MapTouchListener());

        setupRooms();

        // If this is being launched from a room details page, highlight the passed in room
        Intent intent = getIntent();
        if (intent.hasExtra("Room To Show")) {
            currentlyHighlighted = intent.getIntExtra("Room To Show", -1);

            setTitle("3rd Floor - " + currentlyHighlighted);

            if (currentlyHighlighted != -1) {
                findViewById(allRooms.get(currentlyHighlighted).getViewId())
                        .setVisibility(View.VISIBLE);
            }
        // If this is being launched from the main list page, highlight all passed in valid rooms
        } else {
            roomsHighlightedBatch = intent.getIntegerArrayListExtra("Valid Rooms");
            highlightBatch();
        }

        // Display dialog to tell user to place a pin
        toast = Toast.makeText(
                getApplicationContext(), "Touch your location.", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    /**
     * Fills in the allRooms and openRooms data structures
     */
    private void setupRooms() {
        allRooms.put(1002, new RoomInfo(R.id.room1002, 71, 42));
        allRooms.put(1018, new RoomInfo(R.id.room1018, 150, 95));
        allRooms.put(1025, new RoomInfo(R.id.room1025, 163, 35));
        allRooms.put(1026, new RoomInfo(R.id.room1026, 163, 60));
        allRooms.put(1030, new RoomInfo(R.id.room1030, 218, 73));
        allRooms.put(1031, new RoomInfo(R.id.room1031, 266, 73));

        openRooms.add(1002);
        openRooms.add(1018);
        openRooms.add(1025);
        openRooms.add(1026);
        openRooms.add(1030);
        openRooms.add(1031);
    }

    /**
     * Highlight all of the valid rooms if they are defined on the floor map
     */
    private void highlightBatch() {
        setTitle("3rd Floor - All Available Rooms");

        currentlyHighlighted = -2;
        for (Integer roomNum : roomsHighlightedBatch) {
            if (allRooms.containsKey(roomNum)) {
                findViewById(allRooms.get(roomNum).getViewId())
                        .setVisibility(View.VISIBLE);
            } else {
                System.out.println("Could not highlight room " + roomNum);
            }
        }
    }

    /**
     * Just stores the menu in a variable so it can be programmatically cleared and populated later
     * @param menu The action bar menu
     * @return true to denote that the selection was handled
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.menu = menu;
        return true;
    }

    /**
     * Populates the action bar with cancel and confirm buttons
     */
    private void populateMenu() {
        menu.add(0, 0, 0, "Clear").setIcon(android.R.drawable.ic_menu_close_clear_cancel)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, 1, 0, "Set Location").setIcon(android.R.drawable.ic_menu_add)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    /**
     * Handles both the pressing of the cancel button and the confirm button on the action bar
     * @param item Which icon on the action bar was selected
     * @return true to denote that the selection was handled
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        menu.clear();

        // Cancel Button (Hide the last user location nd revert back to the last highlight scheme)
        if (item.getItemId() == 0) {
            userLoc.setVisibility(View.INVISIBLE);
            if (roomsHighlightedBatch != null) {
                highlightBatch();
            }

        // Confirm Button (Display the closest room to the user's location)
        } else if (item.getItemId() == 1) {
            int closestRoom = closestRoomFinder();

            // Highlight that room and clear any past highlighted rooms
            clearPastHighlight();
            currentlyHighlighted = closestRoom;
            findViewById(allRooms.get(closestRoom).getViewId()).setVisibility(View.VISIBLE);

            setTitle("3rd Floor - Closest Available Room");
        // Up Button (Go back to main activity)
        } else {
            //toast.cancel();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return true;
    }

    /**
     * Finds the closest room to the user's current location
     * @return The room number of the closest room
     */
    private int closestRoomFinder() {
        // distances maps from the room number to the distance from the user location
        HashMap<Integer, Double> distances = new HashMap<>();

        // Calculate all of the distances for available rooms
        for (int roomNum : openRooms) {
            distances.put(roomNum,
                    Math.sqrt(Math.pow((userLoc.getX() / 3) - allRooms.get(roomNum).getXCoord(), 2)
                            + Math.pow((userLoc.getY() / 3) - allRooms.get(roomNum).getYCoord(), 2)));
        }

        // Find the closest room
        int closestRoom = 0;
        double minDistance = Double.MAX_VALUE;
        for (Integer roomNum : distances.keySet()) {
            if (distances.get(roomNum) < minDistance) {
                closestRoom = roomNum;
                minDistance = distances.get(roomNum);
            }
        }
        return closestRoom;
    }

    /**
     * Clears the last highlighted room if there was one
     */
    private void clearPastHighlight() {
        if (currentlyHighlighted > -1) {
            findViewById(allRooms.get(currentlyHighlighted).getViewId())
                    .setVisibility(View.INVISIBLE);
        } else if (currentlyHighlighted == -2) {
            for (Integer roomNum : roomsHighlightedBatch) {
                if (allRooms.containsKey(roomNum)) {
                    findViewById(allRooms.get(roomNum).getViewId())
                            .setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    /**
     * Sets up a listener for whenever the user touches the floor map image
     */
    private class MapTouchListener implements View.OnTouchListener {

        /**
         * Places a pin at the touched location, populates the ActionBar with confirm and cancel
         *     options, clears any past highlighted rooms, and displays a dialog to the user
         *     instructing them to confirm this location if they want to see the closest room
         *     to their current location
         * @param v The ImageView holding the floor map
         * @param event The touching of the floor map
         * @return true to denote that the touch was handled
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (menu.size() == 0) {
                populateMenu();
            }

            // Place a pin at the user's specified location
            userLoc.setX(event.getX() - 25);
            userLoc.setY(event.getY() - 25);
            userLoc.setVisibility(View.VISIBLE);

            // Make sure no rooms are highlighted
            clearPastHighlight();
            currentlyHighlighted = -1;

            setTitle("3rd Floor");

            // Display dialog to tell user to confirm their location
            toast = Toast.makeText(
                    mainContext, "Confirm your location.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();

            return true;
        }
    }
}