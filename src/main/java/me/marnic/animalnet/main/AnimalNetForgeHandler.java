package me.marnic.animalnet.main;

import me.marnic.animalnet.api.SpawnerUtil;
import me.marnic.animalnet.config.AnimalNetConfig;
import me.marnic.animalnet.items.AnimalNetItem;
import me.marnic.animalnet.items.CaughtEntityItem;
import me.marnic.animalnet.items.NetSize;
import me.marnic.animalnet.items.NetType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.INpc;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.RecipeType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;
import java.util.Random;

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
        return e instanceof EntityLiving && canBeCaughtAtAll(e);
    }

    @SubscribeEvent
    public static void entityRightClick(PlayerInteractEvent.EntityInteract e) {
        if(!e.getWorld().isRemote) {
            if (e.getHand() == EnumHand.MAIN_HAND) {
                boundingBox = e.getTarget().getBoundingBox();
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

    @SubscribeEvent
    public static void craft(final PlayerEvent.ItemCraftedEvent e) {
        if(!e.getPlayer().getEntityWorld().isRemote) {
            if (e.getCrafting().getItem().equals(AnimalNetItems.caughtEntityItem)) {
                if (e.getCrafting().getTag().getString("age").equalsIgnoreCase("Adult")) {
                    CaughtEntityItem.makeAdult(e.getCrafting(),e.getPlayer().world);
                }
                else {
                    CaughtEntityItem.makeChild(e.getCrafting(),e.getPlayer().world);
                }
            }
        }
    }

    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void dropEvent(BlockEvent.HarvestDropsEvent e) {
        if (!e.getWorld().isRemote()) {
            if (e.getState() == Blocks.SPAWNER.getDefaultState()) {
                boolean b = false;
                for (ItemStack s : e.getDrops()) {
                    if (s.getItem().equals(Item.getItemFromBlock(e.getState().getBlock()))) {
                        b = true;
                        break;
                    }
                }
                if (!b) {
                    if (RANDOM.nextInt(3) == 2) {
                        e.getDrops().add(new ItemStack(AnimalNetItems.spawnerFragmental, 2));
                    } else {
                        e.getDrops().add(new ItemStack(AnimalNetItems.spawnerFragmental));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void blockPlace(BlockEvent.EntityPlaceEvent e) {
        if (!e.getWorld().isRemote()) {
            if (e.getPlacedBlock() == Blocks.SPAWNER.getDefaultState() && e.getEntity() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) e.getEntity();
                if (SpawnerUtil.isCustomSpawner(player.getHeldItemMainhand())) {
                    SpawnerUtil.makeSpawnerBlock(player.getHeldItemMainhand(), e.getWorld(), e.getPos());
                    if(player.isCreative()) {
                        ItemStack stack = player.getHeldItemMainhand().copy();
                        player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND,stack);
                    }
                }
            }
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
                    }else if(e.getTarget() instanceof EntityLiving && net.getType() == NetType.ANIMAL){
                        addNetToInv(e);
                    }else{
                        sendCanNotBeCaught(e.getEntityPlayer(),e.getTarget());
                    }
                }
            } else {
                if(net.getSize()!=NetSize.BIG) {
                    if(canBeCaughtByAnimalNet(e.getTarget())&&net.getType()==NetType.ANIMAL) {
                        sendStatus(e.getEntityPlayer(), new TextComponentTranslation("message.animalnet.net_too_small"));
                    }else if(canBeCaughtByMobNet(e.getTarget())&&net.getType()==NetType.MOB) {
                        sendStatus(e.getEntityPlayer(), new TextComponentTranslation("message.animalnet.net_too_small"));
                    }else if(e.getTarget() instanceof EntityLiving && net.getType() == NetType.ANIMAL){
                        sendStatus(e.getEntityPlayer(), new TextComponentTranslation("message.animalnet.net_too_small"));
                    }else{
                        sendCanNotBeCaught(e.getEntityPlayer(),e.getTarget());
                    }
                }else{
                    sendStatus(e.getEntityPlayer(), new TextComponentTranslation("message.animalnet.entity_too_big",new TextComponentTranslation(net.getType().getFormalTranslationKey())));
                }
            }
        }else{
            sendStatus(e.getEntityPlayer(),new TextComponentTranslation("message.animalnet.can_not_be_caught"));
        }
        return false;
    }

    private static boolean addNetToInv(PlayerInteractEvent.EntityInteract e) {
        ItemStack stack = AnimalNetItems.caughtEntityItem.createInstance(e.getTarget());
        addItem(e.getEntityPlayer(), stack);
        e.getTarget().remove();
        if (!e.getEntityPlayer().isCreative()) {
            currentItem = e.getEntityPlayer().inventory.getCurrentItem();
            boolean add = false;
            ItemStack stack1 = currentItem;
            if(currentItem.getMaxDamage() > 0) {
                if (currentItem.getCount() > 1) {
                    e.getEntityPlayer().getHeldItemMainhand().shrink(1);
                    ItemStack damagedItemStack = new ItemStack(currentItem.getItem());
                    stack1 = damagedItemStack;
                    add = true;
                }
                damageItem(stack1,e.getEntityPlayer().inventory,add);
            }else {
                e.getEntityPlayer().getHeldItemMainhand().shrink(1);
            }
        }
        return true;
    }

    private static void damageItem(ItemStack stack, InventoryPlayer inventoryPlayer, boolean addItem) {
        stack.damageItem(1,inventoryPlayer.player);
        if(addItem) {
            addItem(inventoryPlayer.player,stack);
        }
    }

    private static boolean canBeCaughtByAnimalNet(Entity entity) {
        return entity instanceof EntityAnimal || entity instanceof EntityWaterMob || entity instanceof IAnimal;
    }

    private static boolean canBeCaughtByMobNet(Entity entity) {
        return (entity instanceof EntityMob || entity instanceof IMob);
    }

    private static boolean canBeCaughtAtAll(Entity entity) {
        return !AnimalNetConfig.general_options.getExcluded_entities_array_list().contains(EntityType.getId(entity.getType()).toString());
    }

    private static void sendCanNotBeCaught(EntityPlayer p, Entity e) {
        if(canBeCaughtByAnimalNet(e)) {
            sendStatus(p,new TextComponentTranslation("message.animalnet.animal_needed"));
        }else if(canBeCaughtByMobNet(e)) {
            sendStatus(p,new TextComponentTranslation("message.animalnet.mob_needed"));
        }else if(e instanceof INpc) {
            if(e instanceof EntityVillager) {
                sendStatus(p,new TextComponentTranslation("message.animalnet.villager_needed"));
            }else{
                sendStatus(p,new TextComponentTranslation("message.animalnet.npc_needed"));
            }
        } if(e instanceof EntityLiving) {
            sendStatus(p, new TextComponentTranslation("message.animalnet.animal_needed"));
        } else{
            sendStatus(p,new TextComponentTranslation("message.animalnet.can_not_be_caught"));
        }
    }

    private static void sendStatus(EntityPlayer p,String msg) {
        p.sendStatusMessage(new TextComponentString(msg),true);
    }

    private static void sendStatus(EntityPlayer p, ITextComponent msg) {
        p.sendStatusMessage(msg,true);
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
