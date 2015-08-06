package com.mycompany.wearapp;

import android.support.wearable.view.WearableListView;
import android.support.wearable.view.WearableListView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Handles showing the data set as a list in the app
 * @author Ryan Burns
 */
public class Adapter extends WearableListView.Adapter {
    private final ListEntry[] dataSet;
    private final LayoutInflater inflater;

    /**
     * Constructor that just assigns the two instance variables
     * @param dataSet The data in the list which is being displayed
     * @param inflater The inflater that converts from XML definition to actual View objects
     */
    public Adapter(ListEntry[] dataSet, LayoutInflater inflater) {
        this.dataSet = dataSet;
        this.inflater = inflater;
    }

    /**
     * Create new View objects for the list items from the list_item XML
     * @param parent Not used
     * @param viewType Not used
     * @return A holder of the view that was created from the XML definition
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.list_item, null));
    }

    /**
     * Replace the contents of a list item
     * Instead of creating new views, the list tries to recycle existing ones
     * @param holder The ViewHolder which houses the TextView which will be replaced
     * @param position The position in the data set which will now be displayed here
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView floorField = (TextView) holder.itemView.findViewById(R.id.floor);
        floorField.setText(Integer.toString(dataSet[position].getFloor()));

        TextView roomField = (TextView) holder.itemView.findViewById(R.id.room);
        roomField.setText(Integer.toString(dataSet[position].getRoom()));

        TextView capacityField = (TextView) holder.itemView.findViewById(R.id.capacity);
        capacityField.setText(Integer.toString(dataSet[position].getCapacity()));

        holder.itemView.setTag(position);
    }

    /**
     * Return the size of the data set
     * @return The size of the data set
     */
    @Override
    public int getItemCount() {
        return dataSet.length;
    }
}