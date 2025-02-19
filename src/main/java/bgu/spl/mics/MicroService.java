package bgu.spl.mics;

import java.util.HashMap;
import java.util.Map;

/**
 * The MicroService is an abstract class that any micro-service in the system
 * must extend. The abstract MicroService class is responsible to get and
 * manipulate the singleton {@link MessageBus} instance.
 * <p>
 * Derived classes of MicroService should never directly touch the message-bus.
 * Instead, they have a set of internal protected wrapping methods (e.g.,
 * {@link #sendBroadcast(bgu.spl.mics.Broadcast)}, {@link #sendBroadcast(bgu.spl.mics.Broadcast)},
 * etc.) they can use. When subscribing to message-types,
 * the derived class also supplies a {@link Callback} that should be called when
 * a message of the subscribed type was taken from the micro-service
 * message-queue (see {@link MessageBus#register(bgu.spl.mics.MicroService)}
 * method). The abstract MicroService stores this callback together with the
 * type of the message is related to.
 * <p>
 * Only private fields and methods may be added to this class.
 * <p>
 */
//can add protected fields
//https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=df8d086ce1e673a2e74ce3350687d7f2
public abstract class MicroService implements Runnable {
    private boolean isTerminated = false;
    private MessageBus messageBusInstance;
    private Map<Class, Callback> callbackMap; //map contains the callback messages
    private final String name;


    /**
     * @param name the micro-service name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public MicroService(String name) {
        this.name = name;
        messageBusInstance = MessageBusImpl.getInstance();
        callbackMap = new HashMap<>();
    }

    /**
     * Subscribes to events of type {@code type} with the callback
     * {@code callback}. This means two things:
     * 1. Subscribe to events in the singleton event-bus using the supplied
     * {@code type}
     * 2. Store the {@code callback} so that when events of type {@code type}
     * are received it will be called.
     * <p>
     * For a received message {@code m} of type {@code type = m.getClass()}
     * calling the callback {@code callback} means running the method
     * {@link Callback#call(java.lang.Object)} by calling
     * {@code callback.call(m)}.
     * <p>
     *
     * @param <E>      The type of event to subscribe to.
     * @param <T>      The type of result expected for the subscribed event.
     * @param type     The {@link Class} representing the type of event to
     *                 subscribe to.
     * @param callback The callback that should be called when messages of type
     *                 {@code type} are taken from this micro-service message
     *                 queue.
     */
    //In case one of these threads hasn't subscribed yet, let the messages be sent to the subscribed thread
    //https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=d7d86c279f1054ba985b088234b58743
    protected final <T, E extends Event<T>> void subscribeEvent(Class<E> type, Callback<E> callback) {
        //checks if the callbackMap contains the class type that we sent
        if (!callbackMap.containsKey(type)) {
            //if not contains, adds it to the map
            callbackMap.put(type, callback);
            //sends it to the messageBus instance, to the subscribeEvent
            messageBusInstance.subscribeEvent(type, this);
        }
    }

    /**
     * Subscribes to broadcast message of type {@code type} with the callback
     * {@code callback}. This means two things:
     * 1. Subscribe to broadcast messages in the singleton event-bus using the
     * supplied {@code type}
     * 2. Store the {@code callback} so that when broadcast messages of type
     * {@code type} received it will be called.
     * <p>
     * For a received message {@code m} of type {@code type = m.getClass()}
     * calling the callback {@code callback} means running the method
     * {@link Callback#call(java.lang.Object)} by calling
     * {@code callback.call(m)}.
     * <p>
     *
     * @param <B>      The type of broadcast message to subscribe to
     * @param type     The {@link Class} representing the type of broadcast
     *                 message to subscribe to.
     * @param callback The callback that should be called when messages of type
     *                 {@code type} are taken from this micro-service message
     *                 queue.
     */
    protected final <B extends Broadcast> void subscribeBroadcast(Class<B> type, Callback<B> callback) {
        //checks if the callbackMap contains the class type that we sent
        if (!callbackMap.containsKey(type)) {
            //if not contains, adds it to the map
            callbackMap.put(type, callback);
            //sends it to the messageBus instance, to the subscribeBroadcast
            messageBusInstance.subscribeBroadcast(type, this);
        }
    }

    /**
     * Sends the event {@code e} using the message-bus and receive a {@link Future<T>}
     * object that may be resolved to hold a result. This method must be Non-Blocking since
     * there may be events which do not require any response and resolving.
     * <p>
     *
     * @param <T> The type of the expected result of the request
     *            {@code e}
     * @param e   The event to send
     * @return {@link Future<T>} object that may be resolved later by a different
     * micro-service processing this event.
     * null in case no micro-service has subscribed to {@code e.getClass()}.
     */
    //Non-blocking - the method returns even if the message was not sent (suppose no one is registered to receive it yet).
    //https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=78b4769275378966fc5a2bcc1e5aaa0f
    //If no one is subscribe, the message should be "thrown", and sendMessage() should return.
    //https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=cf677a1d8e2d25c77eb0feafb0c7e456
    protected final <T> Future<T> sendEvent(Event<T> e) {
        System.out.println(this.getName()+ " sent the event "+e.getClass().getSimpleName());
        //sends the event to the messageBus
        return messageBusInstance.sendEvent(e);
    }

    /**
     * A Micro-Service calls this method in order to send the broadcast message {@code b} using the message-bus
     * to all the services subscribed to it.
     * <p>
     *
     * @param b The broadcast message to send
     */
    protected final void sendBroadcast(Broadcast b) {
        System.out.println(this.getName()+ " sent the event "+ b.getClass().getSimpleName());
        //sends the broadcast to the messageBus
        messageBusInstance.sendBroadcast(b);
    }

    /**
     * Completes the received request {@code e} with the result {@code result}
     * using the message-bus.
     * <p>
     *
     * @param <T>    The type of the expected result of the processed event
     *               {@code e}.
     * @param e      The event to complete.
     * @param result The result to resolve the relevant Future object.
     *               {@code e}.
     */
    protected final <T> void complete(Event<T> e, T result) {
        System.out.println(this.getName() + " completed the event " + e.getClass().getSimpleName() + " with the result "+ result.getClass().getSimpleName());
        //sends the event to the messageBus to be completed
        messageBusInstance.complete(e, result);
    }

    /**
     * this method is called once when the event loop starts.
     */
    protected abstract void initialize();

    /**
     * Signals the event loop that it must terminate after handling the current
     * message.
     */
    protected final void terminate() {
        this.isTerminated = true;
    }

    /**
     * @return the name of the service - the service name is given to it in the
     * construction time and is used mainly for debugging purposes.
     */
    public final String getName() {
        return this.name;
    }

    /**
     * The entry point of the micro-service. TODO: you must complete this code
     * otherwise you will end up in an infinite loop.
     */
    @Override
    public final void run() {
        //registers the microservice
        messageBusInstance.register(this);
        //initializes the microservice
        initialize();
        //runs on loop while the microservices are alive
        while (!isTerminated) {
            try {
                //uses the awaitMessages method to wait for messages from the message bus
                Message message = messageBusInstance.awaitMessage(this);
                Callback callback = callbackMap.get(message.getClass());
                callback.call(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //unregisters the microservice
        messageBusInstance.unregister(this);
    }

    /**
     * added from FAQ
     * this method is called once the microservice has done.
     */
    protected abstract void close();


}
