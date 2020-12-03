package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.services.LeiaMicroservice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();
        Reader reader = new FileReader(args[1]);
        Input input = gson.fromJson(reader, Input.class);
        //LeiaMicroservice Leia = new LeiaMicroservice(input.getAttacks());
    }
}

