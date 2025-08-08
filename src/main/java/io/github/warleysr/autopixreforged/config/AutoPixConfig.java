package io.github.warleysr.autopixreforged.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.warleysr.autopixreforged.domain.Product;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class AutoPixConfig {
    private static final String CONFIG_DIR = "config/autopix-reforged";
    private static final String CONFIG_FILE = "config.json";
    private static final String MESSAGES_FILE = "messages.json";
    
    private JsonObject config;
    private JsonObject messages;
    private final Gson gson;
    
    public AutoPixConfig() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadConfigs();
    }
    
    private void loadConfigs() {
        try {
            // Create config directory if it doesn't exist
            Path configDir = Paths.get(CONFIG_DIR);
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }
            
            // Load main config
            loadMainConfig();
            
            // Load messages
            loadMessages();
            
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar configurações", e);
        }
    }
    
    private void loadMainConfig() throws IOException {
        Path configPath = Paths.get(CONFIG_DIR, CONFIG_FILE);
        
        if (!Files.exists(configPath)) {
            // Create default config
            createDefaultConfig(configPath);
        }
        
        String content = Files.readString(configPath);
        this.config = JsonParser.parseString(content).getAsJsonObject();
    }
    
    private void loadMessages() throws IOException {
        Path messagesPath = Paths.get(CONFIG_DIR, MESSAGES_FILE);
        
        if (!Files.exists(messagesPath)) {
            // Create default messages
            createDefaultMessages(messagesPath);
        }
        
        String content = Files.readString(messagesPath);
        this.messages = JsonParser.parseString(content).getAsJsonObject();
    }
    
    private void createDefaultConfig(Path configPath) throws IOException {
        JsonObject defaultConfig = new JsonObject();
        
        // MercadoPago token
        defaultConfig.addProperty("mercadopago_token", "SEU_TOKEN_DO_MERCADO_PAGO");
        
        // PIX settings
        JsonObject pixConfig = new JsonObject();
        pixConfig.addProperty("key", "SUA_CHAVE_PIX");
        pixConfig.addProperty("name", "Fulano de Tal");
        pixConfig.addProperty("debug", false);
        pixConfig.addProperty("send_map", true);
        defaultConfig.add("pix", pixConfig);
        
        // Automatic validation
        JsonObject autoConfig = new JsonObject();
        autoConfig.addProperty("enabled", true);
        autoConfig.addProperty("interval_seconds", 10);
        defaultConfig.add("automatic", autoConfig);
        
        // Database settings
        JsonObject dbConfig = new JsonObject();
        dbConfig.addProperty("type", "sqlite");
        dbConfig.addProperty("host", "localhost");
        dbConfig.addProperty("port", 3306);
        dbConfig.addProperty("database", "autopix");
        dbConfig.addProperty("username", "root");
        dbConfig.addProperty("password", "password");
        defaultConfig.add("database", dbConfig);
        
        // Timeouts
        JsonObject timeouts = new JsonObject();
        timeouts.addProperty("validate_seconds", 60);
        timeouts.addProperty("list_seconds", 30);
        timeouts.addProperty("create_order_seconds", 300);
        timeouts.addProperty("cancel_seconds", 30);
        defaultConfig.add("timeouts", timeouts);
        
        // Map settings
        JsonObject mapConfig = new JsonObject();
        mapConfig.addProperty("name", "§aQR Code de Pagamento");
        mapConfig.addProperty("payment_timeout_minutes", 30);
        mapConfig.addProperty("cleanup_interval_seconds", 60);
        defaultConfig.add("map", mapConfig);
        
        // Products
        JsonObject products = new JsonObject();
        JsonObject vip1 = new JsonObject();
        vip1.addProperty("name", "§6§lVIP 1");
        vip1.addProperty("price", 10.0);
        vip1.addProperty("item", "minecraft:diamond_sword");
        
        JsonObject commands = new JsonObject();
        commands.add("pre_commands", gson.toJsonTree(Arrays.asList("say {player} gerou uma ordem PIX!")));
        commands.add("commands", gson.toJsonTree(Arrays.asList("say {player} pagou por PIX!", "give {player} minecraft:diamond 1")));
        vip1.add("commands", commands);
        
        products.add("vip1", vip1);
        defaultConfig.add("products", products);
        
        // Sound settings
        JsonObject soundConfig = new JsonObject();
        soundConfig.addProperty("enabled", true);
        soundConfig.addProperty("sound", "minecraft:block.note_block.bass");
        defaultConfig.add("sound", soundConfig);
        
        // Top donors
        defaultConfig.addProperty("top_donors_count", 5);
        
        Files.writeString(configPath, gson.toJson(defaultConfig));
    }
    
    private void createDefaultMessages(Path messagesPath) throws IOException {
        JsonObject defaultMessages = new JsonObject();
        
        defaultMessages.addProperty("prefix", "§8[§bAutoPix§8] ");
        defaultMessages.addProperty("no_permission", "§cVocê não tem permissão para usar este comando!");
        defaultMessages.addProperty("player_only", "§cEste comando só pode ser usado por jogadores!");
        defaultMessages.addProperty("invalid_product", "§cProduto inválido!");
        defaultMessages.addProperty("order_created", "§aPedido criado com sucesso! Use o mapa para pagar.");
        defaultMessages.addProperty("payment_validated", "§aPagamento validado com sucesso!");
        defaultMessages.addProperty("payment_not_found", "§cPagamento não encontrado!");
        defaultMessages.addProperty("payment_already_processed", "§cEste pagamento já foi processado!");
        defaultMessages.addProperty("database_error", "§cErro no banco de dados: {error}");
        defaultMessages.addProperty("mercadopago_error", "§cErro na API do MercadoPago: {error}");
        defaultMessages.addProperty("qr_generation_error", "§cErro ao gerar código QR!");
        defaultMessages.addProperty("config_reloaded", "§aConfigurações recarregadas com sucesso!");
        defaultMessages.addProperty("order_expired", "§cSeu pedido expirou!");
        defaultMessages.addProperty("order_cancelled", "§cPedido cancelado!");
        defaultMessages.addProperty("wait_cooldown", "§cAguarde {seconds} segundos antes de usar este comando novamente!");
        defaultMessages.addProperty("menu_title", "§9§lCompre produtos por §c§lPIX");
        defaultMessages.addProperty("confirm_title", "§9§lConfirmar compra por §c§lPIX");
        defaultMessages.addProperty("confirm_purchase", "§aConfirmar");
        defaultMessages.addProperty("cancel_purchase", "§cCancelar");
        defaultMessages.addProperty("price_format", "§aR$ {price}");
        
        Files.writeString(messagesPath, gson.toJson(defaultMessages));
    }
    
    // Getters for config values
    public String getMercadoPagoToken() {
        return config.get("mercadopago_token").getAsString();
    }
    
    public String getPixKey() {
        return config.getAsJsonObject("pix").get("key").getAsString();
    }
    
    public String getPixName() {
        return config.getAsJsonObject("pix").get("name").getAsString();
    }
    
    public boolean isPixDebugEnabled() {
        return config.getAsJsonObject("pix").get("debug").getAsBoolean();
    }
    
    public boolean shouldSendMap() {
        return config.getAsJsonObject("pix").get("send_map").getAsBoolean();
    }
    
    public boolean isAutomaticValidationEnabled() {
        return config.getAsJsonObject("automatic").get("enabled").getAsBoolean();
    }
    
    public int getAutomaticValidationInterval() {
        return config.getAsJsonObject("automatic").get("interval_seconds").getAsInt();
    }
    
    public String getDatabaseType() {
        return config.getAsJsonObject("database").get("type").getAsString();
    }
    
    public String getDatabaseHost() {
        return config.getAsJsonObject("database").get("host").getAsString();
    }
    
    public int getDatabasePort() {
        return config.getAsJsonObject("database").get("port").getAsInt();
    }
    
    public String getDatabaseName() {
        return config.getAsJsonObject("database").get("database").getAsString();
    }
    
    public String getDatabaseUsername() {
        return config.getAsJsonObject("database").get("username").getAsString();
    }
    
    public String getDatabasePassword() {
        return config.getAsJsonObject("database").get("password").getAsString();
    }
    
    public int getMapCleanupInterval() {
        return config.getAsJsonObject("map").get("cleanup_interval_seconds").getAsInt();
    }
    
    public int getPaymentTimeoutMinutes() {
        return config.getAsJsonObject("map").get("payment_timeout_minutes").getAsInt();
    }
    
    public JsonObject getProducts() {
        return config.getAsJsonObject("products");
    }
    
    public List<Product> getProductsList() {
        List<Product> products = new ArrayList<>();
        JsonObject productsObj = getProducts();
        
        for (String key : productsObj.keySet()) {
            JsonObject productData = productsObj.getAsJsonObject(key);
            Product product = new Product();
            product.setId(key);
            product.setName(productData.get("name").getAsString());
            product.setPrice(productData.get("price").getAsDouble());
            product.setItem(productData.get("item").getAsString());
            
            if (productData.has("commands")) {
                JsonObject commandsObj = productData.getAsJsonObject("commands");
                if (commandsObj.has("commands")) {
                    List<String> commands = new ArrayList<>();
                    commandsObj.getAsJsonArray("commands").forEach(element -> 
                        commands.add(element.getAsString()));
                    product.setCommands(commands);
                }
                if (commandsObj.has("pre_commands")) {
                    List<String> preCommands = new ArrayList<>();
                    commandsObj.getAsJsonArray("pre_commands").forEach(element -> 
                        preCommands.add(element.getAsString()));
                    product.setPreCommands(preCommands);
                }
            }
            
            products.add(product);
        }
        
        return products;
    }
    
    public Product getProductById(String id) {
        JsonObject productsObj = getProducts();
        if (!productsObj.has(id)) {
            return null;
        }
        
        JsonObject productData = productsObj.getAsJsonObject(id);
        Product product = new Product();
        product.setId(id);
        product.setName(productData.get("name").getAsString());
        product.setPrice(productData.get("price").getAsDouble());
        product.setItem(productData.get("item").getAsString());
        
        if (productData.has("commands")) {
            JsonObject commandsObj = productData.getAsJsonObject("commands");
            if (commandsObj.has("commands")) {
                List<String> commands = new ArrayList<>();
                commandsObj.getAsJsonArray("commands").forEach(element -> 
                    commands.add(element.getAsString()));
                product.setCommands(commands);
            }
            if (commandsObj.has("pre_commands")) {
                List<String> preCommands = new ArrayList<>();
                commandsObj.getAsJsonArray("pre_commands").forEach(element -> 
                    preCommands.add(element.getAsString()));
                product.setPreCommands(preCommands);
            }
        }
        
        return product;
    }
    
    public String getMessage(String key) {
        if (messages.has(key)) {
            return messages.get(key).getAsString();
        }
        return "§cMensagem não encontrada: " + key;
    }
    
    public String getFormattedMessage(String key, Object... args) {
        String message = getMessage(key);
        for (int i = 0; i < args.length; i += 2) {
            if (i + 1 < args.length) {
                message = message.replace("{" + args[i] + "}", String.valueOf(args[i + 1]));
            }
        }
        return message;
    }
    
    public void reload() {
        loadConfigs();
    }
}