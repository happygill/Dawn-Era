package com.happsg.dawnera;

import com.happsg.dawnera.registry.AllMemoryTypes;
import com.happsg.dawnera.registry.AllEntities;
import com.happsg.dawnera.util.DawnRegistrate;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
        AllEntities.init();

    }

    public static ResourceLocation asResource(String name) {
        return new ResourceLocation(MODID, name);
    }
}
