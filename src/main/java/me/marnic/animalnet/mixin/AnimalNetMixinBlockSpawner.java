package me.marnic.animalnet.mixin;

import me.marnic.animalnet.api.SpawnerUtil;
import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

/**
 * Copyright (c) 26.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
@Mixin(Block.class)
public class AnimalNetMixinBlockSpawner {

    @Inject(method = "onPlaced", at = @At("HEAD"))
    public void onPlaced(World world_1, BlockPos blockPos_1, BlockState blockState_1, LivingEntity livingEntity_1, ItemStack itemStack_1, CallbackInfo info) {
        if (!world_1.isClient) {
            if (blockState_1 == Blocks.SPAWNER.getDefaultState()) {
                PlayerEntity player = (PlayerEntity) livingEntity_1;
                if (SpawnerUtil.isCustomSpawner(player.inventory.getMainHandStack())) {
                    SpawnerUtil.makeSpawnerBlock(player.inventory.getMainHandStack(), world_1, blockPos_1);
                    if (player.isCreative()) {
                        ItemStack stack = player.inventory.getMainHandStack().copy();
                        player.setEquippedStack(EquipmentSlot.MAINHAND, stack);
                    }
                }
            }
        }
    }

    private static final Random RANDOM = new Random();

    @Inject(method = "onBreak", at = @At("HEAD"))
    public void onBroken(World world_1, BlockPos blockPos_1, BlockState blockState_1, PlayerEntity playerEntity_1, CallbackInfo info) {
        if (!world_1.isClient()) {
            if (blockState_1 == Blocks.SPAWNER.getDefaultState()) {
                List<ItemStack> drops = Block.getDroppedStacks(blockState_1,(ServerWorld)world_1,blockPos_1,null,playerEntity_1,playerEntity_1.getMainHandStack());
                boolean b = false;
                for (ItemStack s : drops) {
                    if (s.getItem().equals(blockState_1.getBlock().asItem())) {
                        b = true;
                        break;
                    }
                }

                if (!b) {
                    if (RANDOM.nextInt(3) == 2) {
                        Block.dropStack(world_1,blockPos_1,new ItemStack(AnimalNetItems.spawnerFragmental,2));
                    } else {
                        Block.dropStack(world_1,blockPos_1,new ItemStack(AnimalNetItems.spawnerFragmental));
                    }
                }
            }
        }
    }
}
