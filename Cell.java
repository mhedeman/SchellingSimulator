import java.util.ArrayList;
import java.util.List;

/**
 * A Cell is a part of the world that contains plants and Creatures; Carnivores and Herbivores. The class also keeps track of its location within
 * world. It is possible to add and remove Creatures from the Cell and to change the number of plants. The cell class has a defined natural order:
 * A Cell with more plants is preferred. If Cells contain the same number of plants, the Cell with fewer Creatures is preferred.
 *
 * @author Max Hedeman Joosten
 */

public class Cell implements Comparable<Cell> {
    private final World world;
    private final int x, y;
    private Agent agent;

    /**
     * Constructs a new Cell inside a specified world with specified x and y coordinates
     *
     * @param w the World the Cell belongs to
     * @param x the x coordinate of the Cell
     * @param y the y coordinate of the Cell
     */
    public Cell(World w, int x, int y) {
        this.world = w;
        this.x = x;
        this.y = y;
        this.agent = null;
    }

    /**
     * @return the x coordinate of the cell
     */
    public int getX() {
        return this.x;
    }

    /**
     * @return the y coordinate of the cell
     */
    public int getY() {
        return this.y;
    }

    /**
     * @return the world that the cell belongs to
     */
    public World getWorld() {
        return this.world;
    }

    /**
     * Adds a specified Creature to the Cell's list of Creatures
     */
    public void addAgent(Agent a) {
        this.agent = a;
    }

    /**
     * Removes a specified Creature from the Cell's list of Creatures
     */
    public void removeAgent() {
        this.agent = null;
    }

    /**
     * @return an immutable list containing all Creatures inside the Cell
     */
    public Agent getAgent() {

        return this.agent;
    }

    /**
     * @return true if the cell is empty, false otherwise
     */
    public boolean isEmpty() {
        return this.agent == null;
    }

    /**
     *
     * @param includeMiddle
     * @return
     */
    public List<Cell> getNeighbouringCells(boolean includeMiddle) {
        ArrayList<Cell> neighbouringCells = new ArrayList<>();

        if (!includeMiddle) {
            for (Cell cell : this.getWorld().getCellList()) {
                if (Math.max(Math.abs(this.x - cell.getX()), Math.abs(this.y - cell.getY())) <= 1 && cell != this) {
                    neighbouringCells.add(cell);
                }
            }

            return neighbouringCells;
        }

        for (Cell cell : this.getWorld().getCellList()) {
            if (Math.max(Math.abs(this.x - cell.getX()), Math.abs(this.y - cell.getY())) <= 1) {
                neighbouringCells.add(cell);
            }
        }

        return neighbouringCells;
    }

    /**
     *
     * @param group
     * @return
     */
    public double getPercentage(int group, boolean includeMiddle) {
        double n = 0;
        double g = 0;

        for (Cell cell : this.getNeighbouringCells(includeMiddle)) {
            if (!cell.isEmpty()) {
                n++;
                if (cell.getAgent().getGroup() == group) {
                    g++;
                }
            }
        }

        if (n == 0) {
            return 0;
        }

        return g / n;
    }

    /**
     *
     * @param cell
     * @return
     */
    public int getDistanceTo(Cell cell) {
        return Math.abs((this.x - cell.getX()) + (this.y - cell.getY()));
    }

    /**
     *
     * @return
     */
    public List<Cell> getNearestCells() {
        List<Cell> cellList = this.getWorld().getCellList();
        cellList.sort(new Sortbydistance(this));

        return cellList;
    }

    /**
     * Compares two cell objects with one another
     *
     * @param o the Cell which will be compared to
     * @return integer with a value of either 1, 0 or -1
     */
    @Override
    public int compareTo(Cell o) {
        return Double.compare(this.getPercentage(0, false), o.getPercentage(0, false));
    }
}
