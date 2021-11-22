package com.chinyangatl.github.controller;

import com.chinyangatl.github.model.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.chinyangatl.github.dataSource.DataSource;
import com.chinyangatl.github.model.Customer;
import com.chinyangatl.github.utils.HelperMethods;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * The Controller Class for the login.fxml file which represents the Customer's login page.
 *
 * @author Lesley Tsame Chinyanga, cse20-030
 * @version 1.0
 */
public class Controller {
    // login controls
    @FXML
    private TextField txtAccountNo;
    @FXML
    private TextField txtPinNo;
    @FXML
    private Button btnLogin;
    // sign up controls
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtSurname;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtEmployer;
    @FXML
    private Spinner inputAge;
    @FXML
    private ChoiceBox inputAccountType;
    @FXML
    private Button btnSignup;
    @FXML
    private Label txtNameErr;
    @FXML
    private Label txtSurnameErr;
    @FXML
    private Label txtAddressErr;
    @FXML
    private Label txtAgeErr;
    @FXML
    private Label txtAccountErr;

    public void initialize() {
        HelperMethods.alterTextFieldToNumbersOnly(txtPinNo);
    }

    /**
     * Facilitates login by calling the JDBC login method to compare credentials.
     * Event handler attached in the fxml.
     * @throws NumberFormatException
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void login() throws NumberFormatException, IOException, SQLException {
        if(txtAccountNo.getText().isEmpty() || txtPinNo.getText().isEmpty()) {
            throwError("No fields can be empty");
        }
        Account account = DataSource.getInstance().login(Integer.parseInt(txtAccountNo.getText()), Integer.parseInt(txtPinNo.getText()));

        if (account == null) {
            throwError("Credentials are incorrect, please try again");
        } else {
            goToDashboard(account);
        }
    }

    public static void throwError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    public void signup() throws SQLException, IOException {
        txtNameErr.setOpacity(0);
        txtSurnameErr.setOpacity(0);
        txtAgeErr.setOpacity(0);
        txtAddressErr.setOpacity(0);
        txtAccountErr.setOpacity(0);

        if(txtFirstName.getText().isEmpty() || txtSurname.getText().isEmpty() || txtAddress.getText().isEmpty() || inputAge.getValue() == null || inputAccountType.getValue() == null) {
            throwError("Marked fields cannot be empty");
            txtNameErr.setOpacity(1);
            txtSurnameErr.setOpacity(1);
            txtAgeErr.setOpacity(1);
            txtAddressErr.setOpacity(1);
            txtAccountErr.setOpacity(1);
        } else if(inputAccountType.getValue().equals("Cheque") && txtEmployer.getText().isEmpty()) {
            throwError("To make a Cheque account, you must fill in employer field");
        } else {
            Customer customer = createCustomer();
            DataSource.getInstance().insertCustomer(customer);
            Account account = DataSource.getInstance().insertAccount(Account.AccountType.valueOf((String) inputAccountType.getValue().toString().toUpperCase()), customer);
            HelperMethods.showAlert("INFORMATION", "Account Created Successfully", "To find Account No and Pin No click 'My Account Details'");
            goToDashboard(account);
        }


    }

    public Customer createCustomer() {
        Customer customer = new Customer();
        customer.setFirstName(txtFirstName.getText());
        customer.setSurname(txtSurname.getText());
        customer.setAddress(txtAddress.getText());
        customer.setAge((Integer) inputAge.getValue());
        if (!txtAddress.getText().isEmpty()) {
            customer.setEmployer(txtEmployer.getText());
        } else {
            customer.setEmployer(null);
        }
        return customer;
    }

    public void goToDashboard(Account account) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chinyangatl/github/view/dashboard.fxml"));
        Parent root = loader.load();

        DashboardController dashboardController = loader.getController();
        dashboardController.initialize(account);
        //Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
        Stage window = (Stage) btnLogin.getScene().getWindow();
        window.setTitle("Your Dashboard, " + account.getOwner().getFirstName());
        window.setScene(new Scene(root, 715, 486));
        window.setResizable(false);
    }

    public void switchToAdminLogin() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/chinyangatl/github/view/adminLogin.fxml")));
        Stage window = (Stage) btnLogin.getScene().getWindow();
        window.setTitle("ADMIN LOGIN");
        window.setScene(new Scene(root, 600, 375));
        window.setResizable(false);
    }
}
