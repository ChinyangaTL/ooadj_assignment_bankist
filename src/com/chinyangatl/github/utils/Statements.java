package com.chinyangatl.github.utils;

/**
 * The Statements class holds the Strings of the statements that the DataSource class will use to make calls to the database.
 * This was done to have them declared in one place to avoid silly errors and to encapsulate and abstract that information from the DataSource
 * which was already over populated as is.
 *
 * @author Lesley Tsame Chinyanga, cse20-030
 * @version 1.0
 */
public class Statements {
    /**
     * CUSTOMER
     */
    public static final String FIND_CUSTOMER = "SELECT * FROM " + Constants.TABLE_CUSTOMER + " WHERE " + Constants.COLUMN_CUSTOMER_ID + " = ? ";
    public static final String QUERY_CUSTOMER =
            "SELECT " + Constants.COLUMN_CUSTOMER_ID + " FROM " + Constants.TABLE_CUSTOMER
            + " WHERE " + Constants.COLUMN_CUSTOMER_ID + " = ?";
    public static final String QUERY_CUSTOMERS_START =
            "SELECT * FROM " + Constants.TABLE_CUSTOMER + " " ;
    public static final String QUERY_CUSTOMERS_SORT =
            "ORDER BY " + Constants.TABLE_CUSTOMER + "." + Constants.COLUMN_CUSTOMER_SURNAME + " ";

    /**
     * ACCOUNT
     */
//    SELECT account_no, pin_no, customer.first_name, balance, type FROM account
//    INNER JOIN customer on account.customer_id = customer.customer_id
    public static final String QUERY_ACCOUNT =
            "SELECT * FROM " + Constants.TABLE_ACCOUNT +
                    " INNER JOIN " + Constants.TABLE_CUSTOMER + " ON " + Constants.TABLE_ACCOUNT + "." + Constants.COLUMN_ACCOUNT_OWNER + " = " +
                    Constants.TABLE_CUSTOMER + "." + Constants.COLUMN_CUSTOMER_ID
                    + " WHERE " + Constants.COLUMN_ACCOUNT_NO + " = ?";
    public static final String QUERY_ACCOUNT_START =
            "SELECT * FROM " + Constants.TABLE_ACCOUNT + " " ;
    public static final String QUERY_ACCOUNT_SORT =
            "ORDER BY " + Constants.TABLE_ACCOUNT + "." + Constants.COLUMN_ACCOUNT_NO + " ";

    /**
     * ACCOUNTS
     */
    public static final String QUERY_ACCOUNTS =
            "SELECT *  FROM " + Constants.TABLE_ACCOUNT +
                    " INNER JOIN " + Constants.TABLE_CUSTOMER + " ON " + Constants.TABLE_ACCOUNT + "." + Constants.COLUMN_ACCOUNT_OWNER + " = " +
                    Constants.TABLE_CUSTOMER + "." + Constants.COLUMN_CUSTOMER_ID;


    /**
     * TRANSACTION
     */
    public static final String QUERY_ACCOUNT_TRANSACTIONS =
            "SELECT * FROM " + Constants.TABLE_TRANSACTION + " WHERE " +
                    Constants.COLUMN_ACCOUNT_NO + " = ?";
    public static final String QUERY_TRANSACTION_SORT =
            "ORDER BY " + Constants.TABLE_TRANSACTION + "." + Constants.COLUMN_TRANSACTION_AMOUNT + " ";

    /**
     * INSERTS - CUSTOMER
     */
    public static final String INSERT_INTO_CUSTOMER =
            "INSERT INTO " + Constants.TABLE_CUSTOMER +
                    '(' + Constants.COLUMN_CUSTOMER_ID + ", " + Constants.COLUMN_CUSTOMER_FIRSTNAME + ", " + Constants.COLUMN_CUSTOMER_SURNAME + ", " + Constants.COLUMN_CUSTOMER_ADDRESS + ", " +
                    Constants.COLUMN_CUSTOMER_EMPLOYER + ", " + Constants.COLUMN_CUSTOMER_AGE + ") " +
                    "VALUES(?,?,?,?,?,?)";

    /**
     * INSERTS - ACCOUNT
     */
    public static final String INSERT_INTO_ACCOUNT =
            "INSERT INTO " + Constants.TABLE_ACCOUNT +
                    '(' + Constants.COLUMN_ACCOUNT_NO + ", " +Constants.COLUMN_ACCOUNT_BALANCE + ", " + Constants.COLUMN_ACCOUNT_PIN + ", " +
                    Constants.COLUMN_ACCOUNT_TYPE + ", " + Constants.COLUMN_ACCOUNT_OWNER + ") " +
                    "VALUES(?,?,?,?,?)";

    /**
     * INSERTS - TRANSACTION
     */
    public static final String INSERT_INTO_TRANSACTION =
            "INSERT INTO " + Constants.TABLE_TRANSACTION +
                    '(' + Constants.COLUMN_TRANSACTION_ACCOUNT_NO + ", " + Constants.COLUMN_TRANSACTION_CUSTOMER_ID + ", " + Constants.COLUMN_TRANSACTION_TYPE + ", " +
                    Constants.COLUMN_TRANSACTION_DATE + ") " +
                    "VALUES(?,?,?,?)";
    /**
     * DELETES - ACCOUNT
     */
    public static final String REMOVE_CUSTOMER =
            "DELETE FROM " + Constants.TABLE_CUSTOMER + " WHERE " + Constants.COLUMN_CUSTOMER_ID + " = ?";
    public static final String REMOVE_ACCOUNT =
            "DELETE FROM " + Constants.TABLE_ACCOUNT + " WHERE " + Constants.COLUMN_ACCOUNT_NO + " = ?";

    /**
     * UPDATE BALANCE
     */
    public static final String UPDATE_BALANCE =
            "UPDATE " + Constants.TABLE_ACCOUNT + " SET " + Constants.COLUMN_ACCOUNT_BALANCE + " = " + Constants.COLUMN_ACCOUNT_BALANCE + "+ ?" +
                    " WHERE " + Constants.COLUMN_ACCOUNT_NO + " = ?";
    /**
     * CHECK BALANCE
     */
    public static final String CHECK_BALANCE =
            "SELECT " + Constants.COLUMN_ACCOUNT_BALANCE +
                    " WHERE " + Constants.COLUMN_ACCOUNT_NO + "= ?";

    /**
     * UPDATE PIN
     */
    public static final String UPDATE_PIN =
            "UPDATE " + Constants.TABLE_ACCOUNT + " SET " + Constants.COLUMN_ACCOUNT_PIN + " = ?" +
                    " WHERE " + Constants.COLUMN_ACCOUNT_NO + " = ?";

}
