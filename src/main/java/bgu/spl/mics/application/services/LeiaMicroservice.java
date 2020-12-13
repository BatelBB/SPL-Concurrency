package bgu.spl.mics.application.services;

import java.util.*;
import java.util.concurrent.TimeUnit;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
//there's only one microservice from each
//https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=2e5ba1f89f40b2fd1c44f85cc7c04527
public class LeiaMicroservice extends MicroService {
    private final Attack[] attacks;
    private List<Future> futures = new ArrayList<>();

    //default constructor
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
        System.out.println("Leia: “Aren’t you a little short for a stormtrooper?”");
    }

    //initialize method
    @Override
    protected void initialize() {
        //wait for the others to subscribe
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Leia has been interrupted");
        }

        //starts the time of the diary
        Diary.getInstance().setBeginning();

        //sends the attacks
        sandAttack();
        System.out.println("Leia: “Help me, Han Solo. You’re my only hope.”");

        //sends the deactivation event
        deactivate();

        //subscribes to the terminateBroadcast
        close();

    }

    protected void close() {
        //subscribes to terminateBroadcast
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            //sets the terminate time
            Diary.getInstance().setLeiaTerminate(System.currentTimeMillis());
            System.out.println("Leia: “We have everything we need” - DONE");
            //terminates
            this.terminate();
        });
    }

    private void sandAttack() {
        //creates new AttackEvent for every attack in the array
        for (Attack value : attacks) {
            AttackEvent attack = new AttackEvent(value);
            //saves the return Future object that the sendEvent returns
            Future futureAttack = sendEvent(attack);
            //adds the Future object to the arrayList
            if (futureAttack != null)
                futures.add(futureAttack);
        }
    }

    private void deactivate() {
        //waits for the attackEvents to end
        for (Future future : futures) {
            future.get();
        }
        //sends the deactivation event
        DeactivationEvent deactivate = new DeactivationEvent();
        sendEvent(deactivate);
    }
}
