package me.marnic.animalnet.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.TextComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.stream.Stream;

/**
 * Copyright (c) 18.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class EntityUtil {
    public static LivingEntity createEntity(EntityType<LivingEntity> type,World world_1,CompoundTag compoundTag_1,TextComponent textComponent_1,PlayerEntity playerEntity_1,BlockPos blockPos_1,
                                            SpawnType spawnType_1,boolean boolean_1,boolean boolean_2) {
        return type.create(world_1, compoundTag_1, textComponent_1, playerEntity_1, blockPos_1, spawnType_1, boolean_1, boolean_2);
    }
}


