package me.marnic.animalnet.item;

import me.marnic.animalnet.api.BasicItem;

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

    public AnimalNetItem(String name, NetSize size, NetType type, double acceptedSize,int uses) {
        super(name);
        this.size = size;
        this.type = type;
        this.acceptedSize = acceptedSize;
        this.uses = uses;
        setMaxDamage(uses);
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
}