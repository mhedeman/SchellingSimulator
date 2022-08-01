import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JPanel;


/**
 * Visualization class for the Creature simulation
 * @author Paul Bouman
 */

public class WorldPanel extends JPanel
{
	// Colors to be used for different types of creatures. If more than six types are created,
	// this arrays needs to be extended.
	private static final Color [] colors = {Color.RED, Color.BLUE, Color.GREEN,
											Color.ORANGE, Color.PINK, Color.CYAN};

	// Since JPanel implements the serializable class some random number should be added.
	private static final long serialVersionUID = -8647476667068294328L;

	// Map that assigns a creature type to a color
	private HashMap<Integer, Color> colorMap;
	
	// The world to simulate
	private World world;
	
	// The current drawing of the World state
	private BufferedImage currentImage;
	
	/**
	 * Constructor takes a world of which the current state will be drawn by this component
	 * @param w the world to be drawn
	 */
	public WorldPanel(World w)
	{
		super();
		world = w;
		colorMap = new HashMap<>();
		initializeWorld();
	}
	
	/**
	 * Redefine the default painting behavior of a Panel such that the world state is drawn
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		if (currentImage == null)
		{
			initializeWorld();
		}
		if (currentImage != null)
		{
			int w = getWidth();
			int h = getHeight();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, w, h);
			drawCells(g);
			drawGrid(g);
			drawAgents(g);
			//drawLegend(g);
		}
	}
	
	/**
	 * Builds an image in memory to draw on the component
	 */
	public void initializeWorld()
	{
		int w = getWidth();
		int h = getHeight();
		if (w < 1 || h < 1)
		{
			return;
		}
		BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics g = newImage.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);
		drawCells(g);
		drawGrid(g);
		drawAgents(g);
		//drawLegend(g);
		currentImage = newImage;
		repaint();
	}

	public void resetWorld() {
		this.world = new World(50, 50);
	}

	public World getWorld() {
		return this.world;
	}
	
	/**
	 * Draws the grid according to the number of cells in the world
	 * @param gr the graphics to draw on
	 */
	private void drawGrid(Graphics gr)
	{
		gr.setColor(Color.BLACK);
		int cellSize = getCellSize();
		int boardHeight = cellSize * world.getHeight();
		int boardWidth = cellSize * world.getWidth();
		for (int col=0; col <= world.getWidth(); col++)
		{
			int x = col * cellSize;
			gr.drawLine(x, 0, x, boardHeight);
		}
		for (int row=0; row <= world.getHeight(); row++)
		{
			int y = row * cellSize;
			gr.drawLine(0, y, boardWidth, y);
		}
	}
	
	/**
	 * Draws the background of the cells based on the amount of wealth available in the cell
	 * @param gr the graphics to draw on
	 */
	private void drawCells(Graphics gr)
	{
		int cellSize = getCellSize();
		for (int x=0; x < world.getWidth(); x++)
		{
			for (int y=0; y < world.getHeight(); y++)
			{
				Cell c = world.getCell(x, y);
				gr.setColor(Color.WHITE);
				gr.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
			}
		}
	}
	
	/**
	 * Draws creatures as filled circles with colors based on their types
	 * @param gr the graphics to draw on
	 */
	private void drawAgents(Graphics gr)
	{
		for (int x = 0; x < world.getWidth(); x++)
		{
			for (int y=0; y < world.getHeight(); y++)
			{
				Cell cell = world.getCell(x, y);
				drawAgentsInCell(gr,cell);
			}
		}
	}
	
	/**
	 * Helper function for drawing creatures. Draws all creatures in a single cell
	 * @param gr the graphics to draw one
	 * @param cell the cell for which to draw the creatures
	 */
	private void drawAgentsInCell(Graphics gr, Cell cell)
	{
		int cellSize = getCellSize();
		int cellX = cell.getX() * cellSize;
		int cellY = cell.getY() * cellSize;

		if (!cell.isEmpty()) {
			Color color = getAgentColor(cell.getAgent());
			gr.setColor(color);
			gr.fillRect(cellX, cellY, cellSize, cellSize);
		}
	}

	/**
	 * Draws a legend so that the viewer can see which color maps to which creature type
	 * @param g the graphics to draw on
	 */
	private void drawLegend(Graphics g)
	{
		Graphics2D gr = (Graphics2D) g;
		int height = gr.getFontMetrics().getHeight();
		int xStart = getCellSize() * world.getWidth() + 15;
		int curY = 15;
		for(int i : colorMap.keySet())
		{
			String name = "Group " + (i + 1);
			gr.setColor(colorMap.get(i));
			gr.fillRect(xStart, curY, height, height);
			gr.setColor(Color.BLACK);
			gr.drawString(name, xStart + 2*height, curY + height);
			curY += 2*height;
		}


			gr.drawString("Segregation coefficient: " + String.format("%.3f", this.world.getSegregation()), xStart, curY);
	}
	
	/**
	 * Computes the size of a single cell based on the width of the component
	 * @return the width and height of a cell (cells are square)
	 */
	private int getCellSize()
	{
		int maxWidth = getWidth() / world.getWidth();
		int maxHeight = getHeight() / world.getHeight();
		return Math.min(maxWidth, maxHeight);
	}
	
	/**
	 * Get the color to draw the circle for a creature in. If no color has been assigned
	 * to a creature type yet, a new color will be picked for this creature.
	 * @param a the creature for which we want to know the color
	 * @return the color
	 */
	private Color getAgentColor(Agent a) {
		int i = a.getGroup();

		if (colorMap.containsKey(i)) {
			return colorMap.get(i);
		}

		Color color = colors[colorMap.size() % colors.length];
		colorMap.put(i, color);
		return color;
	}
}
