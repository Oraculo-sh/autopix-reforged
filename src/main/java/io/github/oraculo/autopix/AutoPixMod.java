/*
 * AutoPix Reforged - Sistema de pagamentos PIX para Minecraft Forge
 * Copyright (C) 2024 Leonne Martins (Oraculo-sh)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * Based on the original AutoPix plugin by warleysr
 */

package io.github.oraculo.autopix;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.oraculo.autopix.config.AutoPixConfig;
import io.github.oraculo.autopix.network.NetworkHandler;
import io.github.oraculo.autopix.commands.AutoPixCommands;
import io.github.oraculo.autopix.events.ForgeEventHandler;

/**
 * Classe principal do mod AutoPix Reforged
 * Porte do plugin AutoPix original para Minecraft Forge
 * 
 * @author Leonne Martins (Oraculo-sh)
 * @author Original by warleysr
 */
@Mod(AutoPixMod.MODID)
public class AutoPixMod {
    public static final String MODID = "autopix";
    public static final Logger LOGGER = LogManager.getLogger();
    
    public AutoPixMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Registrar configurações
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AutoPixConfig.SPEC);
        
        // Registrar eventos de setup
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        
        // Registrar eventos do Forge
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
        
        LOGGER.info("AutoPix Reforged inicializando...");
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("AutoPix Reforged - Setup comum iniciado");
        
        // Inicializar rede
        NetworkHandler.init();
        
        // Registrar comandos
        event.enqueueWork(() -> {
            AutoPixCommands.register();
        });
        
        LOGGER.info("AutoPix Reforged - Setup comum concluído");
    }
    
    private void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("AutoPix Reforged - Setup do cliente iniciado");
        // Configurações específicas do cliente serão adicionadas aqui
        LOGGER.info("AutoPix Reforged - Setup do cliente concluído");
    }
}