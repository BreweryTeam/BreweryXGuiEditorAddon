package dev.jsinco.brewery;

import com.dre.brewery.api.addons.AddonInfo;
import com.dre.brewery.api.addons.AddonLogger;
import com.dre.brewery.api.addons.BreweryAddon;
import dev.jsinco.brewery.commands.AddonCommandManager;

@AddonInfo(name = "BreweryXGuiEditorAddon", author = "Jsinco", version = "REV-2.0", description = "A GUI editor for BreweryX")
public class BreweryXGuiEditorAddon extends BreweryAddon {

    private static AddonLogger logger;

    @Override
    public void onAddonEnable() {
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
        } catch (ClassNotFoundException fail) {
            getAddonLogger().info("&cThis addon requires Paper to function properly. Disabling.");
            getAddonManager().unloadAddon(this);
            return;
        }


        registerListener(new Events());
        registerCommand("gui", new AddonCommandManager());
        getAddonLogger().info("BreweryXGuiEditorAddon enabled!");
        logger = getAddonLogger();
    }

    public static AddonLogger getLogger() {
        return logger;
    }
}