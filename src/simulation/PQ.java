package simulation;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Random;

//Mohamed Elayat and Fatima Mostefai

//Class for a generic priority queue data
//structure.
public class PQ <T extends Comparator<T>> {

    private T[] pq;     //heap-ordered complete binary tree
    private int N;      //size of the priority queue

    /***************************************************************************
     * Constructors
     ***************************************************************************/

    public PQ(int initCapacity) {
        pq = (T[]) new Comparator[initCapacity + 1];
        N = 0;
    }

    public PQ() {
        this(1);
    }


    /***************************************************************************
     * Main functions
     ***************************************************************************/



    public void insert(T x) {
        if (N == pq.length - 1) resize(2 * pq.length);      //checks if resizing is necessary
        pq[++N] = x;
        swim(N);
    }


    public T delMin() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        T min = pq[1];
        exch(1, N--);
        pq[N+1] = null; // to avoid loitering
        sink(1);
        if ((N > 0) && (N == (pq.length - 1) / 4)) resize(pq.length / 2);       //checks if resizing is necessary
        return min;
    }

    private void swim(int k) {
        while (  k > 1 && less(k, k/2)  ) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2 * k <= N) {
            int j = 2 * k;
            if (j < N && less(j+1, j)) j++;
            if (  less(k, j)  ) break;
            exch(k, j);
            k = j;
        }
    }

    //Uses the compare function of the generic type to
    //compare elements
    private boolean less (  int i, int j  ){
        return (  pq[i].compare(  pq[i], pq[j]  ) < 0  );
    }

    private void exch(int i, int j) {
        T t = pq[i]; pq[i] = pq[j]; pq[j] = t;
    }

    //Returns a random element in
    //the priority queue
    public Sim randomSim(){
        Random rand = new Random();
        int index = rand.nextInt(  size()  ) + 1;
        return (Sim) pq[index];
    }

    /***************************************************************************
     * Other functions
     ***************************************************************************/

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public int arraySize(){
        return pq.length;
    }

    public T getPq(  int i  ){
        return pq[i];
    }


    //function to print an element
    // in the simulation.PQ
    public void printT(  T x  ) {
        if (x instanceof Event) {
            Event xx = (Event) x;
            System.out.println("Subject = " + xx.getSubject() + " Type is " + xx.getType() + " Time is " + xx.getTime());
        } else if (x instanceof Sim) {
            Sim ss = (Sim) x;
            System.out.println("ID = " + ss.getID() + " TOB = " + ss.getTOB() + " TOD = " + ss.getTOD()
                    + " TOM = " + ss.getTOM() + " mother = " + ss.getMother() + " father = "
                    + ss.getFather() + " gender = " + ss.getGender());
        }
    }

    public void printPQ(){
        for(  int i = 1; i <= N; i++  ){
            printT(  pq[i]  );
        }
        System.out.println();
    }

    // helper function to resize an array
    private void resize(int capacity) {
        assert capacity > N;
        T[] temp = (T[]) new Comparator[capacity];
        for (int i = 1; i <= N; i++) {
            temp[i] = pq[i];
        }
        pq = temp;
    }

}
