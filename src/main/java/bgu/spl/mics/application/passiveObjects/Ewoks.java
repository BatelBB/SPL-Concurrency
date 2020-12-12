package bgu.spl.mics.application.passiveObjects;


import java.util.*;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
//can add public method
//https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=1823489ac57e9f027ea846b1b636a554
public class Ewoks {
    private int numEwoks;
    private final ArrayList<Ewok> synEwokArray;

    private static class EwoksSingletonHolder {
        private static final Ewoks ewoksInstance = new Ewoks();
    }

    private Ewoks() {
        synEwokArray = new ArrayList<Ewok>();
    }

    // private Ewoks (int numEwoks){
    //     this.numEwoks = numEwoks;
    //     System.out.println("EWOKSSSS");
    //     setNumEwoks();
    // }
    // public synchronized void getEwok(int[] serialNumbers){
///return;
    // }
    public static Ewoks getInstance() {
        //if(EwoksSingletonHolder.ewoksInstance == null){
        //synchronized (Ewoks.class){
        //if(EwoksSingletonHolder.ewoksInstance == null){
        return EwoksSingletonHolder.ewoksInstance;
        // }
        // }
    }

    //return Ewoks;
    //}
    public void resourceManager(int serialNumber) {
        Ewok ewok = synEwokArray.get(serialNumber);
        synchronized (ewok) {
            while (!ewok.isAvailable()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println("Exception was thrown: " + e);
                }
            }
            assignResources(serialNumber);
        }
    }

    public void releaseResources(int serialNumber) {
        Ewok ewok = synEwokArray.get(serialNumber);
        synchronized (ewok) {
            ewok.release();
        }
        notifyAll();
    }

    public void setNumEwoks(int numEwoks) {
        for (int i = 1; i <= numEwoks; i++) {
            this.synEwokArray.add(new Ewok(i));
        }
    }

    public boolean isEwokAvailable(int serialNumber) {
        return synEwokArray.get(serialNumber).available;
    }

    private void assignResources(int serialNumber) {
        for (int i = 1; i <= serialNumber; i++) {
            synEwokArray.get(i).acquire();
        }
    }

}
