package carsharing;

import java.sql.*;
import java.util.*;

import static carsharing.Main.DB_URL;
import static carsharing.Main.JDBC_DRIVER;
import static carsharing.Menu.menuLevel2;
import static carsharing.Menu.menuLevel3;

public class Companies {
    static Map<Integer, String> companies = new TreeMap<>();
    static int selectedCompany;

    static void selectCompany() {
        System.out.println("0. Back");
        Scanner scan = new Scanner(System.in);
        selectedCompany = scan.nextInt();
        if (selectedCompany == 0) {
            menuLevel2();
        }
        menuLevel3();
    }

    public static void queryAllCompanies() {
        int counter = 0;
        Connection conn;
        Statement stmt;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();
            String sql_companyList = "SELECT * FROM COMPANY";
            ResultSet rs = stmt.executeQuery(sql_companyList);
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                System.out.println(id + ". " + name);
                companies.put(id, name);
                counter++;
            }
            if (counter == 0) {
                System.out.println("The company list is empty!");
                menuLevel2();
            }
            stmt.close();
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addCompany() {
        Connection conn;
        PreparedStatement stmt;
        System.out.println("Enter the company name:");
        Scanner scan = new Scanner(System.in);
        String companyToAdd = scan.nextLine();
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            String sql_add_rows = "INSERT INTO COMPANY (name) VALUES (?);";
            stmt = conn.prepareStatement(sql_add_rows,
                    Statement.RETURN_GENERATED_KEYS);
            try (PreparedStatement statement = stmt) {
                statement.setString(1, companyToAdd);
                statement.executeUpdate();
                ResultSet tableKeys = statement.getGeneratedKeys();
                tableKeys.next();
            }
            stmt.close();
            conn.close();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
