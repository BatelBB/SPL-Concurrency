package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.Collections;
import java.util.List;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 */

//CANNOT change the constructor signature
//https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=b202aed11416b0f3ddf59342c60749f4
//there's only one microservice from each
//https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=2e5ba1f89f40b2fd1c44f85cc7c04527
public class C3POMicroservice extends MicroService {

    public C3POMicroservice() {
        super("C3PO");
        initialize();
    }

    @Override
    protected void initialize() {
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
            Diary.getInstance().setC3POFinish(System.currentTimeMillis());
            complete(Attack, true);
        });


        close();
    }

    @Override
    protected void close() {
        //subscribe to Broadcasts of type TerminateBroadcast
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            //record in the diary the termination time of hansolo
            Diary.getInstance().setC3POTerminate(System.currentTimeMillis());
            System.out.println("C3PO has done");
            this.terminate();
        });
    }

    //Attacking
    private void ExecuteAttack(Long duration){
        try {
            //record in the diary the attack
            Diary.getInstance().totalAttacks.incrementAndGet();
            Thread.sleep(duration);
        }catch (InterruptedException e){
            System.out.println("Interrupted" +e);
        }

    }

}
