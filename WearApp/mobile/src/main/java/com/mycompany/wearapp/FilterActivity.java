package com.mycompany.wearapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * This activity is the filtering screen for RoomSurfer's Android application
 * @author Ryan Burns
 */
public class FilterActivity extends ActionBarActivity {

    /**
     * Sets the activity's layout, parse through the passed in intent and set the filter choices
     *     to the passed in values (if there are any), and add a listener to the capacity SeekBar
     * @param savedInstanceState I'm assuming this opens the activity in the state it was last in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Intent intent = getIntent();
        if (intent.hasExtra("Capacity Choice")) {
            Bundle extras = intent.getExtras();

            SeekBar capacity = (SeekBar) findViewById(R.id.capacitySelection);
            capacity.setProgress(extras.getInt("Capacity Choice"));
            TextView progress = (TextView) findViewById(R.id.progressText);
            progress.setText(Integer.toString(extras.getInt("Capacity Choice")));

            Spinner floor = (Spinner) findViewById(R.id.floorChoice);
            String floorValue = extras.getString("Floor Choice");
            if (floorValue.equals("3")) {
                floor.setSelection(2);
            } else if (floorValue.equals("2")) {
                floor.setSelection(1);
            } else {
                floor.setSelection(0);
            }

            CheckBox videoConf = (CheckBox) findViewById(R.id.hasVideoConfChoice);
            videoConf.setChecked(extras.getBoolean("Video Conferencing Choice"));

            CheckBox projector = (CheckBox) findViewById(R.id.hasProjectorChoice);
            projector.setChecked(extras.getBoolean("Projector Choice"));
        }

        SeekBar capacity = (SeekBar) findViewById(R.id.capacitySelection);
        capacity.setOnSeekBarChangeListener(new CapacitySeekBarListener());
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
     * Called when the user selects "Apply Filters" and launches the list screen of the app
     *     with these filtering options applied
     * @param view Not used
     */
    public void onApplyFilter(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        SeekBar capacity = (SeekBar) findViewById(R.id.capacitySelection);
        Spinner floor = (Spinner) findViewById(R.id.floorChoice);
        CheckBox videoConf = (CheckBox) findViewById(R.id.hasVideoConfChoice);
        CheckBox projector = (CheckBox) findViewById(R.id.hasProjectorChoice);

        intent.putExtra("Capacity Choice", capacity.getProgress());
        intent.putExtra("Floor Choice", (String) floor.getSelectedItem());
        intent.putExtra("Video Conferencing Choice", videoConf.isChecked());
        intent.putExtra("Projector Choice", projector.isChecked());

        startActivity(intent);
    }

    /**
     * A listener for any time that the capacity SeekBar is dragged to a different value
     */
    private class CapacitySeekBarListener implements SeekBar.OnSeekBarChangeListener {

        /**
         * Handles setting the text of the label next to the capacity SeekBar to whatever value
         *     the SeekBar is currently set at
         * @param seekBar Not used
         * @param progress The new vlue of the capacity SeekBar
         * @param fromUser Not used
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
            TextView progressText = (TextView) findViewById(R.id.progressText);
            progressText.setText(Integer.toString(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    }
}