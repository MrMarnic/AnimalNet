package me.marnic.animalnet.main;

import me.marnic.animalnet.config.AnimalNetConfig;
import me.marnic.animalnet.items.*;
import net.minecraft.entity.INPC;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AmbientEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Predicate;

/**
 * Copyright (c) 19.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class AnimalNetItems {

    public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS,AnimalNet.MODID);
    
    public static ItemGroup ANIMAL_NET_ITEMS = new ItemGroup("AnimalNet Items") {

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ANIMAL_NET_SMALL.get());
        }
    };

    public static final RegistryObject<AnimalNetItem> ANIMAL_NET_SMALL = registerAnimalNet("animal_net_small", NetSize.SMALL, NetType.ANIMAL,AnimalNetConfig.animal_net_small.maxSize, AnimalNetConfig.animal_net_small.maxUses,AnimalNetItem::animalNetPredicate);
    public static final RegistryObject<AnimalNetItem> ANIMAL_NET_MEDIUM = registerAnimalNet("animal_net_medium", NetSize.MEDIUM, NetType.ANIMAL,AnimalNetConfig.animal_net_medium.maxSize, AnimalNetConfig.animal_net_medium.maxUses,AnimalNetItem::animalNetPredicate);
    public static final RegistryObject<AnimalNetItem> ANIMAL_NET_BIG = registerAnimalNet("animal_net_big", NetSize.BIG, NetType.ANIMAL,AnimalNetConfig.animal_net_big.maxSize, AnimalNetConfig.animal_net_big.maxUses,AnimalNetItem::animalNetPredicate);
    public static final RegistryObject<AnimalNetItem> MONSTER_NET_SMALL = registerAnimalNet("monster_net_small", NetSize.SMALL, NetType.MONSTER,AnimalNetConfig.mob_net_small.maxSize, AnimalNetConfig.mob_net_big.maxUses,AnimalNetItem::monsterNetPredicate);
    public static final RegistryObject<AnimalNetItem> MONSTER_NET_BIG = registerAnimalNet("monster_net_big", NetSize.BIG, NetType.MONSTER,AnimalNetConfig.mob_net_big.maxSize, AnimalNetConfig.mob_net_big.maxUses,AnimalNetItem::monsterNetPredicate);
    public static final RegistryObject<AnimalNetItem> NPC_NET = registerAnimalNet("npc_net", NetSize.MEDIUM, NetType.NPC,AnimalNetConfig.npc_net.maxSize, AnimalNetConfig.npc_net.maxUses, (entity) -> entity instanceof INPC);
    
    public static final RegistryObject<CaughtEntityItem> CAUGHT_ENTITY_ITEM = ITEM_REGISTRY.register("caught_entity", () -> new CaughtEntityItem(new Item.Properties().durability(1)));
    public static final RegistryObject<MobCore> MOB_CORE_ITEM = ITEM_REGISTRY.register("monster_core", () -> new MobCore(new Item.Properties().tab(ANIMAL_NET_ITEMS)));
    public static final RegistryObject<ItemSpawnerFragmental> SPAWNER_FRAGMENTAL_ITEM = ITEM_REGISTRY.register("spawner_fragmental", () -> new ItemSpawnerFragmental(new Item.Properties().tab(ANIMAL_NET_ITEMS)));

    public static void register(IEventBus bus) {
        ITEM_REGISTRY.register(bus);
    }
    
    public static RegistryObject<AnimalNetItem> registerAnimalNet(String name, NetSize size, NetType type, ForgeConfigSpec.DoubleValue maxSize, ForgeConfigSpec.IntValue maxUses, Predicate<LivingEntity> accepts) {
        return ITEM_REGISTRY.register(name, () -> new AnimalNetItem(new Item.Properties().tab(ANIMAL_NET_ITEMS).durability(maxUses.get()),size,type, maxSize.get(),accepts));
    }
}


