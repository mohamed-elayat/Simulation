package simulation;

import java.awt.Color;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

//Mohamed Elayat and Fatima Mostefai

//This class is used to display the XY chart on the UI.
//The JFree Chart library is used.

public class ScatterPlot extends JFrame {


    //Constructor that builds a XY Scatter
    //chart given 3 maps
    public ScatterPlot(String title,
                       Map<Double, Integer> population,
                       Map<Double, Integer> guys,
                       Map<Double, Integer> girls) {
        super(title);


        XYDataset dataset = createDataset(  population, guys, girls  );

        JFreeChart chart = ChartFactory.createScatterPlot(
                title,"X-Axis", "Y-Axis", dataset);

        NumberAxis domainAxis = new NumberAxis("Time in Years");
        NumberAxis rangeAxis = new LogarithmicAxis("Population");

        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setBackgroundPaint(new Color(255,228,196));

        plot.setDomainAxis(  domainAxis  );


        //try-catch statement is used to prevent
        //the case of trying to calculate the
        // logarithm of 0
        try {
            plot.setRangeAxis(rangeAxis);
        }
        catch(  RuntimeException re  ){
            rangeAxis.setRange(  1, rangeAxis.getUpperBound()  );
            plot.setRangeAxis(  rangeAxis  );
        }

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) chart.getXYPlot().getRenderer();

        renderer.setSeriesLinesVisible(0, true  );
        renderer.setSeriesShapesVisible(  0, true  );

        // Create Panel
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }


    //uses 3 for loops to move the data from the maps
    //to a dataset. This data will be shown on the XY
    //chart
    private XYDataset createDataset(Map<Double, Integer>  population,
                                       Map<Double, Integer>  guys,
                                       Map<Double, Integer>  girls) {


        XYSeries series0 = new XYSeries("Live population");
        for (  Map.Entry<Double, Integer> entry : population.entrySet()) {
            series0.add(entry.getKey(), entry.getValue());
        }

        XYSeries series1 = new XYSeries("Gene coalescence for males");
        for (  Map.Entry<Double, Integer> entry : guys.entrySet()) {
            series1.add(  entry.getKey(), entry.getValue()  );
        }

        XYSeries series2 = new XYSeries("Gene coalescence for females");
        for (  Map.Entry<Double, Integer> entry : girls.entrySet()) {
            series2.add(  entry.getKey(), entry.getValue()  );
        }

        XYSeriesCollection dataset = new XYSeriesCollection();

        dataset.addSeries(series0);
        dataset.addSeries(series1);
        dataset.addSeries(series2);

        return dataset;
    }


    //method that returns a chart
    public static ScatterPlot createChart(Map<Double, Integer>[] arr  ) {

        ScatterPlot temp = new ScatterPlot("Human Population Simulator",
                    arr[0],
                    arr[1],
                    arr[2]);

        temp.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return temp;
    }

}
