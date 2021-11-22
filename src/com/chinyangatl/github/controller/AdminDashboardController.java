package com.chinyangatl.github.controller;

import com.chinyangatl.github.dataSource.DataSource;
import com.chinyangatl.github.model.Account;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import com.chinyangatl.github.model.Customer;
import com.chinyangatl.github.utils.HelperMethods;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;

/**
 * The Controller Class for the adminDashboard.fxml file which represents the Admin's homepage.
 *
 * @author Lesley Tsame Chinyanga, cse20-030
 * @version 1.1
 */
public class AdminDashboardController {
    @FXML
    private Button btnLogout;
    @FXML
    private TextField txtWithdraw, txtDeposit, txtFindAccount;
    @FXML
    private TextField txtFirstName, txtSurname, txtAddress, txtEmployer, txtAge;
    @FXML
    private TextField txtCustomerID, txtAddEmployer;
    @FXML
    private Label txtAccountNoDetail, txtAccountTypeDetail, txtAccountBalance;
    @FXML
    private Label labelCustomerFound, labelCustomerFirstName, labelCustomerSurname;
    @FXML
    private Button btnDeposit, btnWithdraw, btnAddCustomer, btnAddAccount, btnFindCustomer;
    @FXML
    private ToggleGroup accountTypeGroup;
    @FXML
    private ListView<Customer> listView;
    @FXML
    private ListView<Account> listViewAccounts;
    @FXML
    private Button btnAllCustomers, btnAllAccounts, btnRemoveAccount, btnRemoveCustomer;

    private Account account = null;
    private Customer customer = null;

    public HelperMethods helperMethods = new HelperMethods();
    public AdminLoginController adminLoginController = new AdminLoginController();
    public Controller controller = new Controller();

