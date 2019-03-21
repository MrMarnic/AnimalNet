package me.marnic.animalnet.items;

public enum NetType {
    MOB("mob"),ANIMAL("animal"),NPC("npc");

    private String name;
    private String translationKey;

    NetType(String name) {
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
