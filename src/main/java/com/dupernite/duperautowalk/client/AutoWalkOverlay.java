package com.dupernite.duperautowalk.client;

import com.dupernite.duperautowalk.DuperAutoWalk;
import com.dupernite.duperautowalk.config.AutoWalkConfig;
import com.dupernite.duperautowalk.event.ClientTickHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?} else {
/*import net.minecraft.resources.ResourceLocation;
*///?}

public class AutoWalkOverlay {
    //? if >=1.21.11 {
    private static final Identifier TEXTURE = Identifier.parse(DuperAutoWalk.MOD_ID + ":textures/gui/autowalk.png");
    //?} else {
    /*private static final ResourceLocation TEXTURE = ResourceLocation.parse(DuperAutoWalk.MOD_ID + ":textures/gui/autowalk.png");
    *///?}

    public static void render(GuiGraphics guiGraphics) {
        Minecraft client = Minecraft.getInstance();

        int width = client.getWindow().getGuiScaledWidth();
        int height = client.getWindow().getGuiScaledHeight();
        int x = 0;
        int y = 0;
        int size_x = AutoWalkConfig.getIconSize();
        int size_y = AutoWalkConfig.getIconSize();

        if (ClientTickHandler.isOn && AutoWalkConfig.isHudFeedbackEnabled()) {
            switch (AutoWalkConfig.getPosition()) {
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
                    x = AutoWalkConfig.getCoords_x();
                    y = AutoWalkConfig.getCoords_y();
                }
            }

            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, size_x, size_y, 16, 16, 16, 16);
        }
    }
}
