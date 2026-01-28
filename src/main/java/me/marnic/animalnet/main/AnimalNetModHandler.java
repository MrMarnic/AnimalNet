package me.marnic.animalnet.main;

import me.marnic.animalnet.config.AnimalNetConfig;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

/**
 * Copyright (c) 19.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AnimalNetModHandler {

    @SubscribeEvent
    public static void configLoadEvent(ModConfig.Loading e) {
        AnimalNetConfig.GENERAL_OPTIONS.initExcludedEntitiesArraysList();
    }
}
