package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
    private Map<MicroService, LinkedBlockingQueue<Message>> MicroServiceMap;
    private Map<String, LinkedBlockingQueue<MicroService>> MessageMap;
    private Map<Message, Future> eventFutureHashMap;

    //singleton holder
    private static class MessageBusImplSingletonHolder {
        private static final MessageBus messageBus = new MessageBusImpl();
    }

    //Default constructor
    private MessageBusImpl() {
        MicroServiceMap = new ConcurrentHashMap<>();
        MessageMap = new ConcurrentHashMap<>();
        MessageMap.put(Event.class.getName(), new LinkedBlockingQueue<>());
        MessageMap.put(AttackEvent.class.getName(), new LinkedBlockingQueue<>());
        MessageMap.put(BombDestroyerEvent.class.getName(), new LinkedBlockingQueue<>());
        MessageMap.put(DeactivationEvent.class.getName(), new LinkedBlockingQueue<>());
        MessageMap.put(Broadcast.class.getName(), new LinkedBlockingQueue<>());
        MessageMap.put(TerminateBroadcast.class.getName(), new LinkedBlockingQueue<>());
        eventFutureHashMap = new ConcurrentHashMap<>();
    }

    //https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=babcd640e5192717d1fa0d878f07a254
    //singleton
    public static MessageBus getInstance() {
        return MessageBusImplSingletonHolder.messageBus;
    }

    /**
     * Subscribes {@code m} to receive {@link Event}s of type {@code type}.
     * <p>
     *
     * @param <T>  The type of the result expected by the completed event.
     * @param type The type to subscribe to,
     * @param m    The subscribing micro-service.
     */
    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {

        //if the massage map doesn't contain the Event type add it in
        if (!MessageMap.containsKey(type.getName()))
            MessageMap.put(type.getName(), new LinkedBlockingQueue<>());

        //locks other threads from coming inside
        synchronized (MessageMap.get(type.getName())) {
            //the micro service m isn't subscribed to receive the Event type
            if (!MessageMap.get(type.getName()).contains(m)) {
                try {
                    //gets the message from the messageMap and puts it inside
                    MessageMap.get(type.getName()).put(m);
                    System.out.println(m.getName() + " subscribed to " + type.getSimpleName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Subscribes {@code m} to receive {@link Broadcast}s of type {@code type}.
     * <p>
     *
     * @param type The type to subscribe to.
     * @param m    The subscribing micro-service.
     */
    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        //if the massage map doesn't contain the broadcast type add it in
        if (!MessageMap.containsKey(type.getName()))
            MessageMap.put(type.getName(), new LinkedBlockingQueue<>());

        //locks other threads from coming inside
        synchronized (MessageMap.get(type.getName())) {

            //the micro service m isn't subscribed to receive the broadcast type
            if (!MessageMap.get(type.getName()).contains(m))
                try {
                    //gets the message from the messageMap and puts it inside
                    MessageMap.get(type.getName()).put(m);
                    System.out.println(m.getName() + " subscribed to " + type.getSimpleName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * Notifies the MessageBus that the event {@code e} is completed and its
     * result was {@code result}.
     * When this method is called, the message-bus will resolve the {@link Future}
     * object associated with {@link Event} {@code e}.
     * <p>
     *
     * @param <T>    The type of the result expected by the completed event.
     * @param e      The completed event.
     * @param result The resolved result of the completed event.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> void complete(Event<T> e, T result) {
        //takes the Future object from the HashMap and resolves it
        Future future = eventFutureHashMap.get(e);
        //resolves it
        future.resolve(result);
    }

    /**
     * Adds the {@link Broadcast} {@code b} to the message queues of all the
     * micro-services subscribed to {@code b.getClass()}.
     * <p>
     *
     * @param b The message to added to the queues.
     */
    @Override
    public void sendBroadcast(Broadcast b) {
        //creat a queue that contains all the micro services that are subscribed to receive the broadcast
        LinkedBlockingQueue<MicroService> MicroServiceBlockingQueue = MessageMap.get(b.getClass().getName());

        //runs on the queue and adds the broadcast
        for (MicroService microService : MicroServiceBlockingQueue) {
            try {
                MicroServiceMap.get(microService).put(b);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds the {@link Event} {@code e} to the message queue of one of the
     * micro-services subscribed to {@code e.getClass()} in a round-robin
     * fashion. This method should be non-blocking.
     * <p>
     *
     * @param <T> The type of the result expected by the event and its corresponding future object.
     * @param e   The event to add to the queue.
     * @return {@link Future<T>} object to be resolved once the processing is complete,
     * null in case no micro-service has subscribed to {@code e.getClass()}.
     */
    @Override
    //an event sent without an already-waiting microservice indicates a wrong implementation.
    //https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=9fc3d8cc9abf9ce6694b27318a17d457
    public <T> Future<T> sendEvent(Event<T> e) {

        //if the massage map is empty there are no events to send
        if (!MessageMap.containsKey(e.getClass().getName())) {
            return null;
        }
        synchronized (MessageMap.get(e.getClass().getName())) {

            //creat a queue of micro services that are subscribed for this event type
            LinkedBlockingQueue<MicroService> MicroServiceBlockingQueue = MessageMap.get(e.getClass().getName());

            //if the massage map is empty
            if (MessageMap.get(e.getClass().getName()).isEmpty())
                return null;


            Future<T> future = new Future<>();
            eventFutureHashMap.put(e, future);
            try {
                //ROUND-ROBIN METHOD
                //take out the first micro service and add it to the end of the queue
                MicroService SingleMicroService = MicroServiceBlockingQueue.take();
                MicroServiceBlockingQueue.add(SingleMicroService);

                //looks for the microservice in the microservice map and adds the event to its queue
                if (MicroServiceMap.containsKey(SingleMicroService))
                    MicroServiceMap.get(SingleMicroService).add(e);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            //returns the future that is in the futureMap
            return future;
        }
    }

    /**
     * Allocates a message-queue for the {@link MicroService} {@code m}.
     * <p>
     *
     * @param m the micro-service to create a queue for.
     */
    //no need to check if already registered
    //https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=75e8a7aea910dad3b1eaed9ce4e23a1d
    @Override
    public void register(MicroService m) {
        System.out.println(m.getName() + " registered");
        //adds the microservice to the microservice map
        MicroServiceMap.put(m, new LinkedBlockingQueue<>());
    }

    /**
     * Removes the message queue allocated to {@code m} via the call to
     * {@link #register(bgu.spl.mics.MicroService)} and cleans all references
     * related to {@code m} in this message-bus. If {@code m} was not
     * registered, nothing should happen.
     * <p>
     *
     * @param m the micro-service to unregister.
     */
    @Override
    public void unregister(MicroService m) {
        //checks if the microservice is inside the microserviceMap
        if (MicroServiceMap.containsKey(m)) {
            //checks the messages of the microservice
            for (Message message : MicroServiceMap.get(m)) {
                //enters with the message as the key
                synchronized (eventFutureHashMap.get(message)) {
                    //if there is still a eventFutureHashMap allocated to m remove it
                    if (eventFutureHashMap.get(message) != null) {
                        //if the message isn't null then it resolves it
                        eventFutureHashMap.get(message).resolve(true);
                    }
                }
            }
            //locks other threads from removing themselves while a thread removes it self
            synchronized (m) {
                MicroServiceMap.remove(m);
            }
            for (Map.Entry<String, LinkedBlockingQueue<MicroService>> event : MessageMap.entrySet()) {
                synchronized (event) {
                    //if the event contains the microservice then we remove the microservice from the map
                    if (event.getValue().contains(m)) {
                        event.getValue().remove(m);
                    }
                }
            }
        }
    }

    /**
     * Using this method, a <b>registered</b> micro-service can take message
     * from its allocated queue.
     * This method is blocking meaning that if no messages
     * are available in the micro-service queue it
     * should wait until a message becomes available.
     * The method should throw the {@link IllegalStateException} in the case
     * where {@code m} was never registered.
     * <p>
     *
     * @param m The micro-service requesting to take a message from its message
     *          queue.
     * @return The next message in the {@code m}'s queue (blocking).
     * @throws InterruptedException if interrupted while waiting for a message
     *                              to became available.
     */
    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        Message message = null;
        if(!isRegistered(m)) throw new IllegalStateException();
        try {
            //uses method of the blockingQueue
            //checks if the message is available and if so takes it
            message = MicroServiceMap.get(m).take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return message;
    }

    private boolean isRegistered(MicroService m){
        return MicroServiceMap.containsKey(m);
    }
}