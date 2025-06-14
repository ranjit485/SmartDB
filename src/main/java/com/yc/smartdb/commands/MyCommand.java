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

    @CommandLine.Option(names = {"-d", "--database"}, description = "⚙️ Configure Database")
    private boolean configureDatabase;

    @CommandLine.Option(names = {"-s", "--showdb"}, description = "🔍 Show database properties")
    private boolean showDatabaseProps;

    @CommandLine.Option(names = {"-r", "--run"}, description = "▶️ Run SQL query")
    private boolean runSql;

    @CommandLine.Option(names = {"-q", "--query"}, arity = "0..*", description = "📝 SQL query parts")
    private List<String> queryParts;

    @CommandLine.Option(names = {"-a", "--ai"}, description = "🤖 AI prompt for SQL generation", arity = "0..*", split = " ")
    private List<String> aiPromptParts;

    @CommandLine.Option(names = {"-t", "--test"}, description = "🧪 Test Vanna integration")
    private boolean test;

    @Override
    public void run() {
        try {
            if (configureDatabase) {
                configureDatabaseWithScanner();
            }

            if (showDatabaseProps) {
                showDatabaseProps();
            }

            if (runSql) {
                executeSqlQuery();
            }

            if (aiPromptParts != null && !aiPromptParts.isEmpty()) {
                String prompt = String.join(" ", aiPromptParts);
                generateSqlWithAi(prompt);
            }

            if (test) {
                testVanna();
            }

            if (!configureDatabase && !showDatabaseProps && !runSql &&
                    (aiPromptParts == null || aiPromptParts.isEmpty()) && !test) {
                System.out.println("ℹ️ No command provided. Use -h for help.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configureDatabaseWithScanner() {
        System.out.println("⚙️ Configuring database...");
        applicationService.setDatabaseProps();
    }

    private void showDatabaseProps() {
        System.out.println("📋 Showing database properties:");
        applicationService.getDatabaseProps();
    }

    private void executeSqlQuery() {
        if (queryParts == null || queryParts.isEmpty()) {
            System.out.println("⚠️ Please provide a SQL query using -q option.");
            return;
        }

        String query = String.join(" ", queryParts);
        System.out.println("▶️ Executing SQL query: " + query);

        try {
            List<Map<String, Object>> result = applicationService.execQuery(query);
            applicationService.printTable(result);
        } catch (Exception e) {
            System.out.println("❌ SQL Execution Error: " + e.getMessage());
        }
    }

    private void generateSqlWithAi(String prompt) {
        System.out.println("🤖 Generating SQL with AI for prompt: " + prompt);
        try {
            localOllamaService.generateSqlWithAi(AppConstants.DB_SCHEMA, prompt);
        } catch (Exception e) {
            System.out.println("❌ AI SQL Generation Failed: " + e.getMessage());
        }
    }

    private void testVanna() {
        System.out.println("🧪 Testing Vanna...");
        try {
            String response = vannaService.askVanna("Contact of bus department");
            System.out.println("✅ Vanna Response: " + response);
        } catch (Exception e) {
            System.out.println("❌ Vanna Test Failed: " + e.getMessage());
        }
    }
}
