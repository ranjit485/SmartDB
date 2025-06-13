package com.yc.smartdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;

@Service
public class SchemaExtractorService {

    @Autowired
    private DataSource dataSource;

    public String getSchemaAsString() {
        StringBuilder schemaBuilder = new StringBuilder();

        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();

            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                schemaBuilder.append("Table: ").append(tableName).append("\n");

                ResultSet columns = metaData.getColumns(null, null, tableName, "%");
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String columnType = columns.getString("TYPE_NAME");
                    schemaBuilder.append(" - ").append(columnName).append(": ").append(columnType).append("\n");
                }
                schemaBuilder.append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error extracting schema: " + e.getMessage();
        }

        return schemaBuilder.toString();
    }
    public void printSchema() {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("Table: " + tableName);

                ResultSet columns = metaData.getColumns(null, null, tableName, "%");
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String type = columns.getString("TYPE_NAME");
                    System.out.println(" - " + columnName + " (" + type + ")");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
