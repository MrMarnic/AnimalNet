package me.marnic.animalnet.common.config;

import me.marnic.animalnet.common.main.AnimalNet;
import net.minecraftforge.common.config.Config;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Copyright (c) 20.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
@Config(modid = AnimalNet.MODID)
public class AnimalNetConfig {

    public static GeneralOptions general_options = new GeneralOptions();

    public static BasicAnimalNetConfig animal_net_small = new BasicAnimalNetConfig(1,0.5);
    public static BasicAnimalNetConfig animal_net_medium = new BasicAnimalNetConfig(1,1.2);
    public static BasicAnimalNetConfig animal_net_big = new BasicAnimalNetConfig(2,10);

    public static BasicAnimalNetConfig mob_net_small = new BasicAnimalNetConfig(1,1.3);
    public static BasicAnimalNetConfig mob_net_big = new BasicAnimalNetConfig(2,10);

    public static BasicAnimalNetConfig npc_net = new BasicAnimalNetConfig(1,10);



    public static class BasicAnimalNetConfig {

        public BasicAnimalNetConfig(int maxUses,double size) {
            this.maxUses = maxUses;
            this.maxSize = size;
        }

        @Config.Name("Max uses for the net")
        public int maxUses;

        @Config.Name("Max size for entity")
        public double maxSize;
    }

    public static class GeneralOptions {
        @Config.Comment({"List of entities to be excluded from catching",
                         "Example:",
                         "S:excluded_entities <",
                         "minecraft:pig",
                         "minecraft:cow",
                         ">"})
        public String[] excluded_entities = new String[]{"minecraft:wither","minecraft:ender_dragon"};

        private ArrayList<String> excluded_entities_array_list;

        public void initExcludedEntitiesArraysList() {
            excluded_entities_array_list = new ArrayList<>(Arrays.asList(excluded_entities));
        }

        public ArrayList<String> getExcluded_entities_array_list() {
            return excluded_entities_array_list;
        }
    }
}
