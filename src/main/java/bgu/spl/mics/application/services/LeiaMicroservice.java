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
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
//there's only one microservice from each
//https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=2e5ba1f89f40b2fd1c44f85cc7c04527
public class LeiaMicroservice extends MicroService {
	private final Attack[] attacks;
	private List<Future> futures = new ArrayList<>();

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
        System.out.println("Leia is here!");

    }

    @Override
    protected void initialize()  {
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            System.out.println("Leia has been interrupted");
        }
        Diary.getInstance().setBeginning();
        sandAttack();
        System.out.println("Leia: I sent the attacks");

        deactivate();

    	close();

    }

    protected void close(){
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            Diary.getInstance().setLeiaTerminate(System.currentTimeMillis());
            System.out.println("Leia has done");
            this.terminate();
        });
    }

    private void sandAttack(){

        for(int i=0; i < attacks.length; i++){
            AttackEvent attack = new AttackEvent(attacks[i]);
            System.out.println("Attack " + i);
            //needs to wait for the microservices to subscribe
            //https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=b31765abcb31823c07a7ccadbffe9a7f
            //https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=20be77a3fd206cd30bd83cbd9354aa39

            Future futureAttack = sendEvent(attack);
            if(futureAttack != null)
            futures.add(futureAttack);
            //futures.add(this.sendEvent(attack));
            //.get(1000, TimeUnit.MILLISECONDS);
            //sendBroadcast();
        }


    }

    private void deactivate(){
        assert futures != null;
            for (Future future : futures) {
                future.get();
           }

        DeactivationEvent deactivate = new DeactivationEvent();
        sendEvent(deactivate);
        System.out.println("Leia: Sent the deactivation event!");
    }
}
