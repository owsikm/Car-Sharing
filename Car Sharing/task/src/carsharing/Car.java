package carsharing;

import java.sql.*;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import static carsharing.Companies.selectedCompany;
import static carsharing.Main.DB_URL;
import static carsharing.Main.JDBC_DRIVER;
import static carsharing.Menu.menuLevel3;

public class Car {
    static Map<Integer, String> cars = new TreeMap<>();

    public static void addCar() {
        Connection conn;
        PreparedStatement stmt;
        System.out.println("Enter the car name:");
        Scanner scan = new Scanner(System.in);
        String carToAdd = scan.nextLine();
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            String sql_add_car = "INSERT INTO CAR (name, company_id) VALUES (?,?);";
            stmt = conn.prepareStatement(sql_add_car,
                    Statement.RETURN_GENERATED_KEYS);
            try (PreparedStatement statement = stmt) {
                statement.setString(1, carToAdd);
                statement.setString(2, String.valueOf(selectedCompany));
                statement.executeUpdate();
                ResultSet tableKeys = statement.getGeneratedKeys();
                tableKeys.next();
            }
            stmt.close();
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        menuLevel3();
    }

    public static void queryCars() {
        int counter = 0;
        Connection conn;
        PreparedStatement stmt;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);

            String sql_get_car = "SELECT * FROM CAR where company_id=" + selectedCompany + ";";
            stmt = conn.prepareStatement(sql_get_car);

            ResultSet rs;
            PreparedStatement statement = stmt;
            rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                counter++;
                System.out.println(counter + ". " + name);
                cars.put(id, name);
            }
            if (counter == 0) {
                System.out.println("The car list is empty!");
                menuLevel3();
            }
            stmt.close();
            conn.close();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
