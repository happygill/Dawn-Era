package com.happsg.dawnera.util;

import com.tterrag.registrate.AbstractRegistrate;
import net.minecraftforge.eventbus.api.IEventBus;

public class DawnRegistrate extends AbstractRegistrate<DawnRegistrate> {
    protected DawnRegistrate(String modid) {
        super(modid);
    }
    public static DawnRegistrate create(String modid) {
        return new DawnRegistrate(modid);
    }
    @Override
    public DawnRegistrate registerEventListeners(IEventBus bus) {
        return super.registerEventListeners(bus);
    }
}
