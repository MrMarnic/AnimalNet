package me.marnic.animalnet.mixin;

import me.marnic.animalnet.items.AnimalNetItem;
import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
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
@Mixin(value = {VillagerEntity.class, WanderingTraderEntity.class})
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
            itemStack_1.useOnEntity(playerEntity_1, this, hand_1);
            infoReturnable.setReturnValue(true);
        }
    }
}
