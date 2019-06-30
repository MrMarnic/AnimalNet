package me.marnic.animalnet.api;

import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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

    public static void makeSpawnerCustom(ItemStack stack, ResourceLocation id) {
        CompoundNBT compound = new CompoundNBT();
        compound.putString("entityID", id.toString());
        stack.setTag(compound);
        TranslationTextComponent translation = new TranslationTextComponent(EntityType.byKey(id.toString()).get().getTranslationKey());
        StringTextComponent text = new StringTextComponent(translation.getFormattedText() + " Spawner");
        stack.setDisplayName(text);
    }

    public static void makeSpawnerBlock(ItemStack stack, IWorld world, BlockPos pos) {
        CompoundNBT tagCompound = stack.getTag();
        ResourceLocation id = new ResourceLocation(tagCompound.getString("entityID"));
        MobSpawnerTileEntity spawner = (MobSpawnerTileEntity) world.getTileEntity(pos);
        spawner.getSpawnerBaseLogic().setEntityType(EntityType.byKey(id.toString()).get());
    }
}
