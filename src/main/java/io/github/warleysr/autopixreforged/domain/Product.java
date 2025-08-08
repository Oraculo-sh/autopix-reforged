package io.github.warleysr.autopixreforged.domain;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class Product {
    private String id;
    private String name;
    private double price;
    private Item item;
    private List<String> preCommands;
    private List<String> commands;
    private List<String> description;
    
    public Product() {}
    
    public Product(String id, String name, double price, String itemId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.item = getItemFromString(itemId);
    }
    
    private Item getItemFromString(String itemId) {
        try {
            ResourceLocation resourceLocation = new ResourceLocation(itemId);
            Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
            return item != null ? item : Items.BARRIER;
        } catch (Exception e) {
            return Items.BARRIER;
        }
    }
    
    public ItemStack createItemStack() {
        ItemStack stack = new ItemStack(item);
        // TODO: Add custom name and lore to the item
        return stack;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public Item getItem() {
        return item;
    }
    
    public void setItem(Item item) {
        this.item = item;
    }
    
    public void setItem(String itemId) {
        this.item = getItemFromString(itemId);
    }
    
    public List<String> getPreCommands() {
        return preCommands;
    }
    
    public void setPreCommands(List<String> preCommands) {
        this.preCommands = preCommands;
    }
    
    public List<String> getCommands() {
        return commands;
    }
    
    public void setCommands(List<String> commands) {
        this.commands = commands;
    }
    
    public List<String> getDescription() {
        return description;
    }
    
    public void setDescription(List<String> description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", item=" + item +
                '}';
    }
}