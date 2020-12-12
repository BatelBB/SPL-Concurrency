package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.passiveObjects.Attack;

//https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=7c4c729c97b0d1b0994d76e552587914
//terminate together
public class TerminateBroadcast implements Broadcast {
    public boolean terminateBroadcast;

    public TerminateBroadcast(){
        terminateBroadcast = true;
        System.out.println("The terminate broadcast was created");
    }

    public boolean getTerminateBroadcast() {
        return terminateBroadcast;
    }
}

