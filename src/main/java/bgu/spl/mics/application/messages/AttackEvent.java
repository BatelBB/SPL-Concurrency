package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;
//we are allowed to change the Boolean to Attack
//https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=4c7f8c10563b38d9365ee44b3952040b
public class AttackEvent implements Event<Boolean> {
	public Attack attack;

	public AttackEvent(){

    }
	public AttackEvent(Attack attack){
	    this.attack = attack;
    }

    public int GetDuration(){
		return attack.getDuration();
	}
}
