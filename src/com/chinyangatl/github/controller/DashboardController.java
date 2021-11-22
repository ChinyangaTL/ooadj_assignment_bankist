package com.chinyangatl.github.controller;

import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import com.chinyangatl.github.dataSource.DataSource;
import com.chinyangatl.github.model.Account;
import com.chinyangatl.github.model.Customer;
import com.chinyangatl.github.model.Transaction;
import com.chinyangatl.github.utils.HelperMethods;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * The Controller Class for the dashboard.fxml file which represents the Customer's homepage.
 *
 * All event handlers for this class are attached within the dashboard.fxml.
 *
 * @author Lesley Tsame Chinyanga, cse20-030
 * @version 1.1
 */
public class DashboardController {
    @FXML
    private BorderPane mainPane;
    @FXML
    private Button btnLogout;
    @FXML
    private Label txtWelcome;
    @FXML
    private Label txtBalance;
    @FXML
    private ListView<Transaction> listTransactions;
    @FXML
    private TextField depositAmount;
    @FXML
    private TextField withdrawAmount;
    @FXML
    private Button btnWithdraw;

    // Account Info
    public static int ACC_NO;
    public static double ACC_BALANCE;
    public static int ACC_PIN;
    public static String ACC_TYPE;
    public static Customer ACC_OWNER;

    public static void setAccNo(int accNo) {
        ACC_NO = accNo;
    }
    public static void setAccBalance(double accBalance) {
        ACC_BALANCE = accBalance;
    }
    public static void setAccPin(int accPin) {
        ACC_PIN = accPin;
    }
    public static void setAccType(String accType) {
        ACC_TYPE = accType;
    }
    public static void setAccOwner(Customer accOwner) {
        ACC_OWNER = accOwner;
    }

    public void initialize(Account account) throws SQLException {
        initUserInfo(account);
        txtBalance.setText("Balance: P" + account.getBalance() + "0");
        txtWelcome.setText("Good to have you back, " + account.getOwner().getFirstName());
        if(ACC_TYPE.equals("SAVINGS")) {
            btnWithdraw.setDisable(true);
            btnWithdraw.setCursor(Cursor.DISAPPEAR);
        }

        // deposit and withdraw numbers only
        HelperMethods.alterTextFieldToNumbersOnly(depositAmount);
        HelperMethods.alterTextFieldToNumbersOnly(withdrawAmount);


    }

    public void initUserInfo(Account account) {
        setAccNo(account.getAccountNo());
        setAccOwner(account.getOwner());
        setAccBalance(account.getBalance());
        setAccType(account.getAccountType());
        setAccPin(account.getPinNo());
    }