    public void initialize() {
        HelperMethods.alterTextFieldToNumbersOnly(txtDeposit);
        HelperMethods.alterTextFieldToNumbersOnly(txtWithdraw);
        HelperMethods.alterTextFieldToNumbersOnly(txtFindAccount);
        HelperMethods.alterTextFieldToNumbersOnly(txtCustomerID);
        txtAccountNoDetail.setOpacity(0);
        txtAccountTypeDetail.setOpacity(0);
        txtAccountBalance.setOpacity(0);
        if(txtFindAccount.getText().isEmpty()) {
            txtDeposit.setDisable(true);
            txtWithdraw.setDisable(true);
            btnDeposit.setDisable(true);
            btnWithdraw.setDisable(true);
            btnRemoveAccount.setDisable(true);
        }
        txtFindAccount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (newValue.isEmpty()) {
                    txtDeposit.setDisable(true);
                    txtWithdraw.setDisable(true);
                    btnDeposit.setDisable(true);
                    btnWithdraw.setDisable(true);
                    btnRemoveAccount.setDisable(true);
                    txtAccountNoDetail.setOpacity(0);
                    txtAccountTypeDetail.setOpacity(0);
                    txtAccountBalance.setOpacity(0);
                }
            }
        });

        labelCustomerFirstName.setOpacity(0);
        labelCustomerSurname.setOpacity(0);
        labelCustomerFound.setOpacity(0);
        accountTypeGroup.getToggles().forEach(toggle -> {
            Node node = (Node) toggle;
            node.setDisable(true);
        });
        btnAddAccount.setDisable(true);
        btnRemoveCustomer.setDisable(true);

        txtCustomerID.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if( newValue.isEmpty()) {
                    labelCustomerFirstName.setOpacity(0);
                    labelCustomerSurname.setOpacity(0);
                    labelCustomerFound.setOpacity(0);
                    accountTypeGroup.getToggles().forEach(toggle -> {
                        Node node = (Node) toggle;
                        node.setDisable(true);
                    });
                    btnAddAccount.setDisable(true);
                    btnRemoveCustomer.setDisable(true);
                }
            }
        });

        listView.setVisible(true);
        listViewAccounts.setVisible(false);

        btnAllCustomers.setOnAction(e -> showAllCustomers());
        btnAllAccounts.setOnAction(e -> showAllAccounts());

        btnFindCustomer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Customer retrievedCustomer = DataSource.getInstance().findCustomer(Integer.parseInt(txtCustomerID.getText()));
                    System.out.println(retrievedCustomer);
                    if(retrievedCustomer == null) {
                        HelperMethods.showAlert("ERROR", "No Customer Found", "No customer holds ID: " + txtCustomerID.getText());
                        return;
                    } else {
                        if(!retrievedCustomer.getEmployer().isEmpty()) {
                            txtAddEmployer.setOpacity(0);
                        }
                        labelCustomerFound.setOpacity(1);
                        labelCustomerFirstName.setOpacity(1);
                        labelCustomerFirstName.setText(retrievedCustomer.getFirstName());
                        labelCustomerSurname.setOpacity(1);
                        labelCustomerSurname.setText(retrievedCustomer.getSurname());
                        accountTypeGroup.getToggles().forEach(toggle -> {
                            Node node = (Node) toggle;
                            node.setDisable(false);
                        });
                        btnAddAccount.setDisable(false);
                        btnRemoveCustomer.setDisable(false);
                        customer = retrievedCustomer;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        btnLogout.setOnAction(event -> {
            try {
                logout();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnDeposit.setOnAction(e -> {
                    if(DataSource.getInstance().updateBalance(account.getAccountNo(), Integer.parseInt(txtDeposit.getText()))) {
                        HelperMethods.showAlert("INFORMATION", "Success", txtDeposit.getText() + " deposited" );
                        double newBalance = Integer.parseInt(txtDeposit.getText()) + account.getBalance();
                        txtAccountBalance.setText("Balance: P" + newBalance + "0");
                        txtDeposit.clear();
                    }
                });

        btnWithdraw.setOnAction(e -> {
            if(DataSource.getInstance().updateBalance(account.getAccountNo(), -Integer.parseInt(txtWithdraw.getText()))) {
                HelperMethods.showAlert("INFORMATION", "Success", txtWithdraw.getText() + " withdrawn" );
                double newBalance = account.getBalance() - Integer.parseInt(txtWithdraw.getText());
                txtAccountBalance.setText("Balance: P" + newBalance + "0");
                txtWithdraw.clear();
            }
        });

        btnAddCustomer.setOnAction(e -> {
            if(txtFirstName.getText().isEmpty() || txtSurname.getText().isEmpty() || txtAddress.getText().isEmpty() || txtEmployer.getText().isEmpty() || txtAge.getText().isEmpty()) {
                HelperMethods.showAlert("ERROR", "Empty Fields", "Please fill in necessary details");
                return;
            }
            Customer customer = new Customer();
            customer.setFirstName(txtFirstName.getText());
            customer.setSurname(txtSurname.getText());
            customer.setAddress(txtAddress.getText());
            customer.setAge(Integer.parseInt(txtAge.getText()));
            if (!txtAddress.getText().isEmpty()) {
                customer.setEmployer(txtEmployer.getText());
            } else {
                customer.setEmployer(null);
            }
            try {
                int customerID = DataSource.getInstance().insertCustomer(customer);
                HelperMethods.showAlert("INFORMATION", "Customer Added", "Successfully added: \n" +
                        txtFirstName.getText() + " " + txtSurname.getText() + ", Customer ID: " + customerID);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        btnRemoveCustomer.setOnAction(e -> {
            DataSource.getInstance().removeCustomer(Integer.parseInt(txtCustomerID.getText()));
            HelperMethods.showAlert("INFORMATION", "Customer Removed", "Customer ID: " + txtCustomerID.getText() + " Removed");
        });

        btnAddAccount.setOnAction(e -> {
            RadioButton selected = (RadioButton) accountTypeGroup.getSelectedToggle();

            try {
                Account account = DataSource.getInstance().insertAccount(Account.AccountType.valueOf(selected.getText()), customer);
                HelperMethods.showAlert("INFORMATION", "Account Created ", account.getAccountType() + "account with Account No: " + account.getAccountNo() + " and Pin No: " + account.getPinNo() + ", created for " + customer.getFirstName() + " " + customer.getSurname() + " of Customer ID " + customer.getCustomerId());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        btnRemoveAccount.setOnAction(e -> {
            DataSource.getInstance().removeAccount(Integer.parseInt(txtFindAccount.getText()));
            HelperMethods.showAlert("INFORMATION", "Account Removed", "Account No: " + txtFindAccount.getText() + " Removed");
        });

//        btnAddAccount.setOnAction(e -> {
//            if(txtCustomerID.getText().isEmpty()) {
//                HelperMethods.showAlert("ERROR", "Empty Field", "Customer ID cannot be left blank");
//                return;
//            }
//            if(accountTypeGroup.getProperties().isEmpty()) {
//                HelperMethods.showAlert("ERROR", null, "Please make sure account is selected");
//            }
//        });
    }

    public void findAccount() {
        if(txtFindAccount.getText().isEmpty()) {
            HelperMethods.showAlert("ERROR", "Blank field", "Account field cannot be empty");
            return;
        }
        Account retrievedAccount = DataSource.getInstance().findAccount(Integer.parseInt(txtFindAccount.getText()));
        if(retrievedAccount == null) {
            HelperMethods.showAlert("ERROR", "Couldn't find account", "No account with Account Number " + txtFindAccount.getText());
            txtDeposit.setDisable(true);
            txtWithdraw.setDisable(true);
            btnDeposit.setDisable(true);
            btnWithdraw.setDisable(true);
            btnRemoveAccount.setDisable(true);
        } else {
            account = retrievedAccount;
            if(account.getAccountType() == "SAVINGS") {
                txtDeposit.setDisable(false);
                btnDeposit.setDisable(false);
                btnRemoveAccount.setDisable(false);
            } else {
                txtDeposit.setDisable(false);
                txtWithdraw.setDisable(false);
                btnDeposit.setDisable(false);
                btnWithdraw.setDisable(false);
                btnRemoveAccount.setDisable(false);
            }
            txtAccountNoDetail.setOpacity(1);
            txtAccountNoDetail.setText("Acc No: " + account.getAccountNo() + " | Owner: " + account.getOwner().getSurname() + ", " + account.getOwner().getFirstName());
            txtAccountTypeDetail.setOpacity(1);
            txtAccountTypeDetail.setText(account.getAccountType());
            txtAccountBalance.setOpacity(1);
            txtAccountBalance.setText("Balance: P" + account.getBalance() + "0");
        }
    }

    public void logout() throws IOException {
        switchScenes("/com/chinyangatl/github/view/login.fxml", "Welcome To Bankist", 600, 375, btnLogout);
    }

    /*
    public void showAllAccounts() {
        if(tableViewCustomers.isVisible()) {
            tableViewCustomers.setVisible(false);
        }
        tableView.setVisible(true);

//        accNo.setCellValueFactory(new PropertyValueFactory<>("accountNo"));
//        accType.setCellValueFactory(new PropertyValueFactory<>("accountType"));
//        accBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
//        accHolder.setCellValueFactory(new PropertyValueFactory<>("owner"));


        //ObservableList<Account> list = getAccountList();
        tableView.setItems(getAccountList());
//        //tableView.getColumns().addAll();
//        tableView.getColumns().addAll(accNo, accType, accBalance, accHolder);

//        accNo.setSortType(TableColumn.SortType.DESCENDING);
    }

     */

    /*
    private ObservableList<Account> getAccountList() {
        ObservableList<Account> list = FXCollections.observableArrayList(DataSource.getInstance().queryAccounts());
        List<Account> accounts = DataSource.getInstance().queryAccounts();
        System.out.println(accounts.size());
        System.out.println(list.size());
        for(int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).toString());
        }
        return list;
    }

    public void showAllCustomers() {
        if(tableView.isVisible()) {
            tableView.setVisible(false);
        }
        tableViewCustomers.setVisible(true);

        tableViewCustomers = new TableView<Customer>();

        TableColumn<Customer, String> customerID = new TableColumn<Customer, String>("Customer ID");
        TableColumn<Customer, String> cusFirstName = new TableColumn<Customer, String>("First Name");
        TableColumn<Customer, String> cusLastName = new TableColumn<Customer, String>("Surname");
        TableColumn<Customer, String> cusAge = new TableColumn<Customer, String>("Age");
        TableColumn<Customer, String> cusEmployer = new TableColumn<Customer, String>("Employer");
        TableColumn<Customer, String> cusAddress = new TableColumn<Customer, String>("Address");
    }

     */
    public void switchScenes(String scene, String title, int width, int height, Node node) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(scene)));
        Stage window = (Stage) node.getScene().getWindow();
        window.setTitle(title);
        window.setScene(new Scene(root, width, height));
        window.setResizable(false);
        window.show();
    }

    public void showAllCustomers() {
        listViewAccounts.setVisible(false);
        ObservableList<Customer> customers = FXCollections.observableArrayList(DataSource.getInstance().queryCustomers());
        SortedList<Customer> sortedList = new SortedList<>(customers, new Comparator<Customer>() {
            @Override
            public int compare(Customer o1, Customer o2) {
                return o1.getSurname().compareTo(o2.getSurname());
            }
        });

        listView.setItems(sortedList);
        listView.setCellFactory(new Callback<ListView<Customer>, ListCell<Customer>>() {
            @Override
            public ListCell<Customer> call(ListView<Customer> customerListView) {
                ListCell<Customer> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(Customer customer, boolean b) {
                        super.updateItem(customer, b);
                        if(b) {
                            setText(null);
                        } else {
                            setText(customer.toString());
                        }
                    }
                };
                return cell;
            }
        });
        if(customers.size() == 0) {
            listView.setPlaceholder(new Label("Empty List"));
        }
        listView.setVisible(true);
    }

    public void showAllAccounts() {
        listView.setVisible(false);
        ObservableList<Account> accounts = FXCollections.observableArrayList(DataSource.getInstance().queryAccounts());
        SortedList<Account> sortedList = new SortedList<>(accounts, new Comparator<Account>() {
            @Override
            public int compare(Account o1, Account o2) {
                return o1.getOwner().getSurname().compareTo(o2.getOwner().getSurname());
            }
        });

        listViewAccounts.setItems(sortedList);
        listViewAccounts.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>() {
            @Override
            public ListCell<Account> call(ListView<Account> listView) {
                ListCell<Account> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(Account account, boolean b) {
                        super.updateItem(account, b);
                        if(b) {
                            setText(null);
                        } else {
                            setText(account.toString());
                        }
                    }
                };
                return cell;
            }
        });
        if(accounts.size() == 0) {
            listView.setPlaceholder(new Label("Empty List"));
        }
        listViewAccounts.setVisible(true);
    }
}



//    ObservableList<Account> accounts = FXCollections.observableArrayList(DataSource.getInstance().queryAccounts());
//    SortedList<Account> sortedList = new SortedList<Account>(accounts, new Comparator<>() {
//        @Override
//        public int compare(Account o1, Account o2) {
//            String o1AccNo = String.valueOf(o1.getAccountNo());
//            String o2AccNo = String.valueOf(o2.getAccountNo());
//            return o1AccNo.compareTo(o2AccNo);
//        }
//    });
//        if(sortedList.isEmpty()) System.out.println("empty");
//                else {
//                System.out.println(sortedList.get(0).toString());
//                listView.setItems(sortedList);
//                listView.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>() {
//@Override
//public ListCell<Account> call(ListView<Account> listView) {
//        ListCell<Account> cell = new ListCell<>() {
//@Override
//protected void updateItem(Account account, boolean b) {
//        super.updateItem(account, b);
//        if(b) {
//        setText(null);
//        } else {
//        setText("Acc No: " + account.getAccountNo() +
//        "Acc Holder: " + account.getOwner().getFirstName() + " " + account.getOwner().getSurname() +
//        "Acc Type: " + account.getAccountType() +
//        "Acc Balance: " + account.getBalance());
//        }
//        }
//        };
//        return cell;
//        }
//        });
//        }