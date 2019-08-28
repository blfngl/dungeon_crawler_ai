package csc3335.dungeon_crawl;

import java.util.HashSet;

/**
 * This object represents a single room (and anything in it) in the dungeon
 * @author stuetzlec
 */
public class Room {

    private HashSet<Room> neighbors;

    /**
     * Number of rooms that have been generated so far
     */
    protected static int numRooms = 0;

    /**
     * The ID number of the room
     */
    public final int ROOM_ID;

    /**
     * True if the room contains the dragon
     */
    protected boolean containsDragon;

    /**
     * True if the room contains the sword
     */
    protected boolean containsSword;

    /**
     * True if the room contains the dungeon exit
     */
    protected boolean containsExit;

    /**
     * Default constructor, creates a new room and gives it an ID number
     */
    public Room() {
        ROOM_ID = ++numRooms;
        this.neighbors = new HashSet();
    }

    /**
     * Copy constructor that does NOT create a new Room object
     * @param o The Room object to copy the data from
     */
    public Room(Room o) {
        this.ROOM_ID = o.ROOM_ID;
        this.neighbors = new HashSet();
        this.neighbors.addAll(o.neighbors);
    }

    /**
     * Add a new neighbor to this room's set of neighbors
     * @param r The room to add as a neighbor
     */
    public void addNeighbor(Room r) {
        neighbors.add(r);
        r.neighbors.add(this);
    }

    /**
     * Returns a copy of the room's neighbors (not a reference, you can't change
     *   them).
     * @return A copy HashSet<Room> of this room's neighbors
     */
    public HashSet<Room> getNeighbors() {
        Room r = new Room(this);
        return r.neighbors;
    }

    /**
     * Helper method to print the room's information
     */
    public void print() {
        System.out.println(this.toString());
    }

    /** 
     * Standard toString method
     * @return The room ID plus all neighbors plus what it contains
     */
    public String toString() {
        String s = "Room ID # - " + this.ROOM_ID + "\n  [ ";
        for (Room r : this.neighbors) {
            s += "(" + this.ROOM_ID + ", " + r.ROOM_ID + ") ";
        }
        s += "]" + (this.containsDragon ? "\n  Contains Dragon" : "");
        s += (this.containsSword ? "\n  Contains Sword" : "");
        s += (this.containsExit ? "\n  Contains Exit" : "");
        return s;
    }
}
