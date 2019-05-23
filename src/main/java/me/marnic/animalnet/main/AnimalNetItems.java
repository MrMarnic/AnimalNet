package me.marnic.animalnet.main;

import me.marnic.animalnet.config.AnimalNetConfig;
import me.marnic.animalnet.items.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * Copyright (c) 19.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class AnimalNetItems {

    public static AnimalNetItem animalNetSmall,animalNetMedium,animalNetBig,mobNetSmall,mobNetBig,npcNet;
    public static CaughtEntityItem caughtEntityItem;
    public static ItemGroup ANIMAL_NET_ITEMS;
    public static MobCore mobCore;
    public static ItemSpawnerFragmental spawnerFragmental;


    public static void init() {

        ANIMAL_NET_ITEMS = new ItemGroup("AnimalNet Items") {

            @Override
            public ItemStack createIcon() {
                return new ItemStack(animalNetSmall);
            }
        };

        animalNetSmall = new AnimalNetItem("animal_net_small",new Item.Properties().group(ANIMAL_NET_ITEMS), NetSize.SMALL, NetType.ANIMAL);

        animalNetMedium = new AnimalNetItem("animal_net_medium",new Item.Properties().group(ANIMAL_NET_ITEMS), NetSize.MEDIUM, NetType.ANIMAL);
        animalNetBig = new AnimalNetItem("animal_net_big",new Item.Properties().group(ANIMAL_NET_ITEMS),NetSize.BIG, NetType.ANIMAL);
        mobNetSmall = new AnimalNetItem("mob_net_small",new Item.Properties().group(ANIMAL_NET_ITEMS),NetSize.SMALL, NetType.MOB);
        mobNetBig = new AnimalNetItem("mob_net_big",new Item.Properties().group(ANIMAL_NET_ITEMS), NetSize.BIG, NetType.MOB);
        npcNet = new AnimalNetItem("npc_net",new Item.Properties().group(ANIMAL_NET_ITEMS), NetSize.MEDIUM, NetType.NPC);

        caughtEntityItem = new CaughtEntityItem(new Item.Properties());

        mobCore = new MobCore(new Item.Properties().group(ANIMAL_NET_ITEMS));

        spawnerFragmental = new ItemSpawnerFragmental(new Item.Properties().group(ANIMAL_NET_ITEMS));
    }

    public static void initConfigValues() {
        animalNetSmall.initConfigValues(AnimalNetConfig.animal_net_small.maxSize.get(),AnimalNetConfig.animal_net_small.maxUses.get());
        animalNetMedium.initConfigValues(AnimalNetConfig.animal_net_medium.maxSize.get(),AnimalNetConfig.animal_net_medium.maxUses.get());
        animalNetBig.initConfigValues(AnimalNetConfig.animal_net_big.maxSize.get(),AnimalNetConfig.animal_net_big.maxUses.get());
        mobNetSmall.initConfigValues(AnimalNetConfig.mob_net_small.maxSize.get(),AnimalNetConfig.mob_net_small.maxUses.get());
        mobNetBig.initConfigValues(AnimalNetConfig.mob_net_big.maxSize.get(),AnimalNetConfig.mob_net_big.maxUses.get());
        npcNet.initConfigValues(AnimalNetConfig.npc_net.maxSize.get(),AnimalNetConfig.npc_net.maxUses.get());
    }
}


