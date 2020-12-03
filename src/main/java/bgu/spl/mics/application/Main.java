package bgu.spl.mics.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jdk.internal.util.xml.impl.Input;

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

    }

    public static Input getInputFromJson(String filePath) throws IOException {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, Input.class);
        }
    }
}

