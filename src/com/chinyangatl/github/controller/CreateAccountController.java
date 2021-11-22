package com.chinyangatl.github.controller;

import com.chinyangatl.github.dataSource.DataSource;
import com.chinyangatl.github.model.Account;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import com.chinyangatl.github.utils.HelperMethods;

import java.sql.SQLException;

/**
 * The Controller Class for the createAccountDialog.fxml file which represents the Customer's sign up page..
 *
 * @author Lesley Tsame Chinyanga, cse20-030
 * @version 1.1
 */
public class CreateAccountController {
    @FXML
    private ToggleGroup accountTypeGroup;
    @FXML
    private TextField txtEnterEmployment;

    public void initialize() {

    }
    public void addAccount() throws SQLException {
        txtEnterEmployment.setOpacity(0);
        if(DashboardController.ACC_OWNER.getEmployer().equals("")) {
            txtEnterEmployment.setOpacity(1);
        }
        RadioButton selected = (RadioButton) accountTypeGroup.getSelectedToggle();

        Account newAccount = DataSource.getInstance().insertAccount(Account.AccountType.valueOf(selected.getText()), DashboardController.ACC_OWNER);
        if(newAccount != null) {
            HelperMethods.showAlert("INFORMATION", "Successfully created new account", "NEW ACCOUNT CREDENTIALS!\n\nAccount No: " + newAccount.getAccountNo() + "\nPin No: " + newAccount.getPinNo() + "\n\nUse those credentials to log in" );
        } else {
            HelperMethods.showAlert("ERROR", "Something went wrong", "Could not create account, try again later");
        }
    }
}
