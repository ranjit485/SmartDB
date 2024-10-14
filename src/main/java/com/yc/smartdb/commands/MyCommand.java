package com.yc.smartdb.commands;

import com.yc.smartdb.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine;

import java.util.List;


@CommandLine.Command(name = "SmartDB", mixinStandardHelpOptions = true, version = "1.0",
        description = "Chat With Databases")
public class MyCommand implements Runnable {

    private final ApplicationService applicationService;

    @Autowired
    public MyCommand(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @CommandLine.Option(names = {"-d", "--database"}, description = "Configure Database...")
    private boolean configureDatabase;

    @CommandLine.Option(names = {"-s", "--showdb"}, description = "Show database properties...")
    private boolean showDatabaseProps;

    @CommandLine.Option(names = {"-r", "--run"}, description = "Run SQL queries")
    private boolean runSql;

    @CommandLine.Option(names = {"-a", "--ai"}, description = "@|green Generate SQL queries with AI|@")
    private boolean runAiSql;

    @CommandLine.Option(names = {"-q", "--query"}, arity = "0..*", description = "Enter SQL queries as separate words")
    private List<String> queryParts;


    @Override
    public void run() {
        if (configureDatabase) {
            configureDatabaseWithScanner();
            configureDatabase=false;
        }

        if (showDatabaseProps) {
            showDatabaseProps();
            showDatabaseProps=false;
        }

        if (runSql) {
            if (queryParts != null && !queryParts.isEmpty()) {
                executeSqlQuery();
                runSql=false;
            } else {
                System.out.println("Please provide a SQL query with -q/--query option.");
            }
        }

        if (runAiSql) {
            generateSqlWithAi();
            runAiSql=false;
        }
    }

    // Method to configure database using Scanner input
    private void configureDatabaseWithScanner() {
        applicationService.setDatabaseProps();
    }

    private void showDatabaseProps() {
        applicationService.getDatabaseProps();
    }

    private void executeSqlQuery() {
        String query = String.join(" ", queryParts); // Concatenate parts into a single query
        System.out.println("Running SQL query: " + query);

        try {
            applicationService.printTable(applicationService.execQuery(query));
        } catch (Exception e) {
            System.out.println("Error executing SQL query: " + e.getMessage());
        }
    }

    private void generateSqlWithAi() {
        System.out.println("Generating SQL query with AI...");
        // Placeholder for AI logic
        System.out.println("AI SQL query generation is currently not implemented.");
    }

}
