package com.yc.smartdb.commands;

import com.yc.smartdb.constants.AppConstants;
import com.yc.smartdb.service.ApplicationService;
import com.yc.smartdb.service.LocalOllamaService;
import com.yc.smartdb.service.VannaService;
import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine;

import java.util.List;
import java.util.Map;

@CommandLine.Command(
        name = "SmartDB",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "💬 Chat With Databases Powered by Ollama 3.2 (0.9.0)"
)
public class MyCommand implements Runnable {

    private final ApplicationService applicationService;
    private final LocalOllamaService localOllamaService;
    private final VannaService vannaService;

    @Autowired
    public MyCommand(ApplicationService applicationService,
                     LocalOllamaService localOllamaService,
                     VannaService vannaService) {
        this.applicationService = applicationService;
        this.localOllamaService = localOllamaService;
        this.vannaService = vannaService;
    }

    @CommandLine.Option(names = {"-d", "--database"}, description = "⚙️ Configure Database manually")
    private boolean configureDatabase;

    @CommandLine.Option(names = {"-s", "--showdb"}, description = "🔍 Show configured database properties")
    private boolean showDatabaseProps;

    @CommandLine.Option(names = {"-r", "--run"}, description = "▶️ Run a SQL query")
    private boolean runSql;

    @CommandLine.Option(names = {"-q", "--query"}, arity = "0..*", description = "📝 SQL query parts")
    private List<String> queryParts;

    @CommandLine.Option(names = {"-a", "--ai"}, description = "🤖 Prompt for AI-generated SQL", arity = "0..*", split = " ")
    private List<String> aiPromptParts;

    @CommandLine.Option(names = {"-t", "--test"}, description = "🧪 Test Vanna service")
    private boolean test;

    @Override
    public void run() {
        try {
            // Manual DB Configuration
            if (configureDatabase) {
                configureDatabaseWithScanner();
            }

            // Show DB Props
            if (showDatabaseProps) {
                showDatabaseProps();
            }

            // Run raw SQL
            if (runSql) {
                executeSqlQuery();
            }

            // AI-based query generation
            if (aiPromptParts != null && !aiPromptParts.isEmpty()) {
                String prompt = String.join(" ", aiPromptParts);
                generateSqlWithAi(prompt);
            }

            // Vanna testing
            if (test) {
                testVanna();
            }

            // Help fallback
            if (!configureDatabase && !showDatabaseProps && !runSql &&
                    (aiPromptParts == null || aiPromptParts.isEmpty()) && !test) {
                System.out.println("ℹ️ No command provided. Use -h or --help to see available options.");
            }

        } catch (Exception e) {
            System.out.println("❌ Unexpected Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configureDatabaseWithScanner() {
        System.out.println("⚙️ Starting manual DB configuration...");
        applicationService.setDatabaseProps();
        System.out.println("✅ Database configuration complete.");
    }

    private void showDatabaseProps() {
        System.out.println("📋 Current DB Properties:");
        applicationService.getDatabaseProps();
    }

    private void executeSqlQuery() {
        if (queryParts == null || queryParts.isEmpty()) {
            System.out.println("⚠️ No query provided. Use `-q \"SELECT * FROM table\"` along with `-r`.");
            return;
        }

        String query = String.join(" ", queryParts);
        System.out.println("▶️ Executing query: " + query);

        try {
            List<Map<String, Object>> result = applicationService.execQuery(query);
            applicationService.printTable(result);
        } catch (Exception e) {
            System.out.println("❌ SQL Execution Error: " + e.getMessage());
        }
    }

    private void generateSqlWithAi(String prompt) {
        System.out.println("🤖 Sending prompt to AI: " + prompt);
        try {
            localOllamaService.generateSqlWithAi(AppConstants.DB_SCHEMA, prompt);
        } catch (Exception e) {
            System.out.println("❌ Failed to generate SQL with AI: " + e.getMessage());
        }
    }

    private void testVanna() {
        System.out.println("🧪 Calling Vanna...");
        try {
            String response = vannaService.askVanna("Contact of bus department");
            System.out.println("✅ Vanna says: " + response);
        } catch (Exception e) {
            System.out.println("❌ Vanna Test Error: " + e.getMessage());
        }
    }
}
