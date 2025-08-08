package io.github.oraculo.autopix.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

/**
 * Configurações do AutoPix Reforged
 * Baseado no config.yml original do plugin
 */
public class AutoPixConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    
    // MercadoPago
    public static final ConfigValue<String> MERCADOPAGO_TOKEN;
    
    // PIX Settings
    public static final ConfigValue<String> PIX_KEY;
    public static final ConfigValue<String> PIX_BENEFICIARY_NAME;
    public static final ConfigValue<Boolean> PIX_DEBUG;
    public static final ConfigValue<Boolean> PIX_USE_MAP;
    
    // Automatic activation
    public static final ConfigValue<Integer> AUTOMATIC_ACTIVATION_INTERVAL;
    
    // MySQL Database
    public static final ConfigValue<String> MYSQL_HOST;
    public static final ConfigValue<Integer> MYSQL_PORT;
    public static final ConfigValue<String> MYSQL_DATABASE;
    public static final ConfigValue<String> MYSQL_USERNAME;
    public static final ConfigValue<String> MYSQL_PASSWORD;
    
    // Timeouts
    public static final ConfigValue<Integer> TIMEOUT_VALIDATION;
    public static final ConfigValue<Integer> TIMEOUT_LIST;
    public static final ConfigValue<Integer> TIMEOUT_ORDER_CREATION;
    public static final ConfigValue<Integer> TIMEOUT_HOLOGRAM;
    public static final ConfigValue<Integer> TIMEOUT_CANCEL;
    public static final ConfigValue<Integer> TIMEOUT_TOP;
    
    // Payment Map
    public static final ConfigValue<String> PAYMENT_MAP_NAME;
    public static final ConfigValue<String> PAYMENT_MAP_DESCRIPTION;
    public static final ConfigValue<Integer> PAYMENT_MAP_TIME;
    public static final ConfigValue<Integer> PAYMENT_MAP_VERIFICATION_INTERVAL;
    
    // Menu Settings
    public static final ConfigValue<String> MENU_MAIN_TITLE;
    public static final ConfigValue<Integer> MENU_MAIN_SIZE;
    public static final ConfigValue<String> MENU_CONFIRMATION_TITLE;
    public static final ConfigValue<Integer> MENU_CONFIRMATION_SIZE;
    
    // Sounds
    public static final ConfigValue<String> SOUND_PAYMENT_SUCCESS;
    public static final ConfigValue<String> SOUND_PAYMENT_ERROR;
    
    // Top Settings
    public static final ConfigValue<Integer> TOP_LIMIT;
    
    static {
        BUILDER.comment("AutoPix Reforged Configuration");
        
        // MercadoPago
        BUILDER.push("mercadopago");
        MERCADOPAGO_TOKEN = BUILDER
            .comment("Token de acesso do MercadoPago")
            .define("token", "YOUR_MERCADOPAGO_TOKEN_HERE");
        BUILDER.pop();
        
        // PIX Settings
        BUILDER.push("pix");
        PIX_KEY = BUILDER
            .comment("Chave PIX para recebimento")
            .define("key", "YOUR_PIX_KEY_HERE");
        PIX_BENEFICIARY_NAME = BUILDER
            .comment("Nome do beneficiário PIX")
            .define("beneficiary_name", "Seu Nome Aqui");
        PIX_DEBUG = BUILDER
            .comment("Ativar modo debug")
            .define("debug", false);
        PIX_USE_MAP = BUILDER
            .comment("Usar mapa para exibir QR Code")
            .define("use_map", true);
        BUILDER.pop();
        
        // Automatic activation
        AUTOMATIC_ACTIVATION_INTERVAL = BUILDER
            .comment("Intervalo de ativação automática em segundos")
            .defineInRange("automatic_activation_interval", 30, 1, 3600);
        
        // MySQL Database
        BUILDER.push("mysql");
        MYSQL_HOST = BUILDER
            .comment("Host do banco MySQL")
            .define("host", "localhost");
        MYSQL_PORT = BUILDER
            .comment("Porta do banco MySQL")
            .defineInRange("port", 3306, 1, 65535);
        MYSQL_DATABASE = BUILDER
            .comment("Nome do banco de dados")
            .define("database", "autopix");
        MYSQL_USERNAME = BUILDER
            .comment("Usuário do banco")
            .define("username", "root");
        MYSQL_PASSWORD = BUILDER
            .comment("Senha do banco")
            .define("password", "password");
        BUILDER.pop();
        
        // Timeouts
        BUILDER.push("timeouts");
        TIMEOUT_VALIDATION = BUILDER
            .comment("Timeout para validação em segundos")
            .defineInRange("validation", 300, 30, 3600);
        TIMEOUT_LIST = BUILDER
            .comment("Timeout para listagem em segundos")
            .defineInRange("list", 600, 60, 3600);
        TIMEOUT_ORDER_CREATION = BUILDER
            .comment("Timeout para criação de pedido em segundos")
            .defineInRange("order_creation", 1800, 300, 7200);
        TIMEOUT_HOLOGRAM = BUILDER
            .comment("Timeout para holograma em segundos")
            .defineInRange("hologram", 1800, 300, 7200);
        TIMEOUT_CANCEL = BUILDER
            .comment("Timeout para cancelamento em segundos")
            .defineInRange("cancel", 1800, 300, 7200);
        TIMEOUT_TOP = BUILDER
            .comment("Timeout para top em segundos")
            .defineInRange("top", 3600, 600, 7200);
        BUILDER.pop();
        
        // Payment Map
        BUILDER.push("payment_map");
        PAYMENT_MAP_NAME = BUILDER
            .comment("Nome do mapa de pagamento")
            .define("name", "§6§lPAGAMENTO PIX");
        PAYMENT_MAP_DESCRIPTION = BUILDER
            .comment("Descrição do mapa de pagamento")
            .define("description", "§7Escaneie o QR Code para pagar");
        PAYMENT_MAP_TIME = BUILDER
            .comment("Tempo do mapa em segundos")
            .defineInRange("time", 300, 60, 1800);
        PAYMENT_MAP_VERIFICATION_INTERVAL = BUILDER
            .comment("Intervalo de verificação do mapa em segundos")
            .defineInRange("verification_interval", 5, 1, 60);
        BUILDER.pop();
        
        // Menu Settings
        BUILDER.push("menu");
        MENU_MAIN_TITLE = BUILDER
            .comment("Título do menu principal")
            .define("main_title", "§6§lLoja AutoPix");
        MENU_MAIN_SIZE = BUILDER
            .comment("Tamanho do menu principal")
            .defineInRange("main_size", 54, 9, 54);
        MENU_CONFIRMATION_TITLE = BUILDER
            .comment("Título do menu de confirmação")
            .define("confirmation_title", "§6§lConfirmar Compra");
        MENU_CONFIRMATION_SIZE = BUILDER
            .comment("Tamanho do menu de confirmação")
            .defineInRange("confirmation_size", 27, 9, 54);
        BUILDER.pop();
        
        // Sounds
        BUILDER.push("sounds");
        SOUND_PAYMENT_SUCCESS = BUILDER
            .comment("Som de pagamento bem-sucedido")
            .define("payment_success", "entity.experience_orb.pickup");
        SOUND_PAYMENT_ERROR = BUILDER
            .comment("Som de erro no pagamento")
            .define("payment_error", "entity.villager.no");
        BUILDER.pop();
        
        // Top Settings
        TOP_LIMIT = BUILDER
            .comment("Limite de jogadores no top")
            .defineInRange("top_limit", 10, 1, 50);
        
        SPEC = BUILDER.build();
    }
}