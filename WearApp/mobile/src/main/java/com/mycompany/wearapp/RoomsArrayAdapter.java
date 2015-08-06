package com.mycompany.wearapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Handles showing the data set as a list in the mobile app
 * @author Ryan Burns
 */
public class RoomsArrayAdapter extends ArrayAdapter<ListEntry> {
    private final Context context;
    private final int layoutResourceId;
    private final ArrayList<ListEntry> entries;

    /**
     * Constructor that just assigns the instance variables
     * @param context The context to put these entries in
     * @param layoutResourceId The ID of the resource in the context which will inflate out to
     *                             become the layout element to hold an entry in the list
     * @param entries The entries in the list to be added and then displayed
     */
    public RoomsArrayAdapter(Context context, int layoutResourceId, ArrayList<ListEntry> entries) {
        super(context, layoutResourceId, entries);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.entries = entries;
    }

    /**
     * Gets a view to hold the entry's data and then populates the fields
     * @param position The position in the entries array of the data to be put in this view
     * @param convertView The view which will hold the entry's data
     * @param parent The group that the view is contained in
     * @return The view now holding the passed in entry
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //If convertView is null then you will need to inflate a new row to hold the entry
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        // Otherwise just recycle the passed in view by re-assigning its data
        ListEntry entry = entries.get(position);
        ViewHolder holder = new ViewHolder(convertView);

        holder.floor.setText(Integer.toString(entry.getFloor()));
        holder.room.setText(Integer.toString(entry.getRoom()));
        holder.capacity.setText(Integer.toString(entry.getCapacity()));
        holder.tag = position;

        return convertView;
    }

    /**
     * An inner class representing the data that a view must hold, namely a floor number, room
     *     number, capacity, and tag (used for identifying the view in the layout)
     */
    public class ViewHolder {
        public TextView floor;
        public TextView room;
        public TextView capacity;
        public int tag;

        public ViewHolder(View v) {
            this.floor = (TextView) v.findViewById(R.id.floor);
            this.room = (TextView) v.findViewById(R.id.room);
            this.capacity = (TextView) v.findViewById(R.id.capacity);
        }
    }
}