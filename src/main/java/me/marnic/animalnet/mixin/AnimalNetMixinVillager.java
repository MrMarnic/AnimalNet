package me.marnic.animalnet.mixin;

import com.mojang.authlib.GameProfile;
import me.marnic.animalnet.items.AnimalNetItem;
import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Copyright (c) 05.03.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
@Mixin(VillagerEntity.class)
public abstract class AnimalNetMixinVillager extends LivingEntity {


    protected AnimalNetMixinVillager(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    private boolean isNet(Item item) {
        return AnimalNetItem.class.isAssignableFrom(item.getClass());
    }

    @Overwrite
    public boolean interactMob(PlayerEntity playerEntity_1, Hand hand_1) {
        ItemStack itemStack_1 = playerEntity_1.getStackInHand(hand_1);
        boolean boolean_1 = itemStack_1.getItem() == Items.NAME_TAG || isNet(itemStack_1.getItem()) ;
        if (boolean_1) {
            itemStack_1.interactWithEntity(playerEntity_1, this, hand_1);
            return true;
        } else if (itemStack_1.getItem() != Items.VILLAGER_SPAWN_EGG && this.isValid() && !getVillager().hasCustomer() && !this.isChild()) {
            if (hand_1 == Hand.MAIN) {
                playerEntity_1.increaseStat(Stats.TALKED_TO_VILLAGER);
            }

            if (getVillager().getRecipes().isEmpty()) {
                return getVillager().interactMob(playerEntity_1, hand_1);
            } else {
                if (!this.world.isClient && !getVillager().getRecipes().isEmpty()) {
                    if (getVillager().getVillage() != null && getVillager().getVillage().getRaid() != null && getVillager().getVillage().getRaid().isOnGoing()) {
                        this.world.summonParticle(this, (byte)42);
                    } else {
                        getVillager().setCurrentCustomer(playerEntity_1);
                        getVillager().method_17449(playerEntity_1, this.getDisplayName());
                    }
                }

                return true;
            }
        } else {
            return getVillager().interactMob(playerEntity_1, hand_1);
        }
    }

    public VillagerEntity getVillager() {
        return ((VillagerEntity)(LivingEntity)this);
    }
}
