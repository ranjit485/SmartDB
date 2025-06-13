package com.yc.smartdb.commands;

import com.yc.smartdb.constants.AppConstants;
import com.yc.smartdb.service.ApplicationService;
import com.yc.smartdb.service.LocalOllamaService;
import com.yc.smartdb.service.VannaService;
import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "SmartDB", mixinStandardHelpOptions = true, version = "1.0",
        description = "Chat With Databases Powered by Ollama 3.2(0.9.0)")
public class MyCommand implements Runnable {

    private final ApplicationService applicationService;
    private final LocalOllamaService localOllamaService;
    private final VannaService vannaService;

    @Autowired
    public MyCommand(ApplicationService applicationService, LocalOllamaService localOllamaService, VannaService vannaService) {
        this.applicationService = applicationService;
        this.localOllamaService = localOllamaService;
        this.vannaService = vannaService;
    }

    @CommandLine.Option(names = {"-d", "--database"}, description = "Configure Database...")
    private boolean configureDatabase;

    @CommandLine.Option(names = {"-s", "--showdb"}, description = "Show database properties...")
    private boolean showDatabaseProps;

    @CommandLine.Option(names = {"-r", "--run"}, description = "Run SQL queries")
    private boolean runSql;

    @CommandLine.Option(names = {"-q", "--query"}, arity = "0..*", description = "Enter SQL queries as separate words")
    private List<String> queryParts;

    @CommandLine.Option(names = {"-a", "--ai"}, description = "Prompt for AI to generate SQL", arity = "0..1")
    private String aiPrompt;

    @CommandLine.Option(names = {"-t", "--test"}, description = "Test function")
    private boolean test;

    @Override
    public void run() {
        if (configureDatabase) {
            configureDatabaseWithScanner();
            configureDatabase = false;
        }

        if (showDatabaseProps) {
            showDatabaseProps();
            showDatabaseProps = false;
        }

        if (runSql) {
            if (queryParts != null && !queryParts.isEmpty()) {
                executeSqlQuery();
            } else {
                System.out.println("Please provide a SQL query with -q/--query option.");
            }
            runSql = false;
        }

        if (aiPrompt != null) {
            generateSqlWithAi(aiPrompt);
            aiPrompt = null; // reset after execution
        }

        if (test) {
            test();
            test = false;
        }
    }

    private void configureDatabaseWithScanner() {
        applicationService.setDatabaseProps();
    }

    private void showDatabaseProps() {
        applicationService.getDatabaseProps();
    }

    private void executeSqlQuery() {
        String query = String.join(" ", queryParts);
        System.out.println("Running SQL query: " + query);

        try {
            applicationService.printTable(applicationService.execQuery(query));
        } catch (Exception e) {
            System.out.println("Error executing SQL query: " + e.getMessage());
        }
    }

    private void generateSqlWithAi(String prompt) {
        System.out.println("\uD83D\uDD0D Generating SQL with AI for prompt: " + prompt);
        try {
            localOllamaService.generateSqlWithAi(AppConstants.DB_SCHEMA, prompt);
        } catch (Exception e) {
            System.out.println("\u274C Failed to generate/run SQL: " + e.getMessage());
        }
    }

    public void test() {
        String v = vannaService.askVanna("Contact of bus department");
        System.out.println(v);
    }
}
