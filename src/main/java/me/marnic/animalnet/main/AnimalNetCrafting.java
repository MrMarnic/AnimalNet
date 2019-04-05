package me.marnic.animalnet.main;

import cpw.mods.fml.common.registry.GameRegistry;
import me.marnic.animalnet.item.AnimalNetItem;
import me.marnic.animalnet.recipes.CaughtAnimalToAdultRecipe;
import me.marnic.animalnet.recipes.CaughtAnimalToChildRecipe;
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

        GameRegistry.addShapedRecipe(new ItemStack(AnimalNetItems.animalNetSmall),"CCC","CSC","CCC",'C', Blocks.stone
                ,'S',Items.string);

        GameRegistry.addShapedRecipe(new ItemStack(AnimalNetItems.mobCore)," B ","SIG"," R ",'B', Items.bone
                ,'S',Items.string,'I',Items.iron_sword,'R',Items.rotten_flesh,'G',Items.gunpowder);

        GameRegistry.addShapedRecipe(new ItemStack(AnimalNetItems.mobNetBig),"DED","CBS","GDG",'D',Items.diamond,'E',Items.ender_pearl,'S',Blocks.stone,'B',AnimalNetItems.mobNetSmall,'G',Items.gold_ingot,'C',AnimalNetItems.mobCore);

        GameRegistry.addShapedRecipe(new ItemStack(AnimalNetItems.mobNetSmall),"BMB","CSC","BIB",'B',Items.slime_ball,'M',AnimalNetItems.mobCore,'C',Items.string,'S',AnimalNetItems.animalNetSmall,'I',Items.iron_ingot);

        GameRegistry.addShapedRecipe(new ItemStack(AnimalNetItems.npcNet),"WBL","OSO","PEC",'W',Items.wheat,'B',Items.book,'L',Items.coal,'O',Blocks.planks,'S',AnimalNetItems.animalNetMedium,'P',Items.potato,'E',Items.leather,'C',Items.carrot);

        GameRegistry.addRecipe(new CaughtAnimalToChildRecipe());

        GameRegistry.addRecipe(new CaughtAnimalToAdultRecipe());
    }
}
