package com.dupernite.duperautowalk.client;

import com.dupernite.duperautowalk.event.keyInputHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class DuperAutoWalkClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        keyInputHandler.register();
        HudRenderCallback.EVENT.register(new AutoWalkOverlay());
    }
}
