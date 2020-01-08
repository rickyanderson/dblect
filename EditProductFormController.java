package sample;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import javax.xml.crypto.Data;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditProductFormController implements Initializable {

    private Controller parentController;
    private Product selectedProduct;

    @FXML private TextField productName;
    @FXML private TextField productType;
    @FXML private TextField productPrice;

    @Override
    public void initialize(URL url, ResourceBundle rb){}

    public void initData(Controller parentController, Product selectedProduct){
        this.parentController = parentController;
        this.selectedProduct = selectedProduct;
        productName.setText(this.selectedProduct.getProductName());
        productType.setText(this.selectedProduct.getType());
        productPrice.setText(String.valueOf(this.selectedProduct.getPrice()));
        bindTypeTextFields();
    }

    private void bindTypeTextFields(){
        ArrayList<String> TypeLists = parentController.getProductTypeList();
        TypeLists.remove(0);
        TextFields.bindAutoCompletion(productType, TypeLists);
    }

    @FXML
    public void editProduct(ActionEvent event) throws SQLException {
        System.out.println("Edit_Product_Button clicked in EditProductForm.fxml");

        if (productName.getText().equals("") || productPrice.getText().equals("") || productType.getText().equals("")){
            // Validation with alert box
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Please Fullfil All Data!");
            alert.setContentText("Data is still not completed, please fill in all of the data.");

            alert.showAndWait();
            return;
        }

        // If ProductType does not exists
        if (!Database.isProductTypeExist(productType.getText())){
            // Make new TypeID
            int PrevTypeID = Integer.parseInt(Database.getLastTypeID().replaceAll("[^\\d.]", ""));
            String NewTypeID = String.format("TYP%05d", PrevTypeID+1);
            System.out.println(String.format("New TypeID (%s)", NewTypeID));
            Database.addProductType(NewTypeID, productType.getText());
            parentController.RefreshProductFilter();
        }

        // Query edits to the database
        try {
            Database.editProduct(selectedProduct.getProductID(), productName.getText(), Database.getTypeID(productType.getText()), Integer.parseInt(productPrice.getText()));
            System.out.println(String.format("Edited %s at the products", productName.getText()));

            // Checks if previous TypeID still in products
            if (!(Database.isProductTypeExistInProduct(Database.getTypeID(selectedProduct.getType())))){
                Database.deleteProductTypeByType(selectedProduct.getType());
                parentController.RefreshProductFilter();
            }

            // Close Stage & Refresh Table
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            parentController.RefreshProductTable();
        } catch (NumberFormatException e){
            // Validation with alert box
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Price was filled with the Wrong Format");
            alert.setContentText("Price have wrong format, please fill in with the correct format. \n(e.g. 100000)");

            alert.showAndWait();
        }
    }

}
