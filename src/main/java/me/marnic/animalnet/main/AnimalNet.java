package me.marnic.animalnet.main;

import me.marnic.animalnet.api.AnimalNetItemRegistry;
import me.marnic.animalnet.mechanics.EntityHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.mixin.events.playerinteraction.MixinClientPlayerInteractionManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.world.ServerWorld;

/**
 * Copyright (c) 04.03.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

public class AnimalNet implements ModInitializer {

    public static final AnimalNetItemRegistry ITEM_REGISTRY = new AnimalNetItemRegistry();
    public static final EntityHandler ENTITY_HANDLER = new EntityHandler();

    @Override
    public void onInitialize() {
        AnimalNetItems.init();
        ITEM_REGISTRY.registerItems();
    }
}
