package me.marnic.animalnet.items;

import me.marnic.animalnet.config.AnimalNetConfig;
import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AmbientEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Predicate;

/**
 * Copyright (c) 19.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class AnimalNetItem extends Item {
    private NetSize size;
    private NetType type;
    private double acceptedSize;
    private Predicate<LivingEntity> accepts;

    public AnimalNetItem(Properties properties, NetSize size, NetType type, double acceptedSize, Predicate<LivingEntity> accepts) {
        super(properties);
        this.size = size;
        this.type = type;
        this.acceptedSize = acceptedSize;
        this.accepts = accepts;
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack p_111207_1_, PlayerEntity player, LivingEntity p_111207_3_, Hand p_111207_4_) {
        if (!player.getCommandSenderWorld().isClientSide) {
            AxisAlignedBB boundingBox = p_111207_3_.getBoundingBox();
            double size = (boundingBox.maxX - boundingBox.minX) * (boundingBox.maxY - boundingBox.minY);
            if (!checkEntity(p_111207_3_,size,p_111207_1_,player)) {
                return ActionResultType.PASS;
            }
            return ActionResultType.SUCCESS;
        }
        return super.interactLivingEntity(p_111207_1_, player, p_111207_3_, p_111207_4_);
    }
    
    public boolean checkEntity(LivingEntity e, double size, ItemStack stack, PlayerEntity player) {

        if(!canBeCaughtAtAll(e)) {
            sendStatus(player, new TranslationTextComponent("message.animalnet.can_not_be_caught"));
            return false;
        }

        if(!this.accepts.test(e)) {
            if(AnimalNetItems.ANIMAL_NET_SMALL.get().getAccepts().test(e)) {
                sendStatus(player, new TranslationTextComponent("message.animalnet.animal_needed"));
            } else if(AnimalNetItems.MONSTER_NET_SMALL.get().getAccepts().test(e)) {
                sendStatus(player, new TranslationTextComponent("message.animalnet.monster_needed"));
            } else if(AnimalNetItems.NPC_NET.get().getAccepts().test(e)) {
                sendStatus(player, new TranslationTextComponent("message.animalnet.villager_needed"));
            } else {
                sendStatus(player, new TranslationTextComponent("message.animalnet.can_not_be_caught"));
            }
            return false;
        }

        if(!this.fitSize(size)) {
            if (this.getSize() != NetSize.BIG) {
                sendStatus(player, new TranslationTextComponent("message.animalnet.net_too_small"));
            } else {
                sendStatus(player, new TranslationTextComponent("message.animalnet.entity_too_big", new TranslationTextComponent(this.getType().getFormalTranslationKey())));
            }
            return false;
        }

        addNetToInv(stack,player,e);
        return true;
    }

    private static void addNetToInv(ItemStack current, PlayerEntity player, Entity target) {
        ItemStack stack = AnimalNetItems.CAUGHT_ENTITY_ITEM.get().createInstance(target);
        addItem(player, stack);
        target.remove();
        if (!player.isCreative()) {
            current.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
        }
    }

    private static boolean canBeCaughtAtAll(Entity entity) {
        return !AnimalNetConfig.GENERAL_OPTIONS.getExcluded_entities_array_list().contains(EntityType.byString(entity.getType().getRegistryName().toString()).toString());
    }

    private static void sendStatus(PlayerEntity p, ITextComponent msg) {
        p.displayClientMessage(msg, true);
    }

    private static void addItem(PlayerEntity p, ItemStack stack) {
        boolean full = true;

        for (ItemStack s : p.inventory.items) {
            if (s.isEmpty()) {
                full = false;
                break;
            }
        }
        if (full) {
            p.drop(stack, false);
        } else {
            p.addItem(stack);
        }
    }

    public double getAcceptedSize() {
        return acceptedSize;
    }

    public boolean fitSize(double size) {
        return size <= acceptedSize;
    }

    public NetType getType() {
        return type;
    }

    public NetSize getSize() {
        return size;
    }

    public static boolean animalNetPredicate(LivingEntity entity) {
        return entity instanceof AnimalEntity || entity instanceof WaterMobEntity || entity instanceof AmbientEntity;
    }

    public static boolean monsterNetPredicate(LivingEntity entity) {
        return entity instanceof IMob;
    }

    public Predicate<LivingEntity> getAccepts() {
        return accepts;
    }
}

