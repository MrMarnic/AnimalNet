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
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;
import java.util.ArrayList;

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
        RecipeUtil.replaceRecipe(e,"animalnet:catched_animalToChild",new IRecipe() {

            private ItemStack match;

            private final ItemStack out = new ItemStack(AnimalNetItems.caughtEntityItem);

            @Override
            public boolean matches(InventoryCrafting inv, World worldIn) {
                if(inv.getStackInSlot(0).getItem().equals(Items.REDSTONE)&&
                        inv.getStackInSlot(1).getItem().equals(Items.REDSTONE)&&
                        inv.getStackInSlot(2).getItem().equals(Items.REDSTONE)&&
                        inv.getStackInSlot(3).getItem().equals(Items.REDSTONE)&&
                        inv.getStackInSlot(4).getItem().equals(AnimalNetItems.caughtEntityItem)&&
                        inv.getStackInSlot(5).getItem().equals(Items.REDSTONE)&&
                        inv.getStackInSlot(6).getItem().equals(Items.REDSTONE)&&
                        inv.getStackInSlot(7).getItem().equals(Items.REDSTONE)&&
                        inv.getStackInSlot(8).getItem().equals(Items.REDSTONE)) {
                    match = inv.getStackInSlot(4).copy();

                    if(match.getTagCompound().hasKey("age")) {
                        return true;
                    }

                    return false;
                }
                return false;
            }

            @Override
            public ItemStack getCraftingResult(InventoryCrafting inv) {
                ItemStack stack = match.copy();
                CaughtEntityItem.makeFakeChild(stack);
                return stack;
            }

            @Override
            public boolean canFit(int width, int height) {
                return true;
            }

            @Override
            public ItemStack getRecipeOutput() {
                return out;
            }

            @Override
            public IRecipe setRegistryName(ResourceLocation name) {
                return this;
            }

            @Override
            public ResourceLocation getRegistryName() {
                return new ResourceLocation("animalnet:caught_animalToChild");
            }

            @Override
            public Class<IRecipe> getRegistryType() {
                return null;
            }
        });

        RecipeUtil.replaceRecipe(e,"animalnet:caught_entity",new IRecipe() {

            private ItemStack match;

            private final ItemStack out = new ItemStack(AnimalNetItems.caughtEntityItem);

            @Override
            public boolean matches(InventoryCrafting inv, World worldIn) {
                if(inv.getStackInSlot(0).getItem().equals(Items.DYE)&&
                        inv.getStackInSlot(1).getItem().equals(Items.DYE)&&
                        inv.getStackInSlot(2).getItem().equals(Items.DYE)&&
                        inv.getStackInSlot(3).getItem().equals(Items.DYE)&&
                        inv.getStackInSlot(4).getItem().equals(AnimalNetItems.caughtEntityItem)&&
                        inv.getStackInSlot(5).getItem().equals(Items.DYE)&&
                        inv.getStackInSlot(6).getItem().equals(Items.DYE)&&
                        inv.getStackInSlot(7).getItem().equals(Items.DYE)&&
                        inv.getStackInSlot(8).getItem().equals(Items.DYE)) {
                    match = inv.getStackInSlot(4).copy();

                    if(match.getTagCompound().hasKey("age")) {
                        return true;
                    }
                    return false;
                }
                return false;
            }

            @Override
            public ItemStack getCraftingResult(InventoryCrafting inv) {
                ItemStack stack = match.copy();
                CaughtEntityItem.makeFakeAdult(stack);
                return stack;
            }

            @Override
            public boolean canFit(int width, int height) {
                return true;
            }

            @Override
            public ItemStack getRecipeOutput() {
                return out;
            }

            @Override
            public IRecipe setRegistryName(ResourceLocation name) {
                return this;
            }

            @Override
            public ResourceLocation getRegistryName() {
                return new ResourceLocation("animalnet:caught_animal");
            }

            @Override
            public Class<IRecipe> getRegistryType() {
                return null;
            }
        });
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
        if (e.crafting.getItem().equals(AnimalNetItems.caughtEntityItem)) {
            if (e.crafting.getTagCompound().getString("age").equalsIgnoreCase("Adult")) {
                CaughtEntityItem.makeAdult(e.crafting);
            }
            else {
                CaughtEntityItem.makeChild(e.crafting);
            }
        }
    }

    private static AxisAlignedBB boundingBox;
    private static double size;

    @SubscribeEvent
    public void entityRightClick(PlayerInteractEvent.EntityInteract e) {
        if(!e.getWorld().isRemote) {
            if (e.getHand() == EnumHand.MAIN_HAND) {
                boundingBox = e.getEntityLiving().getRenderBoundingBox();
                size = (boundingBox.maxX-boundingBox.minX)*(boundingBox.maxY-boundingBox.minY);
                if(AnimalNetItem.class.isAssignableFrom(e.getItemStack().getItem().getClass())) {
                    if(!checkEntity((AnimalNetItem)e.getItemStack().getItem(),e)) {
                        e.setCanceled(true);
                    }
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
                e.getEntityPlayer().inventory.removeStackFromSlot(e.getEntityPlayer().inventory.currentItem);
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
