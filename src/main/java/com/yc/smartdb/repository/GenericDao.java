package com.yc.smartdb.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GenericDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenericDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Method to execute a query and return a list of maps
    public List<Map<String, Object>> executeQuery(String sql) {
        return jdbcTemplate.query(sql, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> row = new HashMap<>();
                int columnCount = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                return row;
            }
        });
    }
}
