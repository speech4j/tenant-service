package org.speech4j.tenantservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class CleanService {
    @Autowired
    private DataSource dataSource;

    public void cleanUp(String tables) throws SQLException {
        try(Connection connection = dataSource.getConnection();
            Statement st = connection.createStatement()
        ) {
            st.executeUpdate("TRUNCATE ONLY " + tables);
        }
    }
}