    @FXML
    public void showStatement() throws SQLException {
        ObservableList<Transaction> transactions = DataSource.getInstance().queryAccountTransactions(ACC_NO);
        SortedList<Transaction> sortedList = new SortedList<Transaction>(transactions, new Comparator<>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                return o1.getDatetime().compareTo(o2.getDatetime());
            }
        });

        listTransactions.setItems(sortedList);
        listTransactions.setCellFactory(new Callback<ListView<Transaction>, ListCell<Transaction>>() {
            @Override
            public ListCell<Transaction> call(ListView<Transaction> transactionListView) {
                ListCell<Transaction> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(Transaction transaction, boolean b) {
                        super.updateItem(transaction, b);
                        if(b) {
                            setText(null);
                        } else {
                            setText(transaction.getType().toString() + " : " + transaction.getAmount() + " | Date: " + transaction.getDatetime());
                            if(transaction.getAmount() < 0) {
                                setTextFill(Color.RED);
                            } else {
                                setTextFill(Color.GREEN);
                            }
                        }
                    }
                };
                return cell;
            }
        });
        double interest = 0;
        if (ACC_TYPE.equals("INTERESTBEARING")) {
            interest = 0.05;
        } else if(ACC_TYPE.equals("SAVINGS")) {
            interest = 0.0005;
        }
        double growth = (ACC_BALANCE * interest) + ACC_BALANCE;
        String details = "Account No: " + ACC_NO + "\t\t Account Type: " + ACC_TYPE +
                "\n\n\n" +
                "Current Balance: P" + ACC_BALANCE + "0" + "\n" +
                "Interest Rate: " + interest * 100 + "%" + "\n" +
                "Next Months Projected Balance: P" + growth + "0";
        HelperMethods.showAlert("INFORMATION", "Account Statement", details);
    }

    @FXML
    public void deposit() {
        if(depositAmount.getText().isEmpty()) {
            HelperMethods.showAlert("ERROR", "Empty Field", "Enter a value to proceed");
            return;
        }
        if(DataSource.getInstance().updateBalance(ACC_NO, Double.parseDouble(depositAmount.getText()))) {
            HelperMethods.showAlert("INFORMATION", "Success", "Deposit of " + depositAmount.getText() + " successful.");
            int oldBalance = (int) Double.parseDouble(txtBalance.getText().replaceAll("[^0-9|.]", ""));
            double newAmount = (double) oldBalance + Double.parseDouble(depositAmount.getText());
            txtBalance.setText("Balance: P" + newAmount + "0");
            setAccBalance(newAmount);
            depositAmount.clear();
//            int accountNo, int customerID, TransactionType type, double amount, Timestamp datetime
//            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//            double amount = Double.parseDouble(depositAmount.getText());
//            Transaction newTransaction = new Transaction(ACC_NO, ACC_OWNER.getCustomerId(), Transaction.TransactionType.DEPOSIT, amount, timestamp);
//            listTransactions.getItems().
//            ObservableList<Transaction> transactions = FXCollections.observableArrayList(DataSource.getInstance().queryAccountTransactions(ACC_NO));
//            listTransactions.getItems().setAll(transactions);
//            depositAmount.clear();
        } else {
            HelperMethods.showAlert("ERROR", "Transaction Failed", "Could not deposit funds, check value and try again");
        }

    }

    @FXML
    public void withdraw() {
        if(withdrawAmount.getText().isEmpty()) {
            HelperMethods.showAlert("ERROR", "Empty Field", "Enter a value to proceed");
            return;
        }
        if(DataSource.getInstance().updateBalance(ACC_NO, -Double.parseDouble(withdrawAmount.getText()))) {
            HelperMethods.showAlert("INFORMATION", "Success", "Withdrawal of " + withdrawAmount.getText() + " successful.");
            int oldBalance = (int) Double.parseDouble(txtBalance.getText().replaceAll("[^0-9|.]", ""));
            double newAmount = (double) oldBalance - Double.parseDouble(withdrawAmount.getText());
            txtBalance.setText("Balance: P" + newAmount + "0");
            setAccBalance(newAmount);
            withdrawAmount.clear();
            listTransactions.refresh();
        } else {
            HelperMethods.showAlert("ERROR", "Transaction Failed", "Could not withdraw funds, check value and try again");
        }
    }

    public void showAccountDetails() {
        String details = "Account Number: " + ACC_NO + "\n" +
                "Account Holder: " + ACC_OWNER.getFirstName() + " " + ACC_OWNER.getSurname() + "\n" +
                "Account Type: " + ACC_TYPE + "\n" +
                "Current Balance: P" + ACC_BALANCE + "0" + "\n" +
                "Pin Number: " + ACC_PIN + "\n";
        HelperMethods.showAlert("INFORMATION", null, details);
    }

    public void createNewAccount() throws SQLException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.setTitle("Add an account");
        dialog.setWidth(150);
        dialog.setHeight(200);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/chinyangatl/github/view/createAccountDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            CreateAccountController controller = fxmlLoader.getController();
            controller.addAccount();
        }else {
            System.out.println("cancel pressed");
        }
    }

    public void removeAccount() throws IOException {
//        Alert alert = showAlert("CONFIRMATION", "Delete Account", "Are You Sure? This process is irreversible");
//        alert.setTitle("Remove Account");
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.OK) {
//            if(DataSource.getInstance().removeAccount(ACC_NO)) {
//                showAlert("INFORMATION", null, "Account Deleted");
//            } else {
//                showAlert("ERROR", "Something went wrong", "Couldn't delete account");
//            }
//        }
        if(DataSource.getInstance().removeAccount(ACC_NO)) {
            HelperMethods.showAlert("INFORMATION", null, "Account Deleted");
            logout();
        } else {
            HelperMethods.showAlert("ERROR", "Something went wrong", "Couldn't delete account");
        }
    }

    @FXML
    public void updatePinDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.setTitle("Change Pin");
        dialog.setWidth(150);
        dialog.setHeight(200);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/chinyangatl/github/view/updatePin.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            UpdatePinController controller = fxmlLoader.getController();
            controller.changePin();
        }else {
            System.out.println("cancel pressed");
        }
    }

    @FXML
    public void logout() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/chinyangatl/github/view/login.fxml")));
        Stage window = (Stage) btnLogout.getScene().getWindow();
        window.setTitle("Welcome To Bankist!");
        window.setScene(new Scene(root, 600, 375));
        window.setResizable(false);
        window.show();
    }
}
