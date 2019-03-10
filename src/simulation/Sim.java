package simulation;

import java.util.Comparator;
import java.util.Random;

//Mohamed Elayat and Fatima Mostefai

//Class containg relevant information on
//sims in the form of variable and methods.
public class Sim implements Comparator<Sim> {

    private long ID;
    private double TOB;     //Time of birth
    private double TOD;     //Time of death
    private double TOM;     //Time of mating (for females)
    private Sim mother;
    private Sim father;
    private Sim mate;
    private boolean comparisonByBirth;     //boolean to indicate if we compare by TOD or TOB

    public enum Gender {M, F}
    private Gender gender;
    private static int NEXT_SIM_ID = 0;
    private AgeModel model = new AgeModel();      //Use of simulation.AgeModel class to
                                                                    //get random values
    public static double MIN_MATING_AGE_F = 16.0;
    public static double MIN_MATING_AGE_M = 16.0;
    public static double MAX_MATING_AGE_F = 50.0;
    public static double MAX_MATING_AGE_M = 73.0;
    public static double FIDELITY = 0.90;           //odds that a partner stays faithful


    /***************************************************************************
     * Constructors, getters and setters
     ***************************************************************************/

    //Constructor for founding fathers
    public Sim() {
        this.ID = NEXT_SIM_ID++;
        this.TOB = 0.0;
        this.mother = null;
        this.father = null;
        this.gender = randomGender();
        this.TOD = TOB + randomLifespan();
        comparisonByBirth = false;
        if (gender == Gender.F) {
            this.TOM = randomWaitingTime();
        } else {
            this.TOM = -1;
        }
    }

    //Constructor for subsequent people
    public Sim(  double TOB, Sim mother, Sim father  ){
        this.ID = NEXT_SIM_ID++;
        this.TOB = TOB;
        this.mother = mother;
        this.father = father;
        this.gender = randomGender();
        this.TOD = TOB + randomLifespan();
        comparisonByBirth = false;
        if (gender == Gender.F) {
            this.TOM = TOB + randomWaitingTime();
        } else {
            this.TOM = -1;
        }
    }


    public long getID() {
        return this.ID;
    }

    public double getTOB() {
        return this.TOB;
    }

    public double getTOD() {
        return this.TOD;
    }

    public double getTOM() {
        return this.TOM;
    }

    public Sim getMother() {
        return this.mother;
    }

    public Sim getFather() {
        return this.father;
    }

    public Sim getMate() {
        return this.mate;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setMate(  Sim mate  ){
        this.mate = mate;
    }

    public void setTOM(  double TOM  ){
        this.TOM = TOM;
    }

    public void setCompareByBirth() {
        this.comparisonByBirth = true;
    }

    /***************************************************************************
     * Main functions
     ***************************************************************************/

    @Override
    public int compare(Sim sim1, Sim sim2) {
        if(  sim1.comparisonByBirth  ){
            return compareByBirth(  sim1, sim2  );
        }
        else{
            return compareByDeath(  sim1, sim2  );
        }
    }

    public int compareByBirth(  Sim sim1, Sim sim2  ) {
        if (sim1.TOB > sim2.TOB) {
            return -1;
        } else {
            return 1;
        }
    }

    public int compareByDeath(Sim sim1, Sim sim2) {
        if (sim1.TOD < sim2.TOD) {
            return -1;
        } else {
            return 1;
        }
    }

    //returns a random gender for a newborn
    public Gender randomGender() {
        int random = new Random().nextInt(100);
        if (random % 2 == 0) {
            return Gender.F;
        } else {
            return Gender.M;
        }
    }

    //returns a random lifespan for a newborn
    public double randomLifespan() {
        Random rand = new Random();
        return model.randomAge(rand);
    }

    //returns the random waiting time for mating
    //for females
    public double randomWaitingTime() {
        Random rand = new Random();
        double rate = 2.0 / model.expectedParenthoodSpan(MIN_MATING_AGE_F, MAX_MATING_AGE_F);
        return model.randomWaitingTime(rand, rate);
    }

    public boolean isOfMatingAge(double time) {
        if (time < getTOD()) {
            double age = Math.abs(  time - getTOB()  );
            if (getGender() == Gender.F) {

                if (age >= MIN_MATING_AGE_F && age <= MAX_MATING_AGE_F) {
                    return true;
                } else {
                    return false;
                }

            } else {

                if (age >= MIN_MATING_AGE_M && age <= MAX_MATING_AGE_M) {
                    return true;
                } else {
                    return false;
                }

            }

        } else {
            return false;
        }
    }

    //checks if a person is currently in a relationship
    public boolean isInARelationship(  double time  ) {
        return (  mate != null && mate.getTOD() > time && mate.getMate() == this  );
    }

}

