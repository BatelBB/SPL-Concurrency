package bgu.spl.mics.application.passiveObjects;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    private static long beginning;
    private static final Diary diary = new Diary();

    int totalAttacks;
    long HanSoloFinish;
    long C3POFinish;
    long R2D2Deactivate;
    long LeiaTerminate;
    long HanSoloTerminate;
    long C3POTerminate;
    long R2D2Terminate;
    long LandoTerminate;

    public Diary(){
        diary.setBeginning();
    }

    public static Diary getInstance() {
        return diary;
    }

    public void setBeginning(){
        beginning = System.currentTimeMillis();
    }

    public void setTotalAttacks(int totalAttacks){
        this.totalAttacks = totalAttacks;
    }

    public void setHanSoloFinishTime(){
        HanSoloFinish = beginning - System.currentTimeMillis();
    }

    public void setC3POFinish(){
        C3POFinish = beginning - System.currentTimeMillis();
    }

    public void setR2D2Deactivate(){
        R2D2Deactivate = beginning - System.currentTimeMillis();
    }

    public  void setLeiaTerminate(){
        LeiaTerminate = beginning - System.currentTimeMillis();
    }

    public  void setLandoTerminate(){
        LandoTerminate = beginning - System.currentTimeMillis();
    }
    public  void setHanSoloFinish(){
        HanSoloTerminate = beginning - System.currentTimeMillis();
    }
    public  void setC3POTerminate(){
        C3POTerminate = beginning - System.currentTimeMillis();
    }
    public  void setR2D2Terminate(){
        R2D2Terminate = beginning - System.currentTimeMillis();
    }

}
