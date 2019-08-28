
package csc3335.dungeon_crawl;

import java.util.ArrayList;

/**
 * Simple queue data structure, instead of just using an array
 * list I wanted to visualize it as a queue.
 * @author Nick
 *
 */
public class RoomQueue
{
	private ArrayList<Room> roomList;

	public RoomQueue()
	{
		roomList = new ArrayList<Room>();
	}

	public void enqueue(Room room)
	{
		roomList.add(room);
	}

	public Room dequeue()
	{
		Room r = roomList.get(0);
		roomList.remove(r);
		return r;
	}

	public boolean isEmpty()
	{
		return roomList.isEmpty();
	}

	public void clear()
	{
		roomList.clear();
	}

	@Override
	public String toString()
	{
		String s = "Empty";

		if (!isEmpty())
		{
			s = "";

			for (Room r: roomList)
				s += r.toString() + "\n";
		}

		return s;
	}
}
