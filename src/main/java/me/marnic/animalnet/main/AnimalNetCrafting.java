package me.marnic.animalnet.main;

import cpw.mods.fml.common.registry.GameRegistry;
import me.marnic.animalnet.item.AnimalNetItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * Copyright (c) 04.04.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class AnimalNetCrafting {
    public static void init() {
        GameRegistry.addShapedRecipe(new ItemStack(AnimalNetItems.animalNetBig),"ODO","IBI","ODO",'D', Items.diamond
        ,'B',AnimalNetItems.animalNetMedium,'O', Blocks.obsidian,'I',Items.iron_ingot);

        GameRegistry.addShapedRecipe(new ItemStack(AnimalNetItems.animalNetMedium),"SCI","CBC","ICS",'C', Blocks.stone
                ,'B',AnimalNetItems.animalNetSmall,'S', Items.slime_ball,'I',Items.iron_ingot);

        GameRegistry.addShapedRecipe(new ItemStack(AnimalNetItems.animalNetMedium),"CCC","CSC","CCC",'C', Blocks.stone
                ,'S',Items.string);


    }
}
