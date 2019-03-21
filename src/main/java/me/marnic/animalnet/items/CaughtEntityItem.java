package me.marnic.animalnet.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.marnic.animalnet.api.BasicItem;
import me.marnic.animalnet.api.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

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

    public CaughtEntityItem(Properties properties) {
        super(properties, "caught_entity");
    }

    private Entity entity;

    public ItemStack createInstance(Entity e) {
        this.entity = e;

        ItemStack stack = new ItemStack(this);
        String name = findGoodName(e, e.getEntityWorld().getSaveHandler().getWorldDirectory());

        NBTTagCompound tag = new NBTTagCompound();

        tag.setString("animalName", EntityType.getId(e.getType()).toString());
        tag.setString("fileName", name);


        if(e.hasCustomName()) {
            if (!(toString(e.getCustomName()).equalsIgnoreCase(toString(e.getDisplayName())))) {
                System.out.println(e.getCustomName());
                tag.setString("animalTag", toString(e.getCustomName()));
            }else{
                tag.removeTag("animalTag");
            }
        }else{
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

        stack.setTag(tag);

        stack.addEnchantment(Enchantments.UNBREAKING, 1);

        if(!e.hasCustomName()) {
            stack.setDisplayName(new TextComponentString("Caught " + toStringTranslate(e.getDisplayName())));
        }else{
            stack.setDisplayName(new TextComponentString("Caught " + toStringTranslate(e.getCustomName())));
        }

        try {
            NBTTagCompound newtag = new NBTTagCompound();
            entity.writeWithoutTypeId(newtag);

            File f = new File(e.getEntityWorld().getSaveHandler().getWorldDirectory().getAbsolutePath() + "//animalData//" + name + ".dat");
            f.createNewFile();

            CompressedStreamTools.write(newtag, f);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        ((EntityLiving) e).playAmbientSound();

        return stack;
    }

    private String findGoodName(Entity e, File world) {
        File f = new File(world.getPath() + "//animalData");

        return e.getUniqueID().toString();
    }

    private String toString(ITextComponent c) {
        return ((TextComponentString) c).getText();
    }

    private String toStringTranslate(ITextComponent c) {
        return ( c).getFormattedText();
    }

    @Override
    public EnumActionResult onItemUse(ItemUseContext con) {
        World world = con.getWorld();
        if (world.isRemote) {
            return EnumActionResult.SUCCESS;
        } else {
            ItemStack itemstack = con.getItem();
            BlockPos blockpos = con.getPos();
            EnumFacing enumfacing = con.getFace();
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();


            BlockPos blockpos1;
            if (iblockstate.getCollisionShape(world, blockpos).isEmpty()) {
                blockpos1 = blockpos;
            } else {
                blockpos1 = blockpos.offset(enumfacing);
            }

            EntityType<EntityLiving> entitytype = getType(itemstack.getTag());
            EntityLiving living = null;

            NBTTagCompound tag = con.getItem().getTag();

            NBTTagCompound entTag = null;

            try {
                File f = new File(con.getWorld().getSaveHandler().getWorldDirectory().getAbsolutePath()+"//animalData//"+tag.getString("fileName")+".dat");

                if(f.exists()) {
                    entTag = CompressedStreamTools.read(f);
                }else{
                    entTag = new NBTTagCompound();
                    sendError(con.getPlayer(),"Error: The file for this entity : \""+f.getAbsolutePath() + "\" is missing!");
                }

                ITextComponent custName = null;

                if(tag.hasKey("animalTag")) {
                    custName = (new TextComponentString(tag.getString("animalTag")));
                }

                living = EntityUtil.createEntity(entitytype,world,entTag,custName,con.getPlayer(),blockpos1,true,!Objects.equals(blockpos, blockpos1) && enumfacing == EnumFacing.UP);
                double x = living.posX;
                double y = living.posY;
                double z = living.posZ;
                float yaw = living.rotationYaw;
                float pitch = living.rotationPitch;


                living.read(entTag);

                living.setPositionAndRotation(x,y,z,yaw,pitch);

                world.spawnEntity(living);

                con.getPlayer().inventory.removeStackFromSlot(con.getPlayer().inventory.currentItem);

                f.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (entitytype == null || living != null) {
                itemstack.shrink(1);
            }

            return EnumActionResult.SUCCESS;
        }
    }

    private EntityType getType(NBTTagCompound c) {
        String id = c.getString("animalName");
        return EntityType.getById(id);
    }

    private void sendError(EntityPlayer player,String msg) {
        player.sendMessage(new TextComponentString(ChatFormatting.RED + msg));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(stack.getTag()!=null) {
            if (stack.getTag().hasKey("animalTag")) {
                tooltip.add(new TextComponentString(stack.getTag().getString("animalTag")));
            }
            tooltip.add(new TextComponentString(stack.getTag().getString("location")));
            tooltip.add(new TextComponentString(stack.getTag().getString("date")));
            if (stack.getTag().hasKey("age")) {
                tooltip.add(new TextComponentString("Age: "+stack.getTag().getString("age")));
            }
        }
    }
}
