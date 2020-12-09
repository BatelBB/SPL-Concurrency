package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.services.LandoMicroservice;
import bgu.spl.mics.application.services.LeiaMicroservice;
import bgu.spl.mics.application.services.R2D2Microservice;
import com.google.gson.Gson;

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
        Input input = null;
        input= getInputFromJson(inputFilePath);
        if(input!=null) {
            System.out.println("Long time ago in a galaxy far far away...");
            run(input);
        }
        //we can use pretty printing
        //https://www.cs.bgu.ac.il/~spl211/Assignments/Assignment_2Forum?action=show-thread&id=cf677a1d8e2d25c77eb0feafb0c7e456
        Diary getFromDiary = Diary.getInstance();
        writeToJson(outputFilePath,getFromDiary);
    }
    private static void run(Input input){
        new LeiaMicroservice(input.getAttacks());
        new LandoMicroservice(input.getLando());
        new R2D2Microservice((input.getR2D2()));
        Ewoks.getInstance(input.getEwoks());

    }
    private static Input getInputFromJson(String filePath) throws IOException {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, Input.class);
        }
    }
    private static void writeToJson(String filePath, Diary diary) throws IOException {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(diary, writer);
        }
    }
}


