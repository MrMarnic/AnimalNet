package me.marnic.animalnet.api;

import me.marnic.animalnet.main.AnimalNetItems;
import me.marnic.animalnet.main.AnimalNetModHandler;
import net.minecraft.item.Item;

/**
 * Copyright (c) 18.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

public class BasicItem extends Item implements IModelRegistry {

    public BasicItem(String name) {
        setRegistryName(name);
        setUnlocalizedName(name);
        AnimalNetModHandler.MODELS_TO_REGISTER.add(this);
        AnimalNetItems.ITEMS_TO_REGISTER.add(this);
        if (shouldBeAddedToTab()) {
            setCreativeTab(AnimalNetItems.ANIMAL_NET_ITEMS);
        }
    }

    public boolean shouldBeAddedToTab() {
        return true;
    }

    @Override
    public void registerModel() {
        ModelHelper.registerDefaultItemModel(this);
    }
}
