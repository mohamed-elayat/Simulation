import simulation.Event;
import simulation.PQ;
import simulation.Sim;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

//Mohamed Elayat and Fatima Mostefai

//Main class of the program. Contains
//all the logic: the methods in this
//class handle the priority queues we're
//using to store the data about the sims.
//The class also contains the methods to
//obtain the coalescence of lineages.
public class Model {

    PQ <Event> events_heap = new PQ <>();       //simulation.PQ that holds the events
    PQ<Sim> sims_heap = new PQ <>();           //simulation.PQ that holds the live population
    PQ<Sim> male_heap = new PQ();               //simulation.PQ for males that goes back in time for coalescence
    PQ<Sim> female_heap = new PQ();             //pq for females that goes back in time for coalscence
    Map<Double, Integer> male_coalescence = new LinkedHashMap();
    Map<Double, Integer> female_coalescence = new LinkedHashMap();
    Map<Double, Integer> population = new LinkedHashMap();
    Map<Double, Integer>[] arr;
    View v;


    int count = 1;

    /***************************************************************************
     * SwingWorker method
     ***************************************************************************/

    public void executeBackgroundTask(  int initial_pop, int duration, View v  ){
        this.v = v;
        BackgroundTask task = new BackgroundTask(  initial_pop, duration, this  );
        task.execute();
    }


    /***************************************************************************
     * Simulation functions
     ***************************************************************************/


    public void handleEvent(  Event e  ){
        switch (  e.getType()  ) {
            case BIRTH:
                handleBirth(  e  );
                break;
            case DEATH:
                handleDeath();
                break;
            case MATING:
                handleMating(  e  );
                break;
        }
    }


    public void handleBirth(  Event e  ){
        insertPerson(  e  );
        insertMatingEvent(  e  );
        insertDeathEvent(  e  );
    }

    //removes the earliest person to die from the population simulation.PQ
    public void handleDeath(){
        sims_heap.delMin();
    }

    //if she's dead do nothing
    //if not of age, give her another time
    //if she has the right age, mate
    public void handleMating(  Event e  ){

        if(  e.getSubject().getTOD() <= e.getTime()  ){

        }

        else if(  !e.getSubject().isOfMatingAge(  e.getTime()  ) ){
            e.getSubject().setTOM(  e.getTime() + e.getSubject().randomWaitingTime()  );
            insertMatingEvent(  e  );
        }

        else{
            findMate(  e  );
            if(  e.getSubject().getMate() != null  ) {
                insertBirthEvent(e, e.getSubject(), e.getSubject().getMate());
            }
        }

    }


    public void insertFoundingFatherEvent(){
        Sim fondateur = new Sim();
        Event creation = new Event(  fondateur, 0.0, Event.Type.BIRTH  );
        events_heap.insert(  creation  );
    }

    //create new baby and insert it. Also
    //gives the mother another mating time.
    public void insertBirthEvent(  Event e, Sim mother, Sim father  ){
        Sim baby = new Sim(  e.getTime(), mother, father  );
        Event birth = new Event(  baby, baby.getTOB(), Event.Type.BIRTH  );
        events_heap.insert(  birth  );
        e.getSubject().setTOM(  e.getTime() + e.getSubject().randomWaitingTime()  );
        insertMatingEvent(  e  );
    }

    //removes the person from the live population simulation.PQ
    public void insertDeathEvent(  Event e  ){
        Event death = new Event(  e.getSubject(), e.getSubject().getTOD(), Event.Type.DEATH  );
        events_heap.insert(  death  );
    }

    //inserts a person into the live population simulation.PQ
    public void insertPerson(  Event e  ){
        sims_heap.insert(  e.getSubject()  );
    }

    //inserts a mating event for the newboard if it's a female
    public void insertMatingEvent(  Event e  ){
        if(  e.getSubject().getTOM() != -1  ){
            Event mating = new Event(  e.getSubject(), e.getSubject().getTOM(), Event.Type.MATING  );
            events_heap.insert(  mating  );
        }
    }

