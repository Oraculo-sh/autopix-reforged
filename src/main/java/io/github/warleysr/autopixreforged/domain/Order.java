package io.github.warleysr.autopixreforged.domain;

import java.sql.Timestamp;
import java.util.UUID;

public class Order {
    private int id;
    private String playerName;
    private UUID playerUuid;
    private String productId;
    private double price;
    private Timestamp created;
    private String pixCode;
    private OrderStatus status;
    
    public enum OrderStatus {
        PENDING,
        PAID,
        CANCELLED,
        EXPIRED
    }
    
    public Order() {}
    
    public Order(String playerName, UUID playerUuid, String productId, double price) {
        this.playerName = playerName;
        this.playerUuid = playerUuid;
        this.productId = productId;
        this.price = price;
        this.created = new Timestamp(System.currentTimeMillis());
        this.status = OrderStatus.PENDING;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public UUID getPlayerUuid() {
        return playerUuid;
    }
    
    public void setPlayerUuid(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public Timestamp getCreated() {
        return created;
    }
    
    public void setCreated(Timestamp created) {
        this.created = created;
    }
    
    public String getPixCode() {
        return pixCode;
    }
    
    public void setPixCode(String pixCode) {
        this.pixCode = pixCode;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", playerName='" + playerName + '\'' +
                ", playerUuid=" + playerUuid +
                ", productId='" + productId + '\'' +
                ", price=" + price +
                ", created=" + created +
                ", pixCode='" + pixCode + '\'' +
                ", status=" + status +
                '}';
    }
}