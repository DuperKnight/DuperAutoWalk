package com.dupernite.duperautowalk.event;

import com.dupernite.duperautowalk.config.AutoWalkConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.ClientInput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;

import java.lang.reflect.Field;

@EventBusSubscriber(modid = "duperautowalk", value = Dist.CLIENT)
public class ClientTickHandler {
    public static boolean isOn = false;
    private static boolean ForwardKeyState = false;
    private static Field moveVectorField;

    @SubscribeEvent
    public static void onClientTickPre(ClientTickEvent.Pre event) {
        Minecraft client = Minecraft.getInstance();
        if (isOn) {
            client.options.keyUp.setDown(true);
            ForwardKeyState = true;
        } else if (ForwardKeyState) {
            client.options.keyUp.setDown(false);
            ForwardKeyState = false;
        }
    }

    @SubscribeEvent
    public static void onMovementInputUpdate(MovementInputUpdateEvent event) {
        if (isOn) {
            applyForwardInput(event.getInput());
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft client = Minecraft.getInstance();

        if (keyInputHandler.configKey != null && keyInputHandler.configKey.consumeClick()) {
            client.setScreen(AutoWalkConfig.createScreen(client.screen));
        }

        if (keyInputHandler.autoWalkKey != null && keyInputHandler.autoWalkKey.consumeClick()) {
            isOn = !isOn;

            if (client.player != null && AutoWalkConfig.isChatFeedbackEnabled()) {
                if (isOn) {
                    client.player.displayClientMessage(Component.translatable("chat.duperautowalk.autowalk.enabled")
                            .withStyle(ChatFormatting.GREEN), false);
                } else {
                    client.player.displayClientMessage(Component.translatable("chat.duperautowalk.autowalk.disabled")
                            .withStyle(ChatFormatting.RED), false);
                }
            }
        }

        if (client.options.keyDown.consumeClick()){
            isOn = false;
            if (client.player != null && AutoWalkConfig.isChatFeedbackEnabled() && ForwardKeyState) {
                client.player.displayClientMessage(Component.translatable("chat.duperautowalk.autowalk.disabled")
                        .withStyle(ChatFormatting.RED), false);
            }
        }

        if (isOn) {
            client.options.keyUp.setDown(true);
            ForwardKeyState = true;
        } else if (ForwardKeyState) {
            client.options.keyUp.setDown(false);
            ForwardKeyState = false;
        }
    }

    private static void applyForwardInput(ClientInput input) {
        Input current = input.keyPresses;
        input.keyPresses = new Input(
                true,
                false,
                current.left(),
                current.right(),
                current.jump(),
                current.shift(),
                current.sprint()
        );

        float sideways = calculateImpulse(current.left(), current.right());
        setMoveVector(input, new Vec2(sideways, 1.0F).normalized());
    }

    private static float calculateImpulse(boolean positive, boolean negative) {
        if (positive == negative) {
            return 0.0F;
        }
        return positive ? 1.0F : -1.0F;
    }

    private static void setMoveVector(ClientInput input, Vec2 moveVector) {
        try {
            if (moveVectorField == null) {
                moveVectorField = ClientInput.class.getDeclaredField("moveVector");
                moveVectorField.setAccessible(true);
            }
            moveVectorField.set(input, moveVector);
        } catch (ReflectiveOperationException e) {
            Minecraft.getInstance().options.keyUp.setDown(true);
        }
    }
}