    //finds a mate for a female
    //for loop is used instead of while
    //to avoid infinite while loop.
    public void findMate(  Event e  ){
        Sim x = e.getSubject();
        Sim z;
        Random rand = new Random();
        if (  !x.isInARelationship(  e.getTime()  ) || rand.nextDouble() > Sim.FIDELITY  ) {
            for(  int i = 0; i < 1000; i++  ){
                z = sims_heap.randomSim();
                if (  z.getGender() != x.getGender() && z.isOfMatingAge(  e.getTime()  )  ) {
                    if (  x.isInARelationship(  e.getTime()  ) || !z.isInARelationship(  e.getTime()  )
                            || rand.nextDouble()>Sim.FIDELITY) {
                        x.setMate(  z  );
                        break;
                    }
                }
            }

        }

    }

    /***************************************************************************
     * Coalescence functions
     ***************************************************************************/

    public void getCoalescence(){
        dividePopulation();

        getMaleCoalescence();
        getFemaleCoalescence();

        printMaleCoalescence();
        printFemaleCoalescence();
    }

    //splits the live population in
    //the end into 2 heaps of males
    //and females. Ordered by youngest
    //to oldest sims
    public void dividePopulation() {
        while(!this.sims_heap.isEmpty()) {
            Sim sim = sims_heap.delMin();
            sim.setCompareByBirth();
            if (sim.getGender() == Sim.Gender.M) {
                this.male_heap.insert(sim);
            }

            if (sim.getGender() == Sim.Gender.F) {
                this.female_heap.insert(sim);
            }
        }
    }


    //goes back in time by shifting
    //from a sim to the next younger one.
    //Upon encountering a coalescence point,
    //adds it the the male coalescence map
    public void getMaleCoalescence() {
        while (  !this.male_heap.isEmpty()  ) {

            Sim youngest = male_heap.delMin();
            boolean x = true;

            for(int i = 0; i < this.male_heap.size(); ++i) {
                if (this.male_heap.getPq(i) == youngest.getFather()) {
                    this.male_coalescence.put(  youngest.getTOB(), this.male_heap.size()  );
                    x = false;
                }
            }

            if(  youngest.getFather() == null  ){
                male_heap.insert(  youngest  );
                return;
            }

            if(  x == true  ){
                youngest.getFather().setCompareByBirth();
                male_heap.insert(  youngest.getFather()  );
            }

        }

    }

    //goes back in time by shifting
    //from a sim to the next younger one.
    //Upon encountering a coalescence point,
    //adds it the the female coalescence map
    public void getFemaleCoalescence() {
        while (  !this.female_heap.isEmpty()  ) {
            Sim youngest = female_heap.delMin();

            boolean x = true;

            for(int i = 0; i < this.female_heap.size(); ++i) {
                if (this.female_heap.getPq(i) == youngest.getMother()) {
                    this.female_coalescence.put(  youngest.getTOB(), this.female_heap.size()  );
                    x = false;
                }
            }

            if(  youngest.getMother() == null  ){
                female_heap.insert(  youngest  );
                return;
            }

            if(  x == true  ){
                youngest.getMother().setCompareByBirth();
                female_heap.insert(  youngest.getMother()  );
            }

        }

    }

    //Helper function
    public void printMaleCoalescence() {
        System.out.println(  "**********************************************"  );
        System.out.println(  "**********************************************"  );
        System.out.println(  "**********************************************"  );
        System.out.println(  "**********************************************"  );
        for (Map.Entry<Double, Integer> entry : male_coalescence.entrySet()) {
            System.out.println(  "Time = " + entry.getKey()+ "      Male coalescence lines = " +entry.getValue());
        }
    }
    //Helper function
    public void printFemaleCoalescence() {
        System.out.println(  "**********************************************"  );
        System.out.println(  "**********************************************"  );
        System.out.println(  "**********************************************"  );
        System.out.println(  "**********************************************"  );
        for (Map.Entry<Double, Integer> entry : female_coalescence.entrySet()) {
            System.out.println(  "Time = " + entry.getKey()+ "      Female coalescence lines = " +entry.getValue());
        }
    }



}