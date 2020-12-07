package bgu.spl.mics.application.passiveObjects;


import java.util.List;

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
    private final int numEwoks;
    private List<Ewok> ewokList;
    private static Ewoks instance;

    private Ewoks (int numEwoks){
        this.numEwoks = numEwoks;
        System.out.println("EWOKSSSS");
        setNumEwoks();
    }
    public static Ewoks getInstance(int numEwoks){
        if(instance == null){
            synchronized (Ewoks.class){
                if(instance == null){
                    instance = new Ewoks(numEwoks);
                }
            }
        }
        return instance;
    }
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

    public void releaseResources(int[] serialNumbers){
        for (int serialNumber : serialNumbers) {
            ewokList.get(serialNumbers[serialNumber]).release();
        }
        //notifyAll()
    }

    private void setNumEwoks(){
        for(int i = 1; i<=numEwoks; i++){
            ewokList.add(new Ewok(i));
        }
    }

    private boolean isEwokAvailable(int serialNumber){
        return ewokList.get(serialNumber).available;
    }

    private void assignResources(int[] serialNumbers){
        for (int serialNumber : serialNumbers) {
            ewokList.get(serialNumbers[serialNumber]).acquire();
        }
    }

}
