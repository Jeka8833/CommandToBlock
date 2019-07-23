package com.Jeka8833.CommandToBlock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        CommandToBlock cm = new CommandToBlock();
        Path path = Paths.get("D:\\1.block");
        try {
            byte[] data = Files.readAllBytes(path);
            for(int y = 0; y <100; y++){
                for(int x = 0; x <100; x++){
                    cm.addSingleCommand("setblock ~" + x + " ~"+ y +" ~ ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String command : cm.getCommand()) {
            System.out.println(command);
        }
    }
}
