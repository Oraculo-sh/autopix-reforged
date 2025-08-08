package io.github.oraculo.autopix.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import io.github.oraculo.autopix.AutoPixMod;

/**
 * Comandos do AutoPix Reforged
 * Baseado nos comandos originais: /autopix, /autopixmenu
 */
@Mod.EventBusSubscriber(modid = AutoPixMod.MODID)
public class AutoPixCommands {
    
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        
        // Comando /autopix (equivalente ao /pix original)
        dispatcher.register(Commands.literal("autopix")
            .requires(source -> source.hasPermission(0)) // Todos os jogadores
            .then(Commands.argument("codigo", StringArgumentType.string())
                .executes(AutoPixCommands::executeValidatePix)
            )
            .executes(AutoPixCommands::executeAutoPixHelp)
        );
        
        // Alias /pix
        dispatcher.register(Commands.literal("pix")
            .requires(source -> source.hasPermission(0))
            .then(Commands.argument("codigo", StringArgumentType.string())
                .executes(AutoPixCommands::executeValidatePix)
            )
            .executes(AutoPixCommands::executeAutoPixHelp)
        );
        
        // Comando /autopixmenu (equivalente ao /comprarpix original)
        dispatcher.register(Commands.literal("autopixmenu")
            .requires(source -> source.hasPermission(0))
            .executes(AutoPixCommands::executeOpenMenu)
        );
        
        // Alias /comprarpix
        dispatcher.register(Commands.literal("comprarpix")
            .requires(source -> source.hasPermission(0))
            .executes(AutoPixCommands::executeOpenMenu)
        );
        
        AutoPixMod.LOGGER.info("Comandos do AutoPix registrados com sucesso!");
    }
    
    public static void register() {
        // Método chamado durante a inicialização
        AutoPixMod.LOGGER.info("Preparando registro de comandos do AutoPix...");
    }
    
    /**
     * Executa a validação de código PIX
     */
    private static int executeValidatePix(CommandContext<CommandSourceStack> context) {
        if (!(context.getSource().getEntity() instanceof ServerPlayer player)) {
            context.getSource().sendFailure(Component.literal("Este comando só pode ser usado por jogadores!"));
            return 0;
        }
        
        String codigo = StringArgumentType.getString(context, "codigo");
        
        // TODO: Implementar validação do código PIX
        player.sendSystemMessage(Component.literal("§6[AutoPix] §7Validando código PIX: §e" + codigo));
        player.sendSystemMessage(Component.literal("§6[AutoPix] §cFuncionalidade em desenvolvimento!"));
        
        return 1;
    }
    
    /**
     * Abre o menu de compras
     */
    private static int executeOpenMenu(CommandContext<CommandSourceStack> context) {
        if (!(context.getSource().getEntity() instanceof ServerPlayer player)) {
            context.getSource().sendFailure(Component.literal("Este comando só pode ser usado por jogadores!"));
            return 0;
        }
        
        // TODO: Implementar abertura do menu
        player.sendSystemMessage(Component.literal("§6[AutoPix] §7Abrindo menu de compras..."));
        player.sendSystemMessage(Component.literal("§6[AutoPix] §cFuncionalidade em desenvolvimento!"));
        
        return 1;
    }
    
    /**
     * Mostra ajuda do comando autopix
     */
    private static int executeAutoPixHelp(CommandContext<CommandSourceStack> context) {
        if (!(context.getSource().getEntity() instanceof ServerPlayer player)) {
            context.getSource().sendFailure(Component.literal("Este comando só pode ser usado por jogadores!"));
            return 0;
        }
        
        player.sendSystemMessage(Component.literal("§6§l=== AutoPix Reforged ==="));
        player.sendSystemMessage(Component.literal("§7Comandos disponíveis:"));
        player.sendSystemMessage(Component.literal("§e/autopix <codigo> §7- Validar código PIX"));
        player.sendSystemMessage(Component.literal("§e/autopixmenu §7- Abrir menu de compras"));
        player.sendSystemMessage(Component.literal("§7Aliases: §e/pix, /comprarpix"));
        
        return 1;
    }
}