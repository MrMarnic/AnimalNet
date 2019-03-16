package me.marnic.animalnet.main;

import me.marnic.animalnet.items.AnimalNetItem;
import me.marnic.animalnet.items.NetSize;
import me.marnic.animalnet.items.NetType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.RecipeType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;

/**
 * Copyright (c) 20.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class AnimalNetForgeHandler {
    private static AxisAlignedBB boundingBox;
    private static double size;
    private static ItemStack currentItem;

    static boolean isValidEntity(Entity e) {
        return e instanceof EntityLiving;
    }

    @SubscribeEvent
    public static void entityRightClick(PlayerInteractEvent.EntityInteract e) {
        if(!e.getWorld().isRemote) {
            if (e.getHand() == EnumHand.MAIN_HAND) {
                boundingBox = e.getEntityLiving().getBoundingBox();
                size = (boundingBox.maxX-boundingBox.minX)*(boundingBox.maxY-boundingBox.minY);
                if(AnimalNetItem.class.isAssignableFrom(e.getItemStack().getItem().getClass())) {
                    if(!checkEntity((AnimalNetItem)e.getItemStack().getItem(),e)) {
                        e.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void worldLoad(PlayerEvent.PlayerLoggedInEvent e) {
        File f = new File(e.getPlayer().getEntityWorld().getSaveHandler().getWorldDirectory().getPath()+"//animalData");
        if(!f.exists()) {
            f.mkdir();
        }

    }

    private static boolean checkEntity(AnimalNetItem net,PlayerInteractEvent.EntityInteract e) {
        if(isValidEntity(e.getTarget())) {
            if (net.fitSize(size)) {
                if((canBeCaughtByAnimalNet(e.getTarget())&&net.getType()==NetType.ANIMAL)) {
                    addNetToInv(e);
                }else{
                    if((canBeCaughtByMobNet(e.getTarget())&&net.getType()==NetType.MOB)) {
                        addNetToInv(e);
                    }else if(net.getType()==NetType.NPC && e.getTarget() instanceof INpc){
                        addNetToInv(e);
                    }else{
                        sendCanNotBeCaught(e.getEntityPlayer(),e.getTarget());
                    }
                }
            } else {
                if(net.getSize()!=NetSize.BIG) {
                    if(canBeCaughtByAnimalNet(e.getTarget())&&net.getType()==NetType.ANIMAL) {
                        sendStatus(e.getEntityPlayer(), "You need a bigger net!");
                    }else if(canBeCaughtByMobNet(e.getTarget())&&net.getType()==NetType.MOB) {
                        sendStatus(e.getEntityPlayer(), "You need a bigger net!");
                    }else{
                        sendCanNotBeCaught(e.getEntityPlayer(),e.getTarget());
                    }
                }else{
                    sendStatus(e.getEntityPlayer(), "This " + net.getType().getName() +  " is too big. It can not be caught!");
                }
            }
        }else{
            sendStatus(e.getEntityPlayer(),"This entity can not be caught!");
        }
        return false;
    }

    private static boolean addNetToInv(PlayerInteractEvent.EntityInteract e) {
        ItemStack stack = AnimalNetItems.caughtEntityItem.createInstance(e.getTarget());
        addItem(e.getEntityPlayer(), stack);
        e.getTarget().remove();
        if (!e.getEntityPlayer().isCreative()) {
            currentItem = e.getEntityPlayer().inventory.getCurrentItem();
            currentItem.damageItem(1,e.getEntityPlayer());
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
            sendStatus(p,"You need an animal net to catch normal animals!");
        }else if(canBeCaughtByMobNet(e)) {
            sendStatus(p,"To catch a mob you need a mob net!");
        }else if(e instanceof INpc) {
            if(e instanceof EntityVillager) {
                sendStatus(p,"To catch a villager you need a npc net!");
            }else{
                sendStatus(p,"To catch a npc you need a npc net!");
            }
        }else{
            sendStatus(p,"This entity can not be caught!");
        }
    }

    private static void sendStatus(EntityPlayer p,String msg) {
        p.sendStatusMessage(new TextComponentString(msg),true);
    }

    private static void addItem(EntityPlayer p, ItemStack stack) {
        boolean full = true;

        for(ItemStack s:p.inventory.mainInventory) {
            if(s.isEmpty()) {
                full = false;
                break;
            }
        }
        if(full) {
            p.dropItem(stack,false);
        }else{
            p.addItemStackToInventory(stack);
        }
    }
}
