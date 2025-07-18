package com.dupernite.duperautowalk.event;

import com.dupernite.duperautowalk.compat.YACLconfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

//? if <1.21.6 {
/*@EventBusSubscriber(modid = "duperautowalk", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
*///?} else {
@EventBusSubscriber(modid = "duperautowalk", value = Dist.CLIENT)
//?}
public class ClientTickHandler {
    public static boolean isOn = false;
    private static boolean ForwardKeyState = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft client = Minecraft.getInstance();

        if (keyInputHandler.configKey != null && keyInputHandler.configKey.consumeClick()) {
            client.setScreen(YACLconfig.createScreen(client.screen));
        }

        if (keyInputHandler.autoWalkKey != null && keyInputHandler.autoWalkKey.consumeClick()) {
            isOn = !isOn;

            if (client.player != null && YACLconfig.getFeedback() == YACLconfig.feedbackEnum.CHAT) {
                if (isOn) {
                    //? if <=1.21.1 {
                    /*client.player.sendSystemMessage(Component.translatable("chat.duperautowalk.autowalk.enabled")
                            .withStyle(ChatFormatting.GREEN));
                    *///?} else {
                    client.player.displayClientMessage(Component.translatable("chat.duperautowalk.autowalk.enabled")
                            .withStyle(ChatFormatting.GREEN), true);
                    //?}
                } else {
                    //? if <=1.21.1 {
                    /*client.player.sendSystemMessage(Component.translatable("chat.duperautowalk.autowalk.disabled")
                            .withStyle(ChatFormatting.RED));
                    *///?} else {
                    client.player.displayClientMessage(Component.translatable("chat.duperautowalk.autowalk.disabled")
                            .withStyle(ChatFormatting.RED), true);
                    //?}
                }
            }
        }

        if (client.options.keyDown.consumeClick()){
            isOn = false;
            if (client.player != null && YACLconfig.getFeedback() == YACLconfig.feedbackEnum.CHAT && ForwardKeyState) {
                //? if <=1.21.1 {
                /*client.player.sendSystemMessage(Component.translatable("chat.duperautowalk.autowalk.disabled")
                        .withStyle(ChatFormatting.RED));
                *///?} else {
                client.player.displayClientMessage(Component.translatable("chat.duperautowalk.autowalk.disabled")
                        .withStyle(ChatFormatting.RED), false);
                //?}
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
}