package me.marnic.animalnet.items;

import me.marnic.animalnet.api.BasicItem;
import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Copyright (c) 19.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class AnimalNetItem extends BasicItem {
    private NetSize size;
    private NetType type;
    private double acceptedSize;

    public AnimalNetItem(String name,Properties properties, NetSize size, NetType type, double acceptedSize) {
        super(properties,name);
        this.size = size;
        this.type = type;
        this.acceptedSize = acceptedSize;
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

