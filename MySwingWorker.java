import org.jfree.data.time.Millisecond;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class MySwingWorker extends SwingWorker<Boolean, double[]> {
        LinkedList<Double> fifo = new LinkedList<>();

        private final Chart chart;
        private final boolean type;

        public MySwingWorker(Chart chart, boolean type) {
            this.chart = chart;
            this.type = type;
            fifo.add(0.0);
        }

        @Override
        protected Boolean doInBackground() {

            while (!isCancelled()) {
                fifo.add(fifo.get(fifo.size() - 1) + Math.random() - .5);
                if (fifo.size() > 500) {
                    fifo.removeFirst();
                }

                double[] array = new double[fifo.size()];
                for (int i = 0; i < fifo.size(); i++) {
                    array[i] = fifo.get(i);
                }
                publish(array);


                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    // eat it. caught when interrupt is called
                    System.out.println("MySwingWorker shut down.");
                }
            }

            return true;
        }

        @Override
        protected void process(List<double[]> chunks) {
            //System.out.println("number of chunks: " + chunks.size());

            //double[] mostRecentDataSet = chunks.get(chunks.size() - 1);

            double num;

            if (this.type) {
                num = this.chart.getWorld().getSegregation();
            } else
                num = this.chart.getWorld().getUnhappiness();

            //System.out.println(num);

            this.chart.getTs().add(new Millisecond(), num);

            long start = System.currentTimeMillis();
            long duration = System.currentTimeMillis() - start;
            try {
                //Thread.sleep(40 - duration); // 40 ms ==> 25fps
                Thread.sleep(40 - duration); // 40 ms ==> 2.5fps
            } catch (InterruptedException e) {
                System.out.println("stop");
            }
        }
}
