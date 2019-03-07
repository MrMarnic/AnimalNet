package me.marnic.modapi.handler;

import me.marnic.modapi.interfaces.IModelRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;

/**
 * Copyright (c) 03.12.2018
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ModHandler {

    public static final ArrayList<IModelRegistry> modelsToRegister = new ArrayList<>();

    public void init() {

    }


    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {

    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {

    }

    @SubscribeEvent
    public void modelRegistry(ModelRegistryEvent event) {
        for(IModelRegistry register:modelsToRegister) {
            register.registerModel();
        }
    }

    public void register(IForgeRegistryEntry entry,RegistryEvent.Register register) {
        register.getRegistry().register(entry);
    }
}
