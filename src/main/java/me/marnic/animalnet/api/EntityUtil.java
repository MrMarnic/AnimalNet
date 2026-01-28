package me.marnic.animalnet.api;

import net.minecraft.entity.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.core.jmx.Server;

import java.text.SimpleDateFormat;
import java.util.stream.Stream;

/**
 * Copyright (c) 18.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class EntityUtil {
    public static void saveEntityToItemStack(Entity entity, ItemStack stack) {
        CompoundNBT tag = entity.serializeNBT();
        CompoundNBT animalNetTag = new CompoundNBT();
        animalNetTag.put("entity",tag);

        CompoundNBT extraData = new CompoundNBT();
        extraData.putString("animalName", EntityType.getKey(entity.getType()).toString());
        extraData.putString("modName", EntityType.getKey(entity.getType()).getNamespace());
        extraData.putString("location", "x:" + entity.blockPosition().getX() + " y:" + entity.blockPosition().getY() + " z:" + entity.blockPosition().getZ());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        extraData.putString("date", formatter.format(date));

        if (entity instanceof AnimalEntity) {
            int age = ((AnimalEntity) entity).getAge();
            if (age < 0) {
                extraData.putString("age", "Child");
            } else {
                extraData.putString("age", "Adult");
            }
        }
        animalNetTag.put("extraData",extraData);

        stack.getOrCreateTag().put("animalnet", animalNetTag);

        if (!entity.hasCustomName()) {
            stack.setHoverName(new StringTextComponent("Caught " + toStringTranslate(entity.getDisplayName())).withStyle(TextFormatting.YELLOW));
        } else {
            stack.setHoverName(new StringTextComponent("Caught " + toStringTranslate(entity.getCustomName())).withStyle(TextFormatting.YELLOW));
        }
    }

    private static String toStringTranslate(ITextComponent c) {
        return (c).getString();
    }

    protected static double getYOffset(IWorldReader p_208051_0_, BlockPos p_208051_1_, boolean p_208051_2_, AxisAlignedBB p_208051_3_) {
        AxisAlignedBB axisalignedbb = new AxisAlignedBB(p_208051_1_);
        if (p_208051_2_) {
            axisalignedbb = axisalignedbb.expandTowards(0.0D, -1.0D, 0.0D);
        }

        Stream<VoxelShape> stream = p_208051_0_.getCollisions((Entity)null, axisalignedbb, (p_233596_0_) -> {
            return true;
        });
        return 1.0D + VoxelShapes.collide(Direction.Axis.Y, p_208051_3_, stream, p_208051_2_ ? -2.0D : -1.0D);
    }

    public static Entity createEntityFromItemStack(ItemStack stack, World world, PlayerEntity playerEntity,BlockPos pos) {
        CompoundNBT tag = stack.getOrCreateTag().getCompound("animalnet");
        Entity e = EntityType.loadEntityRecursive(tag.getCompound("entity"),world,(created) -> created);

        double d0;
        e.setPos((double)pos.getX() + 0.5D, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5D);
        d0 = getYOffset(world, pos, true, e.getBoundingBox());

        e.moveTo((double)pos.getX() + 0.5D, (double)pos.getY() + d0, (double)pos.getZ() + 0.5D, MathHelper.wrapDegrees(world.random.nextFloat() * 360.0F), 0.0F);
        if (e instanceof MobEntity) {
            MobEntity mobentity = (MobEntity)e;
            mobentity.yHeadRot = mobentity.yRot;
            mobentity.yBodyRot = mobentity.yRot;
            //mobentity.finalizeSpawn(p_220349_1_, p_220349_1_.getCurrentDifficultyAt(mobentity.blockPosition()), p_220349_6_, (ILivingEntityData)null, p_220349_2_);
        }

        return e;
    }
}


