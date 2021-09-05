package io.github.lukeeey.simplequests.quest.handler;

import io.github.lukeeey.simplequests.player.SimpleQuestsPlayer;
import io.github.lukeeey.simplequests.quest.Quest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;

@RequiredArgsConstructor
public class PlaceBlockHandler implements QuestHandler {
    private final Quest quest;

    // TODO: Abstract this
    private PlaceBlockCustomOptions options;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        SimpleQuestsPlayer simpleQuestsPlayer = SimpleQuestsPlayer.of(event.getPlayer());
        if (simpleQuestsPlayer.getCurrentQuest() != quest) {
            // The players current quest is not this one!
            return;
        }
        if (!options.getBlocks().isEmpty() && !options.getBlocks().contains(event.getBlock().getType().toString())) {
            // They specified specific blocks, and the one we placed wasn't one of them!
            return;
        }
        simpleQuestsPlayer.progress();
    }

    @Override
    public void loadCustomOptions(ConfigurationSection config) {
        options = new PlaceBlockCustomOptions(config.getStringList("blocks"));
    }

    @Data
    public static class PlaceBlockCustomOptions implements QuestCustomOptions {
        private final List<String> blocks;
    }
}