package me.marnic.animalnet.main;

/**
 * Copyright (c) 18.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import me.marnic.animalnet.item.AnimalNetItem;
import me.marnic.animalnet.item.CaughtEntityItem;
import me.marnic.animalnet.item.NetSize;
import me.marnic.animalnet.item.NetType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.io.File;

public class AnimalNetModHandler {
    private static boolean isValidEntity(Entity e) {
        return e instanceof EntityLiving;
    }

    @SubscribeEvent
    public void craft(final PlayerEvent.ItemCraftedEvent e) {
        if (e.crafting.getItem().equals(AnimalNetItems.caughtEntityItem)) {
            if (e.crafting.getTagCompound().getString("age").equalsIgnoreCase("Adult")) {
                CaughtEntityItem.makeAdult(e.crafting);
            }
            else {
                CaughtEntityItem.makeChild(e.crafting);
            }
        }
    }

    @SubscribeEvent
    public void worldLoad(WorldEvent.Load e) {
        if(!e.world.isRemote) {
            File f = new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() + "//animalData");
            if (!f.exists()) {
                f.mkdir();
            }
        }
    }

    private static AxisAlignedBB boundingBox;
    private static double size;

    @SubscribeEvent
    public void entityRightClick(EntityInteractEvent e) {
        if(!e.entityLiving.worldObj.isRemote) {
                boundingBox = e.entityLiving.boundingBox;
                size = e.target.width*e.target.height;
            System.out.println(size);
                if(e.entityPlayer.getHeldItem()!=null) {
                    if (AnimalNetItem.class.isAssignableFrom(e.entityPlayer.getHeldItem().getItem().getClass())) {
                        if (!checkEntity((AnimalNetItem) e.entityPlayer.getHeldItem().getItem(), e)) {
                            e.setCanceled(true);
                        }
                    }
                }
        }
    }

    private static boolean checkEntity(AnimalNetItem net,EntityInteractEvent e) {
        if(isValidEntity(e.target)) {
            if (net.fitSize(size)) {
                if((canBeCaughtByAnimalNet(e.target)&&net.getType()== NetType.ANIMAL)) {
                    addNetToInv(e);
                }else{
                    if((canBeCaughtByMobNet(e.target)&&net.getType()==NetType.MOB)) {
                        addNetToInv(e);
                    }else if(net.getType()==NetType.NPC && e.target instanceof INpc){
                        addNetToInv(e);
                    }else{
                        sendCanNotBeCaught(e.entityPlayer,e.target);
                    }
                }
            } else {
                if(net.getSize()!= NetSize.BIG) {
                    if(canBeCaughtByAnimalNet(e.target)&&net.getType()==NetType.ANIMAL) {
                        sendStatus(e.entityPlayer, new ChatComponentTranslation("message.animalnet.net_too_small"));
                    }else if(canBeCaughtByMobNet(e.target)&&net.getType()==NetType.MOB) {
                        sendStatus(e.entityPlayer, new ChatComponentTranslation("message.animalnet.net_too_small"));
                    }else{
                        sendCanNotBeCaught(e.entityPlayer,e.target);
                    }
                }else{
                    sendStatus(e.entityPlayer, new ChatComponentTranslation("message.animalnet.entity_too_big",new ChatComponentTranslation(net.getType().getFormalTranslationKey())));
                }
            }
        }else{
            sendStatus(e.entityPlayer,new ChatComponentTranslation("message.animalnet.can_not_be_caught"));
        }
        return false;
    }

    private static ItemStack currentItem;

    private static boolean addNetToInv(EntityInteractEvent e) {
        ItemStack stack = AnimalNetItems.caughtEntityItem.createInstance((EntityLiving)e.target);
        addItem(e.entityPlayer, stack);
        e.target.setDead();
        if (!e.entityPlayer.capabilities.isCreativeMode) {
            currentItem = e.entityPlayer.inventory.getCurrentItem();
            if (currentItem.stackSize > 1) {
                currentItem.stackSize = (currentItem.stackSize - 1);
                ItemStack damagedItemStack = new ItemStack(currentItem.getItem());
                damagedItemStack.damageItem(1,e.entityPlayer);
                addItem(e.entityPlayer,damagedItemStack);
            } else {
                e.entityPlayer.inventory.setInventorySlotContents(e.entityPlayer.inventory.currentItem,null);
            }
        }
        return true;
    }

    private static boolean canBeCaughtByAnimalNet(Entity entity) {
        return entity instanceof EntityAnimal || entity instanceof EntityWaterMob;
    }

    private static boolean canBeCaughtByMobNet(Entity entity) {
        return (entity instanceof EntityMob || entity instanceof IMob)  && !(entity instanceof EntityWither) && !(entity instanceof EntityDragon);
    }

    private static void sendCanNotBeCaught(EntityPlayer p, Entity e) {
        if(canBeCaughtByAnimalNet(e)) {
            sendStatus(p,new ChatComponentTranslation("message.animalnet.animal_needed"));
        }else if(canBeCaughtByMobNet(e)) {
            sendStatus(p,new ChatComponentTranslation("message.animalnet.mob_needed"));
        }else if(e instanceof INpc) {
            if(e instanceof EntityVillager) {
                sendStatus(p,new ChatComponentTranslation("message.animalnet.villager_needed"));
            }else{
                sendStatus(p,new ChatComponentTranslation("message.animalnet.npc_needed"));
            }
        }else{
            sendStatus(p,new ChatComponentTranslation("message.animalnet.can_not_be_caught"));
        }
    }

    private static void sendStatus(EntityPlayer p,String msg) {
        p.addChatComponentMessage(new ChatComponentText(msg));
    }

    private static void sendStatus(EntityPlayer p, ChatComponentStyle msg) {
        p.addChatComponentMessage(msg);
    }

    private static void addItem(EntityPlayer p, ItemStack stack) {
        boolean full = true;

        for(ItemStack s:p.inventory.mainInventory) {
            if(s==null) {
                full = false;
                break;
            }
        }
        if(full) {
            p.entityDropItem(stack,0.05f);
        }else{
            p.inventory.addItemStackToInventory(stack);
        }
        p.inventoryContainer.detectAndSendChanges();
    }

}