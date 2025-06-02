package com.dupernite.duperautowalk;

import com.dupernite.duperautowalk.compat.YACLconfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(DuperAutoWalk.MOD_ID)
public class DuperAutoWalk {
    public static final String MOD_ID = "duperautowalk";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public DuperAutoWalk(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        YACLconfig.GSON.load();

        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (client, parent) -> YACLconfig.createScreen(parent)
        );
        
        LOGGER.info("DuperAutoWalk mod is starting up with container: " + modContainer.getModId());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("DuperAutoWalk mod is initializing.");
    }
}