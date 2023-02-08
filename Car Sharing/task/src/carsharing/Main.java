package carsharing;

import java.sql.*;

import static carsharing.Menu.*;

public class Main {
    static String DB_URL;
    static String JDBC_DRIVER = "org.h2.Driver";
    static String dbName;

    public static void main(String[] args) {
        dbName = args[1];
        DB_URL = "jdbc:h2:./src/carsharing/db/" + dbName;
        Connection conn;
        Statement stmt;
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);

            //STEP 3: Execute a query
            System.out.println("Creating table in given database...");
            stmt = conn.createStatement();

            String sql_preparation = "DROP TABLE IF EXISTS CAR, COMPANY, CUSTOMER ";

            String sql_create_company = "CREATE TABLE IF NOT EXISTS COMPANY" +
                    "(id INT AUTO_INCREMENT not NULL PRIMARY KEY , " +
                    " name VARCHAR(255) UNIQUE NOT NULL)";

            String sql_create_car = "CREATE TABLE IF NOT EXISTS CAR" +
                    "(id INT AUTO_INCREMENT PRIMARY KEY , " +
                    " name VARCHAR(255) UNIQUE NOT NULL, " +
                    "company_id INT NOT NULL, " +
                    "CONSTRAINT fk_company FOREIGN KEY (company_id) " +
                    "REFERENCES COMPANY (id))";

            String sql_create_customer = "CREATE TABLE IF NOT EXISTS CUSTOMER " +
                    "(ID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "NAME VARCHAR(255) UNIQUE NOT NULL, " +
                    "RENTED_CAR_ID INT," +
                    "CONSTRAINT fk_customer FOREIGN KEY (RENTED_CAR_ID) " +
                    "REFERENCES CAR(id))";

            //stmt.executeUpdate(sql_preparation);
            stmt.executeUpdate(sql_create_company);
            stmt.executeUpdate(sql_create_car);
            stmt.executeUpdate(sql_create_customer);

            System.out.println("Created table in given database...");

            // STEP 4: Clean-up environment
            stmt.close();
            conn.close();

            menuLevel1();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}