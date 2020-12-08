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
            System.out.println("Run!");
            run(input);
        }
        Diary recordDiary = Diary.getInstance();
        diaryToJson(outputFilePath,recordDiary);
    }
    private static void run(Input input){
        Ewoks.getInstance(input.getEwoks());
        new LandoMicroservice(input.getLando());
        new R2D2Microservice((input.getR2D2()));
        new LeiaMicroservice(input.getAttacks());
    }
    private static Input getInputFromJson(String filePath) throws IOException {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, Input.class);
        }
    }
    private static void diaryToJson(String filePath, Diary recordDiary) throws IOException {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(recordDiary, writer);
        }
    }
}


