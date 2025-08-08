package io.github.warleysr.autopixreforged.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.warleysr.autopixreforged.AutoPixReforged;
import io.github.warleysr.autopixreforged.inventory.ShopMenuProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class AutoPixMenuCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("comprarpix")
                .executes(AutoPixMenuCommand::openShopMenu));
        
        // Register alias
        dispatcher.register(Commands.literal("pixmenu")
                .redirect(dispatcher.getRoot().getChild("comprarpix")));
    }
    
    private static int openShopMenu(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (!(context.getSource().getEntity() instanceof ServerPlayer player)) {
            context.getSource().sendFailure(Component.literal(AutoPixReforged.getConfig().getMessage("player_only")));
            return 0;
        }
        
        try {
            ShopMenuProvider menuProvider = new ShopMenuProvider();
            player.openMenu(menuProvider);
        } catch (Exception e) {
            AutoPixReforged.getLogger().error("Erro ao abrir menu de compras: ", e);
            player.sendSystemMessage(Component.literal("§cErro ao abrir o menu de compras."));
        }
        
        return 1;
    }
}