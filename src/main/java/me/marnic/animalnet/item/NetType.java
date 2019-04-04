package me.marnic.animalnet.item;

/**
 * Copyright (c) 18.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public enum NetType {
    MOB("mob"),ANIMAL("animal"),NPC("npc");

    private String name;
    private String translationKey;

    NetType(String name){
        this.name = name;
        this.translationKey = "message.animalnet.formal_"+name;
    }

    public String getName() {
        return name;
    }

    public String getFormalTranslationKey() {
        return translationKey;
    }
}