package carsharing;

import java.util.Scanner;

import static carsharing.Car.addCar;
import static carsharing.Car.queryCars;
import static carsharing.Companies.*;
import static carsharing.Customer.*;

public class Menu {
    static Scanner scan = new Scanner(System.in);

    public static void menuLevel1() {
        System.out.println("\n1. Log in as a manager\n" +
                "2. Log in as a customer\n" +
                "3. Create a customer\n" +
                "0. Exit");
        int optionLevel1 = scan.nextInt();

        switch (optionLevel1) {
            case 1:
                menuLevel2();
                break;
            case 2:
                queryAllCustomers();
                selectCustomer();
                menuLevel4();
                break;
            case 3:
                addCustomer();
                break;
            case 0:
                System.exit(0);
            default: {
                System.out.println("Wrong input!");
                menuLevel1();
            }
        }
    }

    public static void menuLevel2() {
        System.out.println("\n1. Company list\n" +
                "2. Create a company\n" +
                "0. Back");
        int optionLevel2 = scan.nextInt();

        switch (optionLevel2) {
            case 1: {
                queryAllCompanies();
                selectCompany();
                System.out.println();
                menuLevel2();
                break;
            }
            case 2: {
                System.out.println("Create company:");
                addCompany();
                System.out.println();
                menuLevel2();
                break;
            }
            case 0:
                menuLevel1();
                break;
        }
    }

    public static void menuLevel3() {

        System.out.println("\n'" + companies.get(selectedCompany) + "' " + "company");
        System.out.println("1. Car list\n" +
                "2. Create a car\n" +
                "0. Back");
        int optionLevel3 = scan.nextInt();

        switch (optionLevel3) {
            case 1: {
                System.out.println("Car list:");
                queryCars();
                menuLevel3();
                break;
            }
            case 2: {
                System.out.println("Create a car:");
                addCar();
                break;
            }
            case 0:
                menuLevel2();
                break;
        }
    }

    public static void menuLevel4() {
        System.out.println("\n1. Rent a car\n" +
                "2. Return a rented car\n" +
                "3. My rented car\n" +
                "0. Back");
        int optionLevel4 = scan.nextInt();

        switch (optionLevel4) {
            case 1: {
                chooseACompany();
                customerSelectCar();
                bookingACar();
                break;
            }
            case 2: {
                returnRentedCar();
                menuLevel4();
                break;
            }
            case 3: {
                allCarsOfSelectedCustomer();
                menuLevel4();
                break;
            }
            case 0:
                queryAllCustomers();
                selectCustomer();
                menuLevel4();
                break;
        }
    }
}
