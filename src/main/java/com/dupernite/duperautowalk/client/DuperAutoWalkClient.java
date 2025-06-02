package com.dupernite.duperautowalk.client;

import com.dupernite.duperautowalk.compat.YACLconfig;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = "duperautowalk", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DuperAutoWalkClient {
    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.CROSSHAIR, 
            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("duperautowalk", "autowalk_overlay"), 
            (guiGraphics, deltaTracker) -> AutoWalkOverlay.render(guiGraphics));
    }
}