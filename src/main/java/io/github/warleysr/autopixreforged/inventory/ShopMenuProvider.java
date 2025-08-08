package io.github.warleysr.autopixreforged.inventory;

import io.github.warleysr.autopixreforged.AutoPixReforged;
import io.github.warleysr.autopixreforged.config.AutoPixConfig;
import io.github.warleysr.autopixreforged.domain.Product;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.SimpleContainer;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nullable;
import java.util.List;

public class ShopMenuProvider implements MenuProvider {
    
    private static final int MENU_SIZE = 54; // 6 rows
    
    @Override
    public Component getDisplayName() {
        return Component.literal(AutoPixReforged.getConfig().getMessage("menu_title"));
    }
    
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        SimpleContainer container = new SimpleContainer(MENU_SIZE);
        
        // Fill menu with products and decorative items
        setupMenuItems(container, (ServerPlayer) player);
        
        return new ShopMenu(containerId, playerInventory, container);
    }
    
    private void setupMenuItems(SimpleContainer container, ServerPlayer player) {
        // Clear container
        container.clearContent();
        
        // Add decorative glass panes
        ItemStack glassPane = new ItemStack(Items.GRAY_STAINED_GLASS_PANE);
        glassPane.setHoverName(Component.literal(" "));
        
        // Fill borders with glass panes
        for (int i = 0; i < 9; i++) {
            container.setItem(i, glassPane.copy());
            container.setItem(45 + i, glassPane.copy());
        }
        for (int i = 9; i < 45; i += 9) {
            container.setItem(i, glassPane.copy());
            container.setItem(i + 8, glassPane.copy());
        }
        
        // Add products
        List<Product> products = AutoPixReforged.getConfig().getProductsList();
        int slot = 10; // Start from second row, second column
        
        for (Product product : products) {
            if (slot >= 44) break; // Don't exceed menu bounds
            
            ItemStack productItem = createProductItem(product, player);
            container.setItem(slot, productItem);
            
            slot++;
            if ((slot + 1) % 9 == 0) slot += 2; // Skip border slots
        }
        
        // Add info item
        ItemStack infoItem = new ItemStack(Items.BOOK);
        infoItem.setHoverName(Component.literal("§a§lInformações"));
        CompoundTag infoTag = infoItem.getOrCreateTag();
        infoTag.putString("autopix_action", "info");
        container.setItem(49, infoItem);
        
        // Add close item
        ItemStack closeItem = new ItemStack(Items.BARRIER);
        closeItem.setHoverName(Component.literal("§c§lFechar"));
        CompoundTag closeTag = closeItem.getOrCreateTag();
        closeTag.putString("autopix_action", "close");
        container.setItem(53, closeItem);
    }
    
    private ItemStack createProductItem(Product product, ServerPlayer player) {
        ItemStack item = product.createItemStack();
        
        // Set display name
        item.setHoverName(Component.literal("§a§l" + product.getName()));
        
        // Add NBT data for identification
        CompoundTag tag = item.getOrCreateTag();
        tag.putString("autopix_product", product.getId());
        tag.putString("autopix_action", "buy");
        
        return item;
    }
}