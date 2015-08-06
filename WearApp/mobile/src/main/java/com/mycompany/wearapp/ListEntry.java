package com.mycompany.wearapp;

/**
 * An object to house all 5 pieces of data about a room, some of which will be displayed to the user
 * @author Ryan Burns
 */
public class ListEntry {
    private final int floor;
    private final int room;
    private final int capacity;
    private final boolean hasVideoConf;
    private final boolean hasProjector;

    /**
     * Constructor for ListEntry, has to have all 5 pieces of data
     * @param floor The floor this room is on
     * @param room The room number of this room
     * @param capacity The capacity of this room
     * @param hasVideoConf Whether the room has video conferencing capabilities or not
     * @param hasProjector Whether the room has a projector or not
     */
    public ListEntry(int floor, int room, int capacity,
                     boolean hasVideoConf, boolean hasProjector) {

        this.floor = floor;
        this.room = room;
        this.capacity = capacity;
        this.hasVideoConf = hasVideoConf;
        this.hasProjector = hasProjector;
    }

    /**
     * Getters for all 5 fields of a ListEntry object
     */

    public int getFloor() {
        return floor;
    }

    public int getRoom() {
        return room;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean getHasVideoConf() {
        return hasVideoConf;
    }

    public boolean getHasProjector() {
        return hasProjector;
    }

    /**
     * Defines the printing representation of a ListEntry, only used for debugging
     * @return "floorNum - roomNum - capacity"
     */
    @Override
    public String toString() {
        return floor + " - " + room + " - " + capacity;
    }
}