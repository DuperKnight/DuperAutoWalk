package com.dupernite.duperautowalk.client;

import com.dupernite.duperautowalk.DuperAutoWalk;
import com.dupernite.duperautowalk.compat.YACLconfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import com.dupernite.duperautowalk.event.keyInputHandler;
import net.minecraft.client.gui.DrawContext;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
//? if >1.21.5 {
/*import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.gl.RenderPipelines;
*///?}
//? if >=1.21.2 && <1.21.6 {
import net.minecraft.client.render.RenderLayer;
//?}

//? if <1.21.6 {
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
                //? if <1.21.2 {
                /*drawContext.drawTexture(TEXTURE, x, y, 0, 0, size_x, size_y, size_x, size_y);
                *///?} else {
                drawContext.drawTexture(RenderLayer::getGuiTextured, TEXTURE, x, y, 0, 0, size_x, size_y, size_x, size_y);
                //?}
            }
        }
    }
}
//?} else {
/*public class AutoWalkOverlay implements HudElement {
    private static final Identifier TEXTURE = Identifier.of(DuperAutoWalk.MOD_ID,
            "textures/gui/autowalk.png");

    public static void register() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.SUBTITLES, Identifier.of(DuperAutoWalk.MOD_ID, "autowalk_hud"), new AutoWalkOverlay());
    }

    @Override
    public void render(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if(client != null && keyInputHandler.isOn && YACLconfig.getFeedback() == YACLconfig.feedbackEnum.HUD){
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            int x = 0;
            int y = 0;
            int size_x = 16;
            int size_y = 16;

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

            drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, size_x, size_y, size_x, size_y);
        }
    }
}
*///?}
