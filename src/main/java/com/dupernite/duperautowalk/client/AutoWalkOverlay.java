package com.dupernite.duperautowalk.client;

import com.dupernite.duperautowalk.DuperAutoWalk;
import com.dupernite.duperautowalk.config.AutoWalkConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import com.dupernite.duperautowalk.event.keyInputHandler;
import net.minecraft.client.gui.DrawContext;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.gl.RenderPipelines;

public class AutoWalkOverlay implements HudElement {
    private static final Identifier TEXTURE = Identifier.of(DuperAutoWalk.MOD_ID,
            "textures/gui/autowalk.png");

    public static void register() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.SUBTITLES, Identifier.of(DuperAutoWalk.MOD_ID, "autowalk_hud"), new AutoWalkOverlay());
    }

    @Override
    public void render(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if(client != null && keyInputHandler.isOn && AutoWalkConfig.isHudFeedbackEnabled()){
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            int x = 0;
            int y = 0;
            int size_x = AutoWalkConfig.getIconSize();
            int size_y = AutoWalkConfig.getIconSize();

            switch (AutoWalkConfig.getPosition()) {
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
                    x = AutoWalkConfig.getCoords_x();
                    y = AutoWalkConfig.getCoords_y();
                }
            }

            drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, size_x, size_y, 16, 16, 16, 16);
        }
    }
}
