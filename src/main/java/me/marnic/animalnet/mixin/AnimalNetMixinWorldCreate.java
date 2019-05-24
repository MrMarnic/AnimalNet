package me.marnic.animalnet.mixin;

import com.google.gson.JsonElement;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

/**
 * Copyright (c) 18.02.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

@Mixin(MinecraftServer.class)
public class AnimalNetMixinWorldCreate{


    @Shadow
    private File gameDir;

    @Inject(method = "loadWorld",at=@At(value = "HEAD"))
    public void loadWorld(String string_1, String string_2, long long_1, LevelGeneratorType levelGeneratorType_1, JsonElement jsonElement_1,CallbackInfo info) {
        File f = new File(gameDir.getAbsolutePath()+"\\"+string_1);
        File animalData = new File(f.getAbsolutePath()+"\\animalData");
        if(!animalData.exists()) {
            animalData.mkdir();
        }
    }
}
