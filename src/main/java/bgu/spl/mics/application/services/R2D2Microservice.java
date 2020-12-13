package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 * only informs Lando - no need broadcast
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
//there's only one microservice from each
//https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=2e5ba1f89f40b2fd1c44f85cc7c04527
public class R2D2Microservice extends MicroService {
    private final long duration;

    //default constructor
    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration = duration;
        System.out.println("R2D2: “Bleep Beep Bleep”");

    }

    @Override
    protected void initialize() {
        //subscribes to deactivationEvent
        this.subscribeEvent(DeactivationEvent.class, (DeactivationEvent deactivationEvent) -> {
            //DEACTIVATE
            deactivation();
            //sends bombDestroyerEvent
            BombDestroyerEvent bombDestroyerEvent = new BombDestroyerEvent();
            this.sendEvent(bombDestroyerEvent);
            //completes the deactivation event
            complete(deactivationEvent, true);

        });
        //terminates
        close();
    }

    @Override
    protected void close() {
        //subscribes to the terminationBroadcast
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            //writes to diary
            Diary.getInstance().setR2D2Terminate(System.currentTimeMillis());
            System.out.println("R2D2: “Bleep Bleep Bleep” - DONE");
            //terminates
            this.terminate();
        });
    }

    private synchronized void deactivation() {
        try {
            this.wait(duration);
            Diary.getInstance().setR2D2Deactivate(System.currentTimeMillis());
            System.out.println("R2D2: “Beep Beep” - Shields are deactivated!");
        } catch (InterruptedException e) {
            System.out.println("Exception was thrown: " + e);
        }
    }
}
