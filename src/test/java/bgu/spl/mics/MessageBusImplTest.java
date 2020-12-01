package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.services.DummyMicroservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
    private MessageBusImpl testMessageBusIml;

    @BeforeEach
    void testSetUp() {
        testMessageBusIml = new MessageBusImpl();
    }

    //Tested it in SendEvent
    @Test
    void testSubscribeEvent() {
    }

    //Tested it in SendEvent
    @Test
    void testSubscribeBroadcast() {
    }

    @Test
    void testComplete() {
        AttackEvent attackEvent = new AttackEvent();
        DummyMicroservice dummyMicroservice = new DummyMicroservice();
        Future<Boolean> dummyFuture = dummyMicroservice.sendEvent(attackEvent);
        if (dummyFuture == null) {
            fail("sendEvent has returned null");
        }
        testMessageBusIml.complete(attackEvent, true);
        assertTrue(dummyFuture.isDone());
        assertEquals(true, dummyFuture.get());
    }

    //when I use sendEvent/Broadcast the subscribed microservice will definitely have a message in his queue to fetch?
    @Test
    void testSendBroadcast() {
        TerminateBroadcast terminateBroadcast = new TerminateBroadcast();
        DummyMicroservice microService1 = new DummyMicroservice();
        DummyMicroservice microService2 = new DummyMicroservice();

        microService2.subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast call) -> {
            new Callback<TerminateBroadcast>() {
                @Override
                public void call(TerminateBroadcast c) {
                    //empty callback
                }
            };
        });
        microService1.sendBroadcast(terminateBroadcast);
        try {
            Message message = testMessageBusIml.awaitMessage(microService2);
            assertEquals(message, terminateBroadcast);
        } catch (InterruptedException exception) {
            fail("The method awaitMessage should throw the IllegalStateException in the case\n" +
                    "* where microservice was never registered.");
        }
    }

    //when I use sendEvent/Broadcast the subscribed microservice will definitely have a message in his queue to fetch?
    @Test
    void testSendEvent() {
        AttackEvent attackEvent = new AttackEvent();
        DummyMicroservice microService1 = new DummyMicroservice();
        DummyMicroservice microService2 = new DummyMicroservice();

        microService2.subscribeEvent(AttackEvent.class, (AttackEvent call) -> {
            new Callback<AttackEvent>() {
                @Override
                public void call(AttackEvent attackEvent1) {
                    //empty callback
                }
            };
        });
        microService1.sendEvent(attackEvent);
        try {
            Message message = testMessageBusIml.awaitMessage(microService2);
            assertTrue(message instanceof AttackEvent);
            assertEquals(message, attackEvent);
        } catch (InterruptedException exception) {
            fail("The method awaitMessage should throw the IllegalStateException in the case\n" +
                    "* where microservice was never registered.");
        }
    }

    @Test
    void testRegister() {
        DummyMicroservice microservice = new DummyMicroservice();
        try {
            testMessageBusIml.awaitMessage(microservice);
        } catch (InterruptedException exception) {
            fail("The method awaitMessage should throw the IllegalStateException in the case\n" +
                    "* where microservice was never registered.");
        }
    }

    //test with the assumption that the que is NOT empty (getters are not allowed)
    //test only the case where there's a message waiting to be fetched, and make sure it is indeed fetched.
    @Test
    void testAwaitMessage() {
        DummyMicroservice microService1 = new DummyMicroservice();
        DummyMicroservice microService2 = new DummyMicroservice();
        AttackEvent attackEvent = new AttackEvent();
        try {
            testMessageBusIml.awaitMessage(microService1);
            fail("The method awaitMessage should throw the IllegalStateException in the case\n" +
                    "* where microservice was never registered.");
        } catch (Exception exception) {
            assertTrue(exception instanceof IllegalStateException);
        }
        ;
        testMessageBusIml.register(microService1);
        testMessageBusIml.register(microService2);
        microService2.subscribeBroadcast(Broadcast.class, (Broadcast call) -> {
            new Callback<Broadcast>() {
                @Override
                public void call(Broadcast broadcast) {
                    //empty callback
                }
            };
        });
        microService1.sendBroadcast(new Broadcast() {
        });
        try {
            Message message = testMessageBusIml.awaitMessage(microService2);
            if (message == null) {
                fail("Message is null");
            } else {
                assertTrue(message instanceof Broadcast);
                assertEquals(message, new Broadcast() {
                });
            }
        } catch (InterruptedException exception) {
            fail("The method awaitMessage should throw the IllegalStateException in the case\n" +
                    "* where microservice was never registered.");
        }
    }
}
