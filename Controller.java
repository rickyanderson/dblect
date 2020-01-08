package sample;

import animatefx.animation.FadeIn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Initializable {

    @FXML private Label HomeLabel;
    @FXML private Label OrderLabel;
    @FXML private Label CustomerLabel;
    @FXML private Label ProductLabel;

    @FXML private Rectangle HomeRectangle;
    @FXML private Rectangle OrderRectangle;
    @FXML private Rectangle CustomerRectangle;
    @FXML private Rectangle ProductRectangle;

    @FXML private AnchorPane HomePane;
    @FXML private AnchorPane OrderPane;
    @FXML private AnchorPane CustomerPane;
    @FXML private AnchorPane ProductPane;

    // Home Pane Members
    @FXML private Label FilterDateTitle;
    @FXML private Label StartDate;
    @FXML private Label EndDate;
    @FXML private DatePicker dateStart;
    @FXML private DatePicker dateEnd;
    @FXML private BarChart weeklyRevenueChart;
    @FXML private ChoiceBox<String> bgModePicker;

    // Order Pane Members
    @FXML private Label NewOrderLabel;
    @FXML private Label DeleteOrderLabel;
    @FXML private Label EditOrderLabel;
    @FXML private Label OverviewLabel;
    @FXML private Label DoneOrderLabel;
    @FXML private Label OngoingOrderLabel;
    @FXML private Label PaymentPendingLabel;
    @FXML private Label TotalRevenueLabel;
    @FXML private Label DetailsOrderLabel;
    @FXML private Label DoneOrders;
    @FXML private Label OngoingOrders;
    @FXML private Label PaymentPending;
    @FXML private Label TotalRevenue;
    @FXML private ComboBox OrderFilterComboBox;
    @FXML private DatePicker OrderDateFilter;
    @FXML private TableView<Order> OrderTable;
    @FXML private TableColumn<Order, String> OrdIDCol;
    @FXML private TableColumn<Order, String> OrdCustCol;
    @FXML private TableColumn<Order, String> OrdTypeCol;
    @FXML private TableColumn<Order, String> OrdDateCol;
    @FXML private TableColumn<Order, String> OrdDeliveryDateCol;
    @FXML private TableColumn<Order, String> OrdDeliveryTimeCol;
    @FXML private TableColumn<Order, String> OrdStatusCol;
    @FXML private TableColumn<Order, Integer> OrdBalanceDueCol;
    ObservableList<Order> OrderList = FXCollections.observableArrayList();

    // Customer Pane Members
    @FXML private Label NewCustomerLabel;
    @FXML private Label DeleteCustomerLabel;
    @FXML private Label EditCustomerLabel;
    @FXML private ComboBox CustomerComboBox;
    @FXML private TableView<Customer> CustomerTable;
    @FXML private TableColumn<Customer, Integer> CustNoCol;
    @FXML private TableColumn<Customer, String> CustIDCol;
    @FXML private TableColumn<Customer, String> CustNameCol;
    @FXML private TableColumn<Customer, String> CustPhoneCol;
    @FXML private TableColumn<Customer, String> CustEmailCol;
    @FXML private TableColumn<Customer, String> CustStatusCol;
    ObservableList<Customer> CustomerList = FXCollections.observableArrayList();

    // Product Pane Members
    @FXML private Label NewProductLabel;
    @FXML private Label DeleteProductLabel;
    @FXML private Label EditProductLabel;
    @FXML private ComboBox FilterProduct;
    @FXML private TableView<Product> ProductTable;
    @FXML private TableColumn<Product, Integer> ProdNoCol;
    @FXML private TableColumn<Product, String> ProdIDCol;
    @FXML private TableColumn<Product, String> ProdNameCol;
    @FXML private TableColumn<Product, String> ProdTypeCol;
    @FXML private TableColumn<Product, Integer> ProdPriceCol;
    ArrayList<String> ProductTypeList = new ArrayList<>();
    ObservableList<Product> ProductList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb){
        // Initialize Home Pane
        bgModePicker.getItems().addAll("Weekly","Monthly", "Yearly");
        bgModePicker.setValue("Weekly");

        // Initialize Home Pane
        FilterDateTitle.setFont(Font.loadFont("file:src/fonts/cocolight.ttf", 18));
        StartDate.setFont(Font.loadFont("file:src/fonts/cocolight.ttf", 18));
        EndDate.setFont(Font.loadFont("file:src/fonts/cocolight.ttf", 18));

        // Initialize Order
        NewOrderLabel.setFont(Font.loadFont("file:src/fonts/cocoregular.ttf", 18));
        DeleteOrderLabel.setFont(Font.loadFont("file:src/fonts/cocoregular.ttf", 18));
        EditOrderLabel.setFont(Font.loadFont("file:src/fonts/cocoregular.ttf", 18));
        OverviewLabel.setFont(Font.loadFont("file:src/fonts/cocolight.ttf", 18));
        DoneOrderLabel.setFont(Font.loadFont("file:src/fonts/cocolight.ttf", 18));
        OngoingOrderLabel.setFont(Font.loadFont("file:src/fonts/cocolight.ttf", 18));
        PaymentPendingLabel.setFont(Font.loadFont("file:src/fonts/cocolight.ttf", 18));
        TotalRevenueLabel.setFont(Font.loadFont("file:src/fonts/cocolight.ttf", 18));
        DetailsOrderLabel.setFont(Font.loadFont("file:src/fonts/cocoregular.ttf", 18));
        OrderFilterComboBox.getItems().addAll("All", "Pending", "Completed");

        // Initialize Customer Pane
        NewCustomerLabel.setFont(Font.loadFont("file:src/fonts/cocoregular.ttf", 18));
        DeleteCustomerLabel.setFont(Font.loadFont("file:src/fonts/cocoregular.ttf", 18));
        EditCustomerLabel.setFont(Font.loadFont("file:src/fonts/cocoregular.ttf", 18));
        CustomerComboBox.getItems().addAll("All", "Members", "Non-Members");

        // Initialize Product Pane
        NewProductLabel.setFont(Font.loadFont("file:src/fonts/cocoregular.ttf", 18));
        DeleteProductLabel.setFont(Font.loadFont("file:src/fonts/cocoregular.ttf", 18));
        EditProductLabel.setFont(Font.loadFont("file:src/fonts/cocoregular.ttf", 18));
        RefreshProductFilter();

        // By Default, Home Label is Clicked
        HomeLabelClicked();

        // Refresh Observable Lists
        RefreshCustomerList();
        RefreshProductList();

        // Remove bug caused by animation
        weeklyRevenueChart.getXAxis().setAnimated(false);

        getAllWeeklyGraph();

        // Change the order status of all the orders that is delivered yesterday
        LocalDateTime startDateTime = LocalDate.now().minus(1, ChronoUnit.DAYS).atTime(LocalTime.of(0, 0, 0));
        ResultSet rs = Database.getOrderByDeliveryDateTime(startDateTime, LocalDateTime.now());
        try {
            while (rs.next()){
                if (rs.getInt("Payment") == Database.getGrandTotal(rs.getString("OrderID"))){
                    Database.editOrderStatus(rs.getString("OrderID"), "Completed");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getProductTypeList(){
        return this.ProductTypeList;
    }

    private void LabelDefault(){
        // Set Label Fonts
        HomeLabel.setFont(Font.loadFont("file:src/fonts/expressway.ttf", 20));
        OrderLabel.setFont(Font.loadFont("file:src/fonts/expressway.ttf", 20));
        CustomerLabel.setFont(Font.loadFont("file:src/fonts/expressway.ttf", 20));
        ProductLabel.setFont(Font.loadFont("file:src/fonts/expressway.ttf", 20));
        // Set Label Colors
        HomeLabel.setTextFill(Paint.valueOf("9a9a9a"));
        OrderLabel.setTextFill(Paint.valueOf("9a9a9a"));
        CustomerLabel.setTextFill(Paint.valueOf("9a9a9a"));
        ProductLabel.setTextFill(Paint.valueOf("9a9a9a"));
        // Set Rectangles Off
        HomeRectangle.setVisible(false);
        OrderRectangle.setVisible(false);
        CustomerRectangle.setVisible(false);
        ProductRectangle.setVisible(false);
        // Set Panes Off
        HomePane.setDisable(true);
        HomePane.setVisible(false);
        OrderPane.setDisable(true);
        OrderPane.setVisible(false);
        CustomerPane.setDisable(true);
        CustomerPane.setVisible(false);
        ProductPane.setDisable(true);
        ProductPane.setVisible(false);
    }

    private void SetOverview() throws SQLException {
        DoneOrders.setText(String.valueOf(Database.getDoneOrders()));
        OngoingOrders.setText(String.valueOf(Database.getOngoingOrders()));

        // Select Where Balance Due != 0
        PaymentPending.setText(String.valueOf(Database.getPendingPayments()));
        // Select Sum All Payment
        TotalRevenue.setText(String.valueOf(Database.getTotalRevenue()));
    }

    @FXML
    public void HomeLabelClicked(){
        LabelDefault();
        HomeLabel.setTextFill(Paint.valueOf("5596FD"));
        HomeRectangle.setVisible(true);
        new FadeIn(HomeRectangle).play();
        HomePane.setDisable(false);
        HomePane.setVisible(true);
        new FadeIn(HomePane).play();
    }

    @FXML
    public void OrderLabelClicked() throws SQLException {
        LabelDefault();
        OrderFilterComboBox.setValue("Status: All");
        OrderDateFilter.setValue(null);
        OrderLabel.setTextFill(Paint.valueOf("5596FD"));
        OrderRectangle.setVisible(true);
        new FadeIn(OrderRectangle).play();
        OrderPane.setDisable(false);
        OrderPane.setVisible(true);
        new FadeIn(OrderPane).play();
        RefreshOrderTable();
        SetOverview();
    }

    @FXML
    public void CustomerLabelClicked(){
        LabelDefault();
        CustomerComboBox.setValue("Type: All");
        CustomerLabel.setTextFill(Paint.valueOf("5596FD"));
        CustomerRectangle.setVisible(true);
        new FadeIn(CustomerRectangle).play();
        CustomerPane.setDisable(false);
        CustomerPane.setVisible(true);
        new FadeIn(CustomerPane).play();
        RefreshCustomerTable();
    }

    @FXML
    public void ProductLabelClicked(){
        LabelDefault();
        FilterProduct.setPromptText("Type: All");
        ProductLabel.setTextFill(Paint.valueOf("5596FD"));
        ProductRectangle.setVisible(true);
        new FadeIn(ProductRectangle).play();
        ProductPane.setDisable(false);
        ProductPane.setVisible(true);
        new FadeIn(ProductPane).play();
        RefreshProductTable();
    }

    // Home Page Functions
    @FXML
    public void getAllWeeklyGraph(){
        System.out.println("initializing graph weekly");
        weeklyRevenueChart.getData().clear();

        // get a weekly sales result set from the first and last sales
        ResultSet result = Database.getWeeklySales(Database.getFirstSale(), Database.getLastSale());

        insertIntoGraph(result, "Weekly graph");
    }

    @FXML
    public void getAllMonthlyGraph(){
        System.out.println("initializing graph monthly");
        weeklyRevenueChart.getData().clear();

        // get a monthly sales result set from the first and last sales
        ResultSet result = Database.getMonthlySales(Database.getFirstSale(), Database.getLastSale());

        insertIntoGraph(result, "Monthly graph");
    }

    @FXML
    public void getAllYearlyGraph(){
        System.out.println("initializing graph yearly");
        weeklyRevenueChart.getData().clear();

        // get a monthly sales result set from the first and last sales
        ResultSet result = Database.getYearlySales(Database.getFirstSale(), Database.getLastSale());

        insertIntoGraph(result, "Yearly graph");
    }

    @FXML
    public void filterButtonClicked(){
        System.out.println("FilterButton clicked on MainScreen.fxml");
        weeklyRevenueChart.getData().clear();
        String mode = bgModePicker.getValue();

        // checks what mode the graph should be in
        if (mode.equals("Weekly")) {
            System.out.println("mode = Weekly");
            try {
//            Get dates from DatePickers
                LocalDate startdate = dateStart.getValue();
                LocalDate enddate = dateEnd.getValue();

//            get result set from database
                ResultSet result = Database.getWeeklySales(startdate.toString(), enddate.toString());

                insertIntoGraph(result, "Weekly Filtered Graph");

            } catch (NullPointerException e) {
                System.out.println("Date not selected");
                getAllWeeklyGraph();
            }
        } else if(mode.equals("Monthly")){
            System.out.println("mode = Monthly");

            try {
//            Get dates from DatePickers
                LocalDate startdate = dateStart.getValue();
                LocalDate enddate = dateEnd.getValue();

//            get result set from database
                ResultSet result = Database.getMonthlySales(startdate.toString(), enddate.toString());

                insertIntoGraph(result, "Monthly Filtered Graph");

            } catch (NullPointerException e) {
                System.out.println("Date not selected");
                getAllMonthlyGraph();
            }
        } else{
            System.out.println("mode = Yearly");

            try {
//            Get dates from DatePickers
                LocalDate startdate = dateStart.getValue();
                LocalDate enddate = dateEnd.getValue();

//            get result set from database
                ResultSet result = Database.getYearlySales(startdate.toString(), enddate.toString());

                insertIntoGraph(result, "Yearly Graph");

            } catch (NullPointerException e) {
                System.out.println("Date not selected");
                getAllYearlyGraph();
            }
        }

    }

    @FXML
    public void insertIntoGraph(ResultSet rs, String mode) {
        XYChart.Series set1 = new XYChart.Series<String, Integer>();
        set1.setName(mode);
        try {
            while (rs.next()) {
                String week = rs.getString("t");
                System.out.println(week);
                int data = rs.getInt("revenue");
                System.out.println(data);
                set1.getData().add(new XYChart.Data(week, data));
            }
            weeklyRevenueChart.getData().addAll(set1);

        } catch (SQLException e){
                e.printStackTrace();
            }
    }

    // Order Pane Functions
    @FXML
    public void NewOrderClicked() throws IOException, SQLException {
        System.out.println("New Order Clicked");
        new FadeIn(NewOrderLabel).setSpeed(5).play();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("OrderForm.fxml"));
        Parent OrderFormParent = loader.load();

        Stage stage = new Stage(); // New stage (window)

        // Get CurrentProductID; if no Product exist yet prevProductID set to 0
        String prevOrderID = "ORD00000";
        if (!OrderList.isEmpty()){
            prevOrderID = Database.getLastOrderID();
        }

        // Passing data to ProductFormController
        OrderFormController controller = loader.getController();
        controller.initData(this, prevOrderID, CustomerList, ProductList);

        // Setting the stage up
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("New Order Form");
        stage.setScene(new Scene(OrderFormParent));
        stage.showAndWait();
    }

    @FXML
    public void DeleteOrderClicked() throws SQLException {
        System.out.println("Delete Order Clicked");
        new FadeIn(DeleteOrderLabel).setSpeed(5).play();

        // Gets Selected Row
        Order selectedOrder = OrderTable.getSelectionModel().getSelectedItem();

        String custID = Database.getCustomerID(selectedOrder.getCustomerName());

        if(!(selectedOrder == null)){
            String id = selectedOrder.getOrderID();
            Database.deleteOrder(id);

            if(Database.getNoOrders(custID) < 5){
                Database.setMember(custID, 0);
            }

            RefreshOrderTable();
        }
    }

    @FXML
    public void DetailsOrderClicked() throws IOException {
        System.out.println("Details Order Clicked");
        new FadeIn(DetailsOrderLabel).setSpeed(5).play();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("DetailOrder.fxml"));
        Parent DetailOrderParent = loader.load();

        Stage stage = new Stage(); // New stage (window)

        // Passing data to CustomerFormController
        DetailOrderController controller = loader.getController();
        Order selectedOrder = OrderTable.getSelectionModel().getSelectedItem();
        controller.initData(this, selectedOrder, ProductList);

        // Setting the stage up
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("Details Order Form");
        stage.setScene(new Scene(DetailOrderParent));
        stage.showAndWait();
    }

    @FXML
    public void EditOrderClicked() throws IOException {
        System.out.println("Edit Order Clicked");
        new FadeIn(EditOrderLabel).setSpeed(5).play();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("EditOrderForm.fxml"));
        Parent EditOrderFormParent = loader.load();

        Stage stage = new Stage(); // New stage (window)

        // Passing data to CustomerFormController
        EditOrderFormController controller = loader.getController();
        Order selectedOrder = OrderTable.getSelectionModel().getSelectedItem();
        controller.initData(this, selectedOrder, CustomerList);

        // Setting the stage up
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("Edit Order Form");
        stage.setScene(new Scene(EditOrderFormParent));
        stage.showAndWait();
    }

    @FXML
    public void RefreshOrderList() throws NullPointerException{
        OrderList.clear();
        String filter;
        try {
            // Checks if ComboBox is empty
            try {
                filter = OrderFilterComboBox.getValue().toString();
            } catch (NullPointerException e) {
                filter = "All";
            }

            Connection conn = Database.connect();

            String sql = "SELECT orders.*, (tv.total + orders.DeliveryPrice - orders.discount) as OrderTotal, " +
                    "(tv.total + orders.DeliveryPrice - orders.Payment - orders.discount) as RemainingPayment " +
                    "FROM orders INNER JOIN (SELECT suborders.OrderID, SUM(products.Price * suborders.Qty) as total " +
                    "FROM suborders INNER JOIN products on suborders.ProductID = products.ProductID GROUP BY suborders.OrderID) as tv on orders.OrderID = tv.OrderID";

            boolean option = false;
            if (filter.equals("Pending")){
                sql = sql + " WHERE OrderStatus = 'Pending'";
                option = true;
            } else if (filter.equals("Completed")){
                sql = sql + " WHERE OrderStatus = 'Completed'";
                option = true;
            }

            // Filtering Dates
            if (!(OrderDateFilter.getValue() == null)) {
                LocalDate dateFilter = OrderDateFilter.getValue();
                // Checks if OrderStatus is filtered
                if (option) {
                    sql = sql + " AND DeliveryDateTime Between '%s 00:00:00' AND '%s 23:59:59'";
                    sql = String.format(sql, dateFilter, dateFilter);
                } else {
                    sql = sql + " WHERE DeliveryDateTime Between '%s 00:00:00' AND '%s 23:59:59'";
                    sql = String.format(sql, dateFilter, dateFilter);
                }
            }

            sql = sql + " ORDER BY DeliveryDateTime DESC";

//            System.out.println(sql);

            ResultSet rs = conn.createStatement().executeQuery(sql);

            int colNo = 1;
            while(rs.next()) {
                OrderList.add(new Order(rs.getString("OrderID"), Database.getCustomer(rs.getString("CustomerID")),
                        rs.getString("OrderType"), rs.getString("DeliveryAddress"), rs.getInt("DeliveryPrice"),
                        rs.getDate("OrderDate").toLocalDate(), rs.getTimestamp("DeliveryDateTime").toLocalDateTime(),
                        rs.getString("OrderStatus"), rs.getInt("Payment"), rs.getInt("Discount"), rs.getInt("RemainingPayment")));
                colNo++;
            }

            rs.close();
            conn.close();
        } catch (SQLException e) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    public void RefreshOrderTable() throws NullPointerException, SQLException {
        RefreshOrderList();
        OrdIDCol.setCellValueFactory(new PropertyValueFactory<>("OrderID"));
        OrdCustCol.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        OrdTypeCol.setCellValueFactory(new PropertyValueFactory<>("OrderType"));
        OrdDateCol.setCellValueFactory(new PropertyValueFactory<>("OrderDate"));
        OrdDeliveryDateCol.setCellValueFactory(new PropertyValueFactory<>("DeliveryDate"));
        OrdDeliveryTimeCol.setCellValueFactory(new PropertyValueFactory<>("DeliveryTime"));
        OrdStatusCol.setCellValueFactory(new PropertyValueFactory<>("OrderStatus"));
        OrdBalanceDueCol.setCellValueFactory(new PropertyValueFactory<>("BalanceDue"));
        OrderTable.setItems(OrderList);
        SetOverview();
    }

    // Customer Pane Functions
    @FXML
    public void NewCustomerClicked() throws IOException {
        System.out.println("New Customer Clicked");
        new FadeIn(NewCustomerLabel).setSpeed(5).play();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("CustomerForm.fxml"));
        Parent CustomerFormParent = loader.load();

        Stage stage = new Stage(); // New stage (window)

        // Get CurrentProductID; if no Product exist yet prevProductID set to 0
        String prevCustomerID = "CUS00000";
        if (!CustomerList.isEmpty()){
            prevCustomerID = CustomerList.get(CustomerList.size()-1).getCustomerID();
        }

        // Passing data to ProductFormController
        CustomerFormController controller = loader.getController();
        controller.initData(this, prevCustomerID);

        // Setting the stage up
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("New Customer Form");
        stage.setScene(new Scene(CustomerFormParent));
        stage.showAndWait();
    }

    @FXML
    public void DeleteCustomerClicked(){
        System.out.println("Delete Customer Clicked");
        new FadeIn(DeleteCustomerLabel).setSpeed(5).play();

        // Gets Selected Row
        Customer selectedCustomer = CustomerTable.getSelectionModel().getSelectedItem();
        if(!(selectedCustomer == null)){
            String id = selectedCustomer.getCustomerID();
            Database.deleteCustomer(id);
            RefreshCustomerTable();
        }
    }

    @FXML
    public void EditCustomerClicked() throws IOException {
        System.out.println("Edit Customer Clicked");
        new FadeIn(EditCustomerLabel).setSpeed(5).play();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("EditCustomerForm.fxml"));
        Parent EditCustomerFormParent = loader.load();

        Stage stage = new Stage(); // New stage (window)

        // Passing data to CustomerFormController
        EditCustomerFormController controller = loader.getController();
        Customer selectedCustomer = CustomerTable.getSelectionModel().getSelectedItem();
        controller.initData(this, selectedCustomer);

        // Setting the stage up
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("Edit Customer Form");
        stage.setScene(new Scene(EditCustomerFormParent));
        stage.showAndWait();
    }

    private void RefreshCustomerList() throws NullPointerException{
        CustomerList.clear();
        String filter;
        try {
            // Checks if ComboBox is empty
            try {
                filter = CustomerComboBox.getValue().toString();
            } catch (NullPointerException e) {
                filter = "All";
            }

            Connection conn = Database.connect();
            String sql = "SELECT * FROM customers";
            if (filter.equals("Members")){
                sql = "SELECT * FROM customers WHERE member = 1";
            } else if (filter.equals("Non-Members")){
                sql = "SELECT * FROM customers WHERE member = 0";
            }

            ResultSet rs = conn.createStatement().executeQuery(sql);

            int colNo = 1;
            while(rs.next()) {
                CustomerList.add(new Customer(colNo, rs.getString("CustomerID"), rs.getString("Name"),
                        rs.getString("PhoneNo"), rs.getString("Email"), rs.getBoolean("Member")));
                colNo++;
            }

            rs.close();
            conn.close();
        } catch (SQLException e) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    public void RefreshCustomerTable(){
        RefreshCustomerList();
        CustNoCol.setCellValueFactory(new PropertyValueFactory<>("columnNo"));
        CustIDCol.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        CustNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        CustPhoneCol.setCellValueFactory(new PropertyValueFactory<>("PhoneNo"));
        CustEmailCol.setCellValueFactory(new PropertyValueFactory<>("Email"));
        CustStatusCol.setCellValueFactory(new PropertyValueFactory<>("Member"));
        CustomerTable.setItems(CustomerList);
    }

    // Product Pane Functions
    @FXML
    public void NewProductClicked() throws IOException {
        System.out.println("New_Product_Label clicked in MainScreen.fxml");
        new FadeIn(NewProductLabel).setSpeed(5).play();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProductForm.fxml"));
        Parent ProductFormParent = loader.load();

        Stage stage = new Stage(); // New stage (window)

        // Get CurrentProductID; if no Product exist yet prevProductID set to 0
        String prevProductID = "PRO00000";
        if (!ProductList.isEmpty()){
            prevProductID = ProductList.get(ProductList.size()-1).getProductID();
        }

        // Passing data to ProductFormController
        ProductFormController controller = loader.getController();
        controller.initData(this, prevProductID);

        // Setting the stage up
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("New Product Form");
        stage.setScene(new Scene(ProductFormParent));
        stage.showAndWait();
    }

    @FXML
    public void DeleteProductClicked(){
        System.out.println("Delete_Product_Label clicked on MainScreen.fxml");
        new FadeIn(DeleteProductLabel).setSpeed(5).play();

        // Gets Selected Row
        Product selectedItem = ProductTable.getSelectionModel().getSelectedItem();
        String id = selectedItem.getProductID();

        // Checks if ProductFilter is selected
        if (!(selectedItem == null)){
            try {
                Database.deleteProduct(id);
                // If TypeId exists in products
                if (!(Database.isProductTypeExistInProduct(Database.getTypeID(selectedItem.getType())))){
                    Database.deleteProductTypeByType(selectedItem.getType());
                    RefreshProductFilter();
                }
                RefreshProductTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void EditProductClicked() throws IOException {
        System.out.println("Edit_Product_Label clicked on MainScreen.fxml");
        new FadeIn(EditProductLabel).setSpeed(5).play();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("EditProductForm.fxml"));
        Parent ProductFormParent = loader.load();

        Stage stage = new Stage(); // New stage (window)

        // Passing data to EditProductFormController
        EditProductFormController controller = loader.getController();
        Product selectedProduct = ProductTable.getSelectionModel().getSelectedItem();
        controller.initData(this, selectedProduct);

        // Setting the stage up
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("Edit Product Form");
        stage.setScene(new Scene(ProductFormParent));
        stage.showAndWait();
    }

    public void RefreshProductFilter(){
        FilterProduct.getItems().clear();
        ProductTypeList = Database.getAllTypes();
        ProductTypeList.add(0, "All");
        FilterProduct.setItems(FXCollections.observableArrayList(ProductTypeList));
        System.out.println("Refreshed ProductFilter in MainScreen.fxml");
    }

    public void RefreshProductList(){
        ProductList.clear();
        String filter, sql;
        try {
            // Checks if FilterComboBox is selected
            try {
                filter = FilterProduct.getValue().toString();
                // If user choose a specific filter
                if (filter.equals("All")) {
                    sql = "SELECT * FROM products";
                } else {
                    String TypeID = Database.getTypeID(filter);
                    sql = String.format("SELECT * FROM products WHERE TypeID = '%s'", TypeID);
                }
            } catch (NullPointerException e) {
                sql = "SELECT * FROM products";
            }

            Connection conn = Database.connect();
            ResultSet rs = conn.createStatement().executeQuery(sql);

            int colNo = 1;
            while(rs.next()) {
                ProductList.add(new Product(colNo, rs.getString("ProductID"), rs.getString("ProductName"),
                        Database.getType(rs.getString("TypeID")), rs.getInt("Price")));
                colNo++;
            }

            rs.close();
            conn.close();
        } catch (SQLException e) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    public void RefreshProductTable() throws NullPointerException{
        RefreshProductList();
        ProdNoCol.setCellValueFactory(new PropertyValueFactory<>("columnNo"));
        ProdIDCol.setCellValueFactory(new PropertyValueFactory<>("ProductID"));
        ProdNameCol.setCellValueFactory(new PropertyValueFactory<>("ProductName"));
        ProdTypeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        ProdPriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        ProductTable.setItems(ProductList);
    }
}