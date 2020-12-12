package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.passiveObjects.Diary;


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
        subscribeEvent(AttackEvent.class, (AttackEvent type) -> {
            ExecuteAttack();
        });

        Diary.getInstance().totalAttacks.incrementAndGet();

        close();
    }

    @Override
    protected void close() {
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            Diary.getInstance().setC3POTerminate(System.currentTimeMillis());
            System.out.println("C3PO has done");
            this.terminate();
        });
    }

    private void ExecuteAttack() {

    }

}
