package bgu.spl.mics;

import java.util.concurrent.TimeUnit;

/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * 
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
//Future objects allow threads to inform one each other in regards to the result of the computation that followed an event that was sent.
public class Future<T> {
	private boolean isDone;
    private T result;
    private final Object lockGet = new Object(); //maybe that's wrong and not supposed to be Object()
	
	/**
	 * This should be the the only public constructor in this class.
	 */
	public Future() {
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     * @return return the result of type T if it is available, if not wait until it is available.
     * 	       
     */
	//must use synchronization
	public T get() {
		//checks if resolve
		synchronized(lockGet) {
			while (!this.isDone()) {
				try {
					wait();
				} catch (InterruptedException e){
					Thread.currentThread().interrupt();
					System.out.println("Interrupted the get function thread");

				}
			}
			return this.result; //maybe that's not good
		}
	}
	
	/**
     * Resolves the result of this Future object.
     */
	//maybe that's it?
	public void resolve (T result) {
		this.result = result;
		this.isDone = true;
	}
	
	/**
     * @return true if this object has been resolved, false otherwise
     */
	//maybe that's it?
	public boolean isDone() {
		return this.isDone;
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
     * @param timeout 	the maximal amount of time units to wait for the result.
     * @param unit		the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not, 
     * 	       wait for {@code timeout} TimeUnits {@code unit}. If time has
     *         elapsed, return null.
	 *
	 * https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=490faed59f5937c06644b33f0fbc7c6d
	 * If the value is resolved - the function returns it.
	 * Otherwise - it waits for maximum of 'timeout' for the value to be resolved.
	 * If the value is resolved within 'timeout' - the value is returned.
	 * Otherwise - null (or some default value) is returned.
	 *
	 * https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=2abf8010e8cec3b2b0314222dee2c2ee
	 * You can throw an exception
     */
	public T get(long timeout, TimeUnit unit) {
		long millisTime = unit.toMillis(timeout);
		final long deadline = System.currentTimeMillis() + millisTime;
		long currentTime = deadline - System.currentTimeMillis();
			while(currentTime < deadline){
				if(isDone)
					return this.result;
				currentTime = System.currentTimeMillis();
			}
			return null;
	}


}
