package me.marnic.animalnet.item;

import me.marnic.modapi.objs.item.BasicItem;

/**
 * Created by Marcel on Sep,2018
 */
public class BasicAnimalNetItem extends BasicItem {
    private double acceptedSize;

    public BasicAnimalNetItem(String name,double accpetedSize) {
        super(name);
        this.acceptedSize = accpetedSize;
    }

    public boolean fitSize(double size) {
        return size<=acceptedSize;
    }
}
