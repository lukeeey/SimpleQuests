package io.github.lukeeey.simplequests;

import io.github.lukeeey.simplequests.listener.PlayerJoinLeaveListener;
import io.github.lukeeey.simplequests.provider.DatabaseProvider;
import io.github.lukeeey.simplequests.provider.JsonProvider;
import io.github.lukeeey.simplequests.quest.QuestManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SimpleQuestsPlugin extends JavaPlugin {
    @Getter
    private static SimpleQuestsPlugin instance;

    private QuestManager questManager;
    private DatabaseProvider databaseProvider;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        questManager = new QuestManager(this);

        switch (getConfig().getString("database.provider").toLowerCase()) {
            case "json":
            default:
                databaseProvider = new JsonProvider(this);
                break;
        }

        getServer().getPluginManager().registerEvents(new PlayerJoinLeaveListener(this), this);
    }

    @Override
    public void onDisable() {
        databaseProvider.saveAllPlayerData();
    }
}
