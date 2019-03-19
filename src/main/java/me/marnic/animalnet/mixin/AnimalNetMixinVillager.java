package me.marnic.animalnet.mixin;

import com.mojang.authlib.GameProfile;
import me.marnic.animalnet.items.AnimalNetItem;
import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.class_4139;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.TraderRecipe;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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

    @Overwrite
    public boolean interactMob(PlayerEntity playerEntity_1, Hand hand_1) {
        ItemStack itemStack_1 = playerEntity_1.getStackInHand(hand_1);
        boolean boolean_1 = itemStack_1.getItem() == Items.NAME_TAG || isNet(itemStack_1.getItem());

        if(!boolean_1) {
            return getVillager().interactMob(playerEntity_1,hand_1);
        }

        return false;
    }

    public VillagerEntity getVillager() {
        return ((VillagerEntity)(LivingEntity)this);
    }
}
