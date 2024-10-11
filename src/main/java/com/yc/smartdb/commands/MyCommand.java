package com.yc.smartdb.commands;

import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "SmartDB", mixinStandardHelpOptions = true, version = "1.0",
         description = "Chat With Databases")
public class MyCommand implements Runnable {

    @Option(names = {"-n", "--name"}, description = "Your name", required = true)
    private String name;

    @Override
    public void run() {
        System.out.println("Hello, " + name + "!");
    }
}
