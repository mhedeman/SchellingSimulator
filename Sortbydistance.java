import java.util.Comparator;

public class Sortbydistance implements Comparator<Cell> {


    private final Cell cell;

    public Sortbydistance(Cell cell) {
        this.cell = cell;
    }

    @Override
    public int compare(Cell o1, Cell o2) {
        if (o1.getDistanceTo(this.cell) < o2.getDistanceTo(this.cell)) {
            return 1;
        } else if (o1.getDistanceTo(this.cell) > o2.getDistanceTo(this.cell)) {
            return -1;
        }

        return 0;
    }
}
