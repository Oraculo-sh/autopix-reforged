package io.github.oraculo.autopix.manager;

import io.github.oraculo.autopix.config.AutoPixConfig;
import io.github.oraculo.autopix.domain.PixTransaction;
import io.github.oraculo.autopix.utils.MessageUtils;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Gerenciador de transações PIX
 * Baseado no OrderManager original do plugin
 */
public class TransactionManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private static TransactionManager instance;
    
    private final Map<UUID, PixTransaction> activeTransactions = new ConcurrentHashMap<>();
    private final Map<String, PixTransaction> transactionsByPixCode = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    
    private TransactionManager() {
        // Inicia a tarefa de limpeza de transações expiradas
        startCleanupTask();
    }
    
    public static TransactionManager getInstance() {
        if (instance == null) {
            instance = new TransactionManager();
        }
        return instance;
    }
    
    /**
     * Cria uma nova transação PIX
     */
    public PixTransaction createTransaction(ServerPlayer player, String pixCode, double amount, String description) {
        // Remove transação anterior do jogador se existir
        removePlayerTransaction(player.getUUID());
        
        PixTransaction transaction = new PixTransaction(
            player.getName().getString(),
            player.getUUID(),
            pixCode,
            amount,
            description
        );
        
        activeTransactions.put(player.getUUID(), transaction);
        transactionsByPixCode.put(pixCode, transaction);
        
        LOGGER.info("Nova transação criada: {}", transaction);
        
        // Agenda expiração da transação
        scheduleTransactionExpiration(transaction);
        
        return transaction;
    }
    
    /**
     * Valida uma transação PIX
     */
    public boolean validateTransaction(String pixCode) {
        PixTransaction transaction = transactionsByPixCode.get(pixCode);
        
        if (transaction == null) {
            LOGGER.warn("Tentativa de validar transação inexistente: {}", pixCode);
            return false;
        }
        
        if (transaction.getStatus() != PixTransaction.TransactionStatus.PENDING) {
            LOGGER.warn("Tentativa de validar transação não pendente: {}", transaction);
            return false;
        }
        
        // Verifica se a transação expirou
        if (transaction.isExpired(AutoPixConfig.TIMEOUT_VALIDATION.get())) {
            expireTransaction(transaction);
            return false;
        }
        
        // Valida a transação
        transaction.validate();
        
        LOGGER.info("Transação validada: {}", transaction);
        
        // Remove das coleções ativas
        activeTransactions.remove(transaction.getPlayerId());
        transactionsByPixCode.remove(pixCode);
        
        return true;
    }
    
    /**
     * Obtém a transação ativa de um jogador
     */
    public PixTransaction getPlayerTransaction(UUID playerId) {
        return activeTransactions.get(playerId);
    }
    
    /**
     * Obtém uma transação pelo código PIX
     */
    public PixTransaction getTransactionByPixCode(String pixCode) {
        return transactionsByPixCode.get(pixCode);
    }
    
    /**
     * Remove a transação de um jogador
     */
    public void removePlayerTransaction(UUID playerId) {
        PixTransaction transaction = activeTransactions.remove(playerId);
        if (transaction != null) {
            transactionsByPixCode.remove(transaction.getPixCode());
            LOGGER.info("Transação removida: {}", transaction);
        }
    }
    
    /**
     * Cancela a transação de um jogador
     */
    public boolean cancelPlayerTransaction(UUID playerId) {
        PixTransaction transaction = activeTransactions.get(playerId);
        if (transaction != null && transaction.getStatus() == PixTransaction.TransactionStatus.PENDING) {
            transaction.cancel();
            removePlayerTransaction(playerId);
            return true;
        }
        return false;
    }
    
    /**
     * Agenda a expiração de uma transação
     */
    private void scheduleTransactionExpiration(PixTransaction transaction) {
        scheduler.schedule(() -> {
            if (activeTransactions.containsKey(transaction.getPlayerId()) && 
                transaction.getStatus() == PixTransaction.TransactionStatus.PENDING) {
                expireTransaction(transaction);
            }
        }, AutoPixConfig.TIMEOUT_VALIDATION.get(), TimeUnit.MINUTES);
    }
    
    /**
     * Expira uma transação
     */
    private void expireTransaction(PixTransaction transaction) {
        transaction.expire();
        activeTransactions.remove(transaction.getPlayerId());
        transactionsByPixCode.remove(transaction.getPixCode());
        
        LOGGER.info("Transação expirada: {}", transaction);
        
        // TODO: Notificar o jogador se estiver online
    }
    
    /**
     * Inicia a tarefa de limpeza de transações expiradas
     */
    private void startCleanupTask() {
        scheduler.scheduleAtFixedRate(() -> {
            int timeoutMinutes = AutoPixConfig.TIMEOUT_VALIDATION.get();
            
            activeTransactions.values().removeIf(transaction -> {
                if (transaction.isExpired(timeoutMinutes) && 
                    transaction.getStatus() == PixTransaction.TransactionStatus.PENDING) {
                    
                    transactionsByPixCode.remove(transaction.getPixCode());
                    expireTransaction(transaction);
                    return true;
                }
                return false;
            });
        }, 1, 1, TimeUnit.MINUTES);
    }
    
    /**
     * Obtém o número de transações ativas
     */
    public int getActiveTransactionCount() {
        return activeTransactions.size();
    }
    
    /**
     * Para o gerenciador e limpa recursos
     */
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        activeTransactions.clear();
        transactionsByPixCode.clear();
        
        LOGGER.info("TransactionManager finalizado");
    }
}