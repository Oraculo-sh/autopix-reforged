package io.github.oraculo.autopix.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

/**
 * Utilitários para formatação e envio de mensagens
 * Baseado na classe MSG original do plugin
 */
public class MessageUtils {
    
    // Prefixo padrão das mensagens
    private static final String PREFIX = "§6[AutoPix] ";
    
    /**
     * Converte códigos de cor (&) para códigos do Minecraft (§)
     */
    public static String colorize(String message) {
        return message.replace('&', '§');
    }
    
    /**
     * Cria um componente de texto com formatação
     */
    public static Component createComponent(String message) {
        return Component.literal(colorize(message));
    }
    
    /**
     * Envia uma mensagem para um jogador
     */
    public static void sendMessage(ServerPlayer player, String message) {
        player.sendSystemMessage(createComponent(PREFIX + message));
    }
    
    /**
     * Envia uma mensagem de sucesso para um jogador
     */
    public static void sendSuccess(ServerPlayer player, String message) {
        sendMessage(player, "§a" + message);
    }
    
    /**
     * Envia uma mensagem de erro para um jogador
     */
    public static void sendError(ServerPlayer player, String message) {
        sendMessage(player, "§c" + message);
    }
    
    /**
     * Envia uma mensagem de aviso para um jogador
     */
    public static void sendWarning(ServerPlayer player, String message) {
        sendMessage(player, "§e" + message);
    }
    
    /**
     * Envia uma mensagem de informação para um jogador
     */
    public static void sendInfo(ServerPlayer player, String message) {
        sendMessage(player, "§7" + message);
    }
    
    /**
     * Formata um valor monetário
     */
    public static String formatMoney(double amount) {
        return String.format("R$ %.2f", amount);
    }
    
    /**
     * Formata um tempo em segundos para formato legível
     */
    public static String formatTime(int seconds) {
        if (seconds < 60) {
            return seconds + " segundo" + (seconds != 1 ? "s" : "");
        }
        
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        
        if (minutes < 60) {
            String result = minutes + " minuto" + (minutes != 1 ? "s" : "");
            if (remainingSeconds > 0) {
                result += " e " + remainingSeconds + " segundo" + (remainingSeconds != 1 ? "s" : "");
            }
            return result;
        }
        
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;
        
        String result = hours + " hora" + (hours != 1 ? "s" : "");
        if (remainingMinutes > 0) {
            result += " e " + remainingMinutes + " minuto" + (remainingMinutes != 1 ? "s" : "");
        }
        return result;
    }
    
    /**
     * Cria uma linha separadora para mensagens
     */
    public static String createSeparator() {
        return "§7§m" + "=".repeat(40);
    }
    
    /**
     * Envia uma mensagem centralizada
     */
    public static void sendCenteredMessage(ServerPlayer player, String message) {
        // Calcula espaços para centralizar (aproximadamente)
        int spaces = Math.max(0, (40 - message.length()) / 2);
        String centeredMessage = " ".repeat(spaces) + message;
        sendMessage(player, centeredMessage);
    }
}