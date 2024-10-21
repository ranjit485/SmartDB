package com.yc.smartdb.service;

import com.yc.smartdb.App;
import com.yc.smartdb.repository.GenericDao;
import de.vandermeer.asciitable.AsciiTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.yc.smartdb.rainbow.ConsoleColor.YELLOW;

@Service
public class ApplicationService {

    private final GenericDao genericDao;

    @Autowired
    public ApplicationService(GenericDao genericDao) {
        this.genericDao = genericDao;
    }


    public List<Map<String, Object>> execQuery(String query) {
        return genericDao.executeQuery(query);
    }

//    This method prints table in console
    public void printTable(List<Map<String, Object>> list){
        AsciiTable table = new AsciiTable();

        if (!list.isEmpty()) {
            Map<String, Object> headerRow = list.get(0);
            table.addRule();
            table.addRow(headerRow.keySet().toArray());
        }

        for (Map<String, Object> row : list) {
            table.addRule();
            table.addRow(row.values().toArray());
        }
        table.addRule();

        String renderedTable = table.render();
        System.out.println(renderedTable);
    }

    public void setDatabaseProps() {
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object

        // Prompt the user for database details
        System.out.print("Enter Database URL: ");
        String dbUrl = scanner.nextLine();

        System.out.print("Enter Database Username: ");
        String dbUser = scanner.nextLine();

        System.out.print("Enter Database Password: ");
        String dbPassword = scanner.nextLine();

        // Validate that none of the inputs are empty
        if (dbUrl.isEmpty() || dbUser.isEmpty() || dbPassword.isEmpty()) {
            System.out.println("Error: All fields must be filled.");
        }

        ApplicationContext context = App.getContext();
        DriverManagerDataSource driverManagerDataSource = context.getBean(DriverManagerDataSource.class);
        driverManagerDataSource.setPassword(dbPassword);
        driverManagerDataSource.setUsername(dbUser);
        driverManagerDataSource.setUrl(dbUrl);

        System.out.println("Database Configured successfully");

    }

    public void getDatabaseProps(){
        ApplicationContext context = App.getContext();
        DriverManagerDataSource driverManagerDataSource = context.getBean(DriverManagerDataSource.class);

        System.out.println("Database Properties"+YELLOW);
        System.out.println("DB URL : "+driverManagerDataSource.getUrl());
        System.out.println("DB USERNAME : "+driverManagerDataSource.getUsername());
        System.out.println("DB PASSWORD : "+driverManagerDataSource.getPassword());
    }
}

//    jdbc:mysql://localhost:3306/student