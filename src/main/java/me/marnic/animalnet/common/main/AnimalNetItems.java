package me.marnic.animalnet.common.main;

/**
 * Copyright (c) 18.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

import me.marnic.animalnet.api.BasicItem;
import me.marnic.animalnet.common.config.AnimalNetConfig;
import me.marnic.animalnet.common.item.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class AnimalNetItems {
    public static final ArrayList<BasicItem> ITEMS_TO_REGISTER = new ArrayList<>();

    public static ItemAnimalNet animalNetSmall, animalNetMedium, animalNetBig, mobNetSmall, mobNetBig, npcNet;
    public static ItemCaughtEntity caughtEntityItem;
    public static MobCore mobCore;
    public static ItemSpawnerFragmental spawnerFragmental;

    public static CreativeTabs ANIMAL_NET_ITEMS;

    public static void init() {

        ANIMAL_NET_ITEMS = new CreativeTabs("AnimalNet Items") {

            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(animalNetSmall);
            }
        };

        animalNetSmall = new ItemAnimalNet("animal_net_small", NetSize.SMALL, NetType.ANIMAL, AnimalNetConfig.animal_net_small.maxSize, AnimalNetConfig.animal_net_small.maxUses);

        animalNetMedium = new ItemAnimalNet("animal_net_medium", NetSize.MEDIUM, NetType.ANIMAL, AnimalNetConfig.animal_net_medium.maxSize, AnimalNetConfig.animal_net_medium.maxUses);
        animalNetBig = new ItemAnimalNet("animal_net_big", NetSize.BIG, NetType.ANIMAL, AnimalNetConfig.animal_net_big.maxSize, AnimalNetConfig.animal_net_big.maxUses);
        mobNetSmall = new ItemAnimalNet("mob_net_small", NetSize.SMALL, NetType.MOB, AnimalNetConfig.mob_net_small.maxSize, AnimalNetConfig.mob_net_small.maxUses);
        mobNetBig = new ItemAnimalNet("mob_net_big", NetSize.BIG, NetType.MOB, AnimalNetConfig.mob_net_big.maxSize, AnimalNetConfig.mob_net_big.maxUses);
        npcNet = new ItemAnimalNet("npc_net", NetSize.MEDIUM, NetType.NPC, AnimalNetConfig.npc_net.maxSize, AnimalNetConfig.npc_net.maxUses);

        spawnerFragmental = new ItemSpawnerFragmental();

        caughtEntityItem = new ItemCaughtEntity();

        mobCore = new MobCore();

    }
}
