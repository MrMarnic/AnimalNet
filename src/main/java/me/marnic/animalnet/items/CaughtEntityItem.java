package me.marnic.animalnet.items;

import me.marnic.animalnet.api.EntityUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Copyright (c) 19.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class CaughtEntityItem extends Item {

    private Entity entity;

    public CaughtEntityItem(Properties properties) {
        super(properties);
    }

    public static void makeAdult(ItemStack stack) {

        CompoundNBT animalNet = stack.getOrCreateTag().getCompound("animalnet");
        CompoundNBT entityData = animalNet.getCompound("entity");
        CompoundNBT extraData = animalNet.getCompound("extraData");
        extraData.putString("age","Adult");
        entityData.putInt("Age", 0);
    }

    public static void makeChild(ItemStack stack) {
        CompoundNBT animalNet = stack.getOrCreateTag().getCompound("animalnet");
        CompoundNBT entityData = animalNet.getCompound("entity");
        CompoundNBT extraData = animalNet.getCompound("extraData");
        extraData.putString("age","Child");
        entityData.putInt("Age", -23000);
    }

    public ItemStack createInstance(Entity e) {
        this.entity = e;

        ItemStack stack = new ItemStack(this);
        EntityUtil.saveEntityToItemStack(e,stack);

        return stack;
    }

    public static boolean hasAge(ItemStack stack) {
        return stack.getOrCreateTag().getCompound("animalnet").getCompound("extraData").contains("age");
    }

    @Override
    public ActionResultType useOn(ItemUseContext con) {
        World world = con.getLevel();
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            ItemStack itemstack = con.getItemInHand();
            BlockPos blockpos = con.getClickedPos();
            Direction direction = con.getClickedFace();
            BlockState blockstate = world.getBlockState(blockpos);

            BlockPos blockpos1;
            if (blockstate.getCollisionShape(world, blockpos).isEmpty()) {
                blockpos1 = blockpos;
            } else {
                blockpos1 = blockpos.relative(direction);
            }
            Entity e = EntityUtil.createEntityFromItemStack(itemstack,world,con.getPlayer(),blockpos1);

            world.addFreshEntity(e);
            itemstack.shrink(1);

            if(!con.getPlayer().isCreative()) {
                return ActionResultType.CONSUME;
            }

            con.getPlayer().setItemInHand(con.getHand(), ItemStack.EMPTY);

            return ActionResultType.CONSUME;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getTag() != null) {
            CompoundNBT animalNetData = stack.getOrCreateTagElement("animalnet");
            CompoundNBT extraData = animalNetData.getCompound("extraData");
            tooltip.add(new StringTextComponent(extraData.getString("animalName")));
            tooltip.add(new StringTextComponent(extraData.getString("location")));
            tooltip.add(new StringTextComponent(extraData.getString("date")));
            if (extraData.contains("age")) {
                tooltip.add(new StringTextComponent("Age: " + extraData.getString("age")));
            }
            if (extraData.contains("modName")) {
                tooltip.add(new StringTextComponent("Mod: " + extraData.getString("modName")));
            }
        } else {
            tooltip.add(new StringTextComponent("DO NOT USE THIS ITEM! IT IS JUST A PLACEHOLDER!").withStyle(TextFormatting.RED));
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isFoil(ItemStack p_77636_1_) {
        return true;
    }
}
