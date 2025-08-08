package io.github.warleysr.autopixreforged.domain;

public class PixData {
    private String paymentId;
    private int orderId;
    private String status;
    private String qrCode;
    private String qrCodeBase64;
    
    public PixData() {}
    
    public PixData(String paymentId, int orderId, String status, String qrCode) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.status = status;
        this.qrCode = qrCode;
    }
    
    // Getters and Setters
    public String getPaymentId() {
        return paymentId;
    }
    
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getQrCode() {
        return qrCode;
    }
    
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
    
    public String getQrCodeBase64() {
        return qrCodeBase64;
    }
    
    public void setQrCodeBase64(String qrCodeBase64) {
        this.qrCodeBase64 = qrCodeBase64;
    }
    
    public boolean isPending() {
        return "pending".equalsIgnoreCase(status);
    }
    
    public boolean isApproved() {
        return "approved".equalsIgnoreCase(status);
    }
    
    public boolean isCancelled() {
        return "cancelled".equalsIgnoreCase(status);
    }
    
    @Override
    public String toString() {
        return "PixData{" +
                "paymentId='" + paymentId + '\'' +
                ", orderId=" + orderId +
                ", status='" + status + '\'' +
                ", qrCode='" + qrCode + '\'' +
                '}';
    }
}