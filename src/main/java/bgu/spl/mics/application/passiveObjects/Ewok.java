package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Ewok {
	int serialNumber;
	boolean available;


    /**
     * Constructor
     * *My own*
     * @param serialNumber - represents the creature
     */
	public Ewok(int serialNumber){
        this.available = true;
        this.serialNumber = serialNumber;
    }

    public void setSerialNumber(int serialNumber){
        this.serialNumber = serialNumber;
    }

    public int getSerialNumber(){
        return serialNumber;
    }

    public boolean isAvailable(){
	    return available;
    }
    /**
     * Acquires an Ewok
     */
    public synchronized void acquire() {
		available = false;
        notifyAll();
    }

    /**
     * release an Ewok
     */
    public synchronized void release() {
    	available = true;
    	notifyAll();
    }
}
