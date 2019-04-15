package me.marnic.animalnet.main;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;

@Mod(modid = AnimalNet.MODID, name = AnimalNet.NAME, version = AnimalNet.VERSION)
public class AnimalNet
{
    public static final String MODID = "animalnet";
    public static final String NAME = "AnimalNet";
    public static final String VERSION = "Forge 1.7.10 1.1";
    public static final AnimalNetModHandler ANIMAL_NET_MOD_HANDLER = new AnimalNetModHandler();


    private static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        logger.info("Starting PreInitialization...");
        AnimalNetItems.init();
        AnimalNetCrafting.init();
        MinecraftForge.EVENT_BUS.register(ANIMAL_NET_MOD_HANDLER);
        FMLCommonHandler.instance().bus().register(ANIMAL_NET_MOD_HANDLER);
        logger.info("PreInitialization finished!");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        logger.info("Starting Initialization...");
        logger.info("Initialization finished!");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("Starting PostInitialization...");
        logger.info("PostInitialization finished!");
    }
}
