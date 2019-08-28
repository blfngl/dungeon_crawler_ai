package csc3335.dungeon_crawl;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Nick Mirasol
 * Stuetzle CSC3335
 * 2/20/19
 * 
 * This is an agent that traverses a randomly generated dungeon in search for
 * three things in this order:
 * 
 * 1) The Sword
 * 2) The Key
 * 3) The Exit
 * 
 * The sword allows the agent to kill the dragon, which has a chance of dropping
 * the key. If the dragon doesn't drop the Key, it must be found again. The key
 * gives access to the exit, which, when found, terminates the search.
 * 
 * I haven't observed this version of the agent ever fail.
 */
public class Agent
{
	private Dungeon dungeon;

	private Room currentRoom;

	private HashMap<Integer, Room> knownRooms;
	private HashMap<Room, Room> roomPath;
	private HashMap<Room, Integer> roomDistance;

	private boolean shouldPrint = false;
	private long deltaTime = 0;

	private RoomQueue roomQueue;

	/**
	 * Decided for an Enum to identify targets so I needed only to write
	 * one search method.
	 */
	private enum Target
	{
		SWORD, DRAGON, EXIT;
	}

	/**
	 * Normal constructor, initializes values.
	 */
	public Agent(Dungeon dungeon)
	{
		this.dungeon = dungeon;
		knownRooms = new HashMap<Integer, Room>();
		roomPath = new HashMap<Room, Room>();
		roomDistance = new HashMap<Room, Integer>();
		roomQueue = new RoomQueue();
	}

	/**
	 * Extra constructor
	 * @param dungeon
	 */
	public Agent(Dungeon dungeon, boolean shouldPrint)
	{
		this.dungeon = dungeon;
		this.shouldPrint = shouldPrint;
		knownRooms = new HashMap<Integer, Room>();
		roomPath = new HashMap<Room, Room>();
		roomDistance = new HashMap<Room, Integer>();
		roomQueue = new RoomQueue();
	}

	/**
	 * Finds the desired target using a BFS, as the graph has non-weighted edges.
	 * @param target The desired target.
	 * @return True if successful, false otherwise. This can determine if there's
	 * no exit!
	 */
	private boolean findTarget(Target target)
	{
		// Setup
		roomQueue.clear();
		knownRooms.clear();
		roomPath.clear();
		roomDistance.clear();

		// Store starting room in the queue.
		roomQueue.enqueue(currentRoom);
		// Set distance to start = 0, all other distances should be -1.
		// The way I do it here the other distances simply don't exist, so instead
		// of looking for -1 it looks to see if the distance has been charted.
		roomDistance.put(currentRoom, 0);

		// While there are rooms in the queue:
		while (!roomQueue.isEmpty())
		{
			// Read and dequeue a node
			Room inRoom = roomQueue.dequeue();

			// For all its adjacent rooms:
			for (Room neighbor: inRoom.getNeighbors())
			{
				// If distance hasn't been computed (hasn't been visited)
				if (!roomDistance.containsKey(neighbor))
				{
					// Make distance equal to previous room's distance + 1
					roomDistance.put(neighbor, roomDistance.get(inRoom) + 1);
					// Make path to neighbor = inRoom
					roomPath.put(inRoom, neighbor);
					// Append to queue
					roomQueue.enqueue(neighbor);

					// Check for desired target
					switch (target)
					{
					case SWORD:
						if (dungeon.containsSword(neighbor))
						{
							if (shouldPrint)
							{
								System.out.println("\nSword path:");
								printPath();
							}

							currentRoom = neighbor;
							return true;
						}

						break;

					case DRAGON:
						if (dungeon.containsDragon(neighbor))
						{
							if (shouldPrint)
							{
								System.out.println("\nDragon path:");
								printPath();
							}

							currentRoom = neighbor;
							return true;
						}

						break;

					case EXIT:
						if (dungeon.containsExit(neighbor))
						{
							if (shouldPrint)
							{
								System.out.println("\nExit path:");
								printPath();
							}

							System.out.println("Made it! Exit is here: Room #" + neighbor.ROOM_ID);
							return true;
						}

						break;

					default:
						System.out.println("Invalid target!");
						break;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Prints the path taken.
	 */
	private void printPath()
	{
		if (!roomPath.isEmpty())
		{
			System.out.println("Starting node: " + currentRoom.ROOM_ID);

			for (Room r: roomPath.keySet())
				System.out.println("" + r.ROOM_ID + " -> " + roomPath.get(r).ROOM_ID);
		}
	}

	/**
	 * The high-level brain of the agent.
	 */
	private void escape()
	{
		if (shouldPrint)
			deltaTime = System.currentTimeMillis();

		findTarget(Target.SWORD);
		// I haven't had a problem with finding the dragon without the while loop, however
		// I'm pretty sure adding this ensures that it's found if the key doesn't drop multiple
		// times in a row.
		while(!findTarget(Target.DRAGON));
		findTarget(Target.EXIT);
		
		if (shouldPrint)
		{
			System.out.println("\nExtra stuff:");
			deltaTime -= System.currentTimeMillis();
			deltaTime = Math.abs(deltaTime);
			System.out.println("Operation completed in " + deltaTime + "ms.");
		}
	}

	/**
	 * Initializes the agent.
	 * @param dungeon The dungeon to traverse.
	 */
	public void initAgent()
	{
		System.out.println("Here we go!");
		currentRoom = dungeon.getRandomRoom();
		System.out.println("Starting in room " + currentRoom.ROOM_ID);
		escape();
	}
}
