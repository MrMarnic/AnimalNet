package me.marnic.animalnet.item;

import me.marnic.animalnet.item.net.*;
import me.marnic.modapi.handler.ModHandler;
import me.marnic.modapi.util.RecipeUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.INpc;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import javax.annotation.Nullable;
import java.io.File;
import java.util.HashMap;

/**
 * Created by Marcel on Nov,2018
 */
public class AnimalNetModHandler extends ModHandler {

    public static final HashMap<String,EntityEntry> registeredEntities = new HashMap<>();

    static boolean isValidEntity(Entity e) {
        return registeredEntities.containsKey(e.getClass().getName());
    }

    public static AnimalNetItemSmall animalNetItemSmall;
    public static AnimalNetItemMedium animalNetItemMedium;
    public static AnimalNetItemBig animalNetItemBig;

    public static CaughtAnimalItem catchedAnimalItem;

    public static MobNetItemSmall mobNetItemSmall;
    public static MobNetItemBig mobNetItemBig;

    public static NpcNet npcNet;

    public static FishNet fishNet;

    public static MobCore mobCore;

    public static CreativeTabs CREATIVE_TAB;

    @Override
    public void init() {
        animalNetItemSmall = new AnimalNetItemSmall();
        animalNetItemMedium = new AnimalNetItemMedium();
        animalNetItemBig = new AnimalNetItemBig();
        mobNetItemSmall = new MobNetItemSmall();
        mobNetItemBig = new MobNetItemBig();
        catchedAnimalItem = new CaughtAnimalItem();
        mobCore = new MobCore();
        npcNet = new NpcNet();
        fishNet = new FishNet();

        CREATIVE_TAB = new CreativeTabs("AnimalNet Items") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(animalNetItemSmall);
            }
        };

        animalNetItemSmall.setCreativeTab(CREATIVE_TAB);
        animalNetItemMedium.setCreativeTab(CREATIVE_TAB);
        animalNetItemBig.setCreativeTab(CREATIVE_TAB);
        mobNetItemSmall.setCreativeTab(CREATIVE_TAB);
        mobNetItemBig.setCreativeTab(CREATIVE_TAB);
        catchedAnimalItem.setCreativeTab(CREATIVE_TAB);
        mobCore.setCreativeTab(CREATIVE_TAB);
        npcNet.setCreativeTab(CREATIVE_TAB);
        fishNet.setCreativeTab(CREATIVE_TAB);
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        register(animalNetItemSmall,event);
        register(animalNetItemMedium,event);
        register(animalNetItemBig,event);
        register(mobNetItemSmall,event);
        register(mobNetItemBig,event);
        register(catchedAnimalItem,event);
        register(mobCore,event);
        register(npcNet,event);
    }

    private AxisAlignedBB boundingBox;
    private double size;
    private ItemStack currentItem;

    @SubscribeEvent
    public void recipe(RegistryEvent.Register<IRecipe> e) {

        RecipeUtil.replaceRecipe(e,"animalnet:catched_animalToChild",new IRecipe() {

            private ItemStack match;

            private final ItemStack out = new ItemStack(AnimalNetModHandler.catchedAnimalItem);

            @Override
            public boolean matches(InventoryCrafting inv, World worldIn) {
                if(inv.getStackInSlot(0).getItem().equals(Items.REDSTONE)&&
                        inv.getStackInSlot(1).getItem().equals(Items.REDSTONE)&&
                        inv.getStackInSlot(2).getItem().equals(Items.REDSTONE)&&
                        inv.getStackInSlot(3).getItem().equals(Items.REDSTONE)&&
                        inv.getStackInSlot(4).getItem().equals(AnimalNetModHandler.catchedAnimalItem)&&
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
                CaughtAnimalItem.makeFakeChild(stack);
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

            @Nullable
            @Override
            public ResourceLocation getRegistryName() {
                return new ResourceLocation("animalnet:catched_animalToChild");
            }

            @Override
            public Class<IRecipe> getRegistryType() {
                return null;
            }
        });

        RecipeUtil.replaceRecipe(e,"animalnet:catched_animal",new IRecipe() {

            private ItemStack match;

            private final ItemStack out = new ItemStack(AnimalNetModHandler.catchedAnimalItem);

            @Override
            public boolean matches(InventoryCrafting inv, World worldIn) {
                if(inv.getStackInSlot(0).getItem().equals(Items.DYE)&&
                        inv.getStackInSlot(1).getItem().equals(Items.DYE)&&
                        inv.getStackInSlot(2).getItem().equals(Items.DYE)&&
                        inv.getStackInSlot(3).getItem().equals(Items.DYE)&&
                        inv.getStackInSlot(4).getItem().equals(AnimalNetModHandler.catchedAnimalItem)&&
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
                CaughtAnimalItem.makeFakeAdult(stack);
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

            @Nullable
            @Override
            public ResourceLocation getRegistryName() {
                return new ResourceLocation("animalnet:catched_animal");
            }

            @Override
            public Class<IRecipe> getRegistryType() {
                return null;
            }
        });

    }

    @SubscribeEvent
    public void entityRightClick(PlayerInteractEvent.EntityInteract e) {
        if(!e.getWorld().isRemote) {
            if (e.getHand() == EnumHand.MAIN_HAND) {
                boundingBox = e.getTarget().getEntityBoundingBox();
                size = (boundingBox.maxX-boundingBox.minX)*(boundingBox.maxY-boundingBox.minY);
                if(e.getItemStack().getItem().equals(animalNetItemSmall)) {
                    if(!checkAnimal(animalNetItemSmall,e)) {
                        e.setCanceled(true);
                    }
                }else if(e.getItemStack().getItem().equals(animalNetItemMedium)) {
                    if(!checkAnimal(animalNetItemMedium,e)) {
                        e.setCanceled(true);
                    }
                }else if(e.getItemStack().getItem().equals(animalNetItemBig)) {
                    if(!checkAnimal(animalNetItemBig,e)) {
                        e.setCanceled(true);
                    }
                }else if(e.getItemStack().getItem().equals(mobNetItemSmall)) {
                    if(!checkMob(mobNetItemSmall,e)) {
                        e.setCanceled(true);
                    }
                }else if(e.getItemStack().getItem().equals(mobNetItemBig)) {
                    if(!checkMob(mobNetItemBig,e)) {
                        e.setCanceled(true);
                    }
                }else if(e.getItemStack().getItem().equals(npcNet)) {
                    if(!checkNpc(npcNet,e)) {
                        e.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void craft(PlayerEvent.ItemCraftedEvent e) {
            if (e.crafting.getItem().equals(catchedAnimalItem)) {
                if(e.crafting.getTagCompound().getString("age").equalsIgnoreCase("Adult")) {
                    CaughtAnimalItem.makeAdult(e.crafting);
                }else{
                    CaughtAnimalItem.makeChild(e.crafting);
                }
            }
    }

    @SubscribeEvent
    public void loadWorld(PlayerEvent.PlayerLoggedInEvent e) {
        File f = new File(e.player.getEntityWorld().getSaveHandler().getWorldDirectory().getPath()+"//animalData");
        if(!f.exists()) {
            f.mkdir();
        }
    }

    public boolean checkMob(BasicAnimalNetItem net,PlayerInteractEvent.EntityInteract e) {
        if(isValidEntity(e.getTarget())) {
            if(canBeCaughtByMobNet(e.getTarget())) {
                if (net.fitSize(size)) {
                    ItemStack stack = catchedAnimalItem.createInstance(e.getTarget());
                    addItem(e.getEntityPlayer(), stack);
                    e.getTarget().setDead();
                    if (!e.getEntityPlayer().isCreative()) {
                        currentItem = e.getEntityPlayer().inventory.getCurrentItem();

                        if (currentItem.getCount() > 1) {
                            currentItem.setCount(currentItem.getCount() - 1);
                        } else {
                            e.getEntityPlayer().inventory.removeStackFromSlot(e.getEntityPlayer().inventory.currentItem);
                        }
                    }
                    return true;
                } else {
                    if(!(net instanceof MobNetItemBig)) {
                        sendStatus(e.getEntityPlayer(), "You need a bigger net!");
                    }else{
                        sendStatus(e.getEntityPlayer(), "This mob is too big. It can not be caught!");
                    }
                }
            }else{
                sendCanNotBeCaught(e.getEntityPlayer(),e.getTarget());
            }
        }
        return false;
    }

    public boolean checkAnimal(BasicAnimalNetItem net,PlayerInteractEvent.EntityInteract e) {
        if(isValidEntity(e.getTarget())) {
            if(canBeCaughtByAnimalNet(e.getTarget())) {
                if (net.fitSize(size)) {
                    ItemStack stack = catchedAnimalItem.createInstance(e.getTarget());
                    addItem(e.getEntityPlayer(), stack);
                    e.getTarget().setDead();
                    if (!e.getEntityPlayer().isCreative()) {
                        currentItem = e.getEntityPlayer().inventory.getCurrentItem();
                        if (currentItem.getCount() > 1) {
                            currentItem.setCount(currentItem.getCount() - 1);
                        } else {
                            e.getEntityPlayer().inventory.removeStackFromSlot(e.getEntityPlayer().inventory.currentItem);
                        }
                    }
                    return true;
                } else {
                    if(!(net instanceof AnimalNetItemBig)) {
                        sendStatus(e.getEntityPlayer(), "You need a bigger net!");
                    }else{
                        sendStatus(e.getEntityPlayer(), "This animal is too big. It can not be caught!");
                    }
                }
            }else{
                sendCanNotBeCaught(e.getEntityPlayer(),e.getTarget());
            }
        }
        return false;
    }

    public boolean checkNpc(BasicAnimalNetItem net,PlayerInteractEvent.EntityInteract e) {
        if(isValidEntity(e.getTarget())) {
            if(e.getTarget() instanceof INpc) {
                if (net.fitSize(size)) {
                    ItemStack stack = catchedAnimalItem.createInstance(e.getTarget());
                    addItem(e.getEntityPlayer(), stack);
                    e.getTarget().setDead();
                    if (!e.getEntityPlayer().isCreative()) {
                        currentItem = e.getEntityPlayer().inventory.getCurrentItem();
                        if (currentItem.getCount() > 1) {
                            currentItem.setCount(currentItem.getCount() - 1);
                        } else {
                            e.getEntityPlayer().inventory.removeStackFromSlot(e.getEntityPlayer().inventory.currentItem);
                        }
                    }
                    return true;
                } else {
                    sendStatus(e.getEntityPlayer(),"You need a bigger net!");
                }
            }else{
                sendCanNotBeCaught(e.getEntityPlayer(),e.getTarget());
            }
        }
        return false;
    }

    private void addItem(EntityPlayer p, ItemStack stack) {
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

    private void sendStatus(EntityPlayer p,String msg) {
        p.sendStatusMessage(new TextComponentString(msg),true);
    }

    private boolean canBeCaughtByAnimalNet(Entity entity) {
        return entity instanceof EntityAnimal || entity instanceof EntityWaterMob;
    }

    private boolean canBeCaughtByMobNet(Entity entity) {
        return (entity instanceof EntityMob || entity instanceof IMob)  && !(entity instanceof EntityWither) && !(entity instanceof EntityDragon);
    }

    private void sendCanNotBeCaught(EntityPlayer p,Entity e) {
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
}
