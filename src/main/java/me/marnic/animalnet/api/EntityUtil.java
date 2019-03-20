package me.marnic.animalnet.api;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

/**
 * Copyright (c) 18.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class EntityUtil {
    public static EntityLiving createEntity(EntityType<EntityLiving> type, World world_1, NBTTagCompound compoundTag_1, ITextComponent textComponent_1, EntityPlayer playerEntity_1, BlockPos blockPos_1
                                            , boolean boolean_1, boolean boolean_2) {
        return type.makeEntity(world_1,compoundTag_1,textComponent_1,playerEntity_1,blockPos_1,boolean_1,boolean_2);
    }
}


