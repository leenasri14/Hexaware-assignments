package payxpert.app;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import payxpert.Bean.Employee; 
import payxpert.Bean.Payroll;
import payxpert.Bean.Tax;
import payxpert.repository.EmployeeRepository; 
import payxpert.repository.PayrollRepository;
import payxpert.repository.TaxRepository;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static EmployeeRepository employeeRepository = new EmployeeRepository(); 
    private static PayrollRepository payrollRepository = new PayrollRepository();
    private static TaxRepository taxRepository = new TaxRepository();
    static Scanner sc=new Scanner(System.in);
    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int choice = sc.nextInt(); 
            switch (choice) {
                case 1:
                    addEmployee(); 
                    break;
                case 2:
                    addPayroll();
                    break;
                case 3:
                    getPayrollByEmployeeId();
                    break;
                case 4:
                    addTax();
                    break;
                case 5:
                    getTaxesForEmployee();
                    break;
                case 6:
                    System.out.println("Exit");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("1. Add Employee"); 
        System.out.println("2. Add Payroll");
        System.out.println("3. Get Payroll");
        System.out.println("4. Add Tax");
        System.out.println("5. Get Taxes");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    

    private static void addEmployee() {
        System.out.print("Enter Employee ID: ");
        int employeeID = scanner.nextInt();
        System.out.print("Enter First Name: ");
        String firstName = scanner.next();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.next();
        System.out.print("Enter Date of Birth: ");
        String dob = scanner.next();
        System.out.print("Enter Gender: ");
        String gender = scanner.next();
        System.out.print("Enter Email: ");
        String email = scanner.next();
        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.next();
        System.out.print("Enter Address: ");
        String address = scanner.next();
        System.out.print("Enter Position: ");
        String position = scanner.next();
        System.out.print("Enter Joining Date : ");
        String joiningDate = scanner.next();
        System.out.print("Enter Termination Date: ");
        scanner.nextLine(); 
        String terminationDateInput = scanner.nextLine();
        Date terminationDate = null;
        if (!terminationDateInput.isEmpty()) {
            terminationDate = java.sql.Date.valueOf(terminationDateInput);
        }

        Employee employee = new Employee(employeeID, firstName, lastName, java.sql.Date.valueOf(dob), gender, email, phoneNumber, address, position, java.sql.Date.valueOf(joiningDate), terminationDate);
        try {
            employeeRepository.addEmployee(employee); 
            System.out.println("Employee added");
        } catch (SQLException e) {
            System.out.println("Error adding employee: " + e.getMessage());
        }
    }

    private static void addPayroll() {
        System.out.print("Enter Payroll ID: ");
        int payrollID = scanner.nextInt();
        System.out.print("Enter Employee ID: ");
        int employeeID = scanner.nextInt();
        System.out.print("Enter Pay Period Start Date: ");
        String startDate = scanner.next();
        System.out.print("Enter Pay Period End Date: ");
        String endDate = scanner.next();
        System.out.print("Enter Basic Salary: ");
        Double basicSalary = scanner.nextDouble();
        System.out.print("Enter Overtime Pay: ");
        Double overtimePay = scanner.nextDouble();
        System.out.print("Enter Deductions: ");
        Double deductions = scanner.nextDouble();
        System.out.print("Enter Net Salary: ");
        Double netSalary = scanner.nextDouble();

        Payroll payroll = new Payroll(payrollID, employeeID, java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate), basicSalary, overtimePay, deductions, netSalary);
        try {
            payrollRepository.addPayroll(payroll);
            System.out.println("Payroll added");
        } catch (SQLException e) {
            System.out.println("Error adding payroll: " + e.getMessage());
        }
    }

    private static void getPayrollByEmployeeId() {
        System.out.print("Enter Employee ID: ");
        int employeeID = scanner.nextInt();
        try {
            List<Payroll> payrolls = payrollRepository.getPayrollsForEmployee(employeeID);
            if (payrolls.isEmpty()) {
                System.out.println("No payroll records");
            } else {
                for (Payroll payroll : payrolls) {
                    System.out.println("Payroll ID: " + payroll.getPayrollID() + ", Basic Salary: " + payroll.getBasicSalary() + ", Net Salary: " + payroll.getNetSalary());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving payrolls: " + e.getMessage());
        }
    }

    private static void addTax() {
        System.out.print("Enter Tax ID: ");
        int taxID = scanner.nextInt();
        System.out.print("Enter Employee ID: ");
        int employeeID = scanner.nextInt();
        System.out.print("Enter Tax Year: ");
        int taxYear = scanner.nextInt();
        System.out.print("Enter Taxable Income: ");
        Double taxableIncome = scanner.nextDouble();
        System.out.print("Enter Tax Amount: ");
        Double taxAmount = scanner.nextDouble();

        Tax tax = new Tax(taxID, employeeID, taxYear, taxableIncome, taxAmount);
        try {
            taxRepository.addTax(tax);
            System.out.println("Tax added");
        } catch (SQLException e) {
            System.out.println("Error adding tax: " + e.getMessage());
        }
    }

    private static void getTaxesForEmployee() {
        System.out.print("Enter Employee ID: ");
        int employeeID = scanner.nextInt();
        try {
            List<Tax> taxes = taxRepository.getTaxesForEmployee(employeeID);
            if (taxes.isEmpty()) {
                System.out.println("No tax records found");
            } else {
                for (Tax tax : taxes) {
                    System.out.println("Tax ID: " + tax.getTaxID() + ", Tax Year: " + tax.getTaxYear() + ", Tax Amount: " + tax.getTaxAmount());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving taxes: " + e.getMessage());
        }
    }
}