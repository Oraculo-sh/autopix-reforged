package io.github.warleysr.autopixreforged.inventory;

import io.github.warleysr.autopixreforged.AutoPixReforged;
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
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ConfirmationMenuProvider implements MenuProvider {
    
    private final Product product;
    private static final int MENU_SIZE = 27; // 3 rows
    
    public ConfirmationMenuProvider(Product product) {
        this.product = product;
    }
    
    @Override
    public Component getDisplayName() {
        return Component.literal(AutoPixReforged.getConfig().getMenuConfig().getString("confirmar.titulo"));
    }
    
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        SimpleContainer container = new SimpleContainer(MENU_SIZE);
        
        setupConfirmationMenu(container, (ServerPlayer) player);
        
        return new ConfirmationMenu(containerId, playerInventory, container, product);
    }
    
    private void setupConfirmationMenu(SimpleContainer container, ServerPlayer player) {
        // Clear container
        container.clearContent();
        
        // Add decorative glass panes
        ItemStack glassPane = new ItemStack(Items.GRAY_STAINED_GLASS_PANE);
        glassPane.setHoverName(Component.literal(" "));
        
        // Fill borders
        for (int i = 0; i < 9; i++) {
            container.setItem(i, glassPane.copy());
            container.setItem(18 + i, glassPane.copy());
        }
        container.setItem(9, glassPane.copy());
        container.setItem(17, glassPane.copy());
        
        // Product item in center
        ItemStack productItem = createProductDisplayItem();
        container.setItem(13, productItem);
        
        // Confirm button (green wool)
        ItemStack confirmItem = new ItemStack(Items.GREEN_WOOL);
        confirmItem.setHoverName(Component.literal("§a§lConfirmar Compra"));
        
        List<Component> confirmLore = new ArrayList<>();
        confirmLore.add(Component.literal("§7Clique para confirmar a compra"));
        confirmLore.add(Component.literal("§7do produto: §f" + product.getName()));
        confirmLore.add(Component.literal("§7Valor: §a R$ " + String.format("%.2f", product.getPrice())));
        
        CompoundTag confirmTag = confirmItem.getOrCreateTag();
        confirmTag.putString("autopix_action", "confirm");
        addLoreToItem(confirmItem, confirmLore);
        
        container.setItem(11, confirmItem);
        
        // Cancel button (red wool)
        ItemStack cancelItem = new ItemStack(Items.RED_WOOL);
        cancelItem.setHoverName(Component.literal("§c§lCancelar"));
        
        List<Component> cancelLore = new ArrayList<>();
        cancelLore.add(Component.literal("§7Clique para cancelar"));
        cancelLore.add(Component.literal("§7e voltar ao menu principal"));
        
        CompoundTag cancelTag = cancelItem.getOrCreateTag();
        cancelTag.putString("autopix_action", "cancel");
        addLoreToItem(cancelItem, cancelLore);
        
        container.setItem(15, cancelItem);
    }
    
    private ItemStack createProductDisplayItem() {
        ItemStack item = product.createItemStack();
        item.setHoverName(Component.literal("§a§l" + product.getName()));
        
        List<Component> lore = new ArrayList<>();
        lore.add(Component.literal(""));
        lore.add(Component.literal("§7Preço: §a R$ " + String.format("%.2f", product.getPrice())));
        lore.add(Component.literal(""));
        
        if (product.getDescription() != null && !product.getDescription().isEmpty()) {
            String[] descLines = product.getDescription().split("\\n");
            for (String line : descLines) {
                lore.add(Component.literal("§7" + line));
            }
            lore.add(Component.literal(""));
        }
        
        if (product.getCommands() != null && !product.getCommands().isEmpty()) {
            lore.add(Component.literal("§6Comandos que serão executados:"));
            for (String command : product.getCommands()) {
                lore.add(Component.literal("§7- " + command.replace("%player%", "<jogador>")));
            }
        }
        
        addLoreToItem(item, lore);
        
        return item;
    }
    
    private void addLoreToItem(ItemStack item, List<Component> lore) {
        CompoundTag tag = item.getOrCreateTag();
        CompoundTag display = tag.getCompound("display");
        ListTag loreList = new ListTag();
        
        for (Component line : lore) {
            loreList.add(StringTag.valueOf(Component.Serializer.toJson(line)));
        }
        
        display.put("Lore", loreList);
        tag.put("display", display);
    }
}