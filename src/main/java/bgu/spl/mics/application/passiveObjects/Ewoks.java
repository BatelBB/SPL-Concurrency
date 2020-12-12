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
    private final ArrayList<Ewok> ewokArray;

    //Singleton holder
    private static class EwoksSingletonHolder {
        private static final Ewoks ewoksInstance = new Ewoks();
    }

    //private constructor
    private Ewoks() {
        ewokArray = new ArrayList<>();
    }

    // private Ewoks (int numEwoks){
    //     this.numEwoks = numEwoks;
    //     System.out.println("EWOKSSSS");
    //     setNumEwoks();
    // }
    // public synchronized void getEwok(int[] serialNumbers){
///return;
    // }

    /**
     * OUR
     * Singleton getInstance
     *
     * @return EwoksSingletonHolder.ewoksInstance
     */
    public static Ewoks getInstance() {
        //if(EwoksSingletonHolder.ewoksInstance == null){
        //synchronized (Ewoks.class){
        //if(EwoksSingletonHolder.ewoksInstance == null){
        return EwoksSingletonHolder.ewoksInstance;
        // }
        // }
    }

    /**
     * OUR
     * Checks if the ewok with the serial number is available
     * if it's available it calls the assignResources method
     *
     * @param serialNumber
     */
    public synchronized void resourceManager(int serialNumber) {
        Ewok ewok = ewokArray.get(serialNumber-1);
        while (!ewok.isAvailable()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println("Exception was thrown: " + e);
                }
            }
        ewok.acquire();
    }

    /**
     * OUR
     * Get the serial number to release
     * notifies all when it's done.
     *
     * @param serialNumber
     */
    public void releaseResources(int serialNumber) {
        Ewok ewok = ewokArray.get(serialNumber-1);
        synchronized (this) {
            ewok.release();
            notifyAll();
        }

    }

    /**
     * OUR
     * adds the new ewoks to the ewokArray
     *
     * @param numEwoks
     */
    public void setNumEwoks(int numEwoks) {
        for (int i = 1; i <= numEwoks; i++) {
            this.ewokArray.add(new Ewok(i));
        }
    }
}
