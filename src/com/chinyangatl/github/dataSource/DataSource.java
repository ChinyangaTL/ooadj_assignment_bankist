package com.chinyangatl.github.dataSource;

import com.chinyangatl.github.model.Account;
import com.chinyangatl.github.utils.Statements;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.chinyangatl.github.model.Customer;
import com.chinyangatl.github.model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * The JDBC for the program. It is the middleman that communicates directly with the backend to pull and insert data.
 * All CRUD operations necessary for the program are defined here.
 * Statements and Constants have been abstracted to separate files in the utils directory to avoid over populating this already
 * verbose class and to avoid errors by having constants defined in a single place.
 *
 * @author Lesley Tsame Chinyanga, cse20-030
 * @version 2.0
 */

public class DataSource {
    public static final String DB_NAME = "bank";
    public static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/" + DB_NAME;
    public static final String USERNAME = "root";
    public static final String PASSWORD = "1234";

    private Connection connection;
    private PreparedStatement insertIntoCustomer;
    private PreparedStatement removeCustomer;
    private PreparedStatement insertIntoAccount;
    private PreparedStatement removeAccount;
    private PreparedStatement queryCustomer;
    private PreparedStatement queryAccount;
    private PreparedStatement updateBalance;
    private PreparedStatement updatePin;
    private PreparedStatement loginUser;
    private PreparedStatement queryAccountTransactions;
    private PreparedStatement findCustomer;

    private static DataSource instance = new DataSource();

    private DataSource() {}

    /**
     * Create a single instance of the JDBC to avoid any potential errors in data duplication.
     * @return DataSource, an instance of this class with all the methods contained in it.
     */
    public static DataSource getInstance() {return  instance;}

