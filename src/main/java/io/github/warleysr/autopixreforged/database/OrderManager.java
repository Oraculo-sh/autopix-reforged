package io.github.warleysr.autopixreforged.database;

import io.github.warleysr.autopixreforged.AutoPixReforged;
import io.github.warleysr.autopixreforged.config.AutoPixConfig;
import io.github.warleysr.autopixreforged.domain.Order;
import io.github.warleysr.autopixreforged.domain.PixData;
import io.github.warleysr.autopixreforged.mercadopago.MercadoPagoAPI;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class OrderManager {
    private static Connection connection;
    private static AutoPixConfig config;
    
    public static void initialize(AutoPixConfig cfg) throws SQLException {
        config = cfg;
        setupDatabase();
        createTables();
    }
    
    private static void setupDatabase() throws SQLException {
        String dbType = config.getDatabaseType();
        
        if ("mysql".equalsIgnoreCase(dbType)) {
            String url = String.format("jdbc:mysql://%s:%d/%s?autoReconnect=true&characterEncoding=utf8&useSSL=false",
                    config.getDatabaseHost(),
                    config.getDatabasePort(),
                    config.getDatabaseName());
            
            connection = DriverManager.getConnection(url, config.getDatabaseUsername(), config.getDatabasePassword());
        } else if ("sqlite".equalsIgnoreCase(dbType)) {
            String url = "jdbc:sqlite:config/autopix-reforged/autopix.db";
            connection = DriverManager.getConnection(url);
            connection.createStatement().execute("PRAGMA busy_timeout = 5000;");
        } else {
            throw new SQLException("Tipo de banco de dados não suportado: " + dbType);
        }
    }
    
    private static void createTables() throws SQLException {
        String autoIncrement = "mysql".equalsIgnoreCase(config.getDatabaseType()) ? "AUTO_INCREMENT" : "AUTOINCREMENT";
        
        // Create orders table
        String createOrdersTable = "CREATE TABLE IF NOT EXISTS autopix_orders (" +
                "id INTEGER PRIMARY KEY " + autoIncrement + ", " +
                "player_name VARCHAR(16) NOT NULL, " +
                "player_uuid VARCHAR(36) NOT NULL, " +
                "product_id VARCHAR(32) NOT NULL, " +
                "price DECIMAL(10, 2) NOT NULL, " +
                "created TIMESTAMP NOT NULL, " +
                "pix_code VARCHAR(32) UNIQUE NULL, " +
                "status VARCHAR(16) DEFAULT 'PENDING'" +
                ")";
        
        connection.prepareStatement(createOrdersTable).executeUpdate();
        
        // Create pix_data table
        String createPixDataTable = "CREATE TABLE IF NOT EXISTS autopix_pix_data (" +
                "payment_id VARCHAR(128) PRIMARY KEY, " +
                "order_id INTEGER NOT NULL UNIQUE, " +
                "status VARCHAR(16), " +
                "qr_code TEXT NOT NULL, " +
                "qr_code_base64 TEXT" +
                ")";
        
        connection.prepareStatement(createPixDataTable).executeUpdate();
    }
    
    public static Order createOrder(ServerPlayer player, String productId, double price) {
        try {
            String sql = "INSERT INTO autopix_orders (player_name, player_uuid, product_id, price, created, status) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, player.getName().getString());
            ps.setString(2, player.getUUID().toString());
            ps.setString(3, productId);
            ps.setDouble(4, price);
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            ps.setString(6, Order.OrderStatus.PENDING.name());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int orderId = rs.getInt(1);
                return getOrderById(orderId);
            }
            
        } catch (SQLException e) {
            AutoPixReforged.getLogger().error("Erro ao criar pedido: ", e);
        }
        return null;
    }
    
    public static Order getOrderById(int orderId) {
        try {
            String sql = "SELECT * FROM autopix_orders WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, orderId);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToOrder(rs);
            }
        } catch (SQLException e) {
            AutoPixReforged.getLogger().error("Erro ao buscar pedido: ", e);
        }
        return null;
    }
    
    public static Order getLastOrderByPlayer(String playerName) {
        try {
            String sql = "SELECT * FROM autopix_orders WHERE player_name = ? ORDER BY created DESC LIMIT 1";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, playerName);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToOrder(rs);
            }
        } catch (SQLException e) {
            AutoPixReforged.getLogger().error("Erro ao buscar último pedido: ", e);
        }
        return null;
    }
    
    public static List<Order> getOrdersByPlayer(String playerName) {
        List<Order> orders = new ArrayList<>();
        try {
            String sql = "SELECT * FROM autopix_orders WHERE player_name = ? ORDER BY created DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, playerName);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
        } catch (SQLException e) {
            AutoPixReforged.getLogger().error("Erro ao buscar pedidos do jogador: ", e);
        }
        return orders;
    }
    
    public static boolean savePixData(PixData pixData) {
        try {
            String sql = "INSERT INTO autopix_pix_data (payment_id, order_id, status, qr_code, qr_code_base64) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            
            ps.setString(1, pixData.getPaymentId());
            ps.setInt(2, pixData.getOrderId());
            ps.setString(3, pixData.getStatus());
            ps.setString(4, pixData.getQrCode());
            ps.setString(5, pixData.getQrCodeBase64());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            AutoPixReforged.getLogger().error("Erro ao salvar dados PIX: ", e);
        }
        return false;
    }
    
    public static PixData getPixDataByOrderId(int orderId) {
        try {
            String sql = "SELECT * FROM autopix_pix_data WHERE order_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, orderId);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToPixData(rs);
            }
        } catch (SQLException e) {
            AutoPixReforged.getLogger().error("Erro ao buscar dados PIX: ", e);
        }
        return null;
    }
    
    public static boolean updatePixDataStatus(String paymentId, String status) {
        try {
            String sql = "UPDATE autopix_pix_data SET status = ? WHERE payment_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, paymentId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            AutoPixReforged.getLogger().error("Erro ao atualizar status PIX: ", e);
        }
        return false;
    }
    
    public static boolean updateOrderStatus(int orderId, Order.OrderStatus status) {
        try {
            String sql = "UPDATE autopix_orders SET status = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, status.name());
            ps.setInt(2, orderId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            AutoPixReforged.getLogger().error("Erro ao atualizar status do pedido: ", e);
        }
        return false;
    }
    
    public static void validatePendingOrders() {
        try {
            String sql = "SELECT pd.* FROM autopix_pix_data pd " +
                    "JOIN autopix_orders o ON pd.order_id = o.id " +
                    "WHERE pd.status = 'pending' AND o.status = 'PENDING'";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                PixData pixData = mapResultSetToPixData(rs);
                try {
                    // Check payment status with MercadoPago
                    String status = MercadoPagoAPI.checkPaymentStatus(pixData.getPaymentId());
                    
                    if ("approved".equals(status)) {
                        // Payment approved, process order
                        updatePixDataStatus(pixData.getPaymentId(), "approved");
                        updateOrderStatus(pixData.getOrderId(), Order.OrderStatus.PAID);
                        
                        Order order = getOrderById(pixData.getOrderId());
                        if (order != null) {
                            processApprovedOrder(order);
                        }
                    } else if ("cancelled".equals(status) || "rejected".equals(status)) {
                        // Payment cancelled/rejected
                        updatePixDataStatus(pixData.getPaymentId(), status);
                        updateOrderStatus(pixData.getOrderId(), Order.OrderStatus.CANCELLED);
                    }
                } catch (Exception e) {
                    AutoPixReforged.getLogger().error("Erro ao validar pagamento " + pixData.getPaymentId() + ": ", e);
                }
            }
        } catch (SQLException e) {
            AutoPixReforged.getLogger().error("Erro ao validar pedidos pendentes: ", e);
        }
    }
    
    public static void cleanupExpiredMaps() {
        try {
            long timeoutMillis = TimeUnit.MINUTES.toMillis(config.getPaymentTimeoutMinutes());
            long cutoffTime = System.currentTimeMillis() - timeoutMillis;
            
            String sql = "SELECT o.*, pd.payment_id FROM autopix_orders o " +
                    "LEFT JOIN autopix_pix_data pd ON o.id = pd.order_id " +
                    "WHERE o.status = 'PENDING' AND o.created < ?";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setTimestamp(1, new Timestamp(cutoffTime));
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("id");
                String paymentId = rs.getString("payment_id");
                
                // Cancel order
                updateOrderStatus(orderId, Order.OrderStatus.EXPIRED);
                
                // Cancel payment if exists
                if (paymentId != null) {
                    updatePixDataStatus(paymentId, "cancelled");
                    try {
                        MercadoPagoAPI.cancelPayment(paymentId);
                    } catch (Exception e) {
                        AutoPixReforged.getLogger().error("Erro ao cancelar pagamento " + paymentId + ": ", e);
                    }
                }
            }
        } catch (SQLException e) {
            AutoPixReforged.getLogger().error("Erro ao limpar mapas expirados: ", e);
        }
    }
    
    private static void processApprovedOrder(Order order) {
        // TODO: Execute commands for approved order
        // This will be implemented when we create the command execution system
        AutoPixReforged.getLogger().info("Pedido aprovado: {} - Produto: {} - Jogador: {}", 
                order.getId(), order.getProductId(), order.getPlayerName());
    }
    
    private static Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setPlayerName(rs.getString("player_name"));
        order.setPlayerUuid(UUID.fromString(rs.getString("player_uuid")));
        order.setProductId(rs.getString("product_id"));
        order.setPrice(rs.getDouble("price"));
        order.setCreated(rs.getTimestamp("created"));
        order.setPixCode(rs.getString("pix_code"));
        order.setStatus(Order.OrderStatus.valueOf(rs.getString("status")));
        return order;
    }
    
    private static PixData mapResultSetToPixData(ResultSet rs) throws SQLException {
        PixData pixData = new PixData();
        pixData.setPaymentId(rs.getString("payment_id"));
        pixData.setOrderId(rs.getInt("order_id"));
        pixData.setStatus(rs.getString("status"));
        pixData.setQrCode(rs.getString("qr_code"));
        pixData.setQrCodeBase64(rs.getString("qr_code_base64"));
        return pixData;
    }
}