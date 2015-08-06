package com.mycompany.wearapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This activity is the entry details screen for RoomSurfer's Android application
 * @author Ryan Burns
 */
public class EntryDetailsActivity extends ActionBarActivity {

    /**
     * Sets the activity's layout, and populates the room info based on the passed in intent from
     *    MainActivity
     * @param savedInstanceState I'm assuming this opens the activity in the state it was last in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_details);

        Intent intent = getIntent();
        ((TextView) findViewById(R.id.roomTextView)).setText(
                Integer.toString(intent.getIntExtra("Room", 0)));
        ((TextView) findViewById(R.id.floorTextView)).setText(
                Integer.toString(intent.getIntExtra("Floor", 0)));
        ((TextView) findViewById(R.id.capacityTextView)).setText(
                Integer.toString(intent.getIntExtra("Capacity", 0)));

        String videoConf = "No";
        if (intent.getBooleanExtra("VideoConf", false)) {
            videoConf = "Yes";
        }
        ((TextView) findViewById(R.id.videoConfTextView)).setText(videoConf);

        String projector = "No";
        if (intent.getBooleanExtra("Projector", false)) {
            projector = "Yes";
        }
        ((TextView) findViewById(R.id.projectorTextView)).setText(projector);
    }

    /**
     * Add the up button to the ActionBar
     * @param menu Not used, the menu of the ActionBar
     * @return true to denote this has been handled
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    /**
     * Goes back to the MainActivity
     * @param item Not used, only one item in the menu
     * @return true to denote this has been handled
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
    }

    /**
     * Opens the map with the current room highlighted if there is support for that floor map
     * @param view Not used
     */
    public void onShowOnMap(View view) {
        String floorNum = (String) ((TextView) findViewById(R.id.floorTextView)).getText();
        if (floorNum.equals("3")) {
            String roomNum = (String) ((TextView) findViewById(R.id.roomTextView)).getText();
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("Room To Show", Integer.parseInt(roomNum));
            startActivity(intent);

        // No support for anything but the third floor right now
        } else {
            Toast toast = Toast.makeText(
                    getApplicationContext(), "Do not have that floor map!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
        }
    }
}