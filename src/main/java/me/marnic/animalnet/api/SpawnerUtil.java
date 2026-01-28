package me.marnic.animalnet.api;

import net.minecraft.block.Blocks;
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

    public static ItemStack makeSpawnerCustom(ItemStack stack) {

        CompoundNBT animalnet = stack.getOrCreateTag().getCompound("animalnet").getCompound("entity");
        EntityType<?> type = EntityType.by(animalnet).get();
        ItemStack spawner = new ItemStack(Blocks.SPAWNER);
        CompoundNBT compound = spawner.getOrCreateTag();
        compound.putString("entityID", type.getRegistryName().toString());

        StringTextComponent text = new StringTextComponent(type.getDescription().getString() + " Spawner");
        spawner.setHoverName(text);

        return spawner;
    }

    public static void makeSpawnerBlock(ItemStack stack, IWorld world, BlockPos pos) {
        CompoundNBT tagCompound = stack.getTag();
        ResourceLocation id = new ResourceLocation(tagCompound.getString("entityID"));
        MobSpawnerTileEntity spawner = (MobSpawnerTileEntity) world.getBlockEntity(pos);
        spawner.getSpawner().setEntityId(EntityType.byString(id.toString()).get());
    }
}
