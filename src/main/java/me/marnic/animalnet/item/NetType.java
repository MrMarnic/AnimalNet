package me.marnic.animalnet.item;

/**
 * Copyright (c) 18.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public enum NetType {
    MOB("mob"),ANIMAL("animal"),NPC("npc");

    private String name;

    NetType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
