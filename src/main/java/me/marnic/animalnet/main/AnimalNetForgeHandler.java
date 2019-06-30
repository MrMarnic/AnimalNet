package me.marnic.animalnet.main;

import me.marnic.animalnet.api.SpawnerUtil;
import me.marnic.animalnet.config.AnimalNetConfig;
import me.marnic.animalnet.items.AnimalNetItem;
import me.marnic.animalnet.items.CaughtEntityItem;
import me.marnic.animalnet.items.NetSize;
import me.marnic.animalnet.items.NetType;
import me.marnic.animalnet.recipes.RecipeAnimalToChild;
import me.marnic.animalnet.recipes.RecipeChildToAnimal;
import me.marnic.animalnet.recipes.RecipeSpawner;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

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
        return e instanceof LivingEntity && canBeCaughtAtAll(e);
    }

    @SubscribeEvent
    public static void entityRightClick(PlayerInteractEvent.EntityInteract e) {
        if(!e.getWorld().isRemote) {
            if (e.getHand() == Hand.MAIN_HAND) {
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
        File f = new File(((ServerWorld)e.getPlayer().getEntityWorld()).getSaveHandler().getWorldDirectory().getPath()+"//animalData");
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
                if (RANDOM.nextInt(3) == 2) {
                    e.getDrops().add(new ItemStack(AnimalNetItems.spawnerFragmental, 2));
                } else {
                    e.getDrops().add(new ItemStack(AnimalNetItems.spawnerFragmental));
                }
            }
        }
    }

    @SubscribeEvent
    public static void blockPlace(BlockEvent.EntityPlaceEvent e) {
        if (!e.getWorld().isRemote()) {
            if (e.getPlacedBlock() == Blocks.SPAWNER.getDefaultState() && e.getEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) e.getEntity();
                if (SpawnerUtil.isCustomSpawner(player.getHeldItemMainhand())) {
                    SpawnerUtil.makeSpawnerBlock(player.getHeldItemMainhand(), e.getWorld(), e.getPos());
                    if(player.isCreative()) {
                        ItemStack stack = player.getHeldItemMainhand().copy();
                        player.setItemStackToSlot(EquipmentSlotType.MAINHAND,stack);
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
                    }else if(net.getType()==NetType.NPC && e.getTarget() instanceof INPC){
                        addNetToInv(e);
                    }else{
                        sendCanNotBeCaught(e.getEntityPlayer(),e.getTarget());
                    }
                }
            } else {
                if(net.getSize()!=NetSize.BIG) {
                    if(canBeCaughtByAnimalNet(e.getTarget())&&net.getType()==NetType.ANIMAL) {
                        sendStatus(e.getEntityPlayer(), new TranslationTextComponent("message.animalnet.net_too_small"));
                    }else if(canBeCaughtByMobNet(e.getTarget())&&net.getType()==NetType.MOB) {
                        sendStatus(e.getEntityPlayer(), new TranslationTextComponent("message.animalnet.net_too_small"));
                    }else{
                        sendCanNotBeCaught(e.getEntityPlayer(),e.getTarget());
                    }
                }else{
                    sendStatus(e.getEntityPlayer(), new TranslationTextComponent("message.animalnet.entity_too_big",new TranslationTextComponent(net.getType().getFormalTranslationKey())));
                }
            }
        }else{
            sendStatus(e.getEntityPlayer(),new TranslationTextComponent("message.animalnet.can_not_be_caught"));
        }
        return false;
    }

    private static boolean addNetToInv(PlayerInteractEvent.EntityInteract e) {
        ItemStack stack = AnimalNetItems.caughtEntityItem.createInstance(e.getTarget());
        addItem(e.getEntityPlayer(), stack);
        e.getTarget().remove();
        if (!e.getEntityPlayer().isCreative()) {
            currentItem = e.getEntityPlayer().inventory.getCurrentItem();
            currentItem.attemptDamageItem(1,e.getEntityPlayer().getRNG(), (ServerPlayerEntity) e.getEntityPlayer());
        }
        return true;
    }

    private static boolean canBeCaughtByAnimalNet(Entity entity) {
        return entity instanceof AnimalEntity || entity instanceof WaterMobEntity || entity instanceof AmbientEntity;
    }

    private static boolean canBeCaughtByMobNet(Entity entity) {
        return (entity instanceof MonsterEntity || entity instanceof IMob);
    }

    private static boolean canBeCaughtAtAll(Entity entity) {
        return !AnimalNetConfig.general_options.getExcluded_entities_array_list().contains(EntityType.byKey(entity.getType().getRegistryName().toString()).toString());
    }

    private static void sendCanNotBeCaught(PlayerEntity p, Entity e) {
        if(canBeCaughtByAnimalNet(e)) {
            sendStatus(p,new TranslationTextComponent("message.animalnet.animal_needed"));
        }else if(canBeCaughtByMobNet(e)) {
            sendStatus(p,new TranslationTextComponent("message.animalnet.mob_needed"));
        }else if(e instanceof INPC) {
            if(e instanceof VillagerEntity) {
                sendStatus(p,new TranslationTextComponent("message.animalnet.villager_needed"));
            }else{
                sendStatus(p,new TranslationTextComponent("message.animalnet.npc_needed"));
            }
        }else{
            sendStatus(p,new TranslationTextComponent("message.animalnet.can_not_be_caught"));
        }
    }

    private static void sendStatus(PlayerEntity p,String msg) {
        p.sendStatusMessage(new StringTextComponent(msg),true);
    }

    private static void sendStatus(PlayerEntity p, ITextComponent msg) {
        p.sendStatusMessage(msg,true);
    }

    private static void addItem(PlayerEntity p, ItemStack stack) {
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
