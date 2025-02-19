package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.io.FileReader;
import java.io.Reader;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String inputFilePath = args[0]; //input file path from arguments
        String outputFilePath = args[1];//output file path from arguments
        Input input = getInputFromJson(inputFilePath);
        Ewoks ewoks = Ewoks.getInstance();
        if(input!=null) {
            System.out.println("A Long time ago in a galaxy far far away...");
            System.out.println("MAY THE FORCE BE WITH YOU");
            ewoks.setNumEwoks(input.getEwoks());
            initiate(input);

        }
        //we can use pretty printing
        //https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=cf677a1d8e2d25c77eb0feafb0c7e456
        Diary getFromDiary = Diary.getInstance();
        writeToJson(outputFilePath,getFromDiary);
    }
    private static void initiate(Input input){
        Thread threadLeia = new Thread(new LeiaMicroservice(input.getAttacks()));
        Thread threadHan = new Thread(new HanSoloMicroservice());
        Thread threadC3PO = new Thread(new C3POMicroservice());
        Thread threadR2D2 = new Thread(new R2D2Microservice((input.getR2D2())));
        Thread threadLando = new Thread(new LandoMicroservice(input.getLando()));


        threadLeia.start();
        threadHan.start();
        threadC3PO.start();
        threadR2D2.start();
        threadLando.start();


        try {
            threadLeia.join();
            threadHan.join();
            threadC3PO.join();
            threadR2D2.join();
            threadLando.join();


        }catch (InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("“I’m one with the Force. The Force is with me.” ");

    }
    private static Input getInputFromJson(String filePath) throws IOException {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, Input.class);
        }
    }
    private static void writeToJson(String filePath, Diary diary) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(diary, writer);
        }
    }
}


