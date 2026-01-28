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
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
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
    public static final Random RANDOM = new Random();
    public static ResourceLocation SPAWNER = new ResourceLocation("minecraft", "blocks/spawner");

    @SubscribeEvent
    public static void entityInteractEvent(PlayerInteractEvent.EntityInteract e) {
        if(!(e.getItemStack().getItem() instanceof AnimalNetItem)) return;
        if(!(e.getTarget() instanceof LivingEntity)) return;
        if(e.getPlayer() == null) return;
        e.setCanceled(true);
        e.getItemStack().interactLivingEntity(e.getPlayer(), (LivingEntity) e.getTarget(),e.getHand());
    }

    @SubscribeEvent
    public static void lootTableLoad(LootTableLoadEvent e) {
        if (e.getName().equals(SPAWNER)) {
            LootPool pool = LootPool.lootPool()
                    .add(ItemLootEntry.lootTableItem(AnimalNetItems.SPAWNER_FRAGMENTAL_ITEM.get()))
                    .name("animal_net_spawner_fragments")
                    .build();
            e.getTable().addPool(pool);
        }
    }

    @SubscribeEvent
    public static void blockPlace(BlockEvent.EntityPlaceEvent e) {
        if (!e.getWorld().isClientSide()) {
            if (e.getPlacedBlock() == Blocks.SPAWNER.defaultBlockState() && e.getEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) e.getEntity();
                if (SpawnerUtil.isCustomSpawner(player.getMainHandItem())) {
                    SpawnerUtil.makeSpawnerBlock(player.getMainHandItem(), e.getWorld(), e.getPos());
                    if (player.isCreative()) {
                        ItemStack stack = player.getMainHandItem().copy();
                        player.setItemInHand(Hand.MAIN_HAND, stack);
                    }
                }
            }
        }
    }
}
