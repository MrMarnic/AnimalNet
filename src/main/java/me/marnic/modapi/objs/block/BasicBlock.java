package me.marnic.modapi.objs.block;

import me.marnic.modapi.handler.ModHandler;
import me.marnic.modapi.interfaces.IModelRegistry;
import me.marnic.modapi.interfaces.ModelHelper;
import me.marnic.modapi.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

/**
 * Copyright (c) 03.12.2018
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BasicBlock extends Block implements IModelRegistry {
    public BasicBlock(Material materialIn,String name) {
        super(materialIn);
        setUnlocalizedName(name);
        setRegistryName(name);
        ModHandler.modelsToRegister.add(this);
    }

    @Override
    public void registerModel() {
        ModelHelper.registerDefaultItemModel(Item.getItemFromBlock(this));
    }
}
