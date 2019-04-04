package me.marnic.animalnet.api;

import me.marnic.animalnet.main.AnimalNet;
import me.marnic.animalnet.main.AnimalNetItems;
import me.marnic.animalnet.main.AnimalNetModHandler;
import net.minecraft.item.Item;

/**
 * Copyright (c) 18.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

public class BasicItem extends Item {

    public BasicItem(String name) {
        setUnlocalizedName(name);
        setTextureName(AnimalNet.MODID+":"+name);
        AnimalNetItems.ITEMS_TO_REGISTER.add(this);
        setCreativeTab(AnimalNetItems.ANIMAL_NET_ITEMS);
    }
}