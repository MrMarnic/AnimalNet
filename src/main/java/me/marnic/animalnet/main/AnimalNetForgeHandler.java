package me.marnic.animalnet.main;

import me.marnic.animalnet.api.SpawnerUtil;
import me.marnic.animalnet.config.AnimalNetConfig;
import me.marnic.animalnet.items.AnimalNetItem;
import me.marnic.animalnet.items.CaughtEntityItem;
import me.marnic.animalnet.items.NetSize;
import me.marnic.animalnet.items.NetType;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.INPC;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AmbientEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.util.Random;

/**
 * Copyright (c) 20.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AnimalNetForgeHandler {
    private static final Random RANDOM = new Random();
    public static ResourceLocation SPAWNER = new ResourceLocation("minecraft", "blocks/spawner");
    private static AxisAlignedBB boundingBox;
    private static double size;
    private static ItemStack currentItem;

    static boolean isValidEntity(Entity e) {
        return e instanceof LivingEntity && canBeCaughtAtAll(e);
    }

    @SubscribeEvent
    public static void entityRightClick(PlayerInteractEvent.EntityInteract e) {
        if (!e.getWorld().isRemote) {
            if (e.getHand() == Hand.MAIN_HAND) {
                boundingBox = e.getTarget().getBoundingBox();
                size = (boundingBox.maxX - boundingBox.minX) * (boundingBox.maxY - boundingBox.minY);
                if (AnimalNetItem.class.isAssignableFrom(e.getItemStack().getItem().getClass())) {
                    if (!checkEntity((AnimalNetItem) e.getItemStack().getItem(), e)) {
                        e.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void worldLoad(PlayerEvent.PlayerLoggedInEvent e) {
        File f = new File(((ServerWorld) e.getPlayer().getEntityWorld()).getSaveHandler().getWorldDirectory().getPath() + "//animalData");
        if (!f.exists()) {
            f.mkdir();
        }
    }

    @SubscribeEvent
    public static void craft(final PlayerEvent.ItemCraftedEvent e) {
        if (!e.getPlayer().getEntityWorld().isRemote) {
            if (e.getCrafting().getItem().equals(AnimalNetItems.caughtEntityItem)) {
                if (e.getCrafting().getTag().getString("age").equalsIgnoreCase("Adult")) {
                    CaughtEntityItem.makeAdult(e.getCrafting(), e.getPlayer().world);
                } else {
                    CaughtEntityItem.makeChild(e.getCrafting(), e.getPlayer().world);
                }
            }
        }
    }

    @SubscribeEvent
    public static void lootTableLoad(LootTableLoadEvent e) {
        if (e.getName().equals(SPAWNER)) {
            e.getTable().addPool(LootPool.builder().addEntry(ItemLootEntry.builder(AnimalNetItems.spawnerFragmental)).build());
        }
    }

    @SubscribeEvent
    public static void blockPlace(BlockEvent.EntityPlaceEvent e) {
        if (!e.getWorld().isRemote()) {
            if (e.getPlacedBlock() == Blocks.SPAWNER.getDefaultState() && e.getEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) e.getEntity();
                if (SpawnerUtil.isCustomSpawner(player.getHeldItemMainhand())) {
                    SpawnerUtil.makeSpawnerBlock(player.getHeldItemMainhand(), e.getWorld(), e.getPos());
                    if (player.isCreative()) {
                        ItemStack stack = player.getHeldItemMainhand().copy();
                        player.setItemStackToSlot(EquipmentSlotType.MAINHAND, stack);
                    }
                }
            }
        }
    }

    private static boolean checkEntity(AnimalNetItem net, PlayerInteractEvent.EntityInteract e) {
        if (isValidEntity(e.getTarget())) {
            if (net.fitSize(size)) {
                if ((canBeCaughtByAnimalNet(e.getTarget()) && net.getType() == NetType.ANIMAL)) {
                    addNetToInv(e);
                } else {
                    if ((canBeCaughtByMobNet(e.getTarget()) && net.getType() == NetType.MOB)) {
                        addNetToInv(e);
                    } else if (net.getType() == NetType.NPC && e.getTarget() instanceof INPC) {
                        addNetToInv(e);
                    } else if (e.getTarget() instanceof LivingEntity && net.getType() == NetType.ANIMAL) {
                        addNetToInv(e);
                    } else {
                        sendCanNotBeCaught(e.getPlayer(), e.getTarget());
                    }
                }
            } else {
                if (net.getSize() != NetSize.BIG) {
                    if (canBeCaughtByAnimalNet(e.getTarget()) && net.getType() == NetType.ANIMAL) {
                        sendStatus(e.getPlayer(), new TranslationTextComponent("message.animalnet.net_too_small"));
                    } else if (canBeCaughtByMobNet(e.getTarget()) && net.getType() == NetType.MOB) {
                        sendStatus(e.getPlayer(), new TranslationTextComponent("message.animalnet.net_too_small"));
                    } else if (e.getTarget() instanceof LivingEntity && net.getType() == NetType.ANIMAL) {
                        sendStatus(e.getPlayer(), new TranslationTextComponent("message.animalnet.net_too_small"));
                    } else {
                        sendCanNotBeCaught(e.getPlayer(), e.getTarget());
                    }
                } else {
                    sendStatus(e.getPlayer(), new TranslationTextComponent("message.animalnet.entity_too_big", new TranslationTextComponent(net.getType().getFormalTranslationKey())));
                }
            }
        } else {
            sendStatus(e.getPlayer(), new TranslationTextComponent("message.animalnet.can_not_be_caught"));
        }
        return false;
    }

    private static boolean addNetToInv(PlayerInteractEvent.EntityInteract e) {
        ItemStack stack = AnimalNetItems.caughtEntityItem.createInstance(e.getTarget());
        addItem(e.getPlayer(), stack);
        e.getTarget().remove();
        if (!e.getPlayer().isCreative()) {
            currentItem = e.getPlayer().inventory.getCurrentItem();
            if (currentItem.getMaxDamage() > 0) {
                currentItem.damageItem(1, e.getPlayer(), (p) -> p.sendBreakAnimation(p.getActiveHand()));
            } else {
                e.getPlayer().getHeldItem(e.getPlayer().getActiveHand()).shrink(1);
            }
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
        if (canBeCaughtByAnimalNet(e)) {
            sendStatus(p, new TranslationTextComponent("message.animalnet.animal_needed"));
        } else if (canBeCaughtByMobNet(e)) {
            sendStatus(p, new TranslationTextComponent("message.animalnet.mob_needed"));
        } else if (e instanceof INPC) {
            if (e instanceof VillagerEntity) {
                sendStatus(p, new TranslationTextComponent("message.animalnet.villager_needed"));
            } else {
                sendStatus(p, new TranslationTextComponent("message.animalnet.npc_needed"));
            }
        }
        if (e instanceof LivingEntity) {
            sendStatus(p, new TranslationTextComponent("message.animalnet.animal_needed"));
        } else {
            sendStatus(p, new TranslationTextComponent("message.animalnet.can_not_be_caught"));
        }
    }

    private static void sendStatus(PlayerEntity p, String msg) {
        p.sendStatusMessage(new StringTextComponent(msg), true);
    }

    private static void sendStatus(PlayerEntity p, ITextComponent msg) {
        p.sendStatusMessage(msg, true);
    }

    private static void addItem(PlayerEntity p, ItemStack stack) {
        boolean full = true;

        for (ItemStack s : p.inventory.mainInventory) {
            if (s.isEmpty()) {
                full = false;
                break;
            }
        }
        if (full) {
            p.dropItem(stack, false);
        } else {
            p.addItemStackToInventory(stack);
        }
    }
}
