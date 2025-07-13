package com.dupernite.duperautowalk.client;

import com.dupernite.duperautowalk.event.keyInputHandler;
import net.fabricmc.api.ClientModInitializer;

public class DuperAutoWalkClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        keyInputHandler.register();
        //? if >=1.21.6 {
        AutoWalkOverlay.register();
        //?} else {
        /*HudRenderCallback.EVENT.register(new AutoWalkOverlay());
        *///?}
    }
}
