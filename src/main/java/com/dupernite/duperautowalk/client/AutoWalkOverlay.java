package com.dupernite.duperautowalk.client;

import com.dupernite.duperautowalk.DuperAutoWalk;
import com.dupernite.duperautowalk.compat.YACLconfig;
import com.dupernite.duperautowalk.event.ClientTickHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class AutoWalkOverlay {
    private static final ResourceLocation TEXTURE = ResourceLocation.parse(DuperAutoWalk.MOD_ID + ":textures/gui/autowalk.png");

    public static void render(GuiGraphics guiGraphics) {
        Minecraft client = Minecraft.getInstance();

        int width = client.getWindow().getGuiScaledWidth();
        int height = client.getWindow().getGuiScaledHeight();
        int x = 0;
        int y = 0;
        int size_x = 16;
        int size_y = 16;

        RenderSystem.setShaderColor(1, 1, 1, 1);
        //? if <=1.21.1 {
        /*RenderSystem.setShaderTexture(0, TEXTURE);
        *///?}

        if (ClientTickHandler.isOn && YACLconfig.getFeedback() == YACLconfig.feedbackEnum.HUD) {
            switch (YACLconfig.getPosition()) {
                case BOTTOM_LEFT -> y = height - size_y - 1;
                case BOTTOM_RIGHT -> {
                    x = width - size_x - 1;
                    y = height - size_y - 1;
                }
                case MIDDLE_LEFT -> y = height / 2 - size_y / 2;
                case MIDDLE_RIGHT -> {
                    x = width - size_x - 1;
                    y = height / 2 - size_y / 2;
                }
                case TOP_LEFT -> y = 1;
                case TOP_RIGHT -> {
                    x = width - size_x - 1;
                    y = 1;
                }
                case CUSTOM -> {
                    x = YACLconfig.getCoords_x();
                    y = YACLconfig.getCoords_y();
                }
            }

            switch (YACLconfig.getSize()) {
                case EIGHT -> {
                    size_x = 8;
                    size_y = 8;
                }
                case SIXTEEN -> {
                    size_x = 16;
                    size_y = 16;
                }
                case THIRTY_TWO -> {
                    size_x = 32;
                    size_y = 32;
                }
                case FORTY_EIGHT -> {
                    size_x = 48;
                    size_y = 48;
                }
                case SIXTY_FOUR -> {
                    size_x = 64;
                    size_y = 64;
                }
            }

            //? if <=1.21.1 {
            /*guiGraphics.blit(TEXTURE, x, y, 0, 0, size_x, size_y, size_x, size_y);
            *///?} else {
            guiGraphics.blit(RenderType::guiTextured, TEXTURE, x, y, 0.0f, 0.0f, size_x, size_y, 16, 16);
            //?}
        }
    }
}