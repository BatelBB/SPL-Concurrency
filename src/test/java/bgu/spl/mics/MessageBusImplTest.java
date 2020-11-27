package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.services.DummyMicroservice;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

//create microservices that use the different methods of the message bus
public class MessageBusImplTest {

    private MessageBusImpl testMessageBusIml;


    @Before
    public void setUp() throws Exception {
        testMessageBusIml = new MessageBusImpl();
    }

    //no need to test - forum
    @Test
    public void testSubscribeEvent() {
    }


    @Test
    public void testSubscribeBroadcast() {

    }

    //no need to test - forum
    @Test
    public void testComplete() {

    }

    //when I use sendEvent/Broadcast the subscribed microservice will definitely have a message in his queue to fetch?
    @Test
    public void testSendBroadcast() {
        TerminateBroadcast terminateBroadcast = new TerminateBroadcast();
        DummyMicroservice microService1 = new DummyMicroservice("MicroserviceTest1");
        DummyMicroservice microService2 = new DummyMicroservice("MicroserviceTest2");

        microService2.subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast call) -> {
            new Callback<TerminateBroadcast>() {
                @Override
                public void call(TerminateBroadcast c) {
                }
            };
        });
        microService1.sendBroadcast(terminateBroadcast);
        try {
            Message message = testMessageBusIml.awaitMessage(microService2);
            assertEquals(message, terminateBroadcast);
        }
        catch (InterruptedException e){
            System.out.println("InterruptedException");
        }
    }

    //when I use sendEvent/Broadcast the subscribed microservice will definitely have a message in his queue to fetch?
    @Test
    public void testSendEvent() {
        AttackEvent attackEvent = new AttackEvent();
        DummyMicroservice microService1 = new DummyMicroservice("MicroserviceTest1");
        DummyMicroservice microService2 = new DummyMicroservice("MicroserviceTest2");

        microService2.subscribeEvent(AttackEvent.class, (AttackEvent call) -> {
            new Callback<AttackEvent>() {
                @Override
                public void call(AttackEvent c) {
                }
            };
        });
        microService1.sendEvent(attackEvent);
        try {
            Message message = testMessageBusIml.awaitMessage(microService2);
            assertEquals(message, attackEvent);
        }
        catch (InterruptedException e){
            System.out.println("InterruptedException");
        }
    }

    //no need to test - forum
    @Test
    public void testRegister() {

    }


    //test with the assumption that the que is NOT empty (getters are not allowed)
    //test only the case where there's a message waiting to be fetched, and make sure it is indeed fetched.
    @Test
    public void testAwaitMessage() {
        Queue<Message> qMessage = new Queue<Message>() {
            @Override
            public boolean add(Message message) {
                return false;
            }

            @Override
            public boolean offer(Message message) {
                return false;
            }

            @Override
            public Message remove() {
                return null;
            }

            @Override
            public Message poll() {
                return null;
            }

            @Override
            public Message element() {
                return null;
            }

            @Override
            public Message peek() {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<Message> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends Message> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }
        };
        if(qMessage.size()!=0){

        }
    }
}