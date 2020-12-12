package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.passiveObjects.Diary;

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
        subscribeEvent(AttackEvent.class, (AttackEvent Attack) -> {
            List<Integer> ewoks = SortEwoks(Attack.attack.getSerials());
            int duration = Attack.GetDuration();

            //TODO: release resors

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
    //MergeSort implement
    private List<Integer> SortEwoks(List<Integer> ewoks){
        System.out.println("unsorted attacking ewoks" +ewoks);
        MergeSort(ewoks, 0 , ewoks.size()-1);
        System.out.println("sorted attacking ewoks" +ewoks);
        return ewoks;
    }
    private void MergeSort(List<Integer> ewoks, int Start,int End){
        if(End>Start){
            int mid = (End+Start)/2;
            MergeSort(ewoks, Start, mid);
            MergeSort(ewoks, mid, End);
            merge(ewoks,Start,mid,End);
        }
    }
    private void merge(List<Integer> ewoks, int Start, int mid, int End)
    {
        // Find sizes of two Lists to be merged
        int LeftSize = mid - Start + 1;
        int RightSize = End - mid;

        /* Create temp arrays */
        int Left[] = new int[LeftSize];
        int Right[] = new int[RightSize];

        /*Copy data to temp arrays*/
        for (int i = 0; i < LeftSize; ++i)
            Left[i] = ewoks.get(Start + i);
        for (int j = 0; j < RightSize; ++j)
            Right[j] = ewoks.get(mid + 1 + j);

        /* Merge the temp arrays */

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarry to list
        int k = Start;
        while (i < LeftSize && j < RightSize) {
            if (Left[i] <= Right[j]) {
                ewoks.set(k, Left[i]);
                i++;
            }
            else {
                ewoks.set(k, Right[j]);
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < LeftSize) {
            ewoks.set(k, Left[i]);
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < RightSize) {
            ewoks.set(k, Right[j]);
            j++;
            k++;
        }
    }
}
