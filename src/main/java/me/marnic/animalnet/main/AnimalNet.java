package me.marnic.animalnet.main;

import me.marnic.animalnet.config.AnimalNetConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = AnimalNet.MODID, name = AnimalNet.NAME, version = AnimalNet.VERSION)
public class AnimalNet {
    public static final String MODID = "animalnet";
    public static final String NAME = "Animal Net";
    public static final String VERSION = "1.11";
    public static final AnimalNetModHandler ANIMAL_NET_MOD_HANDLER = new AnimalNetModHandler();


    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("Starting PreInitialization...");
        AnimalNetItems.init();
        MinecraftForge.EVENT_BUS.register(ANIMAL_NET_MOD_HANDLER);
        logger.info("PreInitialization finished!");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("Starting Initialization...");
        AnimalNetConfig.general_options.initExcludedEntitiesArraysList();
        logger.info("Initialization finished!");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("Starting PostInitialization...");
        logger.info("PostInitialization finished!");
    }
}
