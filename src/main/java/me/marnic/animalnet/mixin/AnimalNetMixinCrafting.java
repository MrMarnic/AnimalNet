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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject(method = "onCrafted(Lnet/minecraft/item/ItemStack;)V",at = @At("HEAD"))
    public void onCrafted(ItemStack stack,CallbackInfo info) {
        if (this.amount > 0) {
            ServerHandler.handleOnCrafted(stack, player);
        }
    }
}
