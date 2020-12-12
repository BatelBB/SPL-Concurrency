package bgu.spl.mics.application.services;


import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Event;
import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 */
//CANNOT change the constructor signature
//https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=b202aed11416b0f3ddf59342c60749f4
//there's only one microservice from each
//https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=2e5ba1f89f40b2fd1c44f85cc7c04527
public class HanSoloMicroservice extends MicroService {

    public HanSoloMicroservice() {
        super("Han");

        ConcurrentMap<AttackEvent, LinkedList<Message>> messageMap = new ConcurrentHashMap<>();
        initialize();
    }


    @Override
    protected void initialize() {
        //subscribe to events of type attackevent
        this.subscribeEvent(AttackEvent.class, (AttackEvent Attack) -> {

            //list of serial for the ewoks
            List<Integer> ewoks = Attack.attack.getSerials();
            //sort the list
            Collections.sort(ewoks);
            long duration = Attack.GetDuration();

            //ask for the ewoks
            for (Integer ewok : ewoks) {
                Ewoks.getInstance().resourceManager(ewok);
            }
            //atacks
            ExecuteAttack(duration);

            //release resources
            for (Integer ewok : ewoks) {
                Ewoks.getInstance().releaseResources(ewok);
            }

            //record in the diary the end of the attack
            Diary.getInstance().setHanSoloFinishTime(System.currentTimeMillis());
            complete(Attack, true);
        });

        close();
    }

    @Override
    protected void close() {

        //subscribe to Broadcasts of type TerminateBroadcast
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {

            //record in the diary the termination time of hansolo
            Diary.getInstance().setHanSoloTerminate(System.currentTimeMillis());
            System.out.println("Han Solo has done");
            this.terminate();
        });
    }

    //Attacking
    private void ExecuteAttack(Long duration) {
        try {
            //record in the diary the attack
            Diary.getInstance().totalAttacks.incrementAndGet();
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            System.out.println("Interrupted" + e);
        }

    }

}
