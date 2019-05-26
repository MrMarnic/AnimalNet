package me.marnic.animalnet.mixin;

import com.sun.istack.internal.Nullable;
import me.marnic.animalnet.api.SpawnerUtil;
import me.marnic.animalnet.main.AnimalNetItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

/**
 * Copyright (c) 26.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
@Mixin(Block.class)
public class AnimalNetMixinBlockSpawner {

    @Inject(method = "onPlaced",at=@At("HEAD"))
    public void onPlaced(World world_1, BlockPos blockPos_1, BlockState blockState_1, @Nullable LivingEntity livingEntity_1, ItemStack itemStack_1, CallbackInfo info) {
        if(!world_1.isClient) {
            if(blockState_1 == Blocks.SPAWNER.getDefaultState()) {
                PlayerEntity player = (PlayerEntity) livingEntity_1;
                if (SpawnerUtil.isCustomSpawner(player.inventory.getMainHandStack())) {
                    SpawnerUtil.makeSpawnerBlock(player.inventory.getMainHandStack(), world_1, blockPos_1);
                    if(player.isCreative()) {
                        ItemStack stack = player.inventory.getMainHandStack().copy();
                        player.setEquippedStack(EquipmentSlot.MAINHAND,stack);
                    }
                }
            }
        }
    }

    private static final Random RANDOM = new Random();

    @Inject(method = "onBroken",at=@At("HEAD"))
    public void onBroken(IWorld iWorld_1, BlockPos blockPos_1, BlockState blockState_1,CallbackInfo info) {
        if(!iWorld_1.isClient()) {
            if(blockState_1==Blocks.SPAWNER.getDefaultState()) {
                if (RANDOM.nextInt(3) == 2) {
                    iWorld_1.spawnEntity(new ItemEntity(iWorld_1.getWorld(),blockPos_1.getX(),blockPos_1.getY()+1,blockPos_1.getZ(),new ItemStack(AnimalNetItems.spawnerFragmental,2)));
                } else {
                    iWorld_1.spawnEntity(new ItemEntity(iWorld_1.getWorld(),blockPos_1.getX(),blockPos_1.getY()+1,blockPos_1.getZ(),new ItemStack(AnimalNetItems.spawnerFragmental)));
                }
            }
        }
    }
}