    /**
     * Creates the connection to the database, and pre-compiles all prepared statements used in the class.
     * @return boolean, true if communication to the database was possible and false if an error has occurred.
     */
    public boolean open() {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING, USERNAME,PASSWORD);
            insertIntoCustomer = connection.prepareStatement(Statements.INSERT_INTO_CUSTOMER, Statement.RETURN_GENERATED_KEYS);
            insertIntoAccount = connection.prepareStatement(Statements.INSERT_INTO_ACCOUNT, Statement.RETURN_GENERATED_KEYS);
            removeCustomer = connection.prepareStatement(Statements.REMOVE_CUSTOMER);
            removeAccount = connection.prepareStatement(Statements.REMOVE_ACCOUNT);
            queryCustomer = connection.prepareStatement(Statements.QUERY_CUSTOMER);
            queryAccount = connection.prepareStatement(Statements.QUERY_ACCOUNT);
            updateBalance = connection.prepareStatement(Statements.UPDATE_BALANCE);
            updatePin = connection.prepareStatement(Statements.UPDATE_PIN);
            loginUser = connection.prepareStatement(Statements.QUERY_ACCOUNTS);
            queryAccountTransactions = connection.prepareStatement(Statements.QUERY_ACCOUNT_TRANSACTIONS);
            findCustomer = connection.prepareStatement(Statements.FIND_CUSTOMER);
            System.out.println("Connected to database");
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to db " + e.getMessage());
            return false;
        }
    }

    /**
     * Closes the connection to the database, and explicitly closes all prepared statements to ensure proper cleanup.
     */
    public void close() {
        try {
            if(connection != null) {connection.close();}
            if (insertIntoCustomer != null) {
                insertIntoCustomer.close();
            }
            if(removeCustomer != null) {
                removeCustomer.close();
            }
            if(removeAccount != null) {
                removeAccount.close();
            }
            if(insertIntoAccount != null) {
                insertIntoAccount.close();
            }
            if(queryCustomer != null) {
                queryCustomer.close();
            }
            if(queryAccount != null) {
                queryAccount.close();
            }
            if(updateBalance != null) {
                updateBalance.close();
            }
            if(updatePin != null) {
                updatePin.close();
            }
            if(loginUser != null) {
                loginUser.close();
            }
            if(queryAccountTransactions != null) {
                queryAccountTransactions.close();
            }
            if(findCustomer != null) {
                findCustomer.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection " + e.getMessage());
        }
    }

    /**
     * Makes a query to the backend to return all Customers.
     * @return an List of type Customer
     */
    public List<Customer> queryCustomers() {
        String query = Statements.QUERY_CUSTOMERS_START;
        try(Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(query)) {
            List<Customer> customers = new ArrayList<>();
            while(results.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(results.getInt(1));
                customer.setFirstName(results.getString(2));
                customer.setSurname(results.getString(3));
                customer.setAddress(results.getString(4));
                customer.setEmployer(results.getString(5));
                customer.setAge(results.getInt(6));
                customers.add(customer);
            }
            return customers;

        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            return null;
        }
    }

    /**
     * Makes a query to the backend to return all Accounts.
     * @return an List of type Account
     */
    public List<Account> queryAccounts() {
        String query = Statements.QUERY_ACCOUNTS;
        System.out.println(query);
        try(Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(query)) {
            List<Account> accounts = new ArrayList<>();
            while(results.next()) {
                Account account = new Account();
                Customer customer = new Customer();
                account.setAccountNo(results.getInt(1));
                account.setBalance(results.getDouble(2));
                account.setPinNo(results.getInt(3));
                account.setAccountType(Account.AccountType.valueOf(results.getString(4)));
                customer.setFirstName(results.getString("first_name"));
                customer.setSurname(results.getString("surname"));
                customer.setEmployer(results.getString("employer"));
                customer.setAddress(results.getString("address"));
                customer.setAge(results.getInt("age"));
                customer.setCustomerId(results.getInt("customer_id"));
                account.setOwner(customer);
                accounts.add(account);
            }
            return accounts;

        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            return null;
        }
    }

    /**
     * Makes a query to the backend to return all Transactions belonging to an Account.
     * @param accNo, an int representing the identifier for a specific Account
     * @return an ObservableList of type Transactions (Done this way to get JavaFX to play nice with data binding)
     */
    public ObservableList<Transaction> queryAccountTransactions(int accNo) throws SQLException {
        queryAccountTransactions.setInt(1, accNo);
        ResultSet resultSet = queryAccountTransactions.executeQuery();
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        //List<Transaction> transactions = new ArrayList<>();
        while(resultSet.next()) {
            Transaction transaction = new Transaction();
            transaction.setAccountNo(accNo);
            transaction.setAmount(resultSet.getDouble("amount"));
            transaction.setCustomerID(resultSet.getInt("customer_id"));
            transaction.setType(Transaction.TransactionType.valueOf(resultSet.getString("transaction_type").toUpperCase()));
            transaction.setDatetime(resultSet.getTimestamp("date"));
            transactions.add(transaction);
        }
        //return FXCollections.observableArrayList(transactions);
        return transactions;
    }


    /**
     * Populates the insertIntoCustomer prepared statement with values from the customer to be inserted,
     * inserts the customer into the Customer Table in the backed and return their id.
     * @param customer the Customer to be inserted
     * @return an int representing the customerID of the created Customer
     */
    public int insertCustomer(Customer customer) throws SQLException{
       queryCustomer.setInt(1, customer.getCustomerId());
       ResultSet result = queryCustomer.executeQuery();
       if(result.next()) {
           return result.getInt(1);
       } else {
           insertIntoCustomer.setInt(1, customer.getCustomerId());
           insertIntoCustomer.setString(2, customer.getFirstName());
           insertIntoCustomer.setString(3, customer.getSurname());
           insertIntoCustomer.setString(4, customer.getAddress());
           insertIntoCustomer.setString(5, customer.getEmployer());
           insertIntoCustomer.setInt(6, customer.getAge());
           int affectedRows = insertIntoCustomer.executeUpdate();

           if(affectedRows != 1) {
               throw new SQLException("Couldn't insert customer");
           }
           ResultSet generatedKeys = insertIntoCustomer.getGeneratedKeys();
           if(generatedKeys.next()) {
               //System.out.println(generatedKeys.getInt(1));
               return generatedKeys.getInt(1);
           } else {
               throw new SQLException("Couldn't get id for customer");
           }
        }
    }

    /**
     * Returns an account with all the information about it on the condition that the account number and pin number match what is in the backend
     * @param accountNo the account number assigned to an account
     * @param pinNo the pin number assigned to an account
     * @return Account an account holding all the data tied to that specific account.
     */
    public Account login(int accountNo, int pinNo) {
        try {
            queryAccount.setInt(1, accountNo);
            ResultSet resultSet = queryAccount.executeQuery();

            Account account = new Account();
            Customer customer = new Customer();
            if(resultSet.next()) {
                int retrievedPin = resultSet.getInt("pin_no");
                account.setAccountNo(accountNo);
                account.setPinNo(retrievedPin);
                account.setBalance(resultSet.getDouble("balance"));
                account.setAccountType(Account.AccountType.valueOf(resultSet.getString(4)));
                customer.setFirstName(resultSet.getString("first_name"));
                customer.setSurname(resultSet.getString("surname"));
                customer.setEmployer(resultSet.getString("employer"));
                customer.setAddress(resultSet.getString("address"));
                customer.setAge(resultSet.getInt("age"));
                customer.setCustomerId(resultSet.getInt("customer_id"));
                account.setOwner(customer);
                if(retrievedPin == pinNo) return account;
            } else {
                return null;
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Failed login: " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns an account with all the information about it based on the account number
     * @param accountNo the account number assigned to an account
     * @return Account an account holding all the data tied to that specific account.
     */
    public Account findAccount(int accountNo) {
        try {
            queryAccount.setInt(1, accountNo);
            ResultSet resultSet = queryAccount.executeQuery();

            Account account = new Account();
            Customer customer = new Customer();
            if(resultSet.next()) {
                int retrievedPin = resultSet.getInt("pin_no");
                account.setAccountNo(accountNo);
                account.setPinNo(retrievedPin);
                account.setBalance(resultSet.getDouble("balance"));
                account.setAccountType(Account.AccountType.valueOf(resultSet.getString(4)));
                customer.setFirstName(resultSet.getString("first_name"));
                customer.setSurname(resultSet.getString("surname"));
                customer.setEmployer(resultSet.getString("employer"));
                customer.setAddress(resultSet.getString("address"));
                customer.setAge(resultSet.getInt("age"));
                customer.setCustomerId(resultSet.getInt("customer_id"));
                account.setOwner(customer);
                return account;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Failed login: " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns a customer with all the information about it based on their customerID.
     * @param customerID the unique customer ID number assigned to a customer
     * @return Customer a customer with their information
     */
    public Customer findCustomer(int customerID) throws SQLException {
        findCustomer.setInt(1, customerID);
        ResultSet resultSet = findCustomer.executeQuery();
        if (resultSet.next()) {
            Customer customer = new Customer();
            customer.setCustomerId(customerID);
            customer.setFirstName(resultSet.getString(2));
            customer.setSurname(resultSet.getString(3));
            customer.setAddress(resultSet.getString(4));
            customer.setEmployer(resultSet.getString(5));
            customer.setAge(resultSet.getInt(6));
            return customer;

        }
       return null;
    }

//    public void insertCustomer(String lName, String fName, String address, String employer, int age) {
//        try {
//            connection.setAutoCommit(false);
//
//            System.out.println(lName +  ", " +fName +  ", " + address+  ", " + employer +  ", " + age);
//
//
//            if(affectedRows == 1) {
//                connection.commit();
//            } else {
//                throw new SQLException("Couldn't insert customer");
//            }
//        } catch (SQLException e) {
//            System.out.println("Insert Customer Exception: " + e.getMessage());
//        } finally {
//            try {
//                System.out.println("Reset autocommit behaviour");
//                connection.setAutoCommit(true);
//            } catch (SQLException e) {
//                System.out.println("Couln't reset auto commit " + e.getMessage() );
//            }
//        }
//
//    }

    /**
     * Removes a customer from the customer table and in turn the program.
     * @param id the customer id number assigned to a customer
     * @return boolean true if successful in deleing the customer, false otherwise
     */
    public boolean removeCustomer(int id) {
        try {
            removeCustomer.setInt(1, id);
            int affectedRecords = removeCustomer.executeUpdate();
            return affectedRecords == 1;
        } catch (SQLException e) {
            System.out.println("Couldn't delete customer " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserts an Account into the Account Table, and returns that account back to us.
     * @param type the type of Account to be created based off of the enums specified in Account.AccountType
     * @param customer the owner of the account to be created
     * @return Account an account with all the data now tied to it
     */
    public Account insertAccount(Account.AccountType type, Customer customer) throws SQLException {
        try {
            connection.setAutoCommit(false);
            int accNo = generateAccNo();
            int customerId = insertCustomer(customer);
            double balance = 0.00;
            if(type.equals(Account.AccountType.INTERESTBEARING)) {
                balance = 500.00;
            }
            int pin = generatePin();
            insertIntoAccount.setInt(1, accNo);
            insertIntoAccount.setDouble(2, balance);
            insertIntoAccount.setInt(3, pin);
            insertIntoAccount.setString(4, type.toString());
            insertIntoAccount.setInt(5, customerId);
            int affectedRows = insertIntoAccount.executeUpdate();


            if(affectedRows == 1) {
                connection.commit();
                return new Account(accNo, pin, balance, customer, type);
            } else {
                throw new SQLException("Couldn't insert account");
            }
        } catch (SQLException e) {
            System.out.println("Insert Customer Exception: " + e.getMessage());
        } finally {
            try {
                System.out.println("Reset autocommit behaviour");
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couln't reset auto commit " + e.getMessage() );
            }
        }
        return null;
    }

    /**
     * Makes an update call to the Account Table in the backend to alter the balance.
     * @param accNo the account number to which to make the update to
     * @param amount the amount to update the balance by (positive to add, negative to subtract)
     * @return boolean true if successful in updating the balance, and false otherwise.
     */
    public boolean updateBalance(int accNo, double amount)  {
        try {
            updateBalance.setDouble(1, amount);
            updateBalance.setInt(2, accNo);
            int affectedRecords = updateBalance.executeUpdate();
            return affectedRecords == 1;

        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Makes an update call to the Account Table in the backend to alter the the account's pin number.
     * @param accNo the account number to which to make the update to
     * @param newPin the new pin number to be associated to the account
     * @return boolean true if successful in updating the balance, and false otherwise.
     */
    public boolean updatePin(int accNo, int newPin)  {
        try {
            updatePin.setInt(1, newPin);
            updatePin.setInt(2, accNo);
            int affectedRecords = updatePin.executeUpdate();
            return affectedRecords == 1;

        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Makes an delete call to the Account Table in the backend to remove the specified Account.
     * @param accNo the account number for the account to delete.
     * @return boolean true if successful in deleting the account, and false otherwise.
     */
    public boolean removeAccount(int accNo) {
        try {
            removeAccount.setInt(1, accNo);
            int affectedRecords = removeAccount.executeUpdate();
            return affectedRecords == 1;
        } catch (SQLException e) {
            System.out.println("Couldn't delete customer " + e.getMessage());
            return false;
        }
    }

    /**
     * Generate a unique and random account number when creating an Account
     * @return accNo the new account number
     */
    private int generateAccNo() {
        int min = 10000000;
        int max = 99999999;
        int accNo = (int)(Math.random() * (max - min + 1) + min);
        return accNo;
    }

    /**
     * Generate a random pin number when creating an Account
     * @return pin a four digit pin number
     */
    private int generatePin() {
        int min = 1000;
        int max = 9999;
        int pin = (int)(Math.random() * (max - min + 1) + min);
        return pin;
    }



//    public void showTables() {
//        DatabaseMetaData metaData = null;
//        try {
//            metaData = connection.getMetaData();
//            String[] types = {"TABLE"};
//            //Retrieving the columns in the database
//            ResultSet tables = metaData.getTables(null, null, "%", types);
//            System.out.println("tables");
//            while (tables.next()) {
//                System.out.println(tables.getString("TABLE_NAME"));
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//    }





}
