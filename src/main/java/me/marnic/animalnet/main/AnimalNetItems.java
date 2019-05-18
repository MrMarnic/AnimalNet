package me.marnic.animalnet.main;

/**
 * Copyright (c) 18.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

import me.marnic.animalnet.api.BasicItem;
import me.marnic.animalnet.item.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class AnimalNetItems {
    public static final ArrayList<BasicItem> ITEMS_TO_REGISTER = new ArrayList<>();

    public static AnimalNetItem animalNetSmall,animalNetMedium,animalNetBig,mobNetSmall,mobNetBig,npcNet;
    public static CaughtEntityItem caughtEntityItem;
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

        animalNetSmall = new AnimalNetItem("animal_net_small", NetSize.SMALL, NetType.ANIMAL,0.5,0);

        animalNetMedium = new AnimalNetItem("animal_net_medium", NetSize.MEDIUM, NetType.ANIMAL,1.2,0);
        animalNetBig = new AnimalNetItem("animal_net_big",NetSize.BIG, NetType.ANIMAL,10,2);
        mobNetSmall = new AnimalNetItem("mob_net_small",NetSize.SMALL, NetType.MOB,1.3,0);
        mobNetBig = new AnimalNetItem("mob_net_big", NetSize.BIG, NetType.MOB,10,2);
        npcNet = new AnimalNetItem("npc_net", NetSize.MEDIUM, NetType.NPC,10,0);

        //spawnerFragmental = new ItemSpawnerFragmental();

        caughtEntityItem = new CaughtEntityItem();

        mobCore = new MobCore();
    }
}
