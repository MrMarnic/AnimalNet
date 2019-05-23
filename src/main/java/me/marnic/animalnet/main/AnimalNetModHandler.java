package me.marnic.animalnet.main;

import me.marnic.animalnet.api.BasicItem;
import me.marnic.animalnet.config.AnimalNetConfig;
import me.marnic.animalnet.recipes.RecipeAnimalToChild;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.extensions.ForgeRecipeManager;
import net.minecraftforge.common.extensions.IForgeRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.registries.ForgeRegistries;

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
        AnimalNetItems.init();
        for(BasicItem item:AnimalNetModHandler.itemsToRegister) {
            e.getRegistry().register(item);
        }
    }

    @SubscribeEvent
    public static void configLoadEvent(ModConfig.Loading e) {
        AnimalNetConfig.general_options.initExcludedEntitiesArraysList();
        AnimalNetItems.initConfigValues();
    }
}
