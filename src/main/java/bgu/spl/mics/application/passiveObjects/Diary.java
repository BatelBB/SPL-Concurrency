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
    //singleton holder
    private static class SingletonDiaryHolder {
        private static final Diary diary = new Diary();
    }
    public AtomicInteger totalAttacks = new AtomicInteger();
    long HanSoloFinish;
    long C3POFinish;
    long R2D2Deactivate;
    long LeiaTerminate;
    long HanSoloTerminate;
    long C3POTerminate;
    long R2D2Terminate;
    long LandoTerminate;

    private Diary(){
        setBeginning();
    }

    public static Diary getInstance() {
        return SingletonDiaryHolder.diary;
    }

    public void setBeginning(){
        beginning = System.currentTimeMillis();
    }

    public void setHanSoloFinishTime(long hanSoloFinishTime){
        HanSoloFinish =  hanSoloFinishTime - beginning;
    }

    public void setC3POFinish(long C3POFinishTime){
        C3POFinish = C3POFinishTime- beginning;
    }

    public void setR2D2Deactivate(long r2D2Deactivate){
        R2D2Deactivate = r2D2Deactivate- beginning;
    }

    public  void setLeiaTerminate(long leiaTerminate){
        LeiaTerminate =  leiaTerminate- beginning;
    }

    public  void setLandoTerminate(long landoTerminate){
        LandoTerminate = landoTerminate- beginning;
    }
    public  void setHanSoloTerminate(long hanSoloTerminate){
        HanSoloTerminate =  hanSoloTerminate- beginning;
    }
    public  void setC3POTerminate(long c3POTerminate){
        C3POTerminate =  c3POTerminate- beginning;
    }
    public  void setR2D2Terminate(long r2D2Terminate){
        R2D2Terminate =  r2D2Terminate- beginning;
    }

    public long getHanSoloFinish() {
        return HanSoloFinish;
    }

    public long getC3POFinish() {
        return C3POFinish;
    }

    public long getR2D2Deactivate() {
        return R2D2Deactivate;
    }

    public long getLeiaTerminate() {
        return LeiaTerminate;
    }

    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }

    public long getC3POTerminate() {
        return C3POTerminate;
    }

    public long getR2D2Terminate() {
        return R2D2Terminate;
    }

    public long getLandoTerminate() {
        return LandoTerminate;
    }


}
