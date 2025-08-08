package io.github.warleysr.autopixreforged.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PaymentStatusPacket {
    
    private final String message;
    private final boolean success;
    
    public PaymentStatusPacket(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    
    public static void encode(PaymentStatusPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.message);
        buffer.writeBoolean(packet.success);
    }
    
    public static PaymentStatusPacket decode(FriendlyByteBuf buffer) {
        String message = buffer.readUtf();
        boolean success = buffer.readBoolean();
        return new PaymentStatusPacket(message, success);
    }
    
    public static void handle(PaymentStatusPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                if (packet.success) {
                    player.sendSystemMessage(Component.literal("§a" + packet.message));
                } else {
                    player.sendSystemMessage(Component.literal("§c" + packet.message));
                }
            }
        });
        context.setPacketHandled(true);
    }
    
    public String getMessage() {
        return message;
    }
    
    public boolean isSuccess() {
        return success;
    }
}