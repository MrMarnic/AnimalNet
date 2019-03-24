package me.marnic.animalnet.mixin;

import net.fabricmc.fabric.mixin.registry.MixinWorldSaveHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.WorldGenerationProgressScreen;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

/**
 * Copyright (c) 05.03.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

@Mixin(PlayerManager.class)
public abstract class AnimalNetMixinWorldLoad {

    @Inject(method = "onPlayerConnect",at = @At(value = "RETURN"))
    public void onConnect( ClientConnection clientConnection_1, ServerPlayerEntity serverPlayerEntity_1,CallbackInfo info) {
        File f = serverPlayerEntity_1.getServerWorld().getSaveHandler().getWorldDir();
        File animalData = new File(f.getAbsolutePath()+"\\animalData");
        if(!animalData.exists()) {
            animalData.mkdir();
        }
    }
}
