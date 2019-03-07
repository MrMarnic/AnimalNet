package me.marnic.animalnet.main;

import me.marnic.animalnet.items.*;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

/**
 * Copyright (c) 05.03.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class AnimalNetItems {

    public static AnimalNetItem animalNetSmall,animalNetMedium,animalNetBig,mobNetSmall,mobNetBig,npcNet;
    public static CaughtEntityItem caughtEntityItem;
    public static ItemGroup ANIMAL_NET_ITEMS;
    public static MobCore mobCore;

    public static void init() {
        ANIMAL_NET_ITEMS  = FabricItemGroupBuilder.build(new Identifier(ModIdentification.MODID,"general"),()->new ItemStack(animalNetSmall));

        animalNetSmall = new AnimalNetItem("animal_net_small",new Item.Settings().itemGroup(ANIMAL_NET_ITEMS), NetSize.SMALL, NetType.ANIMAL,1);

        animalNetMedium = new AnimalNetItem("animal_net_medium",new Item.Settings().itemGroup(ANIMAL_NET_ITEMS), NetSize.MEDIUM, NetType.ANIMAL,2.2);
        animalNetBig = new AnimalNetItem("animal_net_big",new Item.Settings().itemGroup(ANIMAL_NET_ITEMS),NetSize.BIG, NetType.ANIMAL,10);
        mobNetSmall = new AnimalNetItem("mob_net_small",new Item.Settings().itemGroup(ANIMAL_NET_ITEMS),NetSize.SMALL, NetType.MOB,1.4);
        mobNetBig = new AnimalNetItem("mob_net_big",new Item.Settings().itemGroup(ANIMAL_NET_ITEMS), NetSize.BIG, NetType.MOB,10);
        npcNet = new AnimalNetItem("npc_net",new Item.Settings().itemGroup(ANIMAL_NET_ITEMS), NetSize.MEDIUM, NetType.NPC,10);

        caughtEntityItem = new CaughtEntityItem(new Item.Settings().itemGroup(ANIMAL_NET_ITEMS));

        mobCore = new MobCore(new Item.Settings().itemGroup(ANIMAL_NET_ITEMS));
    }
}
