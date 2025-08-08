package io.github.warleysr.autopixreforged.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.warleysr.autopixreforged.AutoPixReforged;
import io.github.warleysr.autopixreforged.config.AutoPixConfig;
import io.github.warleysr.autopixreforged.database.OrderManager;
import io.github.warleysr.autopixreforged.domain.Order;
import io.github.warleysr.autopixreforged.mercadopago.MercadoPagoAPI;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AutoPixCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("autopix")
                .then(Commands.literal("validar")
                        .then(Commands.argument("codigo", StringArgumentType.string())
                                .executes(AutoPixCommand::validatePix)))
                .then(Commands.literal("lista")
                        .executes(AutoPixCommand::listOrders)
                        .then(Commands.argument("jogador", StringArgumentType.string())
                                .executes(AutoPixCommand::listPlayerOrders)))
                .then(Commands.literal("reload")
                        .requires(source -> source.hasPermission(2))
                        .executes(AutoPixCommand::reloadConfig))
                .then(Commands.literal("info")
                        .executes(AutoPixCommand::showInfo))
                .executes(AutoPixCommand::showHelp));
        
        // Register aliases
        dispatcher.register(Commands.literal("pix")
                .redirect(dispatcher.getRoot().getChild("autopix")));
        
        dispatcher.register(Commands.literal("ap")
                .redirect(dispatcher.getRoot().getChild("autopix")));
    }
    
    private static int validatePix(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (!(context.getSource().getEntity() instanceof ServerPlayer player)) {
            context.getSource().sendFailure(Component.literal(AutoPixReforged.getConfig().getMessage("player_only")));
            return 0;
        }
        
        String pixCode = StringArgumentType.getString(context, "codigo");
        
        // Run validation asynchronously
        CompletableFuture.runAsync(() -> {
            try {
                boolean isValid = MercadoPagoAPI.validatePixTransaction(pixCode);
                
                if (isValid) {
                    // Find pending order for this player
                    Order lastOrder = OrderManager.getLastOrderByPlayer(player.getName().getString());
                    
                    if (lastOrder != null && lastOrder.getStatus() == Order.OrderStatus.PENDING) {
                        // Update order status
                        OrderManager.updateOrderStatus(lastOrder.getId(), Order.OrderStatus.PAID);
                        
                        // Execute commands for this order
                        executeOrderCommands(lastOrder, player);
                        
                        player.sendSystemMessage(Component.literal(AutoPixReforged.getConfig().getMessage("payment_validated")));
                    } else {
                        player.sendSystemMessage(Component.literal(AutoPixReforged.getConfig().getMessage("payment_not_found")));
                    }
                } else {
                    player.sendSystemMessage(Component.literal(AutoPixReforged.getConfig().getMessage("payment_not_found")));
                }
            } catch (Exception e) {
                AutoPixReforged.getLogger().error("Erro ao validar PIX: ", e);
                player.sendSystemMessage(Component.literal(AutoPixReforged.getConfig().getFormattedMessage("mercadopago_error", "error", e.getMessage())));
            }
        });
        
        return 1;
    }
    
    private static int listOrders(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (!(context.getSource().getEntity() instanceof ServerPlayer player)) {
            context.getSource().sendFailure(Component.literal(AutoPixReforged.getConfig().getMessage("player_only")));
            return 0;
        }
        
        List<Order> orders = OrderManager.getOrdersByPlayer(player.getName().getString());
        
        if (orders.isEmpty()) {
            player.sendSystemMessage(Component.literal("§7Você não possui nenhum pedido."));
        } else {
            player.sendSystemMessage(Component.literal("§6=== Seus Pedidos ==="));
            for (Order order : orders) {
                String statusColor = getStatusColor(order.getStatus());
                player.sendSystemMessage(Component.literal(String.format(
                        "§7#%d - §f%s §7- §a R$ %.2f §7- %s%s",
                        order.getId(),
                        order.getProductId(),
                        order.getPrice(),
                        statusColor,
                        order.getStatus().name()
                )));
            }
        }
        
        return 1;
    }
    
    private static int listPlayerOrders(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (!context.getSource().hasPermission(2)) {
            context.getSource().sendFailure(Component.literal(AutoPixReforged.getConfig().getMessage("no_permission")));
            return 0;
        }
        
        String targetPlayer = StringArgumentType.getString(context, "jogador");
        List<Order> orders = OrderManager.getOrdersByPlayer(targetPlayer);
        
        if (orders.isEmpty()) {
            context.getSource().sendSuccess(Component.literal("§7O jogador " + targetPlayer + " não possui nenhum pedido."), false);
        } else {
            context.getSource().sendSuccess(Component.literal("§6=== Pedidos de " + targetPlayer + " ==="), false);
            for (Order order : orders) {
                String statusColor = getStatusColor(order.getStatus());
                context.getSource().sendSuccess(Component.literal(String.format(
                        "§7#%d - §f%s §7- §a R$ %.2f §7- %s%s",
                        order.getId(),
                        order.getProductId(),
                        order.getPrice(),
                        statusColor,
                        order.getStatus().name()
                )), false);
            }
        }
        
        return 1;
    }
    
    private static int reloadConfig(CommandContext<CommandSourceStack> context) {
        try {
            AutoPixReforged.getConfig().reload();
            context.getSource().sendSuccess(Component.literal(AutoPixReforged.getConfig().getMessage("config_reloaded")), true);
        } catch (Exception e) {
            AutoPixReforged.getLogger().error("Erro ao recarregar configurações: ", e);
            context.getSource().sendFailure(Component.literal("§cErro ao recarregar configurações: " + e.getMessage()));
        }
        
        return 1;
    }
    
    private static int showInfo(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (!(context.getSource().getEntity() instanceof ServerPlayer player)) {
            context.getSource().sendFailure(Component.literal(AutoPixReforged.getConfig().getMessage("player_only")));
            return 0;
        }
        
        // Send info messages
        player.sendSystemMessage(Component.literal("§a§l=== AutoPix Reforged - Informações ==="));
        player.sendSystemMessage(Component.literal("§7Para comprar produtos use: §b/comprarpix"));
        player.sendSystemMessage(Component.literal("§7Para validar um pagamento use: §b/pix validar <codigo>"));
        player.sendSystemMessage(Component.literal("§7Para ver seus pedidos use: §b/pix lista"));
        player.sendSystemMessage(Component.literal("§7O código PIX tem 32 caracteres e começa com 'E'"));
        player.sendSystemMessage(Component.literal("§7Exemplo: §fE00416968202301162037NF2oRtQ73bY"));
        
        return 1;
    }
    
    private static int showHelp(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(Component.literal("§6=== AutoPix Reforged - Comandos ==="), false);
        context.getSource().sendSuccess(Component.literal("§7/pix validar <codigo> §f- Valida um pagamento PIX"), false);
        context.getSource().sendSuccess(Component.literal("§7/pix lista §f- Lista seus pedidos"), false);
        context.getSource().sendSuccess(Component.literal("§7/pix info §f- Mostra informações sobre o sistema"), false);
        context.getSource().sendSuccess(Component.literal("§7/comprarpix §f- Abre o menu de compras"), false);
        
        if (context.getSource().hasPermission(2)) {
            context.getSource().sendSuccess(Component.literal("§c=== Comandos de Admin ==="), false);
            context.getSource().sendSuccess(Component.literal("§7/pix lista <jogador> §f- Lista pedidos de um jogador"), false);
            context.getSource().sendSuccess(Component.literal("§7/pix reload §f- Recarrega as configurações"), false);
        }
        
        return 1;
    }
    
    private static String getStatusColor(Order.OrderStatus status) {
        return switch (status) {
            case PENDING -> "§e";
            case PAID -> "§a";
            case CANCELLED -> "§c";
            case EXPIRED -> "§8";
        };
    }
    
    private static void executeOrderCommands(Order order, ServerPlayer player) {
        // TODO: Implement command execution system
        // This will execute the commands configured for the purchased product
        AutoPixReforged.getLogger().info("Executando comandos para o pedido {} do jogador {}", order.getId(), player.getName().getString());
    }
}