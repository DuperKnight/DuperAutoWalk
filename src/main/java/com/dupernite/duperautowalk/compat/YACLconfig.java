package com.dupernite.duperautowalk.compat;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.gui.controllers.cycling.EnumController;
import dev.isxander.yacl3.gui.controllers.string.number.IntegerFieldController;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.dupernite.duperautowalk.DuperAutoWalk.MOD_ID;
import static dev.isxander.yacl3.platform.YACLPlatform.getConfigDir;

public final class YACLconfig {
    public static final ConfigClassHandler<YACLconfig> GSON = ConfigClassHandler.createBuilder(YACLconfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(getConfigDir().resolve("duperautowalk.json"))
                    .setJson5(true)
                    .build())
            .build();

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
        EIGHT,
        SIXTEEN,
        THIRTY_TWO,
        FORTY_EIGHT,
        SIXTY_FOUR
    }

    /*@SerialEntry
    public static boolean enabled = true*/
    @SerialEntry
    public static positionEnum position = positionEnum.TOP_LEFT;

    @SerialEntry
    public static feedbackEnum feedback = feedbackEnum.HUD;

    @SerialEntry
    public static sizeEnum size = sizeEnum.SIXTEEN;

    @SerialEntry
    public static int coords_x = 16;

    @SerialEntry
    public static int coords_y = 16;

    public static Screen createScreen(Screen parent) {
        return YetAnotherConfigLib.create(YACLconfig.GSON, ((defaults, config, builder) -> {
            var defaultCategoryBuilder = ConfigCategory.createBuilder()
                    .name(Text.translatable("category.duperautowalk.general"));

            var positionCateoryBuilder = ConfigCategory.createBuilder()
                    .name(Text.translatable("category.duperautowalk.position"));

            var feedback = createOptionWithImage(
                    "option.duperautowalk.feedback",
                    "option.duperautowalk.feedback.description",
                    "feedback",
                    YACLconfig.feedback,
                    () -> YACLconfig.feedback,
                    val -> YACLconfig.feedback = val,
                    enumOption -> new EnumController<>(enumOption, v -> Text.translatable(MOD_ID + ".feedback." + v.toString().toLowerCase()), feedbackEnum.values())
            );

            var position = createOptionWithImage(
                    "option.duperautowalk.position",
                    "option.duperautowalk.position.description",
                    "position",
                    YACLconfig.position,
                    () -> YACLconfig.position,
                    val -> YACLconfig.position = val,
                    enumOption -> new EnumController<>(enumOption, v -> Text.translatable(MOD_ID + ".position." + v.toString().toLowerCase()), positionEnum.values())
            );

            var size = createOption(
                    "option.duperautowalk.size",
                    "option.duperautowalk.size.description",
                    YACLconfig.size,
                    () -> YACLconfig.size,
                    val -> YACLconfig.size = val,
                    enumOption -> new EnumController<>(enumOption, v -> Text.translatable(MOD_ID + ".size." + v.toString().toLowerCase()), sizeEnum.values())
            );

            var coords_x = createOption(
                    "option.duperautowalk.coordinates_x",
                    "option.duperautowalk.coordinates_x.description",
                    YACLconfig.coords_x,
                    () -> YACLconfig.coords_x,
                    val -> YACLconfig.coords_x = val,
                    intOption -> new IntegerFieldController(intOption, 0, MinecraftClient.getInstance().getWindow().getScaledWidth())
            );

            var coords_y = createOption(
                    "option.duperautowalk.coordinates_y",
                    "option.duperautowalk.coordinates_y.description",
                    YACLconfig.coords_y,
                    () -> YACLconfig.coords_y,
                    val -> YACLconfig.coords_y = val,
                    intOption -> new IntegerFieldController(intOption, 0, MinecraftClient.getInstance().getWindow().getScaledWidth())
            );

            return builder
                    .title(Text.translatable("config.duperautowalk.title"))
                    .categories(
                            List.of(
                                    defaultCategoryBuilder.options(List.of(feedback, size)).build(),
                                    positionCateoryBuilder.options(List.of(position, coords_x, coords_y)).build()
                            )
                    );

        })).generateScreen(parent);
    }

    private static <T> Option<T> createOptionWithImage(
            String name,
            String description,
            String type,
            T defaultValue,
            Supplier<T> currentValue,
            Consumer<T> newValue,
            Function<Option<T>, Controller<T>> customController
    ) {
        return Option.<T>createBuilder()
                .name(Text.translatable(name))
                .description(
                        OptionDescription.createBuilder()
                                .webpImage(screenshot(type))
                                .text(Text.translatable(description))
                                .build()
                )
                .binding(defaultValue, currentValue, newValue)
                .customController(customController)
                .build();
    }

    private static <T> Option<T> createOption(
            String name,
            String description,
            T defaultValue,
            Supplier<T> currentValue,
            Consumer<T> newValue,
            Function<Option<T>, Controller<T>> customController
    ) {
        return Option.<T>createBuilder()
                .name(Text.translatable(name))
                .description(
                        OptionDescription.createBuilder()
                                .text(Text.translatable(description))
                                .build()
                )
                .binding(defaultValue, currentValue, newValue)
                .customController(customController)
                .build();
    }

    public static feedbackEnum getFeedback(){
        return feedback;
    }

    public static positionEnum getPosition(){
        return position;
    }

    public static sizeEnum getSize(){
        return size;
    }

    public static int getCoords_x(){
        return coords_x;
    }

    public static int getCoords_y(){
        return coords_y;
    }

    public static Identifier screenshot(String type) {
            if (Objects.equals(type, "position")) {
                return Identifier.of(MOD_ID, "textures/screenshots/position/" + position.toString().toLowerCase() + ".webp");
            } else if (Objects.equals(type, "feedback")) {
                return Identifier.of(MOD_ID, "textures/screenshots/feedback/" + feedback.toString().toLowerCase() + ".webp");
            }
        return null;
    }
}