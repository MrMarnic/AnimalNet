package me.marnic.animalnet.mechanics;

import me.marnic.animalnet.items.AnimalNetItem;
import me.marnic.animalnet.items.NetSize;
import me.marnic.animalnet.items.NetType;
import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Npc;
import net.minecraft.entity.WaterCreatureEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BoundingBox;

/**
 * Copyright (c) 05.03.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class EntityHandler {

    private boolean isValidEntity(Entity e) {
        return e instanceof LivingEntity;
    }

    private double size;
    private BoundingBox boundingBox;
    private ItemStack currentItem;

    public boolean handleRightClick(LivingEntity e, Hand hand,ItemStack stack,PlayerEntity entity) {
        if(!e.getEntityWorld().isClient) {
            if (hand == Hand.MAIN) {
                boundingBox = e.getBoundingBox();
                size = (boundingBox.maxX-boundingBox.minX)*(boundingBox.maxY-boundingBox.minY);
                System.out.println(size);
                if(AnimalNetItem.class.isAssignableFrom(stack.getItem().getClass())) {
                    if(!checkEntity((AnimalNetItem)stack.getItem(),e,entity)) {

                    }
                }
            }
        }
        return false;
    }

    public boolean checkEntity(AnimalNetItem net, Entity target, PlayerEntity playerEntity) {
        if(isValidEntity(target)) {
            if (net.fitSize(size)) {
                if((canBeCaughtByAnimalNet(target)&&net.getType()== NetType.ANIMAL)) {
                    addNetToInv(playerEntity,target);
                }else{
                    if((canBeCaughtByMobNet(target)&&net.getType()==NetType.MOB)) {
                        addNetToInv(playerEntity,target);
                    }else if(net.getType()==NetType.NPC && target instanceof Npc){
                        addNetToInv(playerEntity, target);
                    }else{
                        sendCanNotBeCaught(playerEntity,target);
                    }
                }
            } else {
                if(net.getSize()!= NetSize.BIG) {
                    if(canBeCaughtByAnimalNet(target)&&net.getType()==NetType.ANIMAL) {
                        sendStatus(playerEntity, new TranslatableTextComponent("message.animalnet.net_too_small"));
                    }else if(canBeCaughtByMobNet(target)&&net.getType()==NetType.MOB) {
                        sendStatus(playerEntity, new TranslatableTextComponent("message.animalnet.net_too_small"));
                    }else{
                        sendCanNotBeCaught(playerEntity,target);
                    }
                }else{
                    sendStatus(playerEntity,  new TranslatableTextComponent("message.animalnet.entity_too_big",new TranslatableTextComponent(net.getType().getFormalTranslationKey())));
                }
            }
        }else{
            sendStatus(playerEntity,"This entity can not be caught!");
        }
        return false;
    }

    private boolean addNetToInv(PlayerEntity playerEntity,Entity target) {
        ItemStack stack = AnimalNetItems.caughtEntityItem.createInstance(target);
        addItem(playerEntity, stack);
        target.invalidate();
        if (!playerEntity.isCreative()) {
            currentItem = playerEntity.getActiveItem();
            playerEntity.inventory.getMainHandStack().applyDamage(1,playerEntity);
        }
        return true;
    }

    private boolean canBeCaughtByAnimalNet(Entity entity) {
        return entity instanceof AnimalEntity || entity instanceof WaterCreatureEntity;
    }

    private boolean canBeCaughtByMobNet(Entity entity) {
        return (entity instanceof HostileEntity || entity instanceof Monster)  && !(entity instanceof WitherEntity) && !(entity instanceof EnderDragonEntity);
    }

    private void sendCanNotBeCaught(PlayerEntity p, Entity e) {
        if(canBeCaughtByAnimalNet(e)) {
            sendStatus(p,new TranslatableTextComponent("message.animalnet.animal_needed"));
        }else if(canBeCaughtByMobNet(e)) {
            sendStatus(p,new TranslatableTextComponent("message.animalnet.mob_needed"));
        }else if(e instanceof Npc) {
            if(e instanceof VillagerEntity) {
                sendStatus(p,new TranslatableTextComponent("message.animalnet.villager_needed"));
            }else{
                sendStatus(p,new TranslatableTextComponent("message.animalnet.npc_needed"));
            }
        }else{
            sendStatus(p,new TranslatableTextComponent("message.animalnet.can_not_be_caught"));
        }
    }

    private void sendStatus(PlayerEntity p,String msg) {
        p.addChatMessage(new StringTextComponent(msg),true);
    }

    private void sendStatus(PlayerEntity p, TextComponent msg) {
        p.addChatMessage(msg,true);
    }


    private void addItem(PlayerEntity p, ItemStack stack) {
        boolean full = true;

        for(ItemStack s:p.inventory.main) {
            if(s.isEmpty()) {
                full = false;
                break;
            }
        }
        if(full) {
            p.dropItem(stack,false);
        }else{
            p.inventory.setInvStack(p.inventory.getEmptySlot(),stack);
        }
    }
}
