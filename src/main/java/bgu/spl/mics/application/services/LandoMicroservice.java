package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
//there's only one microservice from each
//https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=2e5ba1f89f40b2fd1c44f85cc7c04527
public class LandoMicroservice extends MicroService {
    private final long duration;

    //default constructor
    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
        System.out.println("Lando: “Hello — what have we here?”");

    }

    @Override
    protected void initialize() {
        //subscribes to BombDestroyerEvent
        this.subscribeEvent(BombDestroyerEvent.class, (BombDestroyerEvent bombDestroyerEvent) -> {
            //Attacks
            LandoAttacks();
            //sends the termination broadcast
            this.sendBroadcast(new TerminateBroadcast());

            //completes the bombDestroyerEvent
            complete(bombDestroyerEvent,true);
        });
        //terminates
        close();
    }

    @Override
    protected void close() {
        //subscribes to terminationBroadcast
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            //writes to diary
            Diary.getInstance().setLandoTerminate(System.currentTimeMillis());

            System.out.println("Lando: “This deal is getting worse all the time.” - DONE");
            this.terminate();
        });
    }

    private synchronized void LandoAttacks() {
        try {
            //attacks
            this.wait(duration);
            System.out.println("Lando: “You might want to buckle up, baby.”");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
