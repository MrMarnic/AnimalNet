package me.marnic.animalnet.api;

import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Copyright (c) 20.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class SpawnerUtil {
    public static boolean isCustomSpawner(ItemStack stack) {
        if (stack.hasTagCompound()) {
            return stack.getTagCompound().hasKey("entityID");
        }
        return false;
    }

    public static void makeSpawnerCustom(ItemStack stack, ResourceLocation id) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("entityID", id.toString());
        stack.setTagCompound(compound);
        stack.setStackDisplayName(EntityList.getTranslationName(id) + " Spawner");
    }

    public static void makeSpawnerBlock(ItemStack stack, World world, BlockPos pos) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        ResourceLocation id = new ResourceLocation(tagCompound.getString("entityID"));
        TileEntityMobSpawner spawner = (TileEntityMobSpawner) world.getTileEntity(pos);
        spawner.getSpawnerBaseLogic().setEntityId(id);
    }
}
