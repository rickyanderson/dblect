package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import pdfGeneration.Invoice;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DetailOrderController implements Initializable {
    private Controller parentController;
    private Order order;
    private File photoFile;

    @FXML private Label customerName;
    @FXML private Label orderID;
    @FXML private Label orderType;
    @FXML private Label orderDate;
    @FXML private Label deliveryDate;
    @FXML private Label deliveryTime;
    @FXML private Label orderStatus;
    @FXML private TextArea deliveryAddress;
    @FXML private Label subTotal;
    @FXML private Label deliveryCharge;
    @FXML private Label discount;
    @FXML private Label grandTotal;
    @FXML private TextField paid;
    @FXML private Label balanceDue;
    @FXML private TextField productName;
    @FXML private TextField productPrice;
    @FXML private TextField qty;
    @FXML private TextArea productDescription;
    @FXML private Label FileNameLabel;

    // Product List
    private ObservableList<Product> ProductList = FXCollections.observableArrayList();
    private ArrayList<String> ListofProductNames = new ArrayList<>();
    private Product selectedProduct;

    // Table Members
    @FXML private TableView<SubOrder> SubOrderTable;
    @FXML private TableColumn<SubOrder, Integer> noCol;
    @FXML private TableColumn<SubOrder, String> productNameCol;
    @FXML private TableColumn<SubOrder, Integer> qtyCol;
    @FXML private TableColumn<SubOrder, Integer> priceCol;
    @FXML private ObservableList<SubOrder> SubOrderList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb){
        noCol.setCellValueFactory(new PropertyValueFactory<>("ColNo"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("ProductName"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("Qty"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        resetResourcesDir();

    }

    private void resetResourcesDir(){
        File directory = new File("src/resources");
        File[] files = directory.listFiles();
        for ( File file : files) {
            if (!file.delete()){
                System.out.println("Failed to delete " + file);
            }
        }
    }

    public void initData(Controller parentController, Order order, ObservableList<Product> ProductList){
        this.parentController = parentController;
        this.order = order;
        SubOrderList = Database.getSubOrderList(order.getOrderID());

        // Bind Product
        this.ProductList = ProductList;
        bindProductName();

        // Initialize text from selected Order
        customerName.setText(order.getCustomerName());
        orderID.setText(order.getOrderID());
        orderType.setText(order.getOrderType());
        orderDate.setText(String.valueOf(order.getOrderDate()));
        deliveryDate.setText(order.getDeliveryDateTime().toLocalDate().toString());
        deliveryTime.setText(order.getDeliveryDateTime().toLocalTime().toString());
        orderStatus.setText(order.getOrderStatus());
        deliveryAddress.setText(order.getDeliveryAddress());
        deliveryCharge.setText(String.valueOf(order.getDeliveryPrice()));
        discount.setText(String.valueOf(order.getDiscount()));
        paid.setText(String.valueOf(order.getPayment()));

        RefreshSubOrderTable();
        calculatePaid();
    }

    private void bindProductName(){
        for (Product product : ProductList) {
            ListofProductNames.add(product.getProductName());
        }
        TextFields.bindAutoCompletion(productName, ListofProductNames);
    }

    @FXML
    public void ProductNameAction(){
        setSelectedProduct(productName.getText());
        productPrice.setText(String.valueOf(selectedProduct.getPrice()));
        System.out.println("Selected Product: " + selectedProduct.getProductName());
    }

    private void setSelectedProduct(String selectedProductName) {
        for (Product product : ProductList) {
            if (product.getProductName().equals(selectedProductName)){
                selectedProduct = product;
                return;
            }
        }
    }

    @FXML
    public void GenerateInvoice(ActionEvent event) throws InterruptedException, SQLException, IOException {
        editOrder(event);

        Invoice inv = new Invoice(order, SubOrderList);
        inv.makeInvoice();
    }

    @FXML
    public void addPhotoClicked() {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null){
            System.out.println("File Choosen Path = " + selectedFile.getAbsolutePath());
            System.out.println("File Choosen Name = " + selectedFile.getName());
            FileNameLabel.setText(selectedFile.getName());
            photoFile = selectedFile;
        } else {
            System.out.println("File is not valid");
        }
    }

    @FXML
    public void viewPhotoClicked() throws IOException {
        System.out.println("ViewPhotoButton Clicked on DetailOrder.fxml");
        InputStream input;

        // Get Selected Product
        SubOrder product = SubOrderTable.getSelectionModel().getSelectedItem();

        // Checks if SubOrder has Photo
        if (product.getDescriptionPhoto() == null){
            return;
        }

        // Checks if File in resources directory
        if (!product.LocalPhotoExists()){
            // Number of Files in resources dir
            int num;
            try {
                num = new File("src/resources").list().length;
            } catch (NullPointerException e) {
                num = 0;
            }

            // Set LocalPhotoID
            String LocalPhotoID = "JPG" + String.valueOf(num + 1);

            // Add LocalPhotoID to product object
            product.setLocalPhotoID(LocalPhotoID);

            // Set LocalPhoto PathName
            String pathName = "src/resources/" + LocalPhotoID + ".jpg";
            
            input = product.getDescriptionPhoto();
            OutputStream output = new FileOutputStream(new File(pathName));

            // Writing file
            byte[] content = new byte[1024];
            int size = 0;
            while ((size = input.read(content)) != -1){
                output.write(content, 0, size);
            }
            output.close();
            input.close();

            // Copy to DescriptionPhoto
            File copyPhoto = new File("src/resources/" + product.getLocalPhotoID() + ".jpg");
            product.setDescriptionPhoto(new FileInputStream(copyPhoto));
        }

        // Get LocalPhoto PathName
        String pathName = "file:src/resources/" + product.getLocalPhotoID() + ".jpg";

        Image image = new Image(pathName, 400, 600, true, true);
        ImageView imageView = new ImageView();
        imageView.setImage(image);

        BorderPane pane = new BorderPane();
        pane.setCenter(imageView);

        Scene scene = new Scene(pane);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    public void tableviewOnSelected() {
        System.out.println("TableView was selected");
        SubOrder product = SubOrderTable.getSelectionModel().getSelectedItem();
        setSelectedProduct(product.getProductName());
        productName.setText(product.getProductName());
        productPrice.setText(String.valueOf(product.getPrice()));
        qty.setText(String.valueOf(product.getQty()));
        productDescription.setText(product.getDescription());
        if (product.getDescriptionPhoto() != null) {
            FileNameLabel.setText("Photo Exist");
        } else {
            FileNameLabel.setText("No Photo Available");
        }
    }

    @FXML
    public void addItemClicked() throws FileNotFoundException {
        System.out.println("AddItemButton clicked on DetailOrderForm.fxml");
        int flag = 0;
        try {
            SubOrder subOrder = SubOrderTable.getSelectionModel().getSelectedItem();
            // If Product is Edited
            if (productInList(selectedProduct.getProductID())){
                // If Edited Product has previous Photo
                if (subOrder.getDescriptionPhoto() != null){
                    flag = 1;
                }
                SubOrderList.remove(SubOrderTable.getSelectionModel().getSelectedIndex());
            }
            // Either SubOrder previously has photo or it was a new product (without photo)
            if (photoFile == null){
                if (flag == 0){
                    // If new SubOrder without Photo
                    SubOrderList.add(new SubOrder(SubOrderList.size()+1, order.getOrderID(),selectedProduct.getProductID(), selectedProduct.getProductName(), Integer.parseInt(qty.getText()), productDescription.getText(), selectedProduct.getPrice()));
                } else if (flag == 1){
                    // If not new SubOrder & Has Previous Photo
                    SubOrder newsuborder = new SubOrder(subOrder.getColNo(), order.getOrderID(),selectedProduct.getProductID(), selectedProduct.getProductName(), Integer.parseInt(qty.getText()), productDescription.getText(), subOrder.getDescriptionPhoto(), selectedProduct.getPrice());
                    newsuborder.setLocalPhotoID(subOrder.getLocalPhotoID());
                    SubOrderList.add(newsuborder);
                }
            } else {
                // If SubOrder with new Photo (old/new SubOrder)
                SubOrderList.add(new SubOrder(SubOrderList.size()+1, order.getOrderID(), selectedProduct.getProductID(), selectedProduct.getProductName(), Integer.parseInt(qty.getText()), productDescription.getText(), new FileInputStream(photoFile), selectedProduct.getPrice()));
            }
            RefreshSubOrderTable();
            clearTextfields();
            photoFile = null;
        } catch (NullPointerException e){
            // Validation with alert box
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Please Choose The Correct Product!");
            alert.setContentText("Product not exist, please choose correct product.");

            alert.showAndWait();
        } catch (NumberFormatException e){
            // Validation with alert box
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Please Input The Correct Format of Qty!");
            alert.setContentText("Qty have wrong format, please fullfil with correct format. \n(e.g. Qty with number format.");

            alert.showAndWait();
        }
    }

    private boolean productInList(String ProductID) {
        for (SubOrder subOrder : SubOrderList) {
            if (subOrder.getProductID().equals(ProductID)) {
                return true;
            }
        }
        return false;
    }

    private void clearTextfields(){
        productName.clear();
        productPrice.clear();
        qty.clear();
        productDescription.clear();
        FileNameLabel.setText("Product Photo Status");
    }

    @FXML
    public void deleteItemClicked(){
        System.out.println("DeleteItemButton clicked on DetailOrderForm.fxml");
        SubOrderList.remove(SubOrderTable.getSelectionModel().getSelectedIndex());
        RefreshSubOrderTable();
    }

    @FXML
    public void calculatePaid(){
        try{
            int sTotal = 0;
            // Getting Grand Total value
            int gTotal = Integer.parseInt(grandTotal.getText());
            // Getting Paid value
            int Paid = Integer.parseInt(paid.getText());

            sTotal = gTotal - Paid;
            if(sTotal < 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Please Input The Correct Amount of Paid!");
                alert.setContentText("Paid have more amount than balance due, please fulfill with correct format. \n(e.g. Qty with number format.");

                alert.showAndWait();
            } else{
                balanceDue.setText(String.valueOf(sTotal));
            }
        } catch (NumberFormatException e){
            // Validation with alert box
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Please Input The Correct Format of Paid!");
            alert.setContentText("Paid have wrong format, please fulfill with correct format. \n(e.g. Qty with number format.");

            alert.showAndWait();
        }
    }

    @FXML
    public void editOrder(ActionEvent event) throws SQLException, InterruptedException, IOException {
        try {
            // Set Sub-Orders variables
            String OrderID;
            String ProductID;
            int Qty;
            String Description;
            InputStream DescriptionPhoto;

            // Check Order Status
            if ((order.getDeliveryDate().compareTo(LocalDate.now()) < 0) && (balanceDue.getText().equals("0"))) {
                order.setOrderStatus("Completed");
            } else {
                order.setOrderStatus("Pending");
            }

            // SQL queries
            Database.editOrder(order.getOrderID(), Database.getCustomerID(order.getCustomerName()), order.getOrderType(), order.getDeliveryAddress(),  order.getDeliveryPrice(), order.getOrderDate(), order.getDeliveryDateTime(), order.getOrderStatus(), Integer.parseInt(paid.getText()));

            // Clear SubOrders (Before Adding it again)
            Database.clearSubOrders(order.getOrderID());

            // Adding Suborders Back
            System.out.println("SubOrderList Size = " + SubOrderList.size());
            for (SubOrder subOrder: SubOrderList){
                OrderID = order.getOrderID();
                ProductID = subOrder.getProductID();
                Qty = subOrder.getQty();
                Description = subOrder.getDescription();
                // Check if Product has photo
                if (subOrder.getDescriptionPhoto() == null){
                    Database.addSubOrder(OrderID, ProductID, Qty, Description);
                } else {
                    DescriptionPhoto = subOrder.getDescriptionPhoto();
                    Database.addSubOrder(OrderID, ProductID, Qty, Description, DescriptionPhoto);
                    DescriptionPhoto.close();
                }
            }

            // Close Stage & Refresh Table
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            parentController.RefreshOrderTable();
        }  catch (NumberFormatException e){
            // Validation with alert box
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Paid section was filled with Wrong Format!");
            alert.setContentText("Paid have wrong format, please fullfil with correct format. \n(e.g. 100000)");

            alert.showAndWait();
        }
    }

    private void RefreshSubOrderTable() {
        SubOrderTable.setItems(SubOrderList);
        int disc = 0;

        // Calculate subTotal
        int sTotal = 0;
        for (SubOrder suborder : SubOrderList) {
            sTotal += suborder.getQty() * suborder.getPrice();
        }

        disc = order.getDiscount();

        int gTotal = sTotal + order.getDeliveryPrice() - disc;

        // Set Labels;
        subTotal.setText(String.valueOf(sTotal));
        discount.setText(String.valueOf(disc));
        grandTotal.setText(String.valueOf(gTotal));
        balanceDue.setText(String.valueOf(gTotal));

        // Sort
        noCol.setSortType(TableColumn.SortType.ASCENDING);
        SubOrderTable.getSortOrder().add(noCol);
    }
}
