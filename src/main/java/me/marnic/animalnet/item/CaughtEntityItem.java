package me.marnic.animalnet.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.marnic.animalnet.api.BasicItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Copyright (c) 19.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class CaughtEntityItem extends BasicItem {

    public CaughtEntityItem() {
        super("caught_entity");
    }

    @Override
    public boolean shouldBeAddedToTab() {
        return false;
    }

    private Entity entity;

    public ItemStack createInstance(Entity e) {
        this.entity = e;

        ItemStack stack = new ItemStack(this);

        String name = findGoodName(e);

        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("animalName", EntityList.getKey(entity).toString());
        tag.setString("modName", EntityList.getKey(entity).getResourceDomain());
        tag.setString("fileName", name);

        if (e.hasCustomName()) {
            tag.setString("animalTag", e.getCustomNameTag());
        } else {
            tag.removeTag("animalTag");
        }
        tag.setString("location", "x:" + e.getPosition().getX() + " y:" + e.getPosition().getY() + " z:" + e.getPosition().getZ());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        tag.setString("date", formatter.format(date));

        if (e instanceof EntityAnimal) {
            int age = ((EntityAnimal) e).getGrowingAge();
            if (age < 0) {
                tag.setString("age", "Child");
            } else {
                tag.setString("age", "Adult");
            }
        }

        stack.setTagCompound(tag);

        if (!e.hasCustomName()) {
            stack.setStackDisplayName("Caught " + e.getDisplayName().getFormattedText());
        } else {
            stack.setStackDisplayName("Caught " + e.getCustomNameTag());
        }

        try {
            NBTTagCompound newtag = new NBTTagCompound();
            entity.writeToNBT(newtag);

            File f = new File(e.getEntityWorld().getSaveHandler().getWorldDirectory().getAbsolutePath() + "//animalData//" + name + ".dat");
            f.createNewFile();

            CompressedStreamTools.write(newtag, f);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        ((EntityLiving) e).playLivingSound();

        return stack;
    }

    private String findGoodName(Entity e) {
        return e.getUniqueID().toString();
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        ItemStack itemstack = player.getHeldItem(hand);

        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        } else if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
            return EnumActionResult.FAIL;
        } else {


            NBTTagCompound tagCompound = itemstack.getTagCompound();


            NBTTagCompound entTag = null;

            try {
                File f = new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + "//animalData//" + tagCompound.getString("fileName") + ".dat");
                if (f.exists()) {
                    entTag = CompressedStreamTools.read(f);
                } else {
                    entTag = new NBTTagCompound();
                    sendError(player, "Error: The file for this entity : \"" + f.getAbsolutePath() + "\" is missing!");
                }


                player.inventory.removeStackFromSlot(player.inventory.currentItem);

                f.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }

            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();


            BlockPos blockpos = pos.offset(facing);
            double d0 = this.getYOffset(worldIn, blockpos);

            Entity entity = spawnCreature(worldIn, new ResourceLocation(tagCompound.getString("animalName")), (double) blockpos.getX() + 0.5D, (double) blockpos.getY() + d0, (double) blockpos.getZ() + 0.5D, entTag, player);

            if (tagCompound.hasKey("animalTag")) {
                entity.setCustomNameTag(tagCompound.getString("animalTag"));
                entity.setAlwaysRenderNameTag(true);
            }

            return EnumActionResult.SUCCESS;
        }
    }

    private void sendError(EntityPlayer player, String msg) {
        player.sendMessage(new TextComponentString(ChatFormatting.RED + msg));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.getTagCompound() != null) {
            if (stack.getTagCompound().hasKey("animalTag")) {
                tooltip.add(stack.getTagCompound().getString("animalTag"));
            }
            tooltip.add(stack.getTagCompound().getString("location"));
            tooltip.add(stack.getTagCompound().getString("date"));
            if (stack.getTagCompound().hasKey("age")) {
                tooltip.add("Age: " + stack.getTagCompound().getString("age"));
            }
            if (stack.getTagCompound().hasKey("modName")) {
                tooltip.add("Mod: " + stack.getTagCompound().getString("modName"));
            }
        } else {
            tooltip.add("§4DO NOT USE THIS ITEM! IT IS JUST A PLACEHOLDER!");
        }
    }

    protected double getYOffset(World p_190909_1_, BlockPos p_190909_2_) {
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(p_190909_2_)).expand(0.0D, -1.0D, 0.0D);
        List<AxisAlignedBB> list = p_190909_1_.getCollisionBoxes((Entity) null, axisalignedbb);

        if (list.isEmpty()) {
            return 0.0D;
        } else {
            double d0 = axisalignedbb.minY;

            for (AxisAlignedBB axisalignedbb1 : list) {
                d0 = Math.max(axisalignedbb1.maxY, d0);
            }
            return d0 - (double) p_190909_2_.getY();
        }
    }

    @Nullable
    public static Entity spawnCreature(World worldIn, @Nullable ResourceLocation entityID, double x, double y, double z, NBTTagCompound tag, EntityPlayer player) {
        if (entityID != null) {
            Entity entity = null;


            for (int i = 0; i < 1; ++i) {
                entity = EntityList.createEntityByIDFromName(entityID, worldIn);
                if (entity instanceof EntityLiving) {
                    EntityLiving entityliving = (EntityLiving) entity;
                    entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
                    entityliving.rotationYawHead = entityliving.rotationYaw;
                    entityliving.renderYawOffset = entityliving.rotationYaw;
                    entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), null);
                    entityliving.readEntityFromNBT(tag);
                    worldIn.spawnEntity(entity);
                    entityliving.playLivingSound();
                }
            }

            return entity;
        }

        return null;
    }

    public static NBTTagCompound getTagForEntityFromItem(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        File f = new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + "//animalData//" + tagCompound.getString("fileName") + ".dat");

        try {
            if (f.exists()) {
                return CompressedStreamTools.read(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new NBTTagCompound();
    }

    public static void writeTagForEntityFromItem(ItemStack stack, NBTTagCompound entity) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        File f = new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + "//animalData//" + tagCompound.getString("fileName") + ".dat");

        try {
            f.delete();
            CompressedStreamTools.write(entity, f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void makeAdult(ItemStack stack) {

        NBTTagCompound compound = CaughtEntityItem.getTagForEntityFromItem(stack);
        compound.setInteger("Age", 0);

        CaughtEntityItem.writeTagForEntityFromItem(stack, compound);
    }

    public static void makeFakeAdult(ItemStack stack) {
        NBTTagCompound st = stack.getTagCompound();

        st.setString("age", "Adult");

        stack.setTagCompound(st);
    }

    public static void makeChild(ItemStack stack) {

        NBTTagCompound compound = CaughtEntityItem.getTagForEntityFromItem(stack);
        compound.setInteger("Age", -23000);

        CaughtEntityItem.writeTagForEntityFromItem(stack, compound);
    }

    public static void makeFakeChild(ItemStack stack) {
        NBTTagCompound st = stack.getTagCompound();

        st.setString("age", "Child");

        stack.setTagCompound(st);
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
