package com.mycompany.wearapp;

/**
 * An object to house all 3 piece of data about a room that we will display to the user
 * @author Ryan Burns
 */
public class ListEntry {
    private final int floor;
    private final int room;
    private final int capacity;

    /**
     * Constructor for ListEntry, has to have all 3 pieces of data
     * @param floor The floor this room is on
     * @param room The room number of this room
     * @param capacity The capacity of this room
     */
    public ListEntry(int floor, int room, int capacity) {
        this.floor = floor;
        this.room = room;
        this.capacity = capacity;
    }

    /**
     * Getters for all 3 fields of a ListEntry
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
}