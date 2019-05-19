package me.marnic.animalnet.mixin;

import me.marnic.animalnet.items.AnimalNetItem;
import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillageGossipType;
import net.minecraft.village.VillagerGossips;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;

/**
 * Copyright (c) 05.03.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
@Mixin(VillagerEntity.class)
public abstract class AnimalNetMixinVillager extends LivingEntity {


    protected AnimalNetMixinVillager(EntityType<LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    private boolean isNet(Item item) {
        return AnimalNetItem.class.isAssignableFrom(item.getClass());
    }

    @Inject(method = "interactMob",at = @At("HEAD"),cancellable = true)
    public void intercatMob(PlayerEntity playerEntity_1, Hand hand_1,CallbackInfoReturnable infoReturnable) {
        ItemStack itemStack_1 = playerEntity_1.getStackInHand(hand_1);
        if(isNet(itemStack_1.getItem())) {
            itemStack_1.interactWithEntity(playerEntity_1, this, hand_1);
            infoReturnable.setReturnValue(true);
        }
    }

    /*@Overwrite
    public boolean interactMob(PlayerEntity playerEntity_1, Hand hand_1) {
        ItemStack itemStack_1 = playerEntity_1.getStackInHand(hand_1);
        boolean boolean_1 = itemStack_1.getItem() == Items.NAME_TAG || isNet(itemStack_1.getItem());
        if (boolean_1) {
            itemStack_1.interactWithEntity(playerEntity_1, this, hand_1);
            return true;
        } else if (itemStack_1.getItem() != Items.VILLAGER_SPAWN_EGG && this.isAlive() && !getVillager().hasCustomer() && !this.isSleeping()) {
            if (this.isChild()) {
                this.sayNo();
                return getVillager().interactMob(playerEntity_1, hand_1);
            } else {
                boolean boolean_2 = getVillager().getOffers().isEmpty();
                if (hand_1 == Hand.MAIN_HAND) {
                    if (boolean_2) {
                        this.sayNo();
                    }

                    playerEntity_1.incrementStat(Stats.TALKED_TO_VILLAGER);
                }

                if (boolean_2) {
                    //return getVillager().interactMob(playerEntity_1, hand_1);
                    return false;
                } else {
                    if (!this.world.isClient && !getVillager().getOffers().isEmpty()) {
                        this.beginTradeWith(playerEntity_1);
                    }

                    return true;
                }
            }
        } else {
            return getVillager().interactMob(playerEntity_1, hand_1);
        }
    }*/

    public VillagerEntity getVillager() {
        return ((VillagerEntity)(LivingEntity)this);
    }

    @Shadow
    private void sayNo() {}

    @Shadow
    private void beginTradeWith(PlayerEntity playerEntity_1) { }
}
