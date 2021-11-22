package com.chinyangatl.github.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import com.chinyangatl.github.dataSource.DataSource;
import com.chinyangatl.github.utils.HelperMethods;

public class UpdatePinController {
    @FXML
    private TextField txtNewPin;
    @FXML
    private TextField txtNewPinConfirm;

    /**
     * The Controller Class for the updatePin.fxml file which represents the dialog for a Customer to change their Account's pin number.
     *
     * @author Lesley Tsame Chinyanga, cse20-030
     * @version 1.1
     */
    public void changePin() {
        int accountNo = DashboardController.ACC_NO;
        if(txtNewPin.getText().isEmpty() || txtNewPinConfirm.getText().isEmpty()) {
            HelperMethods.showAlert("ERROR", "Something went wrong", "Cannot have empty values");
            return;
        }
        if(!txtNewPin.getText().equals(txtNewPinConfirm.getText())) {
            HelperMethods.showAlert("ERROR", "Something went wrong", "Values do not match");
            return;
        }
        if(txtNewPin.getText().length() > 4 || txtNewPinConfirm.getText().length() > 4) {
            HelperMethods.showAlert("ERROR", "Something went wrong", "Pin must be four digits long");
            return;
        }
        if(txtNewPin.getText().equals(txtNewPinConfirm.getText())) {
            if(Integer.parseInt(txtNewPin.getText()) < 1000) {
                HelperMethods.showAlert("ERROR", "Something went wrong", "Due to system design, pin number cannot be less than 1000");
                return;
            }
            DataSource.getInstance().updatePin(accountNo, Integer.parseInt(txtNewPin.getText()));
            //DashboardController.setAccPin(Integer.parseInt(txtNewPin.getText()));
            HelperMethods.showAlert("INFORMATION", null, "Pin Changed Successfully to " + txtNewPin.getText());
        }
        else {
            HelperMethods.showAlert("ERROR", "Something went wrong", "Try Again Later");
        }
    }
}
