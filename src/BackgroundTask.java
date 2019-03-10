import simulation.Event;
import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

//Mohamed Elayat and Fatima Mostefai

//In order to dynamically update the progress bar, a SwingWorker
//method is used. The heavy task of simulating the population
//is done in the background while the lighter tasks such
//as updating the progress bar are done on the event dispatch
//thread.
class BackgroundTask extends SwingWorker<Void, Integer> {

    int N;
    int TMax;
    Model m;

    //Constructor that ties the SwingWorker class
    //to its calling Model class.
    public BackgroundTask(int N, int TMax, Model m  ){
        this.N = N;
        this.TMax = TMax;
        this.m = m;
        View.progressBar.setMaximum(  TMax  );
        m.v.running = true;
    }

    //The process method is executed
    //on the event dispatch thread.

    @Override
    protected void process(List<Integer> chunks) {
        int i = chunks.get(chunks.size()-1);
        View.progressBar.setValue(  i  ); // The last value in this array is all we care about.
    }

    //The doInBackground is executed on the working thread.
    //It's executing the heavy task of simulation.
    @Override
    protected Void doInBackground() throws Exception {

        for(  int i = 0; i < N; i++  ){
            m.insertFoundingFatherEvent();        //inserting N founding fathers into the simulation
        }

        while (  !m.events_heap.isEmpty()  ){

            Event e = m.events_heap.delMin();
            if (  e.getTime() > TMax  ){break;}     //stop if we reach TMax

            else if(  e.getSubject().getTOD() >= e.getTime()  ){
                m.handleEvent(  e  );
            }
            if (  e.getTime() > (double)(m.count * 100)  ) {

                publish(  m.count * 100  );

                m.population.put(  Math.round(  e.getTime()  * 100000000d) / 100000000d,
                        m.sims_heap.size()  );


                System.out.println(  "Time = " + Math.round(  e.getTime()  * 100000000d) / 100000000d + "" +
                        "        Population = " + m.sims_heap.size()  );
                ++m.count;

            }
        }
        System.out.println(  "**********************************************"  );
        System.out.println(  "**********************************************"  );
        System.out.println(  "**********************************************"  );
        System.out.println(  "**********************************************"  );

        return null;
    }


    //this method is executed after
    //the doInBackground method is done executing.
    //We use it to determine the coalescence after
    //simulating the population.
    //It runs on the event dispatch thread.
    @Override
    protected void done() {
        try {
            System.out.println(  "Model finished"  );
            m.getCoalescence();
            m.arr = new Map[]{  m.population, m.male_coalescence, m.female_coalescence  };
            m.v.updateChart(  m.arr  );
            m.v.m = new Model();
            m.v.running = false;
            get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}