package io.github.warleysr.autopixreforged.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SoundPacket {
    
    private final String soundName;
    private final float volume;
    private final float pitch;
    
    public SoundPacket(String soundName, float volume, float pitch) {
        this.soundName = soundName;
        this.volume = volume;
        this.pitch = pitch;
    }
    
    public static void encode(SoundPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.soundName);
        buffer.writeFloat(packet.volume);
        buffer.writeFloat(packet.pitch);
    }
    
    public static SoundPacket decode(FriendlyByteBuf buffer) {
        String soundName = buffer.readUtf();
        float volume = buffer.readFloat();
        float pitch = buffer.readFloat();
        return new SoundPacket(soundName, volume, pitch);
    }
    
    public static void handle(SoundPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                SoundEvent sound = getSoundFromName(packet.soundName);
                if (sound != null) {
                    player.level().playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        sound,
                        SoundSource.PLAYERS,
                        packet.volume,
                        packet.pitch
                    );
                }
            }
        });
        context.setPacketHandled(true);
    }
    
    private static SoundEvent getSoundFromName(String soundName) {
        return switch (soundName.toLowerCase()) {
            case "entity.experience_orb.pickup" -> SoundEvents.EXPERIENCE_ORB_PICKUP;
            case "entity.player.levelup" -> SoundEvents.PLAYER_LEVELUP;
            case "block.note_block.pling" -> SoundEvents.NOTE_BLOCK_PLING;
            case "entity.villager.yes" -> SoundEvents.VILLAGER_YES;
            case "entity.villager.trade" -> SoundEvents.VILLAGER_TRADE;
            case "block.anvil.use" -> SoundEvents.ANVIL_USE;
            case "entity.arrow.hit_player" -> SoundEvents.ARROW_HIT_PLAYER;
            case "block.chest.open" -> SoundEvents.CHEST_OPEN;
            case "block.chest.close" -> SoundEvents.CHEST_CLOSE;
            case "ui.button.click" -> SoundEvents.UI_BUTTON_CLICK;
            default -> SoundEvents.EXPERIENCE_ORB_PICKUP; // Default sound
        };
    }
    
    public String getSoundName() {
        return soundName;
    }
    
    public float getVolume() {
        return volume;
    }
    
    public float getPitch() {
        return pitch;
    }
}