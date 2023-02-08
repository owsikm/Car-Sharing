package carsharing;

import java.sql.*;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import static carsharing.Car.cars;
import static carsharing.Companies.*;
import static carsharing.Main.DB_URL;
import static carsharing.Main.JDBC_DRIVER;
import static carsharing.Menu.*;

public class Customer {
    static int selectedCustomer;
    static int customerSelectCompany;
    static int customerSelectCar;
    static Map<Integer, String> customers = new TreeMap<>();

    static void selectCustomer() {
        System.out.println("0. Back");
        Scanner scan = new Scanner(System.in);
        selectedCustomer = scan.nextInt();
        if (selectedCustomer == 0) {
            menuLevel1();
        }
    }

    public static void addCustomer() {
        Connection conn;
        PreparedStatement stmt;
        System.out.println("Enter the customer name:");
        Scanner scan = new Scanner(System.in);
        String customerToAdd = scan.nextLine();
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            String sql_add_customer = "INSERT INTO CUSTOMER (name) VALUES (?);";
            stmt = conn.prepareStatement(sql_add_customer,
                    Statement.RETURN_GENERATED_KEYS);
            try (PreparedStatement statement = stmt) {
                statement.setString(1, customerToAdd);
                statement.executeUpdate();
                ResultSet tableKeys = statement.getGeneratedKeys();
                tableKeys.next();
            }
            stmt.close();
            conn.close();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        menuLevel1();
    }

    public static void queryAllCustomers() {
        int counter = 0;
        Connection conn;
        Statement stmt;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();
            String sql_customer_list = "SELECT * FROM CUSTOMER";
            ResultSet rs = stmt.executeQuery(sql_customer_list);
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                if (counter == 0) {
                    System.out.println("\nThe customer list: ");
                }
                System.out.println(id + ". " + name);
                //     System.out.println(rs.getInt(1) + ". " + rs.getString(2));
                customers.put(id, name);
                counter++;
            }
            if (counter == 0) {
                System.out.println("The customer list is empty!");
                menuLevel1();
            }
            stmt.close();
            conn.close();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void RentACar() {
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
                //     System.out.println(rs.getInt(1) + ". " + rs.getString(2));
                companies.put(id, name);
                counter++;
            }
            if (counter == 0) {
                System.out.println("The car list is empty!");
                menuLevel4();
            }
            stmt.close();
            conn.close();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static void chooseACompany() {
        Connection conn;
        PreparedStatement stmt0;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            String sql_if_customer_rented_car = String.format("SELECT rented_car_id \n" +
                    "FROM CUSTOMER \n" +
                    "WHERE customer.id = %s" +
                    "AND RENTED_CAR_ID IS NOT NULL", selectedCustomer);
            stmt0 = conn.prepareStatement(sql_if_customer_rented_car);
            ResultSet rs;
            PreparedStatement statement0 = stmt0;
            rs = statement0.executeQuery();
            if (rs.next()) {
                System.out.println("You've already rented a car!");
                menuLevel4();
            } else {
                System.out.println("Choose a company:");
                RentACar();
                System.out.println("0. Back");
                Scanner scan = new Scanner(System.in);
                customerSelectCompany = scan.nextInt();
                if (customerSelectCompany == 0) {
                    menuLevel4();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static void customerSelectCar() {
        System.out.println("Choose a car:");
        allCarsForSelectedCompany();
        System.out.println("0. Back");
        Scanner scan = new Scanner(System.in);
        customerSelectCar = scan.nextInt();
        if (customerSelectCar == 0) {
            menuLevel4();
        }
    }

    public static void allCarsForSelectedCompany() {
        int counter = 0;
        Connection conn;
        PreparedStatement stmt;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);

            String sql_get_car = "SELECT * FROM CAR where company_id=" + customerSelectCompany + ";";
            stmt = conn.prepareStatement(sql_get_car);

            ResultSet rs;
            PreparedStatement statement = stmt;
            rs = statement.executeQuery();
            cars.clear();

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                counter++;
                System.out.println(counter + ". " + name);
                cars.put(id, name);
            }
            if (counter == 0) {
                System.out.println("The car list is empty!\n");
                menuLevel4();
            }
            stmt.close();
            conn.close();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void bookingACar() {
        Connection conn;
        PreparedStatement stmt;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);


            // String sql_add_customer = "INSERT INTO CUSTOMER (name) VALUES (?);";
            String sql_booking_a_car = "UPDATE CUSTOMER\n" +
                    "SET RENTED_CAR_ID = VALUES (?)\n" +
                    "WHERE ID= VALUES (?)";
            stmt = conn.prepareStatement(sql_booking_a_car);
            try (PreparedStatement statement = stmt) {
                statement.setString(1, String.valueOf(customerSelectCar));
                statement.setString(2, String.valueOf(selectedCustomer));
                statement.executeUpdate();
                System.out.println("You rented '" + cars.get(customerSelectCar) + "'");
            }
            stmt.close();
            conn.close();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        menuLevel4();
    }

    public static void allCarsOfSelectedCustomer() {
        int counter = 0;
        Connection conn;
        PreparedStatement stmt;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);

            String sql_get_car = String.format("SELECT car.Name, company.name \n" +
                    "FROM CUSTOMER \n" +
                    "JOIN CAR \n" +
                    "ON customer.RENTED_CAR_ID =  car.ID\n" +
                    "JOIN company \n" +
                    "ON car.COMPANY_ID = company.id\n" +
                    "WHERE customer.id = %s", selectedCustomer);
            stmt = conn.prepareStatement(sql_get_car);

            ResultSet rs;
            PreparedStatement statement = stmt;
            rs = statement.executeQuery();

            while (rs.next()) {
                String carName = rs.getString(1);
                String companyName = rs.getString(2);
                counter++;
                System.out.println("Your rented car: \n" + carName + "\n" + "Company: \n" + companyName);
            }
            if (counter == 0) {
                System.out.println("You didn't rent a car!");
                menuLevel4();
            }
            stmt.close();
            conn.close();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    static void returnRentedCar() {
        Connection conn;
        PreparedStatement stmt, stmt0;
        try {

            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            String sql_if_customer_rented_car = String.format("SELECT rented_car_id \n" +
                    "FROM CUSTOMER \n" +
                    "WHERE customer.id = %s" +
                    "AND RENTED_CAR_ID IS NOT NULL", selectedCustomer);
            stmt0 = conn.prepareStatement(sql_if_customer_rented_car);
            ResultSet rs;
            PreparedStatement statement0 = stmt0;
            rs = statement0.executeQuery();
            if (rs.next()) {
                String sql_booking_a_car = "UPDATE CUSTOMER\n" +
                        "SET RENTED_CAR_ID = null\n" +
                        "WHERE ID= " + selectedCustomer;
                stmt = conn.prepareStatement(sql_booking_a_car);
                try (PreparedStatement statement = stmt) {
                    statement.executeUpdate();
                    System.out.println("You've returned a rented car!");
                }
                stmt.close();
                conn.close();
            } else System.out.println("You didn't rent a car!");

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
