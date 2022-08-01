import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class Chart {

    MySwingWorker mySwingWorker;
    JFreeChart chart;
    private World world;
    private final boolean type;

    private final TimeSeries ts = new TimeSeries("data", Millisecond.class);

    public Chart(World world, String title, String yTitle, boolean type) {
        this.world = world;

        TimeSeriesCollection dataset = new TimeSeriesCollection(ts);
        chart = ChartFactory.createTimeSeriesChart(
                title,
                "Time",
                yTitle,
                dataset,
                false,
                false,
                false
        );

        this.type = type;
        this.mySwingWorker = new MySwingWorker(this, this.type);
    }

    public void go() {
        this.mySwingWorker.execute();
    }

    public JFreeChart getChart() {
        return this.chart;
    }

    public void pause() {
        this.mySwingWorker.cancel(true);
        this.mySwingWorker = new MySwingWorker(this, this.type);
    }

    public World getWorld() {
        return this.world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public TimeSeries getTs() {
        return this.ts;
    }
}
