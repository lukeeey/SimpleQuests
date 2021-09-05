package io.github.lukeeey.simplequests.quest;

import io.github.lukeeey.simplequests.SimpleQuestsPlugin;
import io.github.lukeeey.simplequests.quest.handler.QuestHandler;
import lombok.Data;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Data
public class Quest {
    private final QuestHandler handler;

    private final String id;
    private final QuestType type;
    private final String name;
    private final int amount;
    private final List<String> rewards;

    public Quest(String id, QuestType type, String name, int amount, List<String> rewards) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.amount = amount;
        this.rewards = rewards;
        try {
            this.handler = type.getHandlerClass().getDeclaredConstructor(Quest.class).newInstance(this);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate quest handler class " + type.getHandlerClass().getSimpleName(), e);
        }
        handler.loadCustomOptions(SimpleQuestsPlugin.getInstance().getConfig().getConfigurationSection("quests." + id + ".options"));
        Bukkit.getPluginManager().registerEvents(handler, SimpleQuestsPlugin.getInstance());
    }
}