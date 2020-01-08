package sample;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Order {
    // Directly from Database
    private String OrderID;
    private String CustomerName;
    private String OrderType;
    private String DeliveryAddress;
    private int DeliveryPrice;
    private LocalDate OrderDate;
    private LocalDateTime DeliveryDateTime;
    private String OrderStatus;
    private int Payment;
    private int Discount;
    private int BalanceDue;

    // Split the Delivery Date & Time (for tableview)
    private LocalDate DeliveryDate;
    private LocalTime DeliveryTime;

    public Order(String orderID, String customerName, String orderType, String deliveryAddress, int deliveryPrice, LocalDate orderDate, LocalDateTime deliveryDateTime, String orderStatus, int payment, int discount, int balanceDue) {
        OrderID = orderID;
        CustomerName = customerName;
        OrderType = orderType;
        DeliveryAddress = deliveryAddress;
        DeliveryPrice = deliveryPrice;
        OrderDate = orderDate;
        DeliveryDateTime = deliveryDateTime;
        OrderStatus = orderStatus;
        Payment = payment;
        Discount = discount;
        BalanceDue = balanceDue;
        // Split the Delivery Date & Time
        DeliveryDate = DeliveryDateTime.toLocalDate();
        DeliveryTime = DeliveryDateTime.toLocalTime();
    }

    public Order(String orderID, String customerName, String orderType, String deliveryAddress, int deliveryPrice, LocalDate orderDate, LocalDateTime deliveryDateTime, String orderStatus, int payment, int discount) {
        OrderID = orderID;
        CustomerName = customerName;
        OrderType = orderType;
        DeliveryAddress = deliveryAddress;
        DeliveryPrice = deliveryPrice;
        OrderDate = orderDate;
        DeliveryDateTime = deliveryDateTime;
        OrderStatus = orderStatus;
        Payment = payment;
        Discount = discount;
        // Split the Delivery Date & Time
        DeliveryDate = DeliveryDateTime.toLocalDate();
        DeliveryTime = DeliveryDateTime.toLocalTime();
    }

    public int getBalanceDue() {
        return BalanceDue;
    }

    public void setBalanceDue(int balanceDue) {
        BalanceDue = balanceDue;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public void setDeliveryTime(LocalTime deliveryTime) {
        DeliveryTime = deliveryTime;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getDeliveryAddress() {
        return DeliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAdress) {
        DeliveryAddress = deliveryAdress;
    }

    public int getDeliveryPrice() {
        return DeliveryPrice;
    }

    public void setDeliveryPrice(int deliveryPrice) {
        DeliveryPrice = deliveryPrice;
    }

    public LocalDate getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        OrderDate = orderDate;
    }

    public LocalDateTime getDeliveryDateTime() {
        return DeliveryDateTime;
    }

    public void setDeliveryDateTime(LocalDateTime deliveryDateTime) {
        DeliveryDateTime = deliveryDateTime;
        DeliveryDate = DeliveryDateTime.toLocalDate();
        DeliveryTime = DeliveryDateTime.toLocalTime();
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public int getPayment() {
        return Payment;
    }

    public void setPayment(int payment) {
        Payment = payment;
    }

    public int getDiscount() {
        return Discount;
    }

    public void setDiscount(int discount) {
        Discount = discount;
    }

    public LocalDate getDeliveryDate() {
        return DeliveryDate;
    }

    public LocalTime getDeliveryTime() {
        return DeliveryTime;
    }
}
