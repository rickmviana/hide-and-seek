package cz.ragy.hideandseek.managers;

import cz.ragy.hideandseek.game.Arena;
import cz.ragy.hideandseek.HideAndSeek;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigManager {
    public static final File configFile = new File(HideAndSeek.instance.getDataFolder(), "config.yml");
    public static final File arenasFile = new File(HideAndSeek.instance.getDataFolder(), "arenas.yml");
    public FileConfiguration confik = YamlConfiguration.loadConfiguration(configFile);
    public static YamlConfiguration arenas;
    public static YamlConfiguration config;
    public String setArena;
    public String creating = (String) confik.get("Create-Arena.Creating-Arena");
    public String arenaCreated = (String) confik.get("Create-Arena.Created");
    public String arenaExists = (String) confik.get("Create-Arena.Arena-Exists");
    public void startup() {
        if(!configFile.exists()) {
            HideAndSeek.instance.saveResource("config.yml", true);
        }
        if(!arenasFile.exists()){
            HideAndSeek.instance.saveResource("arenas.yml", true);
        }
        arenas = new YamlConfiguration().loadConfiguration(arenasFile);
        config = new YamlConfiguration().loadConfiguration(configFile);

    }
    public void saveArenasToConfig(List<Arena> arenaList, CommandSender sender){

        if (arenas.getConfigurationSection("arenas") == null) {
            arenas.createSection("arenas");
            try {
                arenas.save(arenasFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ConfigurationSection parentSection = arenas.getConfigurationSection("arenas");

        for (Arena arena : arenaList) {
            if (parentSection.getConfigurationSection(arena.arenaName) == null) {
                ConfigurationSection childSection = parentSection.createSection(arena.arenaName);
                setArena = arenaCreated;
                setArena = setArena.replace("%arena%", arena.arenaName);
                childSection.set("ArenaWorld", arena.arenaWorldName);
                childSection.set("ArenaMaxPlayers", arena.maxPlayers);
                childSection.set("ArenaMinPlayers", arena.minPlayers);
                childSection.set("ArenaSeekersCount", arena.seekersCount);

                sender.sendMessage("Arena: " + arena.arenaName);
                sender.sendMessage("World: " + arena.arenaWorldName);
                sender.sendMessage("Max Players: " + arena.maxPlayers);
                sender.sendMessage("Min Players: " + arena.minPlayers);
                sender.sendMessage("Seekers: " + arena.seekersCount);
                sender.sendMessage(creating);
                sender.sendMessage(setArena);
            } else {
                sender.sendMessage(arenaExists);
                continue;
            }
        }
        try {
            arenas.save(arenasFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void reloadAllConfigs(){
        Bukkit.getPluginManager().disablePlugin(HideAndSeek.instance);
        Bukkit.getPluginManager().enablePlugin(HideAndSeek.instance);
    }
}
