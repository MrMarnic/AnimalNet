package me.marnic.animalnet.main;

import me.marnic.animalnet.api.BasicItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

/**
 * Copyright (c) 19.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class AnimalNetModHandler {

    public static ArrayList<BasicItem> itemsToRegister = new ArrayList<>();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {
        for(BasicItem item:AnimalNetModHandler.itemsToRegister) {
            e.getRegistry().register(item);
        }
    }
}
