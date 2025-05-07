package lii.hospitalmanagementsystem.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/hospital_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Postgres@1";

    /**
     * This method establishes a connection to the PostgreSQL database using JDBC.
     * @return Connection object representing the connection to the database.
     */
    public static Connection getConnection(){
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }catch (Exception e){
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
        return connection;
    }


}
