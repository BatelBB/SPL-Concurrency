package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.mics.MicroService;
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

	
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
        System.out.println("Leia is here!");
		initialize();
    }

    @Override
    protected void initialize() {
        //new Diary();

    	for(int i=0; i < attacks.length; i++){
            AttackEvent attack = new AttackEvent(attacks[i]);
            //needs to wait for the microservices to subscribe
            //https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=b31765abcb31823c07a7ccadbffe9a7f
            //https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=20be77a3fd206cd30bd83cbd9354aa39
    	     this.sendEvent(attack);
        }
        System.out.println("Leia: I sent the attacks");
    }

}
