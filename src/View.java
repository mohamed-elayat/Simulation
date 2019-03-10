import simulation.ScatterPlot;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.*;

//Mohamed Elayat and Fatima Mostefai

//Method that handles the user interface
//of the program. It handles the layout
//of the interface and the updating of the
//XY chart.

public class View{
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;

    public JButton button;
    JFrame frame;
    Container panel;
    GridBagConstraints constraints;
    ScatterPlot chart;
    static JProgressBar progressBar;
    BackgroundTask task;
    JLabel populationLabel;
    JLabel timeLabel;
    JTextField populationField;
    JTextField timeField;
    Model m;
    boolean running;


    public View(  Model m  ){
        this.m = m;
        initializeComponents();
        initializeConstraints();
        frame.pack();
        frame.setVisible(true);
        running = false;
    }


    public void addActionListeners(  ActionListener a  ) {
        button.addActionListener(  a  );
    }



    public void initializeComponents() {


        frame = new JFrame("View");
        frame.setSize(  800, 600  );
        frame.setPreferredSize(  new Dimension(  800, 600  )  );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setTitle(  "Human Population Simulator"  );

        panel = frame.getContentPane();
        panel.setLayout(new GridBagLayout());

        button = new JButton(  "SIMULATE"  );

        populationLabel = new JLabel("Starting population: ");
        timeLabel = new JLabel("Time span: ");

        populationField = new JTextField(15);
        populationField.setText(  "1000"  );

        timeField = new JTextField(15);
        timeField.setText(  "10000"  );

        progressBar = new JProgressBar();
        progressBar.setMinimum(  0  );
        progressBar.setMaximum(  100  );


        if (RIGHT_TO_LEFT) {
            panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

    }

    public void initializeConstraints(){

        constraints = new GridBagConstraints();

        if (shouldFill) {
            constraints.fill = GridBagConstraints.HORIZONTAL;
        }

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(  0, 5, 0, 0  );
        panel.add(  populationLabel, constraints  );

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.ipadx = 50;
        constraints.insets = new Insets(  0, 0, 0, 0  );
        panel.add(  populationField, constraints  );

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(  0, 5, 0, 0  );
        panel.add(timeLabel, constraints);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.ipadx = 50;
        constraints.insets = new Insets(  0, 0, 0, 0  );
        panel.add(timeField, constraints);


        button.setFont(new Font("Arial", Font.PLAIN, 22));
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridheight = 2;
        constraints.insets = new Insets(  0, 0, 0, 0  );
        panel.add(button, constraints);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        constraints.insets = new Insets(  10, 10, 10, 10  );
        constraints.gridheight = 1;
        panel.add(  progressBar, constraints  );


        JFrame blank = new JFrame();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;       //aligned with button 2
        constraints.gridwidth = 3;   //2 columns wide
        constraints.gridy = 3;       //third row
        constraints.ipady = 0;       //reset to default
        constraints.weighty = 1.0;   //request any extra vertical space
        constraints.weightx = 1.0;
        panel.add(blank.getContentPane(), constraints);

    }


    public void updateChart(  Map<Double, Integer>[] arr  ){

        chart = ScatterPlot.createChart(  arr  );

        panel.remove(  panel.getComponent(  6  )  );
        panel.add(  chart.getContentPane(), constraints  );

        progressBar.setValue(  progressBar.getMaximum()  );

        panel.revalidate();
        panel.repaint();

    }

}