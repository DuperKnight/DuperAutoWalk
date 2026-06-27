package com.dupernite.duperautowalk.compat;

import com.dupernite.duperautowalk.config.AutoWalkConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuAPI implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return AutoWalkConfig::createScreen;
    }
}
