
public class Agent {

    private final int group;
    private double satisfactionThreshold;
    private Cell currentCell;

    public Agent(int group, double satisfactionThreshold, Cell currentCell) {
        this.group = group;
        this.satisfactionThreshold = satisfactionThreshold;
        this.currentCell = currentCell;
    }

    public int getGroup() {
        return this.group;
    }

    public Cell getCurrentCell() {
        return this.currentCell;
    }

    public void setSatisfactionThreshold(double satisfactionThreshold) {
        this.satisfactionThreshold = satisfactionThreshold;
    }

    public double getSatisfactionThreshold() {
        return this.satisfactionThreshold;
    }

    /**
     * Method that determines which visible Cell a Creature moves to
     */
    public void move() {
        // Check if Agent is presently unhappy and move the agent to a satisfactory location
        if (this.currentCell.getPercentage(this.group, false) < this.satisfactionThreshold) {
            for (Cell cell : this.currentCell.getNearestCells()) {
                if (cell.getPercentage(this.group, false) >= this.satisfactionThreshold && cell.isEmpty()) {
                    this.moveTo(cell);
                }
            }
        }
    }

    /**
     * Moves the Agent to a new cell. In doing so, first the Agent is removed from its current cell, if it has one.
     * Then, the Agent its current Cell is changed to the new Cell and the Agent is added to the new Cell
     *
     * @param newCell Cell which the Creature is to be moved to
     */
    public void moveTo(Cell newCell) {
        if (this.currentCell != null) {
            this.currentCell.removeAgent();
        }

        this.currentCell = newCell;
        this.currentCell.addAgent(this);
    }
}
