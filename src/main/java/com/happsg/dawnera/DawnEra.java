package com.happsg.dawnera;

import com.happsg.dawnera.registry.AllMemoryTypes;
import com.happsg.dawnera.registry.DawnEntities;
import com.happsg.dawnera.util.DawnRegistrate;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

@Mod(DawnEra.MODID)
public class DawnEra {

    public static final String MODID = "dawnera";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DawnRegistrate REGISTRATE = DawnRegistrate.create(MODID);

    public DawnEra() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        REGISTRATE.registerEventListeners(modEventBus);

        AllMemoryTypes.register(modEventBus);
        DawnEntities.init();

    }

    public static ResourceLocation asResource(String name) {
        return new ResourceLocation(MODID, name);
    }
}
