package com.yc.smartdb;

import com.yc.smartdb.commands.MyCommand;
import com.yc.smartdb.config.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import picocli.CommandLine;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        MyCommand command = context.getBean(MyCommand.class);

        int exit = new CommandLine(command).execute(args);

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.print("> ");
            String input = scanner.nextLine().trim();

            // Exit condition
            if ("exit".equalsIgnoreCase(input)) {
                System.out.println("Exiting calculator. Goodbye!");
                break;
            }

            // Split input into args for command parsing
            String[] inputArgs = input.split(" ");

            if (inputArgs.length > 0) {
                // Run PicoCLI command with provided input
                new CommandLine(command).execute(inputArgs);
            } else {
                System.out.println("Please provide a valid command.");
            }
        }

        scanner.close();
    }

}
