package me.marnic.animalnet.items;

import me.marnic.animalnet.api.BasicItem;
import me.marnic.animalnet.main.AnimalNet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

/**
 * Copyright (c) 04.03.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class AnimalNetItem extends BasicItem {

    private NetSize size;
    private NetType type;
    private double acceptedSize;
    private int uses;

    public AnimalNetItem(String name, Settings settings, NetSize size, NetType type, double acceptedSize, int uses) {
        super(settings.maxDamageIfAbsent(uses), name);
        this.size = size;
        this.type = type;
        this.acceptedSize = acceptedSize;
        this.uses = uses;
    }

    @Override
    public boolean useOnEntity(ItemStack itemStack_1, PlayerEntity playerEntity_1, LivingEntity livingEntity_1, Hand hand_1) {
        if (!playerEntity_1.getEntityWorld().isClient) {
            return AnimalNet.ENTITY_HANDLER.handleRightClick(livingEntity_1, hand_1, itemStack_1, playerEntity_1);
        }
        return super.useOnEntity(itemStack_1, playerEntity_1, livingEntity_1, hand_1);
    }

    public double getAcceptedSize() {
        return acceptedSize;
    }

    public boolean fitSize(double size) {
        return size <= acceptedSize;
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
}
