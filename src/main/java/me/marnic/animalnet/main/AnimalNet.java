package me.marnic.animalnet.main;

import me.marnic.animalnet.api.BasicItem;
import me.marnic.animalnet.config.AnimalNetConfig;
import me.marnic.animalnet.items.CaughtEntityItem;
import me.marnic.animalnet.recipes.RecipeAnimalToChild;
import me.marnic.animalnet.recipes.RecipeChildToAnimal;
import me.marnic.animalnet.recipes.RecipeSpawner;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.IngredientNBT;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.GameData;

/**
 * Copyright (c) 19.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

@Mod("animalnet")
public class AnimalNet {

    public static AnimalNetModHandler modHandler;
    public static final String MODID = "animalnet";

    public AnimalNet() {
        MinecraftForge.EVENT_BUS.register(modHandler = new AnimalNetModHandler());
        MinecraftForge.EVENT_BUS.register(this);
        AnimalNetConfig.init();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AnimalNetConfig.SPEC);
    }

    @SubscribeEvent
    public void serverStarting(FMLServerStartingEvent e) {
        System.out.println("dddddd");
        e.getServer().getRecipeManager().addRecipe(new RecipeChildToAnimal());
        e.getServer().getRecipeManager().addRecipe(new RecipeAnimalToChild());
        e.getServer().getRecipeManager().addRecipe(new RecipeSpawner());
    }
}
