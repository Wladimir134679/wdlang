package com.wdeath.wdlang;

import com.wdeath.wdlang.script.ScriptProgram;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainLauncher {

    public static void main(String[] args) throws IOException {
        System.out.println("==== Start program ====");
        testProgram("./test1.wdl");
        System.out.println("=========");
        testProgram("./test2.wdl");
    }

    private static void testProgram(String path) throws IOException {
        String input = Files.readString(Paths.get(path));
        ScriptProgram scriptProgram = new ScriptProgram();
        scriptProgram
                .init()
                .load(input);
        scriptProgram.execute("init");
        scriptProgram.execute("main");
    }
}
