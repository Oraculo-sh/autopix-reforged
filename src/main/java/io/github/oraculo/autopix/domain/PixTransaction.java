package io.github.oraculo.autopix.domain;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa uma transação PIX no sistema
 */
public class PixTransaction {
    private final UUID id;
    private final String playerName;
    private final UUID playerId;
    private final String pixCode;
    private final double amount;
    private final String description;
    private final LocalDateTime createdAt;
    private TransactionStatus status;
    private LocalDateTime validatedAt;
    private String mercadoPagoId;
    
    public enum TransactionStatus {
        PENDING,
        VALIDATED,
        EXPIRED,
        CANCELLED
    }
    
    public PixTransaction(String playerName, UUID playerId, String pixCode, double amount, String description) {
        this.id = UUID.randomUUID();
        this.playerName = playerName;
        this.playerId = playerId;
        this.pixCode = pixCode;
        this.amount = amount;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.status = TransactionStatus.PENDING;
    }
    
    // Getters
    public UUID getId() { return id; }
    public String getPlayerName() { return playerName; }
    public UUID getPlayerId() { return playerId; }
    public String getPixCode() { return pixCode; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public TransactionStatus getStatus() { return status; }
    public LocalDateTime getValidatedAt() { return validatedAt; }
    public String getMercadoPagoId() { return mercadoPagoId; }
    
    // Setters
    public void setStatus(TransactionStatus status) { this.status = status; }
    public void setValidatedAt(LocalDateTime validatedAt) { this.validatedAt = validatedAt; }
    public void setMercadoPagoId(String mercadoPagoId) { this.mercadoPagoId = mercadoPagoId; }
    
    /**
     * Verifica se a transação expirou
     */
    public boolean isExpired(int timeoutMinutes) {
        return createdAt.plusMinutes(timeoutMinutes).isBefore(LocalDateTime.now());
    }
    
    /**
     * Marca a transação como validada
     */
    public void validate() {
        this.status = TransactionStatus.VALIDATED;
        this.validatedAt = LocalDateTime.now();
    }
    
    /**
     * Marca a transação como expirada
     */
    public void expire() {
        this.status = TransactionStatus.EXPIRED;
    }
    
    /**
     * Marca a transação como cancelada
     */
    public void cancel() {
        this.status = TransactionStatus.CANCELLED;
    }
    
    @Override
    public String toString() {
        return String.format("PixTransaction{id=%s, player=%s, amount=%.2f, status=%s}", 
                           id, playerName, amount, status);
    }
}