package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/db_databasesystem";
    static final String USER = "root";
//    passwordnya Aleep
    static final String PASS = "2201798295Binus";
//    static final String PASS = "";
    static Connection conn;
    static Statement stmt;
    static PreparedStatement pstmt;
    static ResultSet rs;

    public static Connection connect(){
        try {
            Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addCustomer(String id, String name, String phone, String email, boolean Member){
        try {
            conn = connect();
            stmt = conn.createStatement();

            int mem = 0;
            if (Member) {
                mem = 1;
            }

            String sql = "INSERT INTO customers(CustomerID, Name, PhoneNo, Email, Member) VALUE('%s', '%s', '%s', '%s', '%d')";
            sql = String.format(sql, id, name, phone, email, mem);
            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editCustomer(String id, String name, String phone, String email, boolean Member){
        try {
            conn = connect();
            stmt = conn.createStatement();

            int mem = 0;
            if (Member) {
                mem = 1;
            }

            String sql = "UPDATE customers SET Name='%s', PhoneNo='%s', Email='%s', Member='%d' WHERE CustomerID='%s'";
            sql = String.format(sql, name, phone, email, mem, id);
            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCustomer(String id){
        try {
            conn = connect();
            stmt = conn.createStatement();

            String sql = String.format("DELETE FROM customers where CustomerID = '%s'", id);
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getCustomer(String CustomerID) throws SQLException{
        conn = connect();

        String sql = "SELECT * FROM customers WHERE CustomerID = '%s'";
        sql = String.format(sql, CustomerID);
        ResultSet rs = conn.createStatement().executeQuery(sql);

        rs.next();
        return rs.getString("Name");
    }

    public static String getCustomerID(String CustomerName) throws SQLException{
        conn = connect();

        String sql = "SELECT * FROM customers WHERE Name = '%s'";
        sql = String.format(sql, CustomerName);
        ResultSet rs = conn.createStatement().executeQuery(sql);

        rs.next();
        return rs.getString("CustomerID");
    }

    public static boolean getMember(String CustomerName){
        boolean flag = false;
        try {
            conn = connect();

            String sql = "SELECT * FROM customers WHERE Name = '%s'";
            sql = String.format(sql, CustomerName);
            ResultSet rs = conn.createStatement().executeQuery(sql);

            rs.next();
            if (rs.getInt("Member") == 1){
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static void addProductType(String TypeID, String Type){
        try {
            conn = connect();
            stmt = conn.createStatement();

            String sql = "INSERT INTO product_type(TypeID, Type) value('%s', '%s')";
            sql = String.format(sql, TypeID, Type);
            stmt.execute(sql);

            System.out.println(String.format("Added %s to product_type", TypeID));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteProductTypeByID(String TypeID){
        try {
            conn = connect();
            stmt = conn.createStatement();

            String sql = String.format("DELETE FROM product_type where TypeID = '%s'", TypeID);
            stmt.execute(sql);

            System.out.println(String.format("Deleted %s from product_type", TypeID));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteProductTypeByType(String Type){
        try {
            conn = connect();
            stmt = conn.createStatement();

            String sql = String.format("DELETE FROM product_type where Type = '%s'", Type);
            stmt.execute(sql);

            System.out.println(String.format("Deleted %s from product_type", Type));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getAllTypes() throws NullPointerException {
        ArrayList<String> listofTypes = new ArrayList<>();
        try {
            conn = connect();
            String sql = "SELECT Type FROM product_type";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            while (rs.next()){
                listofTypes.add(rs.getString("Type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listofTypes;
    }

    public static boolean isProductTypeExist(String Type) throws SQLException {
        conn = connect();

        String sql = "SELECT * FROM product_type WHERE Type = '%s'";
        sql = String.format(sql, Type);
        ResultSet rs = conn.createStatement().executeQuery(sql);

        // Checks if ProductType exists
        int counter = 0;
        while (rs.next()){
            counter++;
        }

        if (counter != 0) {
            System.out.println(String.format("%s exists in product_type", Type));
            return true;
        } else {
            System.out.println(String.format("%s doesn't exists in product_type", Type));
            return false;
        }
    }

    public static String getTypeID(String Type) throws SQLException{
        conn = connect();

        String sql = "SELECT * FROM product_type WHERE Type = '%s'";
        sql = String.format(sql, Type);
        ResultSet rs = conn.createStatement().executeQuery(sql);

        rs.next();
        return rs.getString("TypeID");
    }

    public static String getType(String TypeID) throws SQLException{
        conn = connect();

        String sql = "SELECT * FROM product_type WHERE TypeID = '%s'";
        sql = String.format(sql, TypeID);
        ResultSet rs = conn.createStatement().executeQuery(sql);

        rs.next();
        return rs.getString("Type");
    }

    public static String getLastTypeID() throws SQLException{
        try {
            conn = connect();

            String sql = "SELECT MAX(TypeID) FROM product_type";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            rs.next();

            // If no data exists yet
            if (rs.getString("MAX(TypeID)") == null){
                System.out.println("Last TypeID = TYP00000");
                return "TYP00000";
            }

            // If data exists
            String LastTypeID = rs.getString("MAX(TypeID)");
            System.out.println(String.format("Last TypeID = %s", LastTypeID));
            return LastTypeID;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addProduct(String ProductID, String ProductName, String TypeID, int Price){
        try {
            conn = connect();
            stmt = conn.createStatement();

            String sql = "INSERT INTO products(ProductID, ProductName, TypeID, Price) VALUE('%s', '%s', '%s', '%d')";
            sql = String.format(sql, ProductID, ProductName, TypeID, Price);
            stmt.execute(sql);

            System.out.println(String.format("Added %s to product", ProductID));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void editProduct(String ProductID, String ProductName, String TypeID, int Price){
        try {
            conn = connect();
            stmt = conn.createStatement();

            String sql = "UPDATE products SET ProductName='%s', TypeID='%s', Price='%d' WHERE ProductID='%s'";
            sql = String.format(sql, ProductName, TypeID, Price, ProductID);
            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteProduct(String ProductID){
        try {
            conn = connect();
            stmt = conn.createStatement();

            String sql = String.format("DELETE FROM products where ProductID = '%s'", ProductID);
            stmt.execute(sql);

            System.out.println(String.format("Deleted %s from product", ProductID));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getProductName(String ProductID) throws SQLException{
        conn = connect();

        String sql = "SELECT ProductName FROM products WHERE ProductID = '%s'";
        sql = String.format(sql, ProductID);
        ResultSet rs = conn.createStatement().executeQuery(sql);

        rs.next();
        return rs.getString("ProductName");
    }

    public static int getProductPrice(String ProductID) throws SQLException{
        conn = connect();

        String sql = "SELECT Price FROM products WHERE ProductID = '%s'";
        sql = String.format(sql, ProductID);
        ResultSet rs = conn.createStatement().executeQuery(sql);

        rs.next();
        return rs.getInt("Price");
    }

    public static boolean isProductTypeExistInProduct(String TypeID) throws SQLException{
        conn = connect();

        String sql = "SELECT * FROM products WHERE TypeID = '%s'";
        sql = String.format(sql, TypeID);
        ResultSet rs = conn.createStatement().executeQuery(sql);

        // Checks if ProductType exists
        int counter = 0;
        while (rs.next()){
            counter++;
        }

        if (counter != 0) {
            System.out.println(String.format("TypeID %s exists in products", TypeID));
            return true;
        } else {
            System.out.println(String.format("TypeID %s doesn't exists in product_type", TypeID));
            return false;
        }
    }

    public static int getPayment(String OrderID) throws SQLException{
        conn = connect();

        String sql = "SELECT Payment FROM orders WHERE OrderID = '%s'";
        sql = String.format(sql, OrderID);
        ResultSet rs = conn.createStatement().executeQuery(sql);

        rs.next();
        return rs.getInt("Payment");
    }

    public static String getLastOrderID() throws SQLException{
        try {
            conn = connect();

            String sql = "SELECT MAX(OrderID) FROM orders";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            rs.next();

            // If no data exists yet
            if (rs.getString("MAX(OrderID)") == null){
                System.out.println("Last OrderID = ORD00000");
                return "ORD00000";
            }

            // If data exists
            String LastOrderID = rs.getString("MAX(OrderID)");
            System.out.println(String.format("Last OrderID = %s", LastOrderID));
            return LastOrderID;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getDoneOrders() throws SQLException{
        conn = connect();

        String sql = "SELECT COUNT(*) as Done FROM orders WHERE OrderStatus = 'Completed'";
        sql = String.format(sql);
        ResultSet rs = conn.createStatement().executeQuery(sql);

        rs.next();
        return rs.getInt("Done");
    }

    public static int getOngoingOrders() throws SQLException{
        conn = connect();

        String sql = "SELECT COUNT(*) as Ongoing FROM orders WHERE OrderStatus = 'Pending'";
        sql = String.format(sql);
        ResultSet rs = conn.createStatement().executeQuery(sql);

        rs.next();
        return rs.getInt("Ongoing");
    }

    public static int getPendingPayments() throws SQLException{
        conn = connect();

        String sql = "SELECT COUNT(*) FROM " +
                "(SELECT orders.*, (tv.total + orders.DeliveryPrice - orders.discount) as OrderTotal, " +
                "(tv.total + orders.DeliveryPrice - orders.Payment - orders.discount) as RemainingPayment FROM orders INNER JOIN " +
                "(SELECT suborders.OrderID, SUM(products.Price * suborders.Qty) as total FROM suborders INNER JOIN " +
                "products on suborders.ProductID = products.ProductID GROUP BY suborders.OrderID) as tv on orders.OrderID = tv.OrderID) AS test " +
                "WHERE test.RemainingPayment != 0;";
        sql = String.format(sql);
        ResultSet rs = conn.createStatement().executeQuery(sql);

        rs.next();
        return rs.getInt("COUNT(*)");
    }

    public static int getTotalRevenue() throws SQLException{
        conn = connect();

        String sql = "SELECT SUM(Payment) FROM orders";
        sql = String.format(sql);
        ResultSet rs = conn.createStatement().executeQuery(sql);

        rs.next();
        return rs.getInt("SUM(Payment)");
    }

    public static void addOrder(String OrderID, String CustomerID, String OrderType, String DeliveryAddress, int DeliveryPrice, LocalDate OrderDate, LocalDateTime DeliveryDateTime, String OrderStatus, int Payment, int Discount){
        try {
            conn = connect();
            stmt = conn.createStatement();

            String sql = "INSERT INTO orders(OrderID, CustomerID, OrderType, DeliveryAddress, DeliveryPrice, OrderDate, DeliveryDateTime, OrderStatus, Payment, Discount) VALUE('%s', '%s', '%s', '%s', '%d', '%s', '%s', '%s', '%d', '%d')";
            sql = String.format(sql, OrderID, CustomerID, OrderType, DeliveryAddress, DeliveryPrice, OrderDate, DeliveryDateTime, OrderStatus, Payment, Discount);
            stmt.execute(sql);

            System.out.println(String.format("Added %s to orders", OrderID));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteOrder(String id){
        try {
            conn = connect();
            stmt = conn.createStatement();

            String sql = String.format("DELETE FROM orders where OrderID = '%s'", id);
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editOrder(String OrderID, String CustomerID, String OrderType, String DeliveryAddress, int DeliveryPrice, LocalDate OrderDate, LocalDateTime DeliveryDateTime, String OrderStatus, int Payment){
        try {
            conn = connect();
            stmt = conn.createStatement();

            String sql = "UPDATE orders SET CustomerID='%s', OrderType='%s', DeliveryAddress='%s', DeliveryPrice='%s', OrderDate='%s', DeliveryDateTime='%s', OrderStatus='%s', Payment='%d' WHERE OrderID='%s'";
            sql = String.format(sql, CustomerID, OrderType, DeliveryAddress, DeliveryPrice, OrderDate, DeliveryDateTime, OrderStatus, Payment, OrderID);
            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editOrderStatus(String OrderID, String OrderStatus){
        try {
            stmt = conn.createStatement();

            String sql = "UPDATE orders SET OrderStatus='%s' WHERE OrderID='%s'";
            sql = String.format(sql, OrderStatus, OrderID);
            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getOrderByDeliveryDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime){
        try{
            conn = connect();

            // Get orders between that datetime frame
            String sql = String.format("SELECT * FROM orders WHERE DeliveryDateTime BETWEEN '%s' AND '%s'", startDateTime.toString(), endDateTime.toString());

            ResultSet rs = conn.createStatement().executeQuery(sql);

            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addSubOrder(String OrderID, String ProductID, int Qty, String Description, InputStream DescriptionPhoto){
        try {
            conn = connect();

            String sql = "INSERT INTO suborders(OrderID, ProductID, Qty, Description, DescriptionPhoto) VALUE('%s', '%s', '%d', '%s', ?)";
            sql = String.format(sql, OrderID, ProductID, Qty, Description);

            pstmt = conn.prepareStatement(sql);
            pstmt.setBinaryStream(1, DescriptionPhoto, DescriptionPhoto.available());

            pstmt.execute();

            DescriptionPhoto.close();

            System.out.println(String.format("Added %s to sub-orders", ProductID));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addSubOrder(String OrderID, String ProductID, int Qty, String Description){
        try {
            conn = connect();
            stmt = conn.createStatement();

            String sql = "INSERT INTO suborders(OrderID, ProductID, Qty, Description) VALUE('%s', '%s', '%d', '%s')";
            sql = String.format(sql, OrderID, ProductID, Qty, Description);
            stmt.execute(sql);

            System.out.println(String.format("Added %s to sub-orders", ProductID));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearSubOrders(String OrderID){
        try {
            conn = connect();
            stmt = conn.createStatement();

            String sql = String.format("DELETE FROM suborders where OrderID = '%s'", OrderID);
            stmt.execute(sql);

            System.out.println("Cleared SubOrder where OrderID = " + OrderID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<SubOrder> getSubOrderList(String OrderID){
        ObservableList<SubOrder> SubOrderList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM suborders WHERE OrderID = '%s'";
            sql = String.format(sql, OrderID);

            conn = connect();
            ResultSet rs = conn.createStatement().executeQuery(sql);

            int colNo = 1;
            while(rs.next()) {
                Blob blob = rs.getBlob("DescriptionPhoto");
                InputStream inputStream = null;
                if (blob != null) {
                    inputStream = blob.getBinaryStream();
                }
                SubOrderList.add(new SubOrder(colNo, rs.getString("OrderID"), rs.getString("ProductID"), Database.getProductName(rs.getString("ProductID")),
                        rs.getInt("Qty"), rs.getString("Description"), inputStream, Database.getProductPrice(rs.getString("ProductID"))));
                colNo++;
            }

            rs.close();
            conn.close();
        } catch (Exception e) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, e);
        }
        return SubOrderList;
    }

    public static int getGrandTotal(String OrderID){
        int grandTotal = 0;
        try {
            String sql = String.format("SELECT ProductID, Qty, DeliveryPrice, Discount FROM suborders, orders WHERE orders.OrderID = '%s'", OrderID, OrderID);

            ResultSet rs = conn.createStatement().executeQuery(sql);

            int subTotal = 0;
            int deliveryPrice = 0;
            int discount = 0;
            while(rs.next()){
                subTotal += Database.getProductPrice(rs.getString("ProductID")) * rs.getInt("Qty");
                deliveryPrice = rs.getInt("DeliveryPrice");
                discount = rs.getInt("Discount");
            }

            grandTotal = subTotal + deliveryPrice - discount;
        } catch (SQLException e) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, e);
        }
        return grandTotal;
    }

    public static ResultSet getWeeklySales(String dateStart, String dateEnd){
        try{
            conn = connect();

//            get weekly sum based on the Payment column of orders
            String sql = "SELECT SUM(Payment) AS revenue," +
                    " CONCAT( STR_TO_DATE(CONCAT(YEARWEEK(OrderDate, 2), ' Sunday'), '%X%V %W'), ' to ', STR_TO_DATE(CONCAT(YEARWEEK(OrderDate, 2), ' Sunday'), '%X%V %W') + INTERVAL 6 DAY ) AS t FROM orders " +
                    "WHERE OrderDate BETWEEN \'" + dateStart + "\' and \'" + dateEnd +"\' GROUP BY YEARWEEK(OrderDate, 2) ORDER BY YEARWEEK(OrderDate, 2);";

            ResultSet rs = conn.createStatement().executeQuery(sql);

//          return the entire result set to be processed
            return rs;


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet getMonthlySales(String dateStart, String dateEnd){
        try{
            conn = connect();

//            get weekly sum based on the Payment column of orders
            String sql = "SELECT CONCAT( MONTHNAME(OrderDate), ' ', YEAR(OrderDate)) AS t, SUM(Payment) AS revenue FROM orders WHERE OrderDate BETWEEN '"+dateStart+"' and '"+dateEnd+"' GROUP BY YEAR(OrderDate), MONTH(OrderDate)";

            ResultSet rs = conn.createStatement().executeQuery(sql);

//          return the entire result set to be processed
            return rs;


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet getYearlySales(String dateStart, String dateEnd){
        try{
            conn = connect();

//            get weekly sum based on the Payment column of orders
            String sql = "SELECT YEAR(OrderDate) AS t, SUM(Payment) AS revenue FROM orders WHERE OrderDate BETWEEN '"+dateStart+"' and '"+dateEnd+"' GROUP BY YEAR(OrderDate)";

            ResultSet rs = conn.createStatement().executeQuery(sql);

//          return the entire result set to be processed
            return rs;


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFirstSale(){
        try{
            conn = connect();

            String sql = "SELECT OrderDate from orders ORDER BY OrderDate ASC LIMIT 1";

            ResultSet rs = conn.createStatement().executeQuery(sql);

            rs.next();

            return rs.getString("OrderDate");

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getLastSale(){
        try{
            conn = connect();

            String sql = "SELECT OrderDate from orders ORDER BY OrderDate DESC LIMIT 1";

            ResultSet rs = conn.createStatement().executeQuery(sql);

            rs.next();

            return rs.getString("OrderDate");

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


//  Getting number of orders a customer has in total
    public static int getNoOrders(String customerID){
        try{
            conn = connect();

//            get weekly sum based on the Payment column of orders
            String sql = "SELECT COUNT(*) AS numOrders FROM orders WHERE CustomerID = '%s'";
            sql = String.format(sql, customerID);

            ResultSet rs = conn.createStatement().executeQuery(sql);
            rs.next();

//          return the entire result set to be processed
            return rs.getInt("numOrders");


        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public static int getNoOrdersByName(String customerName){
        try{
            conn = connect();

//            get weekly sum based on the Payment column of orders
            String sql = "SELECT COUNT(*) AS numOrders FROM orders WHERE 'Name' = '%s'";
            sql = String.format(sql, customerName);

            ResultSet rs = conn.createStatement().executeQuery(sql);
            rs.next();

//          return the entire result set to be processed
            return rs.getInt("numOrders");


        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }

//    Sets a customer to become a member
    public static void setMember(String customerID, int status){
        try{
            conn = connect();
            stmt = conn.createStatement();

            String sql = "UPDATE customers SET Member = '%d' WHERE CustomerID = '%s'";

            sql = String.format(sql, status, customerID);
            stmt.execute(sql);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}