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
    void setUp() {
        testMessageBusIml = new MessageBusImpl();
    }

    //Tested it in SendEvent
    @Test
    void subscribeEvent() {
    }
    //Tested it in SendEvent
    @Test
    void subscribeBroadcast() {
    }

    @Test
    void complete() {
        AttackEvent attackEvent = new AttackEvent();
        DummyMicroservice dummyMicroservice = new DummyMicroservice();
        Future<Boolean> dummyFuture = dummyMicroservice.sendEvent(attackEvent);
        if(dummyFuture==null){
            fail("Microservice is empty");
        }
        testMessageBusIml.complete(attackEvent,true);
        assertTrue(dummyFuture.isDone());
        assertEquals(true, dummyFuture.get());
    }
    //when I use sendEvent/Broadcast the subscribed microservice will definitely have a message in his queue to fetch?
    @Test
    void sendBroadcast() {
        TerminateBroadcast terminateBroadcast = new TerminateBroadcast();
        DummyMicroservice microService1 = new DummyMicroservice();
        DummyMicroservice microService2 = new DummyMicroservice();

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
    void sendEvent() {
        AttackEvent attackEvent = new AttackEvent();
        DummyMicroservice microService1 = new DummyMicroservice();
        DummyMicroservice microService2 = new DummyMicroservice();

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

    @Test
    void register() {
        DummyMicroservice microservice = new DummyMicroservice();
        try {
            testMessageBusIml.awaitMessage(microservice);
        }catch (IllegalStateException | InterruptedException e){
            fail("Test Failed");
        }
    }
    //test with the assumption that the que is NOT empty (getters are not allowed)
    //test only the case where there's a message waiting to be fetched, and make sure it is indeed fetched.
    @Test
    void awaitMessage() {
        DummyMicroservice microService1 = new DummyMicroservice();
        DummyMicroservice microService2 = new DummyMicroservice();
        testMessageBusIml.register(microService1);
        testMessageBusIml.register(microService2);
        microService2.subscribeBroadcast(Broadcast.class, (Broadcast call) -> {
            new Callback<Broadcast>() {
                @Override
                public void call(Broadcast c) {
                }
            };
        });
        microService1.sendBroadcast(new Broadcast() {});
        try{
            Message message = testMessageBusIml.awaitMessage(microService2);
            assertEquals(message, new Broadcast() {});
        }catch (IllegalStateException | InterruptedException e){
            fail("Test Failed");
        }
    }
    }
