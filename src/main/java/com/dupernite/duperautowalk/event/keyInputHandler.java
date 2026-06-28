package com.dupernite.duperautowalk.event;

import net.minecraft.client.KeyMapping;
//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?} else if >=1.21.9 {
/*import net.minecraft.resources.ResourceLocation;
*///?}
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = "duperautowalk", value = Dist.CLIENT)
public class keyInputHandler {
    //? if >=1.21.9 {
    //? if >=1.21.11 {
    public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("duperautowalk", "key_category"));
    //?} else {
    /*public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath("duperautowalk", "key_category"));
    *///?}
    //?} else {
    /*public static final String CATEGORY = "key.category.duperautowalk.key_category";
    *///?}
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
