import org.jfree.chart.ChartPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class WorldWindow extends JFrame {
	private static final long serialVersionUID = 3998702201167042838L;
	
	// The simulation controller
	private Controller controller;
	
	// The panel used for visualizing the world
	private WorldPanel worldPanel;
	private Chart chart, happyChart;
	private ChartPanel chartPanel, happyChartPanel;
	private JPanel rightPanel;
	
	// Interface Components
	private JButton step;
	private JButton animate;
	private JSpinner spinner, agentNumberSpinner, groupNumberSpinner;
	private JButton reset;
	private JSlider threshold;
	private JLabel thresholdLabel;
	
	// If an animation is active it is performed by this Thread
	private Thread running;
	
	/**
	 * Creates a windows that visualizes a CreatureWorld simulation using the provided controller
	 * @param e the controller which manages the creature world simulation displayed
	 *          and controlled by this window
	 */
	public WorldWindow(Controller e) {
		super();
		setTitle("Schelling Segregation Simulation");
		setSize(1300,750);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		controller = e;
		init();
	}
	
	/**
	 * Initializes GUI elements
	 */
	private void init() {
		setLayout(new BorderLayout());
		JPanel mainPanel = new JPanel(new GridLayout(1, 2));

		this.rightPanel = new JPanel(new GridLayout(2, 0));

		// The WorldPanel shows the visualization and should be central in the window
		worldPanel = new WorldPanel(controller.getWorld());
		mainPanel.add(worldPanel);
		worldPanel.repaint();

		chart = new Chart(controller.getWorld(), "Segregation", "Segregation coefficient", true);
		chartPanel = new ChartPanel(this.chart.getChart());
		rightPanel.add(chartPanel);

		happyChart = new Chart(controller.getWorld(), "Unhappiness", "% Unhappy", false);
		happyChartPanel = new ChartPanel(this.happyChart.getChart());
		rightPanel.add(happyChartPanel);

		mainPanel.add(rightPanel);
		add(mainPanel, BorderLayout.CENTER);
		
		// At the bottom we will contruct a panel with some button to control the simulation
		JPanel control = new JPanel();
		
		// Button for a single step
		step = new JButton("Step");
		step.addActionListener(step());
		control.add(step);
		
		// Buton to start an animation
		animate = new JButton("Start");
		animate.addActionListener(animate());
		control.add(animate);
		
		control.add(new JLabel("Sleep time (ms) : "));
		
		// Spinner to control the pauze between animation steps
		spinner = new JSpinner(new SpinnerNumberModel(5,0,10000,100));
		control.add(spinner);

		// Group number
		control.add(new JLabel("Number of groups:"));
		groupNumberSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 5, 1));
		control.add(groupNumberSpinner);

		// Agent number
		control.add(new JLabel("Number of agents:"));
		agentNumberSpinner = new JSpinner(new SpinnerNumberModel(1900, 0, 2500, 100));
		control.add(agentNumberSpinner);

		// Satisfation threshold adjuster
		thresholdLabel = new JLabel("Satisfaction threshold (%): 50");
		control.add(thresholdLabel);

		threshold = new JSlider(0, 100, 50);
		threshold.addChangeListener(new ThresholdListener());
		threshold.setMajorTickSpacing(20);
		threshold.setMinorTickSpacing(10);
		threshold.setPaintTicks(true);
		threshold.setPaintLabels(true);

		control.add(threshold);

		// Reset
		reset = new JButton("Reset Simulation");
		reset.addActionListener(reset());
		control.add(reset);
		
		// Adds the control panel to the bottom of the window
		add(control, BorderLayout.SOUTH);
	}
	
	/**
	 * Creates an action listener that can be called by a button to start an animation
	 * @return an ActionListener
	 */
	private ActionListener animate() {
		// Create an anonymous inner class
		return new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// If no Thread is active, start an animation
				if (running == null)
				{
					animate.setText("Stop");
					running = new AnimateThread();
					// Start the animation thread
					running.start();
					// disable all other buttons
					step.setEnabled(false);
					//spawn.setEnabled(false);
					chart.go();
					happyChart.go();
				}
				// If a thread is active, disable the animation
				else
				{
					// set the instance variable to null to indicate the thread should stop
					running = null;
					// Set all butons back to normal
					animate.setText("Start");
					step.setEnabled(true);
					//spawn.setEnabled(true);
					chart.pause();
					happyChart.pause();
				}
			}
		};
	}
	
	/**
	* Creates an action listener that can be called by a button to do one step of the simulation
	* @return the ActionListener 
	*/
	private ActionListener step() {
		// Create an anonymous inner class
		return new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Let the controller update the states of the creatures and cells
				controller.step();
				// Let the WorldPanel redraw the state of the world
				worldPanel.repaint();
			}
		};
	}

	/**
	 * Creates an action listener that can be called by a button to spawn a new creature
	 * @return the ActionListener
	 */
	private ActionListener reset() {
		return new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				worldPanel.resetWorld();
				controller = new Controller(worldPanel.getWorld(), 0.35, (int) groupNumberSpinner.getValue(), (int) agentNumberSpinner.getValue());
				controller.createRandomNeighbourhood();

				chart.getTs().clear();
				chart.setWorld(controller.getWorld());

				happyChart.getTs().clear();
				happyChart.setWorld(controller.getWorld());

				// Let the WorldPanel redraw the state of the world
				worldPanel.repaint();
			}
		};
	}
	
	/**
	 * Inner class which extends Thread for the purpose of animating the simulation
	 * @author Paul Bouman
	 */
	private class AnimateThread extends Thread {
		public AnimateThread()
		{
			super();
			// Let this Thread be a Daemon, so that it will end if all other Threads have ended
			this.setDaemon(true);
		}
		
		@Override
		public void run()
		{
			while (running == this)
			{
				// Let the controller do a step
				controller.step();
				// Let the WorldPanel redraw the state of the world
				worldPanel.repaint();

				try
				{
					// Sleep for the specified amount of time
					int time = ((Number)spinner.getValue()).intValue();
					Thread.sleep(time);
				}
				catch (Exception e) {}
			}
		}
	}

	private class ThresholdListener  implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			controller.setSatisfactionThreshold((double) threshold.getValue() / 100);
			for (Agent agent : controller.getWorld().getAgents()) {
				agent.setSatisfactionThreshold(controller.getSatisfactionThreshold());
			}

			thresholdLabel.setText("Satisfaction threshold (%): " + threshold.getValue());
		}
	}
}
