package io.github.oraculo.autopix.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import io.github.oraculo.autopix.AutoPixMod;

/**
 * Handler de rede para comunicação cliente-servidor
 */
public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(AutoPixMod.MODID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );
    
    private static int packetId = 0;
    
    public static void init() {
        AutoPixMod.LOGGER.info("Inicializando rede do AutoPix...");
        
        // Registrar packets aqui quando necessário
        // INSTANCE.registerMessage(packetId++, PacketClass.class, PacketClass::encode, PacketClass::decode, PacketClass::handle);
        
        AutoPixMod.LOGGER.info("Rede do AutoPix inicializada com sucesso!");
    }
    
    private static int nextId() {
        return packetId++;
    }
}