package com.expensetracker;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class ExpenseTracker {
    static List<Transaction> transactions = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Expense Tracker ---");
            System.out.println("1. Add Transaction");
            System.out.println("2. View Monthly Summary");
            System.out.println("3. Save to File");
            System.out.println("4. Load from File");
            System.out.println("5. Exit");

            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> addTransaction();
                case 2 -> viewMonthlySummary();
                case 3 -> saveToFile();
                case 4 -> loadFromFile();
                case 5 -> System.exit(0);
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void addTransaction() {
        sc.nextLine(); // clear buffer
        
        System.out.println("Select type:");
        System.out.println("1. Income");
        System.out.println("2. Expense");
        System.out.print("Enter choice (1 or 2): ");
        int typeChoice = sc.nextInt();
        sc.nextLine(); // clear buffer after nextInt
        
        String type;
        if (typeChoice == 1) {
            type = "Income";
        } else if (typeChoice == 2) {
            type = "Expense";
        } else {
            System.out.println("Error: Invalid type choice. Only 1 or 2 allowed.");
            return;
        }

        String category = null;
        if (type.equals("Income")) {
            System.out.println("Select Income category:");
            System.out.println("1. Salary");
            System.out.println("2. Business");
            System.out.print("Enter choice (1 or 2): ");
            int catChoice = sc.nextInt();
            sc.nextLine();

            if (catChoice == 1) {
                category = "Salary";
            } else if (catChoice == 2) {
                category = "Business";
            } else {
                System.out.println("Error: Invalid Income category choice.");
                return;
            }
        } else { // Expense
            System.out.println("Select Expense category:");
            System.out.println("1. Food");
            System.out.println("2. Rent");
            System.out.println("3. Travel");
            System.out.print("Enter choice (1, 2 or 3): ");
            int catChoice = sc.nextInt();
            sc.nextLine();

            if (catChoice == 1) {
                category = "Food";
            } else if (catChoice == 2) {
                category = "Rent";
            } else if (catChoice == 3) {
                category = "Travel";
            } else {
                System.out.println("Error: Invalid Expense category choice.");
                return;
            }
        }

        System.out.print("Enter amount: ");
        if (!sc.hasNextDouble()) {
            System.out.println("Error: Invalid amount entered.");
            sc.nextLine(); // clear invalid input
            return; // reject input
        }
        double amount = sc.nextDouble();

        Transaction t = new Transaction(type, category, amount, LocalDate.now());
        transactions.add(t);
        System.out.println("Transaction added successfully.");
    }

    static void viewMonthlySummary() {
        double totalIncome = 0, totalExpense = 0;
        for (Transaction t : transactions) {
            if (t.date.getMonthValue() == LocalDate.now().getMonthValue()) {
                if (t.type.equalsIgnoreCase("Income"))
                    totalIncome += t.amount;
                else
                    totalExpense += t.amount;
            }
        }
        System.out.println("\n--- Monthly Summary ---");
        System.out.println("Total Income : " + totalIncome);
        System.out.println("Total Expense: " + totalExpense);
        System.out.println("Balance      : " + (totalIncome - totalExpense));
        
    }

    static void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("transactions.csv", true))) {
            // Write header (optional)
        	pw.println();         // Write empty line
            pw.println("Type,Category,Amount,Date");

            // Write all transactions
            for (Transaction t : transactions) {
                pw.println(t);
            }

            // Calculate current month summary
            double totalIncome = 0, totalExpense = 0;
            for (Transaction t : transactions) {
                if (t.date.getMonthValue() == LocalDate.now().getMonthValue() &&
                    t.date.getYear() == LocalDate.now().getYear()) {
                    if (t.type.equalsIgnoreCase("Income")) {
                        totalIncome += t.amount;
                    } else {
                        totalExpense += t.amount;
                    }
                }
            }

            double balance = totalIncome - totalExpense;

            // Write summary
            pw.println(); // blank line

            pw.println("Summary for " + LocalDate.now().getMonth() + " " + LocalDate.now().getYear());
            pw.println("Total Income," + totalIncome);
            pw.println("Total Expense," + totalExpense);
            pw.println("Balance," + balance);

            System.out.println("Saved to transactions.csv (including monthly summary)");
        } catch (Exception e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    

    static void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("transactions.csv"))) {
            String line;
            transactions.clear(); // Clear previous in-memory entries
            boolean headerSkipped = false;

            System.out.println("\n--- Loaded Transactions ---");

            while ((line = br.readLine()) != null) {
            	
                if (line.trim().isEmpty()) continue; // Skip empty lines

                String[] parts = line.split(",");

                // Skip CSV header
                if (!headerSkipped && parts[0].equalsIgnoreCase("Type")) {
                    headerSkipped = true;
                    continue;
                }

                if (parts.length < 4 || parts[2].equalsIgnoreCase("Amount")) {
                    continue; // Silently skip summary or header lines
                }


                String type = parts[0].trim().split("=")[parts[0].contains("=") ? 1 : 0];
                String category = parts[1].trim().split("=")[parts[1].contains("=") ? 1 : 0];
                double amount = Double.parseDouble(parts[2].trim().split("=")[parts[2].contains("=") ? 1 : 0]);
                String dateStr = parts[3].trim().split("=")[parts[3].contains("=") ? 1 : 0].replace("]", "");
                LocalDate date = LocalDate.parse(dateStr);


                Transaction transaction = new Transaction(type, category, amount, date);
                transactions.add(transaction);

                // Print the transaction details
                System.out.println("Type     : " + type);
                System.out.println("Category : " + category);
                System.out.println("Amount   : " + amount);
                System.out.println("Date     : " + date);
                System.out.println(); // Add spacing between transactions
            }
            
            double totalIncome = 0, totalExpense = 0;
            for (Transaction t : transactions) {
                if (t.type.equalsIgnoreCase("Income")) {
                    totalIncome += t.amount;
                } else if (t.type.equalsIgnoreCase("Expense")) {
                    totalExpense += t.amount;
                }
            }

            System.out.println("--- Summary ---");
            System.out.println("Total Income : " + totalIncome);
            System.out.println("Total Expense: " + totalExpense);
            System.out.println("Balance      : " + (totalIncome - totalExpense));

            if (transactions.isEmpty()) {
                System.out.println("No transactions found.");
            }

        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

}
