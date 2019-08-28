package csc3335.dungeon_crawl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * This class represents a dungeon for CSC 3335 Project 1. Consider it BETA, if
 * there are bugs or issues, please let me know so I can fix them ASAP for the
 * other students and future classes.
 *
 * @author stuetzlec
 */
public class Dungeon {

    private ArrayList<Room> rooms;
    private int numRooms;
    private Random rng;
    private final boolean isNonDeterministic;
    private boolean swordFound;
    private final int P = 50;

    /**
     * Overloaded: Initialize the dungeon, providing the number of rooms and a
     * seed for the, random number generator, as well as an argument for the
     * deterministic nature of the dragon's key drops.
     *
     * @param _numRooms the number of rooms in the dungeon
     * @param _randomSeed the number to seed the random number generation
     * @param _isNonDeterministic false if the dragon's key drop is guaranteed
     */
    public Dungeon(int _numRooms, int _randomSeed, boolean _isNonDeterministic) {
        this.isNonDeterministic = _isNonDeterministic;
        initializeDungeon(_numRooms, _randomSeed);
    }

    /**
     * Overloaded: Initialize the dungeon, providing the number of rooms and a
     * seed for the random number generator
     *
     * @param _numRooms the number of rooms in the dungeon
     * @param _randomSeed the number to seed the random number generation
     */
    public Dungeon(int _numRooms, int _randomSeed) {
        this.isNonDeterministic = true;
        initializeDungeon(_numRooms, _randomSeed);
    }

    /**
     * Helper function to initialize the rooms and random number generator
     * @param _numRooms Total number of rooms in the dungeon to initialize
     * @param _randomSeed Seed for the RNG
     */
    private void initializeDungeon(int _numRooms, int _randomSeed) {
        // Seed the generator
        rng = new Random(_randomSeed);
        rooms = new ArrayList();

        // The number of rooms has to be at least four
        if (_numRooms < 4) {
            System.err.println("ERROR: Too few rooms: " + _numRooms);
            System.err.println("Using 4 rooms.");
            _numRooms = 4;
        }
        this.numRooms = _numRooms;
        swordFound = false;
    }

    /**
     * This method should be called once to initialize the dungeon by
     * randomizing the room layout
     */
    public void generateDungeon() {

        // Decide on a number of edges to add
        int numEdges = (numRooms * (numRooms - 1)) / 4;

        // Generate the rooms
        for (int i = 0; i < this.numRooms; i++) {
            rooms.add(new Room());
            if (rooms.size() > 1) // Connect the room to the room before it
            {
                rooms.get(rooms.size() - 1).addNeighbor(rooms.get(rooms.size() - 2));
                numEdges--;
            }
        }
        // Add random edges until we have our total number of edges fulfilled
        while (numEdges > 0) {
            Room r1 = getRandomRoom();
            Room r2 = getRandomRoom();
            r1.addNeighbor(r2);
            numEdges--;
        }

        // Finally, add the dragon and sword to a random room
        getRandomRoom().containsSword = true;
        getRandomRoom().containsExit = true;
        addDragonToRandomRoom();
    }

    /**
     * Returns true if dragon is in this room and drops the key for the player
     * @param r The room to check for the dragon in
     * @return true if the dragon is here AND drops the key
     */
    public boolean containsDragon(Room r) {
        if (r.containsDragon) {
            // Figure out if he drops the key
            System.out.println("The dragon is here: Room #" + r.ROOM_ID);
            r.containsDragon = false;
            if (this.swordFound && 
                    (!isNonDeterministic || rng.nextInt(100) < P) ) {
                // Return true, THE KEY IS FOUND
                System.out.println("Dragon dropped the key: Room #" + r.ROOM_ID);
                return true;
            }
            // Move the dragon
            this.addDragonToRandomRoom();
            return false;
        }
        System.out.println("Dragon is not here: Room #" + r.ROOM_ID);
        return false;
    }

    /**
     * Returns true if the sword is here (and the player picks it up)...it
     *   is then removed from the room (so it can be added to the inventory)
     * @param r The room to check for the sword
     * @return true if the sword is in this room, false otherwise
     */
    public boolean containsSword(Room r) {
        if (r.containsSword && !swordFound) {
            System.out.println("The sword is here: Room #" + r.ROOM_ID);
            r.containsSword = false;
            swordFound = true;
            return true;
        }
        return false;
    }

    /**
     * Determines if the user has found the exit, necessary for the goal state
     * @param r The room to check to determine if the exit is here
     * @return true if the room contains the dungeon's exit
     */
    public boolean containsExit(Room r) {
        return r.containsExit;
    }

    /**
     * Helper function to print all the rooms and their contents in the dungeon
     *   This is for testing purposes only
     */
    public void printDungeon() {
        this.rooms.forEach((r) -> {
            r.print();
        });
    }

    /**
     * Helper function to move the dragon to a new room
     */
    private void addDragonToRandomRoom() {
        Room r = getRandomRoom();
        while (r.containsSword) {
            r = getRandomRoom();
        }
        r.containsDragon = true;
    }

    /**
     * Helper to grab a random room in the dungeon
     * @return A single random room from the dungeon
     */
    public Room getRandomRoom() {
        return rooms.get(rng.nextInt(rooms.size()));
    }
    
    /**
     * A method to return a HashSet of all of the neighbors of a room r
     * @param r The room to return the neighbors of
     * @return A HashSet<Room> of all of the neighbors of r
     */
    public HashSet<Room> getNeighbors(Room r){
        return r.getNeighbors();
    }
}
