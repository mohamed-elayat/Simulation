package simulation;

//Mohamed Elayat and Fatima Mostefai

import java.util.Comparator;

//A class characterizing events in a simulation's
//life.
public class Event implements Comparator<Event> {

    private Sim subject;        //the person the event is applied to
    private double time;        //when the event happens
    public enum Type{BIRTH, DEATH, MATING}       //the 3 types of events
    private Type type;

    /***************************************************************************
     * Constructors, getters and setters
     ***************************************************************************/

    public Event(  Sim subject, double time, Type type  ){
        this.subject = subject;
        this.time = time;
        this.type = type;
    }

    public Type getType(){
        return this.type;
    }

    public Sim getSubject(){
        return this.subject;
    }

    public double getTime(){
        return this.time;
    }

    /***************************************************************************
     * Other
     ***************************************************************************/

    //compare function that is used by the simulation.PQ class
    //to check which event occurs first
    @Override
    public int compare(  Event event1, Event event2  ){
        if(  event1.time < event2.time  ){
            return -1;
        }
        else{
            return 1;
        }
    }

}
