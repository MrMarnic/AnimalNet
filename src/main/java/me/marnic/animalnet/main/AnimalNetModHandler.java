package me.marnic.animalnet.main;

/**
 * Copyright (c) 18.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

import me.marnic.animalnet.api.BasicItem;
import me.marnic.animalnet.api.IModelRegistry;
import me.marnic.animalnet.api.RecipeUtil;
import me.marnic.animalnet.item.AnimalNetItem;
import me.marnic.animalnet.item.CaughtEntityItem;
import me.marnic.animalnet.item.NetSize;
import me.marnic.animalnet.item.NetType;
import me.marnic.animalnet.recipes.RecipeAnimalToChild;
import me.marnic.animalnet.recipes.RecipeChildToAnimal;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class AnimalNetModHandler {
    public static final ArrayList<IModelRegistry> MODELS_TO_REGISTER = new ArrayList<>();

    private static boolean isValidEntity(Entity e) {
        return e instanceof EntityLiving;
    }

    @SubscribeEvent
    public void modelRegistryEvent(ModelRegistryEvent e) {
        for(IModelRegistry modelRegistry:MODELS_TO_REGISTER) {
            modelRegistry.registerModel();
        }
    }

    @SubscribeEvent
    public void itemRegisterEvent(RegistryEvent.Register<Item> e) {
        for(BasicItem item:AnimalNetItems.ITEMS_TO_REGISTER) {
            e.getRegistry().register(item);
        }
    }

    @SubscribeEvent
    public void recipeRegisterEvent(RegistryEvent.Register<IRecipe> e) {
        RecipeUtil.replaceRecipe(e,"animalnet:catched_animalToChild",new RecipeAnimalToChild());

        RecipeUtil.replaceRecipe(e,"animalnet:caught_entity",new RecipeChildToAnimal());
    }

    @SubscribeEvent
    public void worldLoad(PlayerEvent.PlayerLoggedInEvent e) {
        File f = new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath() +"//animalData");
        if(!f.exists()) {
            f.mkdir();
        }
    }

    @SubscribeEvent
    public void craft(final PlayerEvent.ItemCraftedEvent e) {
        if(!e.player.world.isRemote) {
            if (e.crafting.getItem().equals(AnimalNetItems.caughtEntityItem)) {
                if (e.crafting.getTagCompound().getString("age").equalsIgnoreCase("Adult")) {
                    CaughtEntityItem.makeAdult(e.crafting);
                }
                else {
                    CaughtEntityItem.makeChild(e.crafting);
                }
            }
        }
    }

    private static AxisAlignedBB boundingBox;
    private static double size;

    @SubscribeEvent
    public void entityRightClick(PlayerInteractEvent.EntityInteract e) {
        if(!e.getWorld().isRemote) {
            if (e.getHand() == EnumHand.MAIN_HAND) {
                boundingBox = e.getTarget().getEntityBoundingBox();
                size = (boundingBox.maxX-boundingBox.minX)*(boundingBox.maxY-boundingBox.minY);
                if(AnimalNetItem.class.isAssignableFrom(e.getItemStack().getItem().getClass())) {
                    if(!checkEntity((AnimalNetItem)e.getItemStack().getItem(),e)) {
                        e.setCanceled(true);
                    }
                }
            }
        }
    }

    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public void dropEvent(BlockEvent.HarvestDropsEvent e) {
        if(!e.getWorld().isRemote) {
            if(e.getState() == Blocks.MOB_SPAWNER.getDefaultState()) {
                if(RANDOM.nextInt(3)==2) {
                    e.getDrops().add(new ItemStack(AnimalNetItems.spawnerFragmental,2));
                }else {
                    e.getDrops().add(new ItemStack(AnimalNetItems.spawnerFragmental));
                }
            }
        }
    }

    private static boolean checkEntity(AnimalNetItem net,PlayerInteractEvent.EntityInteract e) {
        if(isValidEntity(e.getTarget())) {
            if (net.fitSize(size)) {
                if((canBeCaughtByAnimalNet(e.getTarget())&&net.getType()== NetType.ANIMAL)) {
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
                if(net.getSize()!= NetSize.BIG) {
                    if(canBeCaughtByAnimalNet(e.getTarget())&&net.getType()==NetType.ANIMAL) {
                        sendStatus(e.getEntityPlayer(), new TextComponentTranslation("message.animalnet.net_too_small"));
                    }else if(canBeCaughtByMobNet(e.getTarget())&&net.getType()==NetType.MOB) {
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

    private static ItemStack currentItem;

    private static boolean addNetToInv(PlayerInteractEvent.EntityInteract e) {
        ItemStack stack = AnimalNetItems.caughtEntityItem.createInstance(e.getTarget());
        addItem(e.getEntityPlayer(), stack);
        e.getTarget().setDead();
        if (!e.getEntityPlayer().isCreative()) {
            currentItem = e.getEntityPlayer().inventory.getCurrentItem();
            if (currentItem.getCount() > 1) {
                currentItem.setCount(currentItem.getCount() - 1);
                ItemStack damagedItemStack = new ItemStack(currentItem.getItem());
                damagedItemStack.damageItem(1,e.getEntityPlayer());
                addItem(e.getEntityPlayer(),damagedItemStack);
            } else {
                if(currentItem.getItemDamage()==0) {
                    currentItem.damageItem(1,e.getEntityPlayer());
                }else {
                    e.getEntityPlayer().inventory.removeStackFromSlot(e.getEntityPlayer().inventory.currentItem);
                }
            }
        }
        return true;
    }

    private static boolean canBeCaughtByAnimalNet(Entity entity) {
        return entity instanceof EntityAnimal || entity instanceof EntityWaterMob || entity instanceof IAnimals;
    }

    private static boolean canBeCaughtByMobNet(Entity entity) {
        return (entity instanceof EntityMob || entity instanceof IMob)  && !(entity instanceof EntityWither) && !(entity instanceof EntityDragon);
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
        }else{
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
