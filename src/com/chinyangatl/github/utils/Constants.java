package com.chinyangatl.github.utils;

/**
 * The Constants class holds the names of tables and columns specified in the backend.
 * This was done to have them declared in one place to avoid silly errors and to encapsulate and abstract that information from the DataSource
 * which was already over populated as is.
 *
 * @author Lesley Tsame Chinyanga, cse20-030
 * @version 1.0
 */

public class Constants {
    public static final String TABLE_CUSTOMER = "customer";
    public static final String COLUMN_CUSTOMER_ID = "customer_id";
    public static final String COLUMN_CUSTOMER_SURNAME = "surname";
    public static final String COLUMN_CUSTOMER_FIRSTNAME = "first_name";
    public static final String COLUMN_CUSTOMER_ADDRESS = "address";
    public static final String COLUMN_CUSTOMER_EMPLOYER = "employer";
    public static final String COLUMN_CUSTOMER_AGE = "age";

    public static final String TABLE_ACCOUNT = "account";
    public static final String COLUMN_ACCOUNT_NO = "account_no";
    public static final String COLUMN_ACCOUNT_BALANCE = "balance";
    public static final String COLUMN_ACCOUNT_PIN = "pin_no";
    public static final String COLUMN_ACCOUNT_TYPE = "type";
    public static final String COLUMN_ACCOUNT_OWNER = "customer_id";

    public static final String TABLE_TRANSACTION = "transaction";
    public static final String COLUMN_TRANSACTION_ACCOUNT_NO = "account_no";
    public static final String COLUMN_TRANSACTION_CUSTOMER_ID = "customer_id";
    public static final String COLUMN_TRANSACTION_TYPE = "transaction_type";
    public static final String COLUMN_TRANSACTION_AMOUNT = "amount";
    public static final String COLUMN_TRANSACTION_DATE = "date";

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

}
