package io.github.warleysr.autopixreforged;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import io.github.warleysr.autopixreforged.config.AutoPixConfig;
import io.github.warleysr.autopixreforged.database.OrderManager;
import io.github.warleysr.autopixreforged.commands.AutoPixCommand;
import io.github.warleysr.autopixreforged.commands.AutoPixMenuCommand;
import io.github.warleysr.autopixreforged.network.NetworkHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AutoPixReforged.MODID)
public class AutoPixReforged {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "autopixreforged";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    
    private static AutoPixReforged instance;
    private static AutoPixConfig config;
    private static ScheduledExecutorService scheduler;
    
    public AutoPixReforged() {
        instance = this;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        
        // Initialize scheduler
        scheduler = Executors.newScheduledThreadPool(2);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("AutoPix Reforged - Inicializando mod...");
        
        // Initialize config
        config = new AutoPixConfig();
        
        // Initialize network handler
        NetworkHandler.init();
        
        // Initialize database
        try {
            OrderManager.initialize(config);
            LOGGER.info("AutoPix Reforged - Banco de dados inicializado com sucesso!");
        } catch (Exception e) {
            LOGGER.error("AutoPix Reforged - Erro ao inicializar banco de dados: ", e);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("AutoPix Reforged - Servidor iniciando...");
        
        // Register commands
        AutoPixCommand.register(event.getServer().getCommands().getDispatcher());
        AutoPixMenuCommand.register(event.getServer().getCommands().getDispatcher());
        
        // Start automatic validation task if enabled
        if (config.isAutomaticValidationEnabled()) {
            int interval = config.getAutomaticValidationInterval();
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    OrderManager.validatePendingOrders();
                } catch (Exception e) {
                    LOGGER.error("Erro durante validação automática: ", e);
                }
            }, interval, interval, TimeUnit.SECONDS);
            
            LOGGER.info("AutoPix Reforged - Validação automática ativada (intervalo: {}s)", interval);
        }
        
        // Start map cleanup task
        int mapInterval = config.getMapCleanupInterval();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                OrderManager.cleanupExpiredMaps();
            } catch (Exception e) {
                LOGGER.error("Erro durante limpeza de mapas: ", e);
            }
        }, mapInterval, mapInterval, TimeUnit.SECONDS);
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("AutoPix Reforged - Cliente inicializado!");
        }
    }
    
    public static AutoPixReforged getInstance() {
        return instance;
    }
    
    public static AutoPixConfig getConfig() {
        return config;
    }
    
    public static Logger getLogger() {
        return LOGGER;
    }
    
    public static ScheduledExecutorService getScheduler() {
        return scheduler;
    }
}