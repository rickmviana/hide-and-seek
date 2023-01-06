package cz.ragy.hideandseek;

import cz.ragy.hideandseek.managers.StartupManager;
import cz.ragy.hideandseek.menusystem.PlayerMenuUtility;
import games.negative.framework.BasePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

import static org.bukkit.Bukkit.getPluginManager;

public final class HideAndSeek extends BasePlugin {
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    public static HideAndSeek instance;
    @Override
    public void onEnable() {
        instance = this;
        super.onEnable();
        getLogger().info("Getting depends");
        Plugin placeholderAPI = getPluginManager().getPlugin("PlaceholderAPI");
        if (placeholderAPI != null && placeholderAPI.isEnabled()) {
            new StartupManager().startup(getServer(), this, this);
        } else {
            getLogger().warning("Please install and enable PlaceholderAPI for the Hide And Seek Plugin to work.");
            getPluginManager().disablePlugin(this);
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
    }
    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p))) {
            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);
            return playerMenuUtility;
        } else { return playerMenuUtilityMap.get(p); }

    }
}
