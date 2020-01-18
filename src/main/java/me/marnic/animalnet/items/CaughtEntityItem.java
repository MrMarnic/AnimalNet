package me.marnic.animalnet.items;

import me.marnic.animalnet.api.BasicItem;
import me.marnic.animalnet.api.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

/**
 * Copyright (c) 19.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class CaughtEntityItem extends BasicItem {

    private Entity entity;

    public CaughtEntityItem(Item.Properties properties) {
        super(properties, "caught_entity");
    }

    public static void makeAdult(ItemStack stack, World world) {

        CompoundNBT compound = CaughtEntityItem.getTagForEntityFromItem(stack, world);
        compound.putInt("Age", 0);

        CaughtEntityItem.writeTagForEntityFromItem(stack, compound, world);
    }

    public static void makeFakeAdult(ItemStack stack) {
        CompoundNBT st = stack.getTag();

        st.putString("age", "Adult");

        stack.setTag(st);
    }

    public static void makeChild(ItemStack stack, World world) {

        CompoundNBT compound = CaughtEntityItem.getTagForEntityFromItem(stack, world);
        compound.putInt("Age", -23000);

        CaughtEntityItem.writeTagForEntityFromItem(stack, compound, world);
    }

    public static void makeFakeChild(ItemStack stack) {
        CompoundNBT st = stack.getTag();

        st.putString("age", "Child");

        stack.setTag(st);
    }

    public static CompoundNBT getTagForEntityFromItem(ItemStack stack, World world) {
        CompoundNBT tagCompound = stack.getTag();

        File f = new File(((ServerWorld) world).getSaveHandler().getWorldDirectory().getAbsolutePath() + "//animalData//" + tagCompound.getString("fileName") + ".dat");

        try {
            if (f.exists()) {
                return CompressedStreamTools.read(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CompoundNBT();
    }

    public static void writeTagForEntityFromItem(ItemStack stack, CompoundNBT entity, World world) {
        CompoundNBT tagCompound = stack.getTag();
        File f = new File(((ServerWorld) world).getSaveHandler().getWorldDirectory().getAbsolutePath() + "//animalData//" + tagCompound.getString("fileName") + ".dat");

        try {
            f.delete();
            CompressedStreamTools.write(entity, f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ItemStack createInstance(Entity e) {
        this.entity = e;

        ItemStack stack = new ItemStack(this);
        String name = findGoodName(e);

        CompoundNBT tag = new CompoundNBT();

        tag.putString("animalName", EntityType.getKey(e.getType()).toString());
        tag.putString("modName", EntityType.getKey(e.getType()).getNamespace());
        tag.putString("fileName", name);


        if (e.hasCustomName()) {
            if (!(toString(e.getCustomName()).equalsIgnoreCase(toString(e.getDisplayName())))) {
                System.out.println(e.getCustomName());
                tag.putString("animalTag", toString(e.getCustomName()));
            } else {
                tag.remove("animalTag");
            }
        } else {
            tag.remove("animalTag");
        }
        tag.putString("location", "x:" + e.getPosition().getX() + " y:" + e.getPosition().getY() + " z:" + e.getPosition().getZ());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        tag.putString("date", formatter.format(date));

        if (e instanceof AnimalEntity) {
            int age = ((AnimalEntity) e).getGrowingAge();
            if (age < 0) {
                tag.putString("age", "Child");
            } else {
                tag.putString("age", "Adult");
            }
        }

        stack.setTag(tag);

        if (!e.hasCustomName()) {
            stack.setDisplayName(new StringTextComponent("Caught " + toStringTranslate(e.getDisplayName())).applyTextStyle(TextFormatting.YELLOW));
        } else {
            stack.setDisplayName(new StringTextComponent("Caught " + toStringTranslate(e.getCustomName())).applyTextStyle(TextFormatting.YELLOW));
        }

        try {
            CompoundNBT newtag = new CompoundNBT();
            entity.writeWithoutTypeId(newtag);

            File f = new File(((ServerWorld) e.getEntityWorld()).getSaveHandler().getWorldDirectory().getAbsolutePath() + "//animalData//" + name + ".dat");
            f.createNewFile();

            CompressedStreamTools.write(newtag, f);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        if (e instanceof AnimalEntity) {
            ((AnimalEntity) e).playAmbientSound();
        }

        return stack;
    }

    private String findGoodName(Entity e) {
        return e.getUniqueID().toString();
    }

    private String toString(ITextComponent c) {
        return ((StringTextComponent) c).getText();
    }

    private String toStringTranslate(ITextComponent c) {
        return (c).getFormattedText();
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext con) {
        World world = con.getWorld();
        if (world.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            ItemStack itemstack = con.getItem();
            BlockPos blockpos = con.getPos();
            Direction enumfacing = con.getFace();
            BlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();


            BlockPos blockpos1;
            if (iblockstate.getCollisionShape(world, blockpos).isEmpty()) {
                blockpos1 = blockpos;
            } else {
                blockpos1 = blockpos.offset(enumfacing);
            }

            EntityType<LivingEntity> entitytype = getType(itemstack.getTag());
            LivingEntity living = null;

            CompoundNBT tag = con.getItem().getTag();

            CompoundNBT entTag = null;

            try {
                File f = new File(((ServerWorld) con.getWorld()).getSaveHandler().getWorldDirectory().getAbsolutePath() + "//animalData//" + tag.getString("fileName") + ".dat");

                if (f.exists()) {
                    entTag = CompressedStreamTools.read(f);
                } else {
                    entTag = new CompoundNBT();
                    sendError(con.getPlayer(), "Error: The file for this entity : \"" + f.getAbsolutePath() + "\" is missing!");
                }

                ITextComponent custName = null;

                if (tag.contains("animalTag")) {
                    custName = (new StringTextComponent(tag.getString("animalTag")));
                }

                living = EntityUtil.createEntity(entitytype, world, entTag, custName, con.getPlayer(), blockpos1, true, !Objects.equals(blockpos, blockpos1) && enumfacing == Direction.UP);
                double x = living.getPosition().getX();
                double y = living.getPosition().getY();
                double z = living.getPosition().getZ();
                float yaw = living.rotationYaw;
                float pitch = living.rotationPitch;


                living.read(entTag);

                living.setPositionAndRotation(x, y, z, yaw, pitch);

                world.addEntity(living);

                con.getPlayer().inventory.removeStackFromSlot(con.getPlayer().inventory.currentItem);

                f.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (entitytype == null || living != null) {
                itemstack.shrink(1);
            }

            return ActionResultType.SUCCESS;
        }
    }

    private EntityType getType(CompoundNBT c) {
        String id = c.getString("animalName");
        return EntityType.byKey(id).get();
    }

    private void sendError(PlayerEntity player, String msg) {
        player.sendMessage(new StringTextComponent(TextFormatting.RED + msg));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getTag() != null) {
            if (stack.getTag().contains("animalTag")) {
                tooltip.add(new StringTextComponent(stack.getTag().getString("animalTag")));
            }
            tooltip.add(new StringTextComponent(stack.getTag().getString("location")));
            tooltip.add(new StringTextComponent(stack.getTag().getString("date")));
            if (stack.getTag().contains("age")) {
                tooltip.add(new StringTextComponent("Age: " + stack.getTag().getString("age")));
            }
            if (stack.getTag().contains("modName")) {
                tooltip.add(new StringTextComponent("Mod: " + stack.getTag().getString("modName")));
            }
        } else {
            tooltip.add(new StringTextComponent("DO NOT USE THIS ITEM! IT IS JUST A PLACEHOLDER!").applyTextStyle(TextFormatting.RED));
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }
}
