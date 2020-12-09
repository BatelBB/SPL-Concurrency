package bgu.spl.mics;

import java.util.Queue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	//Default constructor
	private void MessageBusImpl(){

	}

	//https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=babcd640e5192717d1fa0d878f07a254
	//singleton
	public void getInstance(){

	}
	
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		
    }

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		
	}

	
	@Override
	//an event sent without an already-waiting microservice indicates a wrong implementation.
	//https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=9fc3d8cc9abf9ce6694b27318a17d457
	public <T> Future<T> sendEvent(Event<T> e) {
		
        return null;
	}

	//no need to check if already registered
	//https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=75e8a7aea910dad3b1eaed9ce4e23a1d
	@Override
	public void register(MicroService m) {

	}

	@Override
	public void unregister(MicroService m) {
		
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		return null;
	}
}
