package com.dupernite.duperautowalk.mixin;

import net.neoforged.neoforge.client.gui.ModListScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(value = ModListScreen.class, remap = false)
public abstract class NeoForgeModListScreenMixin {
    @Redirect(
            method = "reloadMods",
            at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;toList()Ljava/util/List;"),
            require = 0
    )
    private List<?> duperautowalk$makeReloadedModListMutable(Stream<?> stream) {
        return stream.collect(Collectors.toCollection(ArrayList::new));
    }
}
