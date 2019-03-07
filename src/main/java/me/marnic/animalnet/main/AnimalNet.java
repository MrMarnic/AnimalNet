package me.marnic.animalnet.main;

import com.google.gson.JsonObject;
import me.marnic.animalnet.item.AnimalNetModHandler;
import me.marnic.modapi.proxy.CommonProxy;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

@Mod(modid = AnimalNet.MODID, name = AnimalNet.NAME, version = AnimalNet.VERSION)
public class AnimalNet
{
    public static final String MODID = "animalnet";
    public static final String NAME = "Animal Net";
    public static final String VERSION = "1.7";
    public static final AnimalNetModHandler ANIMAL_NET_MOD_HANDLER = new AnimalNetModHandler();


    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        logger.info("Starting PreInitialization...");
        ANIMAL_NET_MOD_HANDLER.init();
        MinecraftForge.EVENT_BUS.register(ANIMAL_NET_MOD_HANDLER);
        proxy.preInit();
        logger.info("PreInitialization finished!");
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        logger.info("Starting Initialization...");
        for(EntityEntry e:ForgeRegistries.ENTITIES.getValuesCollection()) {
            if(EntityLiving.class.isAssignableFrom(e.getEntityClass())) {
                ANIMAL_NET_MOD_HANDLER.registeredEntities.put(e.getEntityClass().getName(),e);
            }
        }

        proxy.init();
        logger.info("Initialization finished!");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("Starting PostInitialization...");
        proxy.postInit();
        logger.info("PostInitialization finished!");
    }

    @SidedProxy(clientSide = "me.marnic.modapi.proxy.ClientProxy",serverSide = "me.marnic.modapi.proxy.CommonProxy")
    public static CommonProxy proxy;
}
