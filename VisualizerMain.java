
public class VisualizerMain
{
	public static void main(String[] args)
	{
		World world = new World(50, 50);
		Controller controller = new Controller(world, 1234, 0.35, 2, 1900);
		controller.createRandomNeighbourhood();

		WorldWindow ww = new WorldWindow(controller);
		ww.setVisible(true);
	}
}
