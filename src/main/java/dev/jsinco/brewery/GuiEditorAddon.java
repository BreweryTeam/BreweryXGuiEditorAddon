package dev.jsinco.brewery;

import com.dre.brewery.api.addons.AddonInfo;
import com.dre.brewery.api.addons.BreweryAddon;
import dev.jsinco.brewery.commands.AddonCommandManager;

@AddonInfo(name = "GuiEditor", author = "Jsinco", version = "BX3.4.3-SNAPSHOT", description = "A GUI editor for modifying and adding potions BreweryX")
public class GuiEditorAddon extends BreweryAddon {

    // TODO: Add permissions for specific actions?

    private static GuiEditorAddon instance;

    @Override
    public void onAddonEnable() {
        if (!isPaper()) {
            getAddonLogger().info("&cThis addon requires Paper to function. If you're currently running Spigot, I highly suggest moving over to Paper or a fork of Paper.");
            getAddonManager().unloadAddon(this);
            return;
        }

        instance = this;
        registerListener(new Events());
        registerCommand("gui", new AddonCommandManager());
    }

    public static GuiEditorAddon getInstance() {
        return instance;
    }
}