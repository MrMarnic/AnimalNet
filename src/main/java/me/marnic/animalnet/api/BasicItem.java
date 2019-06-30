package me.marnic.animalnet.api;

import me.marnic.animalnet.main.AnimalNet;
import me.marnic.animalnet.main.AnimalNetModHandler;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Copyright (c) 19.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicItem extends Item {
    public BasicItem(String name) {
        super(new Properties().group(ItemGroup.MISC));
        setRegistryName(AnimalNet.MODID,name);
        AnimalNetModHandler.itemsToRegister.add(this);
    }

    public BasicItem(Properties properties, String name) {
        super(properties);
        setRegistryName(AnimalNet.MODID,name);
        AnimalNetModHandler.itemsToRegister.add(this);
    }
}
