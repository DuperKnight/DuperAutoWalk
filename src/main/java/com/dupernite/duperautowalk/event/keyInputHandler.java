package com.dupernite.duperautowalk.event;


import com.dupernite.duperautowalk.compat.YACLconfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
public class keyInputHandler {
    public static final String CATEGORY = "key.category";
    public static final String KEY_AUTO_WALK = "key.duperautowalk.autowalk";
    public static final String KEY_OPEN_CONFIG = "key.duperautowalk.config";

    public static KeyBinding autoWalkKey;
    public static KeyBinding configKey;

    public static boolean isOn = false;
    private static boolean ForwardKeyState = false;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (configKey.wasPressed()) {
                MinecraftClient.getInstance().setScreen(YACLconfig.createScreen(null));
            }
            if (autoWalkKey.wasPressed()) {
                isOn = !isOn;

                if (client.player != null && YACLconfig.getFeedback() == YACLconfig.feedbackEnum.CHAT) {
                    if(isOn){
                        client.player.sendMessage(Text.translatable("chat.duperautowalk.autowalk.enabled").formatted(Formatting.GREEN), false);
                    } else {
                        client.player.sendMessage(Text.translatable("chat.duperautowalk.autowalk.disabled").formatted(Formatting.RED), false);
                    }
                }
            }

            if (MinecraftClient.getInstance().options.backKey.wasPressed()){
                isOn = false;
                if (client.player != null && YACLconfig.getFeedback() == YACLconfig.feedbackEnum.CHAT && ForwardKeyState) {
                    client.player.sendMessage(Text.translatable("chat.duperautowalk.autowalk.disabled").formatted(Formatting.RED), false);
                }
            }

            if (isOn) {
                MinecraftClient.getInstance().options.forwardKey.setPressed(true);
                ForwardKeyState = true;
            } else if (ForwardKeyState) {
                MinecraftClient.getInstance().options.forwardKey.setPressed(false);
                ForwardKeyState = false;
            }
        });
    }

    public static void register() {
        autoWalkKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_AUTO_WALK,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                CATEGORY
                )
        );
        configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_OPEN_CONFIG,
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_UNKNOWN,
                        CATEGORY
                )
        );

        registerKeyInputs();
    }
}
