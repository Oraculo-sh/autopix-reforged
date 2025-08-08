package io.github.warleysr.autopixreforged.inventory;

import io.github.warleysr.autopixreforged.domain.Product;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class ConfirmationMenu extends ChestMenu {
    
    private final Container container;
    private final ServerPlayer player;
    private final Product product;
    
    public ConfirmationMenu(int containerId, Inventory playerInventory, Container container, Product product) {
        super(MenuType.GENERIC_9x3, containerId, playerInventory, container, 3);
        this.container = container;
        this.player = (ServerPlayer) playerInventory.player;
        this.product = product;
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
            case "confirm" -> handleConfirmPurchase();
            case "cancel" -> handleCancelPurchase();
        }
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
    
    private void handleConfirmPurchase() {
        player.closeContainer();
        
        // Process the purchase
        ShopMenu.processPurchase(product, player);
    }
    
    private void handleCancelPurchase() {
        player.closeContainer();
        
        // Reopen shop menu
        ShopMenuProvider shopMenu = new ShopMenuProvider();
        player.openMenu(shopMenu);
    }
}