package me.marnic.animalnet.api;

import me.marnic.animalnet.main.ModIdentification;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;

/**
 * Copyright (c) 04.03.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class AnimalNetItemRegistry {
    public ArrayList<BasicItem> itemsToRegister;

    public AnimalNetItemRegistry() {
        this.itemsToRegister = new ArrayList<>();
    }

    public void registerItems() {
        for(BasicItem item:itemsToRegister) {
            Registry.register(Registry.ITEM,new Identifier(ModIdentification.MODID,item.getItemName()),item);
        }
    }
}
