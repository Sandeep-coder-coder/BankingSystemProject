import java.io.*;
import java.util.*;

public class BankingSystem {
    static final String FILE = "accounts.txt";
    static ArrayList<Account> accounts = new ArrayList<>();

    public static void main(String[] args) {
        loadAccounts();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Create Account\n2. Deposit\n3. Withdraw\n4. Transfer\n5. View Accounts\n6. Exit");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1 -> createAccount(sc);
                case 2 -> deposit(sc);
                case 3 -> withdraw(sc);
                case 4 -> transfer(sc);
                case 5 -> viewAccounts();
                case 6 -> {
                    saveAccounts();
                    System.out.println("Data saved. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    static void createAccount(Scanner sc) {
        System.out.print("Enter account number: ");
        String accNo = sc.nextLine();
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        accounts.add(new Account(accNo, name, 0));
        System.out.println("Account created!");
    }

    static void deposit(Scanner sc) {
        Account a = findAccount(sc);
        if (a != null) {
            System.out.print("Enter amount to deposit: ");
            double amt = sc.nextDouble();
            sc.nextLine();
            a.balance += amt;
            System.out.println("Deposited successfully!");
        }
    }

    static void withdraw(Scanner sc) {
        Account a = findAccount(sc);
        if (a != null) {
            System.out.print("Enter amount to withdraw: ");
            double amt = sc.nextDouble();
            sc.nextLine();
            if (amt <= a.balance) {
                a.balance -= amt;
                System.out.println("Withdrawn successfully!");
            } else {
                System.out.println("Insufficient balance!");
            }
        }
    }

    static void transfer(Scanner sc) {
        System.out.println("From Account:");
        Account from = findAccount(sc);
        if (from == null) return;

        System.out.println("To Account:");
        Account to = findAccount(sc);
        if (to == null) return;

        System.out.print("Enter amount to transfer: ");
        double amt = sc.nextDouble();
        sc.nextLine();
        if (amt <= from.balance) {
            from.balance -= amt;
            to.balance += amt;
            System.out.println("Transfer successful!");
        } else {
            System.out.println("Insufficient balance!");
        }
    }

    static Account findAccount(Scanner sc) {
        System.out.print("Enter account number: ");
        String accNo = sc.nextLine();
        for (Account a : accounts) if (a.accNo.equals(accNo)) return a;
        System.out.println("Account not found!");
        return null;
    }

    static void viewAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts yet!");
            return;
        }
        for (Account a : accounts) System.out.println(a);
    }

    static void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    static void loadAccounts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE))) {
            accounts = (ArrayList<Account>) ois.readObject();
        } catch (Exception e) {
            // File may not exist yet
        }
    }
}

class Account implements Serializable {
    String accNo, name;
    double balance;

    Account(String accNo, String name, double balance) {
        this.accNo = accNo;
        this.name = name;
        this.balance = balance;
    }

    public String toString() {
        return "Account: " + accNo + ", Name: " + name + ", Balance: " + balance;
    }
}
