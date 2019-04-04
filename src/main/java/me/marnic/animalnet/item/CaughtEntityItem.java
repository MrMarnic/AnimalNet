package me.marnic.animalnet.item;

import me.marnic.animalnet.api.BasicItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.enchantment.EnchantmentFishingSpeed;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
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

    private Entity entity;

    public ItemStack createInstance(EntityLiving e) {
        this.entity = e;

        ItemStack stack = new ItemStack(this);

        String name = findGoodName(e, e.worldObj.getSaveHandler().getWorldDirectory());

        NBTTagCompound tag = new NBTTagCompound();

        tag.setInteger("animalName", EntityList.getEntityID(entity));
        tag.setString("fileName", name);

        if(e.hasCustomNameTag()) {
            tag.setString("animalTag",e.getCustomNameTag());
        }else{
            tag.removeTag("animalTag");
        }
        tag.setString("location", "x:" + (int)e.posX + " y:" + (int)e.posY + " z:" + (int)e.posZ);
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

        stack.addEnchantment(EnchantmentFishingSpeed.unbreaking, 1);

        if(!e.hasCustomNameTag()) {
            stack.setStackDisplayName("Caught " + EntityList.getEntityString(entity));
        }else{
            stack.setStackDisplayName("Caught " + e.getCustomNameTag());
        }

        try {
            NBTTagCompound newtag = new NBTTagCompound();
            entity.writeToNBT(newtag);

            File f = new File(e.worldObj.getSaveHandler().getWorldDirectory().getAbsolutePath() + "//animalData//" + name + ".dat");
            f.createNewFile();

            CompressedStreamTools.write(newtag, f);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        ((EntityLiving) e).playLivingSound();

        return stack;
    }

    private String findGoodName(Entity e, File world) {
        File f = new File(world.getPath() + "//animalData");

        return e.getUniqueID().toString();
    }

    private void sendError(EntityPlayer player,String msg) {
        player.addChatComponentMessage(new ChatComponentText("§4" + msg));
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        if(p_77624_1_.getTagCompound()!=null) {
            if (p_77624_1_.getTagCompound().hasKey("animalTag")) {
                p_77624_3_.add(p_77624_1_.getTagCompound().getString("animalTag"));
            }
            p_77624_3_.add(p_77624_1_.getTagCompound().getString("location"));
            p_77624_3_.add(p_77624_1_.getTagCompound().getString("date"));
            if (p_77624_1_.getTagCompound().hasKey("age")) {
                p_77624_3_.add("Age: "+p_77624_1_.getTagCompound().getString("age"));
            }
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (p_77648_3_.isRemote)
        {
            return true;
        }
        else
        {
            Block block = p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_);
            p_77648_4_ += Facing.offsetsXForSide[p_77648_7_];
            p_77648_5_ += Facing.offsetsYForSide[p_77648_7_];
            p_77648_6_ += Facing.offsetsZForSide[p_77648_7_];
            double d0 = 0.0D;

            if (p_77648_7_ == 1 && block.getRenderType() == 11)
            {
                d0 = 0.5D;
            }

            NBTTagCompound tagCompound = p_77648_1_.getTagCompound();


            NBTTagCompound entTag = null;

            try {
                File f = new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath()+"//animalData//"+tagCompound.getString("fileName")+".dat");
                if(f.exists()) {
                    entTag = CompressedStreamTools.read(f);
                }else{
                    entTag = new NBTTagCompound();
                    sendError(p_77648_2_,"Error: The file for this entity : \""+f.getAbsolutePath() + "\" is missing!");
                }


                p_77648_2_.inventory.setInventorySlotContents(p_77648_2_.inventory.currentItem,null);

                f.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Entity entity = spawnCreature(p_77648_3_, tagCompound.getInteger("animalName"), (double)p_77648_4_ + 0.5D, (double)p_77648_5_ + d0, (double)p_77648_6_ + 0.5D,entTag);


            if (entity != null)
            {
                if(tagCompound.hasKey("animalTag")) {
                    ((EntityLiving)entity).setCustomNameTag(tagCompound.getString("animalTag"));
                    ((EntityLiving)entity).setAlwaysRenderNameTag(true);
                }

                if (!p_77648_2_.capabilities.isCreativeMode)
                {
                    --p_77648_1_.stackSize;
                }
            }

            return true;
        }
    }

    public static Entity spawnCreature(World p_77840_0_, int p_77840_1_, double p_77840_2_, double p_77840_4_, double p_77840_6_,NBTTagCompound tagCompound)
    {
        if (!EntityList.entityEggs.containsKey(Integer.valueOf(p_77840_1_)))
        {
            return null;
        }
        else
        {
            Entity entity = null;

            for (int j = 0; j < 1; ++j)
            {
                entity = EntityList.createEntityByID(p_77840_1_, p_77840_0_);

                if (entity != null && entity instanceof EntityLivingBase)
                {
                    EntityLiving entityliving = (EntityLiving)entity;
                    entity.setLocationAndAngles(p_77840_2_, p_77840_4_, p_77840_6_, MathHelper.wrapAngleTo180_float(p_77840_0_.rand.nextFloat() * 360.0F), 0.0F);
                    entityliving.rotationYawHead = entityliving.rotationYaw;
                    entityliving.renderYawOffset = entityliving.rotationYaw;
                    entityliving.onSpawnWithEgg((IEntityLivingData)null);
                    entityliving.readEntityFromNBT(tagCompound);
                    p_77840_0_.spawnEntityInWorld(entityliving);
                    entityliving.playLivingSound();
                }
            }

            return entity;
        }
    }

    public static NBTTagCompound getTagForEntityFromItem(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        File f = new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath()+"//animalData//"+tagCompound.getString("fileName")+".dat");

        try {
            if(f.exists()) {
                return CompressedStreamTools.read(f);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new NBTTagCompound();
    }

    public static void writeTagForEntityFromItem(ItemStack stack,NBTTagCompound entity) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        File f = new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath()+"//animalData//"+tagCompound.getString("fileName")+".dat");

        try {
            f.delete();
            CompressedStreamTools.write(entity,f);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void makeAdult(ItemStack stack) {

        NBTTagCompound compound = CaughtEntityItem.getTagForEntityFromItem(stack);
        compound.setInteger("Age",0);

        CaughtEntityItem.writeTagForEntityFromItem(stack,compound);
    }

    public static void makeFakeAdult(ItemStack stack) {
        NBTTagCompound st = stack.getTagCompound();

        st.setString("age","Adult");

        stack.setTagCompound(st);
    }

    public static void makeChild(ItemStack stack) {

        NBTTagCompound compound = CaughtEntityItem.getTagForEntityFromItem(stack);
        compound.setInteger("Age",-23000);

        CaughtEntityItem.writeTagForEntityFromItem(stack,compound);
    }

    public static void makeFakeChild(ItemStack stack) {
        NBTTagCompound st = stack.getTagCompound();

        st.setString("age","Child");

        stack.setTagCompound(st);
    }

}