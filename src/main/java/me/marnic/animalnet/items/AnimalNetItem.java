package me.marnic.animalnet.items;

import me.marnic.animalnet.api.BasicItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Copyright (c) 19.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class AnimalNetItem extends BasicItem {
    private NetSize size;
    private NetType type;
    private double acceptedSize;
    private int uses;

    public AnimalNetItem(String name, Item.Properties properties, NetSize size, NetType type) {
        super(properties,name);
        this.size = size;
        this.type = type;
    }

    public double getAcceptedSize() {
        return acceptedSize;
    }

    public boolean fitSize(double size) {
        return size<=acceptedSize;
    }

    public NetType getType() {
        return type;
    }

    public NetSize getSize() {
        return size;
    }

    public int getUses() {
        return uses;
    }

    public void initConfigValues(double acceptedSize,int uses) {
        this.acceptedSize = acceptedSize;
        this.uses = uses;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return uses;
    }
}

