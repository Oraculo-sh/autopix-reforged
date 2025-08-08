package io.github.warleysr.autopixreforged.inventory;

import io.github.warleysr.autopixreforged.AutoPixReforged;
import io.github.warleysr.autopixreforged.database.OrderManager;
import io.github.warleysr.autopixreforged.domain.Order;
import io.github.warleysr.autopixreforged.domain.PixData;
import io.github.warleysr.autopixreforged.domain.Product;
import io.github.warleysr.autopixreforged.mercadopago.MercadoPagoAPI;
import io.github.warleysr.autopixreforged.utils.QRCodeGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import java.util.concurrent.CompletableFuture;

public class ShopMenu extends ChestMenu {
    
    private final Container container;
    private final ServerPlayer player;
    
    public ShopMenu(int containerId, Inventory playerInventory, Container container) {
        super(MenuType.GENERIC_9x6, containerId, playerInventory, container, 6);
        this.container = container;
        this.player = (ServerPlayer) playerInventory.player;
    }
    
    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (slotId < 0 || slotId >= container.getContainerSize()) {
            return;
        }
        
        ItemStack clickedItem = container.getItem(slotId);
        if (clickedItem.isEmpty()) {
            return;
        }
        
        CompoundTag tag = clickedItem.getTag();
        if (tag == null) {
            return;
        }
        
        String action = tag.getString("autopix_action");
        
        switch (action) {
            case "buy" -> handleProductPurchase(tag.getString("autopix_product"));
            case "info" -> handleInfoClick();
            case "close" -> handleCloseClick();
        }
        
        // Prevent item movement
    }
    
    @Override
    public boolean stillValid(Player player) {
        return true;
    }
    
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // Prevent shift-clicking items
        return ItemStack.EMPTY;
    }
    
    private void handleProductPurchase(String productId) {
        Product product = AutoPixReforged.getConfig().getProductById(productId);
        if (product == null) {
            player.sendSystemMessage(Component.literal("§cProduto não encontrado!"));
            return;
        }
        
        // Close current menu
        player.closeContainer();
        
        // Show confirmation menu
        ConfirmationMenuProvider confirmationMenu = new ConfirmationMenuProvider(product);
        player.openMenu(confirmationMenu);
    }
    
    private void handleInfoClick() {
        player.closeContainer();
        
        // Send info messages
        player.sendSystemMessage(Component.literal("§a§l=== AutoPix Reforged - Informações ==="));
        player.sendSystemMessage(Component.literal("§71. Clique no produto desejado"));
        player.sendSystemMessage(Component.literal("§72. Confirme a compra"));
        player.sendSystemMessage(Component.literal("§73. Escaneie o QR Code ou copie o código PIX"));
        player.sendSystemMessage(Component.literal("§74. Após o pagamento, use §b/pix validar <codigo>"));
        player.sendSystemMessage(Component.literal("§7O código PIX tem 32 caracteres e começa com 'E'"));
        player.sendSystemMessage(Component.literal("§7Exemplo: §fE00416968202301162037NF2oRtQ73bY"));
    }
    
    private void handleCloseClick() {
        player.closeContainer();
    }
    
    public static void processPurchase(Product product, ServerPlayer player) {
        CompletableFuture.runAsync(() -> {
            try {
                // Create order
                Order order = OrderManager.createOrder(player, product.getId(), product.getPrice());
                
                if (order == null) {
                    player.sendSystemMessage(Component.literal("§cErro ao criar pedido. Tente novamente."));
                    return;
                }
                
                // Create PIX payment
                PixData pixData = MercadoPagoAPI.createPixPayment(order);
                if (pixData != null) {
                    // Save PIX data
                    OrderManager.savePixData(pixData);
                    
                    // Generate QR Code
                    String qrCodePath = QRCodeGenerator.generateQRCode(pixData.getQrCode(), order.getId());
                    
                    // Send payment info to player
                    player.sendSystemMessage(Component.literal("§a§l=== Pagamento PIX Gerado ==="));
                    player.sendSystemMessage(Component.literal("§7Produto: §f" + product.getName()));
                    player.sendSystemMessage(Component.literal("§7Valor: §a R$ " + String.format("%.2f", product.getPrice())));
                    player.sendSystemMessage(Component.literal("§7Código PIX: §b" + pixData.getQrCode()));
                    
                    if (qrCodePath != null) {
                        player.sendSystemMessage(Component.literal("§7QR Code salvo em: §f" + qrCodePath));
                    }
                    
                    player.sendSystemMessage(Component.literal("§7Use §b/pix validar " + pixData.getQrCode() + " §7após o pagamento"));
                    
                    int paymentTimeout = AutoPixReforged.getConfig().getPaymentTimeoutMinutes();
                    player.sendSystemMessage(Component.literal("§cTempo para pagamento: " + paymentTimeout + " minutos"));
                
                } else {
                    player.sendSystemMessage(Component.literal("§cErro ao gerar pagamento PIX. Tente novamente."));
                    OrderManager.updateOrderStatus(order.getId(), Order.OrderStatus.CANCELLED);
                }
                
            } catch (Exception e) {
                AutoPixReforged.getLogger().error("Erro ao processar compra: ", e);
                player.sendSystemMessage(Component.literal("§cErro interno. Contate um administrador."));
            }
        });
    }
}