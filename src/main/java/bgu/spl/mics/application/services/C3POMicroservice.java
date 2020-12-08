package bgu.spl.mics.application.services;

import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.MessageBusImpl;

import static org.graalvm.compiler.nodes.java.RegisterFinalizerNode.register;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 */

//CANNOT change the constructor signature
//https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=b202aed11416b0f3ddf59342c60749f4
//there's only one microservice from each
//https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=2e5ba1f89f40b2fd1c44f85cc7c04527
public class C3POMicroservice extends MicroService {
    private AttackEvent attackEvent;

    public C3POMicroservice(AttackEvent attackEvent) {
        super("C3PO");
        this.attackEvent = attackEvent;

        initialize();
    }

    @Override
    protected void initialize() {

    }


}
