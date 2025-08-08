package io.github.warleysr.autopixreforged.network;

import io.github.warleysr.autopixreforged.AutoPixReforged;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(AutoPixReforged.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    
    private static int packetId = 0;
    
    public static void registerMessages() {
        // Register packet messages here
        INSTANCE.messageBuilder(PaymentStatusPacket.class, nextId())
                .decoder(PaymentStatusPacket::decode)
                .encoder(PaymentStatusPacket::encode)
                .consumerMainThread(PaymentStatusPacket::handle)
                .add();
                
        INSTANCE.messageBuilder(SoundPacket.class, nextId())
                .decoder(SoundPacket::decode)
                .encoder(SoundPacket::encode)
                .consumerMainThread(SoundPacket::handle)
                .add();
    }
    
    private static int nextId() {
        return packetId++;
    }
}