
package csc3335.dungeon_crawl;

public class Main
{
	public static void main(String[] args)
	{
		final int ROOMS = 5000;
		final int SEED = 1859461127;
		final boolean IS_NON_DETERMINISTIC = true;
		final boolean SHOULD_PRINT = true;

		long deltaTime = System.currentTimeMillis();

		Dungeon dungeon = new Dungeon(ROOMS, SEED, IS_NON_DETERMINISTIC);
		dungeon.generateDungeon();

		// Mitch and I wanted to compare to see who's agent was faster.
		deltaTime = Math.abs(deltaTime - System.currentTimeMillis());
		System.out.println("Dungeon generated.");

		Agent agent = new Agent(dungeon, SHOULD_PRINT);
		agent.initAgent();

		if (SHOULD_PRINT)
			System.out.println("Dungeon generation completed in " + deltaTime + "ms.");
	}
}
