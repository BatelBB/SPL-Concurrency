package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
    private MessageBus testMessageBusIml;

    @BeforeEach
    void testSetUp() {
        testMessageBusIml = MessageBusImpl.getInstance();
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
        HanSoloMicroservice dummyMicroservice = new HanSoloMicroservice();
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
        HanSoloMicroservice microService1 = new HanSoloMicroservice();
        LandoMicroservice microService2 = new LandoMicroservice(1000);
        testMessageBusIml.register(microService2);
        testMessageBusIml.register(microService1);

        microService2.subscribeBroadcast(TerminateBroadcast.class, call -> {
            microService2.terminate();
        });
        microService1.sendBroadcast(terminateBroadcast);
        try {
            Message message = testMessageBusIml.awaitMessage(microService2);
            assertEquals(message, terminateBroadcast);
        } catch (InterruptedException | IllegalStateException exception) {
            fail("The method awaitMessage should throw the IllegalStateException in the case\n" +
                    "* where microservice was never registered.");
        }
    }

    //when I use sendEvent/Broadcast the subscribed microservice will definitely have a message in his queue to fetch?
    @Test
    void testSendEvent() {
        AttackEvent attackEvent = new AttackEvent();
        HanSoloMicroservice microService1 = new HanSoloMicroservice();
        C3POMicroservice microService2 = new C3POMicroservice();
        testMessageBusIml.register(microService2);
        microService2.subscribeEvent(AttackEvent.class, (AttackEvent call) -> {


        });
        microService1.sendEvent(attackEvent);
        try {
            Message message = testMessageBusIml.awaitMessage(microService2);
            assertTrue(message instanceof AttackEvent);
            assertEquals(message, attackEvent);
        } catch (InterruptedException | IllegalStateException exception) {
            fail("The method awaitMessage should throw the IllegalStateException in the case\n" +
                    "* where microservice was never registered.");
        }
    }

    @Test
    void testRegister() {
        HanSoloMicroservice microservice = new HanSoloMicroservice();
        try {
            testMessageBusIml.awaitMessage(microservice);
        } catch (IllegalStateException | InterruptedException exception) {
            fail("The method awaitMessage should throw the IllegalStateException in the case\n" +
                    "* where microservice was never registered.");
        }
    }

    //test with the assumption that the que is NOT empty (getters are not allowed)
    //test only the case where there's a message waiting to be fetched, and make sure it is indeed fetched.
    @Test
    void testAwaitMessage() {
        HanSoloMicroservice microService1 = new HanSoloMicroservice();
        C3POMicroservice microService2 = new C3POMicroservice();
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

        microService2.subscribeBroadcast(TerminateBroadcast.class, call-> {
            microService2.terminate();

        });
        TerminateBroadcast terminateBroadcast = new TerminateBroadcast();

        try {
            microService1.sendBroadcast(terminateBroadcast);
            Message message = testMessageBusIml.awaitMessage(microService2);
            if (message == null) {
                fail("Message is null");
            } else {
                assertTrue(message instanceof Broadcast);
                assertEquals(message, terminateBroadcast);
            }
        } catch (InterruptedException | IllegalStateException exception) {
            fail("The method awaitMessage should throw the IllegalStateException in the case\n" +
                    "* where microservice was never registered.");
        }
    }
}
