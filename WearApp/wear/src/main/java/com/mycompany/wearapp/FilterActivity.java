package com.mycompany.wearapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

/**
 * Displays the filtering options for RoomSurfer's Wear application
 * @author Ryan Burns
 */
public class FilterActivity extends Activity {

    /**
     * Handles instantiating the filtering activity (page) of the app
     * @param savedInstanceState I'm assuming this opens the activity in the state it was last in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
    }

    /**
     * Called when the user selects "Apply Filters" and launches the list screen of the app
     *     with these filtering options applied
     * @param view Not used
     */
    public void onApplyFilter(View view) {
        Intent intent = new Intent(this, WearActivity.class);
        Spinner capacityChoice = (Spinner) findViewById(R.id.capacityChoice);
        Spinner floorChoice = (Spinner) findViewById(R.id.floorChoice);
        intent.putExtra("Choices", new String[]{(String) capacityChoice.getSelectedItem(),
                (String) floorChoice.getSelectedItem()});
        startActivity(intent);
    }
}