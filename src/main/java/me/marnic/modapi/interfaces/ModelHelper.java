package me.marnic.modapi.interfaces;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

/**
 * Copyright (c) 03.12.2018
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ModelHelper {
    public static void registerComplexItemModel(Item item, int meta, String variant) {
        ModelLoader.setCustomModelResourceLocation(item,meta,new ModelResourceLocation(item.getRegistryName(),variant));
    }

    public static void registerDefaultItemModel(Item item) {
        registerComplexItemModel(item,0,"inventory");
    }
}
