package io.github.lukeeey.simplequests.quest.handler;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

public interface QuestHandler extends Listener {
    void loadCustomOptions(ConfigurationSection config);

    interface QuestCustomOptions {

    }
}
