package io.github.oraculo.autopix.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import io.github.oraculo.autopix.AutoPixMod;

/**
 * Handler de eventos do Forge para o AutoPix
 */
@Mod.EventBusSubscriber(modid = AutoPixMod.MODID)
public class ForgeEventHandler {
    
    /**
     * Evento quando jogador entra no servidor
     */
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            AutoPixMod.LOGGER.info("Jogador {} entrou no servidor", player.getName().getString());
            
            // Enviar mensagem de boas-vindas (opcional)
            player.sendSystemMessage(Component.literal("§6[AutoPix] §7Bem-vindo! Use §e/autopixmenu §7para acessar a loja."));
        }
    }
    
    /**
     * Evento quando jogador sai do servidor
     */
    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            AutoPixMod.LOGGER.info("Jogador {} saiu do servidor", player.getName().getString());
            
            // TODO: Cancelar transações pendentes do jogador
        }
    }
}