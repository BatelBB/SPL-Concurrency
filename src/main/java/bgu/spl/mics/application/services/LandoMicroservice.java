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

    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
        System.out.println("Lando is here");
    }

    @Override
    protected void initialize() {
        subscribeEvent(BombDestroyerEvent.class, (BombDestroyerEvent type) -> {
            LandoAttacks();
        });
        close();
    }

    @Override
    protected void close() {
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            Diary.getInstance().setLandoTerminate(System.currentTimeMillis());
            System.out.println("Lando has done");
            this.terminate();
        });
    }

    private void LandoAttacks() {
        try {
            this.wait(duration);
        } catch (InterruptedException e) {
            System.out.println("Exception was thrown: " + e);
        }
    }
}
