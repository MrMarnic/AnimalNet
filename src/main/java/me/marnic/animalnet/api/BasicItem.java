package me.marnic.animalnet.api;

import me.marnic.animalnet.main.AnimalNet;
import net.minecraft.item.Item;

/**
 * Copyright (c) 04.03.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicItem extends Item {
    private String name;

    public BasicItem(Settings settings, String name) {
        super(settings);
        this.name = name;
        AnimalNet.ITEM_REGISTRY.itemsToRegister.add(this);
    }

    public String getItemName() {
        return name;
    }
}
