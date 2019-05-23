package me.marnic.animalnet.api;

import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/**
 * Copyright (c) 20.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class SpawnerUtil {
    public static boolean isCustomSpawner(ItemStack stack) {
        if (stack.hasTag()) {
            return stack.getTag().hasKey("entityID");
        }
        return false;
    }

    public static void makeSpawnerCustom(ItemStack stack, ResourceLocation id) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("entityID", id.toString());
        stack.setTag(compound);
        TextComponentTranslation translation = new TextComponentTranslation(EntityType.getById(id.toString()).getTranslationKey());
        TextComponentString text = new TextComponentString(translation.getFormattedText() + " Spawner");
        stack.setDisplayName(text);
    }

    public static void makeSpawnerBlock(ItemStack stack, IWorld world, BlockPos pos) {
        NBTTagCompound tagCompound = stack.getTag();
        ResourceLocation id = new ResourceLocation(tagCompound.getString("entityID"));
        TileEntityMobSpawner spawner = (TileEntityMobSpawner) world.getTileEntity(pos);
        spawner.getSpawnerBaseLogic().setEntityType(EntityType.getById(id.toString()));
    }
}
