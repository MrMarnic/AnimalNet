package me.marnic.animalnet.api;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistryModifiable;

/**
 * Copyright (c) 18.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

public class RecipeUtil {
    public static void replaceRecipe(RegistryEvent.Register<IRecipe> e, String idToReplace, IRecipe replacement) {
        IForgeRegistryModifiable<IRecipe> ee = (IForgeRegistryModifiable) e.getRegistry();

        ee.remove(new ResourceLocation(idToReplace));

        ee.register(replacement);
    }
}
