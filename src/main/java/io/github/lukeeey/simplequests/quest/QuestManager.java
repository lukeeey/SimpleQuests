package io.github.lukeeey.simplequests.quest;

import io.github.lukeeey.simplequests.SimpleQuestsPlugin;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class QuestManager {
    private final Map<String, Quest> quests = new LinkedHashMap<>();
    private final SimpleQuestsPlugin plugin;

    public QuestManager(SimpleQuestsPlugin plugin) {
        this.plugin = plugin;
        loadQuests();
    }

    /**
     * Load quests from the config.
     */
    private void loadQuests() {
        ConfigurationSection questSection = plugin.getConfig().getConfigurationSection("quests");
        if (questSection == null) {
            throw new RuntimeException("Failed to load quests: 'quests' section missing from config");
        }
        Set<String> questIds = questSection.getKeys(false);
        questIds.forEach(id -> {
            ConfigurationSection questData = questSection.getConfigurationSection(id);
            QuestType type = QuestType.valueOf(questData.getString("type"));
            String name = questData.getString("name");
            int amount = questData.getInt("amount");
            List<String> rewards = questData.getStringList("rewards");

            quests.put(id, new Quest(id, type, name, amount, rewards));
        });
        plugin.getLogger().info("Loaded " + quests.size() + " quests!");
    }

    public Quest getQuest(String id) {
        return quests.get(id);
    }

    public Quest getNextQuest(Quest quest) {
        List<String> ids = new ArrayList<>(quests.keySet());
        try {
            for (int i = 0; i < ids.size(); i++) {
                if (ids.get(i).equalsIgnoreCase(quest.getId())) {
                    return quests.get(ids.get(i + 1));
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }

    public Quest getFirstQuest() {
        return new ArrayList<>(quests.values()).get(0);
    }
}