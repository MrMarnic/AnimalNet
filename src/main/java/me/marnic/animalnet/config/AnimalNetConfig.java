package me.marnic.animalnet.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright (c) 20.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class AnimalNetConfig {

    private static ForgeConfigSpec.Builder BUILDER;

    public static GeneralOptions general_options;

    public static BasicAnimalNetConfig animal_net_small;
    public static BasicAnimalNetConfig animal_net_medium;
    public static BasicAnimalNetConfig animal_net_big;

    public static BasicAnimalNetConfig mob_net_small;
    public static BasicAnimalNetConfig mob_net_big;

    public static BasicAnimalNetConfig npc_net;

    public static ForgeConfigSpec SPEC;

    public static void init() {
        BUILDER =  new ForgeConfigSpec.Builder();

        general_options =  new GeneralOptions(BUILDER);
        animal_net_small  = new BasicAnimalNetConfig(BUILDER,1,0.5,"animal_net_small");
        animal_net_medium  = new BasicAnimalNetConfig(BUILDER,1,1.2,"animal_net_medium");
        animal_net_big = new BasicAnimalNetConfig(BUILDER,2,10,"animal_net_big");
        mob_net_small = new BasicAnimalNetConfig(BUILDER,1,1.3,"mob_net_small");
        mob_net_big  = new BasicAnimalNetConfig(BUILDER,2,10,"mob_net_big");
        npc_net  = new BasicAnimalNetConfig(BUILDER,1,10,"npc_net");

        SPEC = BUILDER.build();
    }


    public static class BasicAnimalNetConfig {

        public BasicAnimalNetConfig(ForgeConfigSpec.Builder builder, int maxUses, double size, String name) {
            builder.push(name);
            this.maxUses = builder.defineInRange("maxUses",maxUses,0,1000);
            this.maxSize = builder.defineInRange("maxSize",size,0,1000);
            builder.pop();
        }

        public ForgeConfigSpec.IntValue maxUses;

        public ForgeConfigSpec.DoubleValue maxSize;
    }

    public static class GeneralOptions {

        public GeneralOptions(ForgeConfigSpec.Builder builder) {
            builder.push("General");

            excluded_entities = builder.comment("List of entities to be excluded from catching",
                    "Example:",
                    "excluded_entities = [\"minecraft:wither\", \"minecraft:ender_dragon\"]").define("excluded_entities",Arrays.asList("minecraft:wither","minecraft:ender_dragon"));
        }

        public ForgeConfigSpec.ConfigValue<List<String>> excluded_entities;

        private ArrayList<String> excluded_entities_array_list;

        public void initExcludedEntitiesArraysList() {
            excluded_entities_array_list = new ArrayList(excluded_entities.get());
        }

        public ArrayList<String> getExcluded_entities_array_list() {
            return excluded_entities_array_list;
        }
    }
}
