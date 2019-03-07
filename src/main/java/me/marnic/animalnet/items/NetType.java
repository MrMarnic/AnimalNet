package me.marnic.animalnet.items;

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
