import java.util.ArrayList;
import java.util.List;

/**
 * An instance of the World class represents a rectangular world with a certain width and height, filled with Cells everywhere.
 * The class keeps track of all the Cells and all the Agents the world contains and can be used to find a specific cell in the world
 * with known coordinates
 *
 * @author Max Hedeman Joosten
 */

public class World {
    private final int width, height;
    private final ArrayList<Cell> cellList;

    /**
     * Constructs a new World with dimensions w and h
     *
     * @param w width of the World
     * @param h height of the World
     */
    public World(int w, int h) {
        this.width = w;
        this.height = h;
        this.cellList = new ArrayList<>();

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                cellList.add(new Cell(this, i, j));
            }
        }
    }

    /**
     * @return integer containing the width of the World
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * @return integer containing the hight of the World
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * @param x x-coordinate of the Cell
     * @param y y-coordinate of the Cell
     * @return Cell with coordinates x and y
     * @throws IllegalArgumentException if the World does not contain the x and y coordinates specified
     */
    public Cell getCell(int x, int y) throws IllegalArgumentException {
        if (x < this.width && y < this.height && x >= 0 && y >= 0) {
            for (Cell cell : this.cellList) {
                if (cell.getX() == x & cell.getY() == y) {
                    return cell;
                }
            }
        }

        throw new IllegalArgumentException("Invalid coordinates");
    }

    /**
     * @return ArrayList containing all Agents in the World
     */
    public List<Agent> getAgents() {
        ArrayList<Agent> agents = new ArrayList<>();

        for (Cell cell : this.cellList) {
            if (!cell.isEmpty()) {
                agents.add(cell.getAgent());
            }
        }

        return agents;
    }

    /**
     * @return ArrayList containing all Cells belonging to the World
     */
    public List<Cell> getCellList() {
        return new ArrayList<>(this.cellList);
    }

    /**
     *
     * @return double containing the Segregation perentage
     */
    public double getSegregation() {
        double sum = 0;
        double agents = 0;

        for (int i = 0; i < this.cellList.size(); i++) {
            Cell cell = this.cellList.get(i);

            if (!cell.isEmpty()) {
                sum += cell.getPercentage(cell.getAgent().getGroup(), false);
                agents++;
            }
        }

        return sum / agents;
    }

    public double getUnhappiness() {
        double n = 0;

        for (Agent agent : this.getAgents()) {
            if (agent.getCurrentCell().getPercentage(agent.getGroup(), false) < agent.getSatisfactionThreshold()) {
                n++;
            }
        }

        return n / this.getAgents().size();
    }
}
