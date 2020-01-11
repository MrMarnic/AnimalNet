package me.marnic.animalnet.api;

import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

/**
 * Copyright (c) 20.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class SpawnerUtil {
    public static boolean isCustomSpawner(ItemStack stack) {
        if (stack.hasTag()) {
            return stack.getTag().contains("entityID");
        }
        return false;
    }

    public static void makeSpawnerCustom(ItemStack stack, Identifier id) {
        CompoundTag compound = new CompoundTag();
        compound.putString("entityID", id.toString());
        stack.setTag(compound);
        TranslatableText translation = new TranslatableText(EntityType.get(id.toString()).get().getTranslationKey());
        BaseText text = new LiteralText(translation.asFormattedString() + " Spawner");
        stack.setCustomName(text);
    }

    public static void makeSpawnerBlock(ItemStack stack, IWorld world, BlockPos pos) {
        CompoundTag tagCompound = stack.getTag();
        Identifier id = new Identifier(tagCompound.getString("entityID"));
        MobSpawnerBlockEntity spawner = (MobSpawnerBlockEntity) world.getBlockEntity(pos);
        spawner.getLogic().setEntityId(EntityType.get(id.toString()).get());
    }
}
