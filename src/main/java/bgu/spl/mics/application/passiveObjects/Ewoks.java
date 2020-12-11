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
    private List<Ewok> synEwokList;

    private static class EwoksSingletonHolder{
        private static Ewoks ewoksInstance = new Ewoks();
    }
    private Ewoks(){
        synEwokList = new LinkedList<Ewok>();
    }

   // private Ewoks (int numEwoks){
   //     this.numEwoks = numEwoks;
   //     System.out.println("EWOKSSSS");
   //     setNumEwoks();
   // }
   // public synchronized void getEwok(int[] serialNumbers){
///return;
   // }
    public static Ewoks getInstance(){
        //if(EwoksSingletonHolder.ewoksInstance == null){
            //synchronized (Ewoks.class){
                //if(EwoksSingletonHolder.ewoksInstance == null){
                    return EwoksSingletonHolder.ewoksInstance;
               // }
           // }
        }
        //return Ewoks;
    //}
    private boolean resourceManager(int[] serialNumbers){
        for(int i = 0; i < serialNumbers.length; i++){
            if(!isEwokAvailable(i)){
                return false;
                //TODO: wait for the next event
            }
        }
        assignResources(serialNumbers);
        return true;
    }

    private void releaseResources(int[] serialNumbers){
        for (int serialNumber : serialNumbers) {
            synEwokList.get(serialNumbers[serialNumber]).release();
        }
        //notifyAll()
    }

    private void setNumEwoks(){
        List<Ewok> ewokList = null;
        //for(int i = 1; i<=numEwoks; i++){
           // ewokList.add(new Ewok(i));
       // }
       // synEwokList = Collections.synchronizedList(ewokList);
    }

    private boolean isEwokAvailable(int serialNumber){
        return synEwokList.get(serialNumber).available;
    }

    private void assignResources(int[] serialNumbers){
        for (int serialNumber : serialNumbers) {
            synEwokList.get(serialNumbers[serialNumber]).acquire();
        }
    }

}
