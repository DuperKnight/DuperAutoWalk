package com.dupernite.duperautowalk.event;

import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = "duperautowalk", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class keyInputHandler {
    public static final String CATEGORY = "key.category";
    public static final String KEY_AUTO_WALK = "key.duperautowalk.autowalk";
    public static final String KEY_OPEN_CONFIG = "key.duperautowalk.config";

    public static KeyMapping autoWalkKey;
    public static KeyMapping configKey;

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        autoWalkKey = new KeyMapping(
                KEY_AUTO_WALK,
                GLFW.GLFW_KEY_Z,
                CATEGORY
        );
        configKey = new KeyMapping(
                KEY_OPEN_CONFIG,
                GLFW.GLFW_KEY_O,
                CATEGORY
        );
        
        event.register(autoWalkKey);
        event.register(configKey);
    }
}