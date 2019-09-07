package me.marnic.animalnet.api;

import java.io.File;

/**
 * Copyright (c) 24.07.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class StartingUtil {
    public static void handleServerStart(File gameDir) {
        File animalData = new File(gameDir.getAbsolutePath() + File.separator + "animalData");
        if (!animalData.exists()) {
            animalData.mkdir();
        }
    }
}
