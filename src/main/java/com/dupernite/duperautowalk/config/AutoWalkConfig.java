package com.dupernite.duperautowalk.config;

import com.dupernite.duperautowalk.DuperAutoWalk;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
//? if >=1.21.9 {
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
//?}
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?} else {
/*import net.minecraft.resources.ResourceLocation;
*///?}
import net.neoforged.fml.loading.FMLPaths;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public final class AutoWalkConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FMLPaths.CONFIGDIR.get().resolve("duperautowalk.json").toFile();
    private static final int MIN_ICON_SIZE = 8;
    private static final int MAX_ICON_SIZE = 64;

    public enum positionEnum {
        TOP_LEFT,
        TOP_RIGHT,
        MIDDLE_LEFT,
        MIDDLE_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        CUSTOM
    }

    public enum feedbackEnum {
        NONE,
        HUD,
        CHAT
    }

    public enum sizeEnum {
        EIGHT(8),
        SIXTEEN(16),
        THIRTY_TWO(32),
        FORTY_EIGHT(48),
        SIXTY_FOUR(64);

        private final int pixels;

        sizeEnum(int pixels) {
            this.pixels = pixels;
        }

        public int pixels() {
            return pixels;
        }
    }

    private static positionEnum position = positionEnum.TOP_LEFT;
    private static feedbackEnum feedback = feedbackEnum.HUD;
    private static boolean hudFeedback = true;
    private static boolean chatFeedback = false;
    private static sizeEnum size = sizeEnum.SIXTEEN;
    private static int iconSize = sizeEnum.SIXTEEN.pixels();
    private static int coords_x = 16;
    private static int coords_y = 16;

    private AutoWalkConfig() {
    }

    public static void load() {
        if (!CONFIG_FILE.exists()) {
            save();
            return;
        }

        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> config = GSON.fromJson(reader, type);
            if (config == null) {
                save();
                return;
            }

            position = readEnum(config.get("position"), positionEnum.class, positionEnum.TOP_LEFT);
            feedback = readEnum(config.get("feedback"), feedbackEnum.class, feedbackEnum.HUD);
            hudFeedback = switch (feedback) {
                case HUD -> true;
                case CHAT, NONE -> false;
            };
            chatFeedback = feedback == feedbackEnum.CHAT;
            if (config.get("hud_feedback") instanceof Boolean value) {
                hudFeedback = value;
            }
            if (config.get("chat_feedback") instanceof Boolean value) {
                chatFeedback = value;
            }
            size = readEnum(config.get("size"), sizeEnum.class, sizeEnum.SIXTEEN);
            iconSize = clamp(readInt(config.get("size_pixels"), size.pixels()), MIN_ICON_SIZE, MAX_ICON_SIZE);
            coords_x = readInt(config.get("coords_x"), 16);
            coords_y = readInt(config.get("coords_y"), 16);
        } catch (IOException e) {
            DuperAutoWalk.LOGGER.error("Failed to load config {}", CONFIG_FILE.getAbsolutePath(), e);
        }
    }

    public static void save() {
        Map<String, Object> config = new HashMap<>();
        config.put("position", position.name());
        config.put("feedback", getFeedback().name());
        config.put("hud_feedback", hudFeedback);
        config.put("chat_feedback", chatFeedback);
        config.put("size", size.name());
        config.put("size_pixels", iconSize);
        config.put("coords_x", coords_x);
        config.put("coords_y", coords_y);

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            DuperAutoWalk.LOGGER.error("Failed to save config {}", CONFIG_FILE.getAbsolutePath(), e);
        }
    }

    public static Screen createScreen(Screen parent) {
        return new AutoWalkConfigScreen(parent);
    }

    public static feedbackEnum getFeedback() {
        if (chatFeedback) {
            return feedbackEnum.CHAT;
        }
        return hudFeedback ? feedbackEnum.HUD : feedbackEnum.NONE;
    }

    public static boolean isHudFeedbackEnabled() {
        return hudFeedback;
    }

    public static boolean isChatFeedbackEnabled() {
        return chatFeedback;
    }

    public static positionEnum getPosition() {
        return position;
    }

    public static sizeEnum getSize() {
        return size;
    }

    public static int getIconSize() {
        return iconSize;
    }

    public static int getCoords_x() {
        return coords_x;
    }

    public static int getCoords_y() {
        return coords_y;
    }

    private static void setChatFeedback(boolean value) {
        chatFeedback = value;
        feedback = getFeedback();
        save();
    }

    private static void setHudFeedback(boolean value) {
        hudFeedback = value;
        feedback = getFeedback();
        save();
    }

    private static void setPosition(positionEnum value) {
        position = value;
        save();
    }

    private static void setSize(sizeEnum value) {
        size = value;
        iconSize = value.pixels();
        save();
    }

    private static void setIconSize(int value) {
        iconSize = clamp(value, MIN_ICON_SIZE, MAX_ICON_SIZE);
        size = closestSize(iconSize);
        save();
    }

    private static void setCustomCoords(int x, int y) {
        coords_x = x;
        coords_y = y;
        position = positionEnum.CUSTOM;
        save();
    }

    private static <T extends Enum<T>> T readEnum(Object value, Class<T> enumType, T fallback) {
        if (value instanceof String text) {
            try {
                return Enum.valueOf(enumType, text);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return fallback;
    }

    private static int readInt(Object value, int fallback) {
        return value instanceof Number number ? number.intValue() : fallback;
    }

    private static sizeEnum closestSize(int pixels) {
        sizeEnum closest = sizeEnum.SIXTEEN;
        int closestDistance = Math.abs(closest.pixels() - pixels);
        for (sizeEnum value : sizeEnum.values()) {
            int distance = Math.abs(value.pixels() - pixels);
            if (distance < closestDistance) {
                closest = value;
                closestDistance = distance;
            }
        }
        return closest;
    }

    private static int clamp(int value, int min, int max) {
        return value < min ? min : Math.min(value, max);
    }

    private static final class AutoWalkConfigScreen extends Screen {
        private static final int HANDLE_SIZE = 8;
        private static final int PREVIEW_MARGIN = 10;
        private static final int SNAP_TOLERANCE = 4;
        //? if >=1.21.11 {
        private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(DuperAutoWalk.MOD_ID, "textures/gui/autowalk.png");
        //?} else {
        /*private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(DuperAutoWalk.MOD_ID, "textures/gui/autowalk.png");
        *///?}

        private final Screen parent;
        private int previewX;
        private int previewY;
        private boolean dragging;
        private boolean resizing;
        private int dragOffsetX;
        private int dragOffsetY;
        private int resizeStartSize;
        private double resizeStartDistance;

        private Button feedbackButton;
        private Button positionButton;
        private Button sizeButton;

        private AutoWalkConfigScreen(Screen parent) {
            super(Component.translatable("config.duperautowalk.title"));
            this.parent = parent;
            this.previewX = coords_x;
            this.previewY = coords_y;
        }

        @Override
        protected void init() {
            syncPreviewWithPosition();

            int centerX = this.width / 2;
            int buttonWidth = 220;
            int buttonHeight = 20;
            int startY = this.height - 92;

            feedbackButton = addRenderableWidget(Button.builder(feedbackText(), button -> {
                setChatFeedback(!chatFeedback);
                button.setMessage(feedbackText());
            }).bounds(centerX - buttonWidth / 2, startY, buttonWidth, buttonHeight).build());

            positionButton = addRenderableWidget(Button.builder(positionText(), button -> {
                setPosition(next(position));
                syncPreviewWithPosition();
                button.setMessage(positionText());
            }).bounds(centerX - buttonWidth / 2, startY + 24, buttonWidth, buttonHeight).build());

            sizeButton = addRenderableWidget(Button.builder(sizeText(), button -> {
                setSize(next(size));
                syncPreviewWithPosition();
                button.setMessage(sizeText());
            }).bounds(centerX - buttonWidth / 2, startY + 48, buttonWidth, buttonHeight).build());

            addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose())
                    .bounds(centerX - buttonWidth / 2, startY + 72, buttonWidth, buttonHeight)
                    .build());
        }

        @Override
        public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
            super.render(context, mouseX, mouseY, delta);

            context.drawCenteredString(font, this.title, this.width / 2, 12, 0xFFFFFFFF);
            if (dragging && !isShiftCurrentlyDown()) {
                drawGuides(context);
            }
            drawPreview(context, mouseX, mouseY);
            context.drawCenteredString(font, Component.translatable("screen.duperautowalk.config.drag_help"), this.width / 2, this.height - 112, 0xFFAAAAAA);
            context.drawCenteredString(font, Component.translatable("screen.duperautowalk.config.coords", previewX, previewY), this.width / 2, this.height - 102, 0xFFCCCCCC);
        }

        //? if >=1.21.9 {
        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
            if (event.button() == 1 && isInsidePreview(event.x(), event.y())) {
                setHudFeedback(!hudFeedback);
                return true;
            }
            if (event.button() == 0 && isInsidePreview(event.x(), event.y())) {
                if (isInsideResizeHandle(event.x(), event.y())) {
                    startResize(event.x(), event.y());
                    return true;
                }
                dragging = true;
                dragOffsetX = (int) event.x() - previewX;
                dragOffsetY = (int) event.y() - previewY;
                return true;
            }
            return super.mouseClicked(event, doubled);
        }
        //?} else {
        /*@Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 1 && isInsidePreview(mouseX, mouseY)) {
                setHudFeedback(!hudFeedback);
                return true;
            }
            if (button == 0 && isInsidePreview(mouseX, mouseY)) {
                if (isInsideResizeHandle(mouseX, mouseY)) {
                    startResize(mouseX, mouseY);
                    return true;
                }
                dragging = true;
                dragOffsetX = (int) mouseX - previewX;
                dragOffsetY = (int) mouseY - previewY;
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }
        *///?}

        //? if >=1.21.9 {
        @Override
        public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
            if (resizing && event.button() == 0) {
                resizePreview(event.x(), event.y());
                return true;
            }
            if (dragging && event.button() == 0) {
                movePreview((int) event.x() - dragOffsetX, (int) event.y() - dragOffsetY, !isShiftCurrentlyDown());
                return true;
            }
            return super.mouseDragged(event, deltaX, deltaY);
        }
        //?} else {
        /*@Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
            if (resizing && button == 0) {
                resizePreview(mouseX, mouseY);
                return true;
            }
            if (dragging && button == 0) {
                movePreview((int) mouseX - dragOffsetX, (int) mouseY - dragOffsetY, !isShiftCurrentlyDown());
                return true;
            }
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
        *///?}

        //? if >=1.21.9 {
        @Override
        public boolean mouseReleased(MouseButtonEvent event) {
            if (dragging && event.button() == 0) {
                dragging = false;
                setCustomCoords(previewX, previewY);
                if (positionButton != null) {
                    positionButton.setMessage(positionText());
                }
                return true;
            }
            if (resizing && event.button() == 0) {
                resizing = false;
                setCustomCoords(previewX, previewY);
                setIconSize(iconSize);
                updateButtons();
                return true;
            }
            return super.mouseReleased(event);
        }
        //?} else {
        /*@Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            if (dragging && button == 0) {
                dragging = false;
                setCustomCoords(previewX, previewY);
                if (positionButton != null) {
                    positionButton.setMessage(positionText());
                }
                return true;
            }
            if (resizing && button == 0) {
                resizing = false;
                setCustomCoords(previewX, previewY);
                setIconSize(iconSize);
                updateButtons();
                return true;
            }
            return super.mouseReleased(mouseX, mouseY, button);
        }
        *///?}

        //? if >=1.21.9 {
        @Override
        public boolean keyPressed(KeyEvent event) {
            int step = (event.modifiers() & GLFW.GLFW_MOD_SHIFT) != 0 ? 5 : 1;
            boolean handled = false;
            if (event.key() == GLFW.GLFW_KEY_LEFT) {
                movePreview(previewX - step, previewY, false);
                handled = true;
            } else if (event.key() == GLFW.GLFW_KEY_RIGHT) {
                movePreview(previewX + step, previewY, false);
                handled = true;
            } else if (event.key() == GLFW.GLFW_KEY_UP) {
                movePreview(previewX, previewY - step, false);
                handled = true;
            } else if (event.key() == GLFW.GLFW_KEY_DOWN) {
                movePreview(previewX, previewY + step, false);
                handled = true;
            }

            if (handled) {
                setCustomCoords(previewX, previewY);
                if (positionButton != null) {
                    positionButton.setMessage(positionText());
                }
                return true;
            }
            return super.keyPressed(event);
        }
        //?} else {
        /*@Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            int step = (modifiers & GLFW.GLFW_MOD_SHIFT) != 0 ? 5 : 1;
            boolean handled = false;
            if (keyCode == GLFW.GLFW_KEY_LEFT) {
                movePreview(previewX - step, previewY, false);
                handled = true;
            } else if (keyCode == GLFW.GLFW_KEY_RIGHT) {
                movePreview(previewX + step, previewY, false);
                handled = true;
            } else if (keyCode == GLFW.GLFW_KEY_UP) {
                movePreview(previewX, previewY - step, false);
                handled = true;
            } else if (keyCode == GLFW.GLFW_KEY_DOWN) {
                movePreview(previewX, previewY + step, false);
                handled = true;
            }

            if (handled) {
                setCustomCoords(previewX, previewY);
                if (positionButton != null) {
                    positionButton.setMessage(positionText());
                }
                return true;
            }
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
        *///?}

        @Override
        public void onClose() {
            if (this.minecraft != null) {
                this.minecraft.setScreen(parent);
            }
        }

        private void drawPreview(GuiGraphics context, int mouseX, int mouseY) {
            int sizePixels = iconSize;
            int right = previewX + sizePixels;
            int bottom = previewY + sizePixels;
            boolean hovering = isInsidePreview(mouseX, mouseY);
            boolean resizingHover = isInsideResizeHandle(mouseX, mouseY);

            int backgroundColor = hudFeedback ? 0xAA2B2B2B : 0x552B2B2B;
            int borderColor = hudFeedback ? 0xFFFFFFFF : 0x88FFFFFF;
            context.fill(previewX, previewY, right, bottom, backgroundColor);
            context.fill(previewX, previewY, right, previewY + 1, borderColor);
            context.fill(previewX, bottom - 1, right, bottom, borderColor);
            context.fill(previewX, previewY, previewX + 1, bottom, borderColor);
            context.fill(right - 1, previewY, right, bottom, borderColor);
            context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, previewX, previewY, 0, 0, sizePixels, sizePixels, 16, 16, 16, 16, hudFeedback ? 0xFFFFFFFF : 0x75FFFFFF);

            if (hovering || dragging || resizing) {
                int handleX = right - HANDLE_SIZE / 2;
                int handleY = bottom - HANDLE_SIZE / 2;
                context.fill(handleX, handleY, handleX + HANDLE_SIZE, handleY + HANDLE_SIZE, resizingHover || resizing ? 0xFF40FF40 : 0xC020FF20);
            }
        }

        private void drawGuides(GuiGraphics context) {
            int centerX = this.width / 2;
            int centerY = this.height / 2;
            int sizePixels = iconSize;
            int previewCenterX = previewX + sizePixels / 2;
            int previewCenterY = previewY + sizePixels / 2;
            int previewRight = previewX + sizePixels;
            int previewBottom = previewY + sizePixels;

            if (Math.abs(previewCenterX - centerX) <= 3) {
                context.fill(centerX, 0, centerX + 1, this.height, 0x60FF4080);
            }
            if (Math.abs(previewCenterY - centerY) <= 3) {
                context.fill(0, centerY, this.width, centerY + 1, 0x60FF4080);
            }
            if (Math.abs(previewX - PREVIEW_MARGIN) <= SNAP_TOLERANCE) {
                context.fill(PREVIEW_MARGIN, 0, PREVIEW_MARGIN + 1, this.height, 0x70FFAA00);
            }
            if (Math.abs(previewRight - (this.width - PREVIEW_MARGIN)) <= SNAP_TOLERANCE) {
                int x = this.width - PREVIEW_MARGIN;
                context.fill(x, 0, x + 1, this.height, 0x70FFAA00);
            }
            if (Math.abs(previewY - PREVIEW_MARGIN) <= SNAP_TOLERANCE) {
                context.fill(0, PREVIEW_MARGIN, this.width, PREVIEW_MARGIN + 1, 0x70FFAA00);
            }
            if (Math.abs(previewBottom - (this.height - PREVIEW_MARGIN)) <= SNAP_TOLERANCE) {
                int y = this.height - PREVIEW_MARGIN;
                context.fill(0, y, this.width, y + 1, 0x70FFAA00);
            }
        }

        private void movePreview(int x, int y, boolean snap) {
            int maxX = Math.max(0, this.width - iconSize);
            int maxY = Math.max(0, this.height - iconSize);
            int nextX = clamp(x, 0, maxX);
            int nextY = clamp(y, 0, maxY);

            if (snap) {
                int centerX = this.width / 2 - iconSize / 2;
                int centerY = this.height / 2 - iconSize / 2;
                int rightX = this.width - PREVIEW_MARGIN - iconSize;
                int bottomY = this.height - PREVIEW_MARGIN - iconSize;
                nextX = snap(nextX, PREVIEW_MARGIN);
                nextX = snap(nextX, centerX);
                nextX = snap(nextX, rightX);
                nextY = snap(nextY, PREVIEW_MARGIN);
                nextY = snap(nextY, centerY);
                nextY = snap(nextY, bottomY);
            }

            previewX = clamp(nextX, 0, maxX);
            previewY = clamp(nextY, 0, maxY);
        }

        private boolean isInsidePreview(double mouseX, double mouseY) {
            int sizePixels = iconSize;
            return mouseX >= previewX && mouseX <= previewX + sizePixels && mouseY >= previewY && mouseY <= previewY + sizePixels;
        }

        private boolean isInsideResizeHandle(double mouseX, double mouseY) {
            int handleX = previewX + iconSize - HANDLE_SIZE / 2;
            int handleY = previewY + iconSize - HANDLE_SIZE / 2;
            return mouseX >= handleX && mouseX <= handleX + HANDLE_SIZE && mouseY >= handleY && mouseY <= handleY + HANDLE_SIZE;
        }

        private void startResize(double mouseX, double mouseY) {
            resizing = true;
            resizeStartSize = iconSize;
            double dx = mouseX - previewX;
            double dy = mouseY - previewY;
            resizeStartDistance = Math.max(1.0, Math.sqrt(dx * dx + dy * dy));
        }

        private void resizePreview(double mouseX, double mouseY) {
            double dx = mouseX - previewX;
            double dy = mouseY - previewY;
            double distance = Math.sqrt(dx * dx + dy * dy);
            int nextSize = clamp((int) Math.round(resizeStartSize * (distance / resizeStartDistance)), MIN_ICON_SIZE, MAX_ICON_SIZE);
            iconSize = nextSize;
            size = closestSize(iconSize);
            movePreview(previewX, previewY, false);
            updateButtons();
        }

        private void syncPreviewWithPosition() {
            int sizePixels = iconSize;
            int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            switch (position) {
                case TOP_LEFT -> {
                    previewX = 0;
                    previewY = 1;
                }
                case TOP_RIGHT -> {
                    previewX = screenWidth - sizePixels - 1;
                    previewY = 1;
                }
                case MIDDLE_LEFT -> {
                    previewX = 0;
                    previewY = screenHeight / 2 - sizePixels / 2;
                }
                case MIDDLE_RIGHT -> {
                    previewX = screenWidth - sizePixels - 1;
                    previewY = screenHeight / 2 - sizePixels / 2;
                }
                case BOTTOM_LEFT -> {
                    previewX = 0;
                    previewY = screenHeight - sizePixels - 1;
                }
                case BOTTOM_RIGHT -> {
                    previewX = screenWidth - sizePixels - 1;
                    previewY = screenHeight - sizePixels - 1;
                }
                case CUSTOM -> {
                    previewX = coords_x;
                    previewY = coords_y;
                }
            }
            movePreview(previewX, previewY, false);
        }

        private Component feedbackText() {
            return Component.translatable("screen.duperautowalk.config.chat_feedback", Component.translatable(chatFeedback ? "duperautowalk.toggle.on" : "duperautowalk.toggle.off"));
        }

        private Component positionText() {
            return Component.translatable("screen.duperautowalk.config.position", Component.translatable("duperautowalk.position." + position.name().toLowerCase()));
        }

        private Component sizeText() {
            return Component.translatable("screen.duperautowalk.config.size", iconSize, iconSize);
        }

        private void updateButtons() {
            if (feedbackButton != null) {
                feedbackButton.setMessage(feedbackText());
            }
            if (positionButton != null) {
                positionButton.setMessage(positionText());
            }
            if (sizeButton != null) {
                sizeButton.setMessage(sizeText());
            }
        }

        private static <T extends Enum<T>> T next(T current) {
            T[] values = current.getDeclaringClass().getEnumConstants();
            return values[(current.ordinal() + 1) % values.length];
        }

        private static int clamp(int value, int min, int max) {
            return value < min ? min : Math.min(value, max);
        }

        private static int snap(int value, int target) {
            return Math.abs(value - target) <= SNAP_TOLERANCE ? target : value;
        }

        private static boolean isShiftCurrentlyDown() {
            //? if >=1.21.9 {
            long handle = Minecraft.getInstance().getWindow().handle();
            //?} else {
            /*long handle = Minecraft.getInstance().getWindow().getWindow();
            *///?}
            return GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS
                    || GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
        }
    }
}
