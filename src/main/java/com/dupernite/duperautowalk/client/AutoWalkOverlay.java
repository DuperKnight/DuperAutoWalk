package com.dupernite.duperautowalk.client;

import com.dupernite.duperautowalk.DuperAutoWalk;
import com.dupernite.duperautowalk.compat.YACLconfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import com.dupernite.duperautowalk.event.keyInputHandler;

public class AutoWalkOverlay implements HudRenderCallback {
    private static final Identifier TEXTURE = Identifier.of(DuperAutoWalk.MOD_ID,
            "textures/gui/autowalk.png");

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();

        if(client != null){
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            int x = 0;
            int y = 0;
            int size_x = 16;
            int size_y = 16;

            //? if <1.21.2
            /*RenderSystem.setShader(GameRenderer::getPositionTexProgram);*/
            RenderSystem.setShaderColor(1, 1, 1, 1);

            RenderSystem.setShaderTexture(0, TEXTURE);
            if(keyInputHandler.isOn && YACLconfig.getFeedback() == YACLconfig.feedbackEnum.HUD){
                switch (YACLconfig.getPosition()) {
                    case BOTTOM_LEFT-> y = height - size_y - 1;

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
              
                if(YACLconfig.getPosition() == YACLconfig.positionEnum.BOTTOM_LEFT){
                    y = height - 17;
                } else if(YACLconfig.getPosition() == YACLconfig.positionEnum.BOTTOM_RIGHT){
                    x = width - 16;
                    y = height - 17;
                } else if(YACLconfig.getPosition() == YACLconfig.positionEnum.MIDDLE_LEFT){
                    y = height / 2 - 8;
                } else if(YACLconfig.getPosition() == YACLconfig.positionEnum.MIDDLE_RIGHT){
                    x = width - 16;
                    y = height / 2 - 8;
                } else if(YACLconfig.getPosition() == YACLconfig.positionEnum.TOP_LEFT){
                    y = 1;
                } else if(YACLconfig.getPosition() == YACLconfig.positionEnum.TOP_RIGHT){
                    x = width - 16;
                    y = 1;
                }
                //? if <1.21.2
                /*drawContext.drawTexture(TEXTURE, x, y, 0,0,16,16,16,16);*/
                //? if >=1.21.2
                drawContext.drawTexture(RenderLayer::getGuiTexturedOverlay, TEXTURE, x, y, 0,0,16,16,16,16);
            }
        }
    }
}