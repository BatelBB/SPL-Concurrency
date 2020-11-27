package bgu.spl.mics;

/**
 * a callback is a function designed to be called when a message is received.
 */
//can implement Callback<boolean> eg..
    //https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=6ebca58b5b409c139db10aeee579732d
public interface Callback<T> {

    public void call(T c);

}
