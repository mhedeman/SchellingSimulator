import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Class that controls and performs simulation steps
 */
public class Controller
{
	private final World world;
	private final Random random;
	private double satisfactionThreshold;
	private int numberOfAgents, numberOfGroups;
	private int stepDelta;
	
	/**
	 * Constructor which creates a simulation based on a current World state.
	 * Random events performed by this simulator are based on a random seed
	 * @param w The world to perform a simulation with
	 * @param seed The seed to be used for Random events
	 * @param satisfactionThreshold The satisfaction threshold of all agents in the world
	 */
	public Controller(World w, long seed, double satisfactionThreshold, int numberOfGroups, int numberOfAgents)
	{
		world = w;
		random = new Random(seed);
		this.satisfactionThreshold = satisfactionThreshold;
		this.stepDelta = 0;
		this.numberOfGroups = numberOfGroups;
		this.numberOfAgents = numberOfAgents;
	}

	public Controller(World w, double satisfactionThreshold, int numberOfGroups, int numberOfAgents)
	{
		world = w;
		random = new Random();
		this.satisfactionThreshold = satisfactionThreshold;
		this.stepDelta = 0;
		this.numberOfGroups = numberOfGroups;
		this.numberOfAgents = numberOfAgents;
	}
	
	/**
	 * Getter for obtaining the current state of the world
	 * @return the current state of the world
	 */
	public World getWorld()
	{
		return world;
	}

	public void setSatisfactionThreshold(double satisfactionThreshold) {
		this.satisfactionThreshold = satisfactionThreshold;
	}

	public double getSatisfactionThreshold() {
		return this.satisfactionThreshold;
	}

	/**
	 * Creates a new Human or a new Zombie at a random spot.
	 * Modify this method if you want to play around with the simulator.
	 */
	public void createRandomAgent() {
		int x = this.random.nextInt(world.getWidth());
		int y = this.random.nextInt(world.getHeight());
		Cell cell = world.getCell(x, y);

		while (!cell.isEmpty()) {
			x = this.random.nextInt(world.getWidth());
			y = this.random.nextInt(world.getHeight());
			cell = world.getCell(x, y);
		}

		Agent agent;

		for (int i = 0; i < numberOfGroups; i++) {
			if (this.random.nextDouble() < (double) (i + 1) / numberOfGroups) {
				agent = new Agent(i, this.satisfactionThreshold, cell);
				agent.moveTo(cell);
				return;
			}
		}
	}

	/**
	 * Creates a random neighbourhood of agents
	 */
	public void createRandomNeighbourhood() {
		for (int i = 0; i < this.numberOfAgents; i++) {
			createRandomAgent();
		}
	}

	/**
	 *
	 * @return number of groups
	 */
	public int getNumberOfGroups() {
		return this.numberOfGroups;
	}

	/**
	 *
	 * @return number of agents
	 */
	public int getNumberOfAgents() {
		return this.numberOfAgents;
	}

	/**
	 * Perform a step in the simulation
	 */
	public void step()
	{
		List<Agent> agents = world.getAgents();
		Collections.shuffle(agents);

		// Let all creatures move
		agents.get(this.stepDelta).move();
		this.stepDelta++;

		if (this.stepDelta == agents.size() - 1) {
			this.stepDelta = 0;
		}
	}

}
