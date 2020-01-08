package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditCustomerFormController implements Initializable {

    private Controller parentController;
    private Customer customer;
    private boolean memberStatus;

    @FXML private TextField customerName;
    @FXML private TextField customerPhone;
    @FXML private TextField customerEmail;
    @FXML private ComboBox customerStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        customerStatus.getItems().addAll("Non-Member", "Member");
    }

    public void initData(Controller parentController, Customer customer){
        this.parentController = parentController;
        this.customer = customer;
        customerName.setText(customer.getName());
        customerPhone.setText(customer.getPhoneNo());
        customerEmail.setText(customer.getEmail());
        customerStatus.setPromptText(customerStatus.getItems().get(0).toString());
        memberStatus = false;
        if (customer.getMember().equals("Member")){
            customerStatus.setPromptText(customerStatus.getItems().get(1).toString());
            memberStatus = true;
        }
    }

    @FXML
    public void editCustomer(ActionEvent event){
        boolean flag = true;
        if (customerName.getText().equals("") || customerPhone.getText().equals("") || customerEmail.getText().equals("")){
            // Validation with alert box
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Please Fullfil All Data!");
            alert.setContentText("Data is still not completed, please fullfil all data.");

            alert.showAndWait();
            flag = false;
        }

        if (!customerName.getText().matches("[a-zA-Z][a-zA-Z\\s]+")){
            // Validation with alert box
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Customer Name must be alphabetical");
            alert.setContentText("Please fill in customer name with only alphabetical characters and spaces!");

            alert.showAndWait();
            flag = false;
        }

        if (!customerPhone.getText().matches("^[\\d]{10,16}$")){
            // Validation with alert box
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Phone No must be 10 - 16 digits");
            alert.setContentText("Please fill in phone no with only numbers; 10 - 16 digits!");

            alert.showAndWait();
            flag = false;
        }

        if (!customerEmail.getText().matches("(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$)")){
            // Validation with alert box
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Wrong Email Format");
            alert.setContentText("Please fill in the email correctly!");

            alert.showAndWait();
            flag = false;
        }

        if (!flag) {
            return;
        }

        if (!customerStatus.getSelectionModel().isEmpty()){
            memberStatus = customerStatus.getValue().toString().equals("Member");
        }
        Database.editCustomer(customer.getCustomerID(), customerName.getText(), customerPhone.getText(), customerEmail.getText(), memberStatus);
        System.out.println(String.format("Edited %s's data", customerName.getText()));

        // Close Stage & Refresh Table
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        parentController.RefreshCustomerTable();
    }

}
