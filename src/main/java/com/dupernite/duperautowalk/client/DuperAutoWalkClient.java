package com.dupernite.duperautowalk.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

//? if <1.21.6 {
/*@EventBusSubscriber(modid = "duperautowalk", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
*///?} else {
@EventBusSubscriber(modid = "duperautowalk", value = Dist.CLIENT)
 //?}
public class DuperAutoWalkClient {
    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.CROSSHAIR, 
            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("duperautowalk", "autowalk_overlay"), 
            (guiGraphics, deltaTracker) -> AutoWalkOverlay.render(guiGraphics));
    }
}