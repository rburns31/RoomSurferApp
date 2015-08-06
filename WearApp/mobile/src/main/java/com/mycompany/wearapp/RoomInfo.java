package com.mycompany.wearapp;

/**
 * An object that holds the view id, x coordinate and y coordinate of a room
 *     Allows for mapping from the room number to all relevant data at once
 * @author Ryan Burns
 */
public class RoomInfo {
    private final int viewId;
    private final int xCoord;
    private final int yCoord;

    public RoomInfo(int viewId, int xCoord, int yCoord) {
        this.viewId = viewId;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    /**
     * Getters for all 3 fields of a RoomInfo object
     */

    public int getViewId() {
        return viewId;
    }

    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }
}