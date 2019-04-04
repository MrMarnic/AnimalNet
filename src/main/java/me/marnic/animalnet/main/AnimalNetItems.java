package me.marnic.animalnet.main;

/**
 * Copyright (c) 18.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

import cpw.mods.fml.common.registry.GameRegistry;
import me.marnic.animalnet.api.BasicItem;
import me.marnic.animalnet.item.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class AnimalNetItems {
    public static final ArrayList<BasicItem> ITEMS_TO_REGISTER = new ArrayList<BasicItem>();

    public static AnimalNetItem animalNetSmall,animalNetMedium,animalNetBig,mobNetSmall,mobNetBig,npcNet;
    public static CaughtEntityItem caughtEntityItem;
    public static MobCore mobCore;

    public static CreativeTabs ANIMAL_NET_ITEMS;

    public static void init() {

        ANIMAL_NET_ITEMS = new CreativeTabs("AnimalNet Items") {

            @Override
            public Item getTabIconItem() {
                return animalNetSmall;
            }
        };

        animalNetSmall = new AnimalNetItem("animal_net_small", NetSize.SMALL, NetType.ANIMAL,1,0);

        animalNetMedium = new AnimalNetItem("animal_net_medium", NetSize.MEDIUM, NetType.ANIMAL,2.2,0);
        animalNetBig = new AnimalNetItem("animal_net_big",NetSize.BIG, NetType.ANIMAL,10,1);
        mobNetSmall = new AnimalNetItem("mob_net_small",NetSize.SMALL, NetType.MOB,1.4,0);
        mobNetBig = new AnimalNetItem("mob_net_big", NetSize.BIG, NetType.MOB,10,1);
        npcNet = new AnimalNetItem("npc_net", NetSize.MEDIUM, NetType.NPC,10,0);

        caughtEntityItem = new CaughtEntityItem();

        mobCore = new MobCore();

        for(BasicItem item:ITEMS_TO_REGISTER) {
            GameRegistry.registerItem(item,item.getUnlocalizedName());
        }
    }
}