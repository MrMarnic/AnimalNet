package me.marnic.animalnet.mixin;

import me.marnic.animalnet.mechanics.ServerHandler;
import net.minecraft.container.CraftingResultSlot;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeUnlocker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Copyright (c) 18.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

@Mixin(CraftingResultSlot.class)
public class AnimalNetMixinCrafting extends Slot {

    @Shadow
    private int amount;

    @Shadow
    private PlayerEntity player;

    public AnimalNetMixinCrafting(Inventory inventory_1, int int_1, int int_2, int int_3) {
        super(inventory_1, int_1, int_2, int_3);
    }

    @Overwrite
    public void onCrafted(ItemStack itemStack_1) {
        if (this.amount > 0) {
            itemStack_1.onCraft(this.player.world, this.player, this.amount);
            ServerHandler.handleOnCrafted(itemStack_1, player);
        }

        if (this.inventory instanceof RecipeUnlocker) {
            ((RecipeUnlocker) this.inventory).unlockLastRecipe(this.player);
        }

        this.amount = 0;
    }
}
