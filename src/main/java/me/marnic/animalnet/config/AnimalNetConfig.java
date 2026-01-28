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

    public static GeneralOptions GENERAL_OPTIONS;
    public static BasicAnimalNetConfig ANIMAL_NET_SMALL;
    public static BasicAnimalNetConfig ANIMAL_NET_MEDIUM;
    public static BasicAnimalNetConfig ANIMAL_NET_BIG;
    public static BasicAnimalNetConfig MONSTER_NET_SMALL;
    public static BasicAnimalNetConfig MONSTER_NET_BIG;
    public static BasicAnimalNetConfig NPC_NET;
    public static ForgeConfigSpec SPEC;
    private static ForgeConfigSpec.Builder BUILDER;

    public static void init() {
        BUILDER = new ForgeConfigSpec.Builder();

        GENERAL_OPTIONS = new GeneralOptions(BUILDER);
        ANIMAL_NET_SMALL = new BasicAnimalNetConfig(BUILDER, 1, 0.5, "ANIMAL_NET_SMALL");
        ANIMAL_NET_MEDIUM = new BasicAnimalNetConfig(BUILDER, 1, 1.2, "ANIMAL_NET_MEDIUM");
        ANIMAL_NET_BIG = new BasicAnimalNetConfig(BUILDER, 2, 10, "ANIMAL_NET_BIG");
        MONSTER_NET_SMALL = new BasicAnimalNetConfig(BUILDER, 1, 1.3, "MONSTER_NET_SMALL");
        MONSTER_NET_BIG = new BasicAnimalNetConfig(BUILDER, 2, 10, "MONSTER_NET_BIG");
        NPC_NET = new BasicAnimalNetConfig(BUILDER, 1, 10, "NPC_NET");

        SPEC = BUILDER.build();
    }


    public static class BasicAnimalNetConfig {

        public ForgeConfigSpec.IntValue maxUses;
        public ForgeConfigSpec.DoubleValue maxSize;

        public BasicAnimalNetConfig(ForgeConfigSpec.Builder builder, int maxUses, double size, String name) {
            builder.push(name);
            this.maxUses = builder.defineInRange("maxUses", maxUses, 0, 1000);
            this.maxSize = builder.defineInRange("maxSize", size, 0, 1000);
            builder.pop();
        }
    }

    public static class GeneralOptions {

        public ForgeConfigSpec.ConfigValue<List<String>> excluded_entities;
        private ArrayList<String> excluded_entities_array_list;

        public GeneralOptions(ForgeConfigSpec.Builder builder) {
            builder.push("General");

            excluded_entities = builder.comment("List of entities to be excluded from catching",
                    "Example:",
                    "excluded_entities = [\"minecraft:wither\", \"minecraft:ender_dragon\"]").define("excluded_entities", Arrays.asList("minecraft:wither", "minecraft:ender_dragon"));
        }

        public void initExcludedEntitiesArraysList() {
            excluded_entities_array_list = new ArrayList(excluded_entities.get());
        }

        public ArrayList<String> getExcluded_entities_array_list() {
            return excluded_entities_array_list;
        }
    }
}
