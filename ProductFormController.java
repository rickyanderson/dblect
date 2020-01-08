package sample;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProductFormController implements Initializable {

    private Controller parentController;

    private int prevProductID;
    private String newProductID;

    @FXML private TextField productName;
    @FXML private TextField productType;
    @FXML private TextField productPrice;

    @Override
    public void initialize(URL url, ResourceBundle rb){}

    public void initData(Controller parentController, String prevProductID){
        // Set parentController;
        this.parentController = parentController;
        // Remove alphabetic char and get integer value from latest customer/member
        this.prevProductID = Integer.parseInt(prevProductID.replaceAll("[^\\d.]", ""));
        // Make new CustomerID
        newProductID = String.format("PRO%05d", this.prevProductID+1);
        System.out.println(newProductID);
        // Bind productType TextField
        bindTypeTextFields();
    }

    private void bindTypeTextFields(){
        ArrayList<String> TypeLists = parentController.getProductTypeList();
        TypeLists.remove(0);
        TextFields.bindAutoCompletion(productType, TypeLists);
    }

    @FXML
    public void addProduct(ActionEvent event) throws SQLException {
        System.out.println("Add_Product_Button clicked in ProductForm.fxml");

        if (productName.getText().equals("") || productPrice.getText().equals("") || productType.getText().equals("")){
            // Validation with alert box
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Please Fullfil All Data!");
            alert.setContentText("Data is still not completed, please fullfil all data.");

            alert.showAndWait();
            return;
        }

        // Store type of product
        String ProductType = productType.getText();

        try {
            // If ProductType does not exists
            if (!Database.isProductTypeExist(ProductType)){
                // Make new TypeID
                int PrevTypeID = Integer.parseInt(Database.getLastTypeID().replaceAll("[^\\d.]", ""));
                String NewTypeID = String.format("TYP%05d", PrevTypeID+1);
                System.out.println(String.format("New TypeID (%s)", NewTypeID));
                Database.addProductType(NewTypeID, ProductType);
                parentController.RefreshProductFilter();
            }

            Database.addProduct(newProductID, productName.getText(), Database.getTypeID(ProductType), Integer.parseInt(productPrice.getText()));

            // Close Stage & Refresh Table
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            parentController.RefreshProductTable();
        } catch (NumberFormatException e){
            // Validation with alert box
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Price was filled with the Wrong Format");
            alert.setContentText("Price have wrong format, please fullfil with correct format. \n(e.g. 100000)");

            alert.showAndWait();
        }
    }
}
