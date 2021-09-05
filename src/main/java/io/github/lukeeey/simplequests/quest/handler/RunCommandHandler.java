package io.github.lukeeey.simplequests.quest.handler;

import io.github.lukeeey.simplequests.player.SimpleQuestsPlayer;
import io.github.lukeeey.simplequests.quest.Quest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RunCommandHandler implements QuestHandler {
    private final Quest quest;

    // TODO: Abstract this
    private RunCommandCustomOptions options;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String[] split = event.getMessage().split(" ");
        SimpleQuestsPlayer simpleQuestsPlayer = SimpleQuestsPlayer.of(event.getPlayer());

        if (simpleQuestsPlayer.getCurrentQuest() != quest) {
            // The players current quest is not this one!
            return;
        }
        if (!options.getCommands().isEmpty()) {
            String command = split[0].substring(1);
            Optional<String> configCommandOpt = options.getCommands().stream().filter(c -> c.equalsIgnoreCase(command)).findFirst();

            if (configCommandOpt.isPresent()) {
                String configCommand = configCommandOpt.get();
                if (configCommand.startsWith("regex:")) {
                    // TODO: Regex parsing?
                }
            } else {
                return;
            }
        }
        simpleQuestsPlayer.progress();
    }

    @Override
    public void loadCustomOptions(ConfigurationSection config) {
        options = new RunCommandCustomOptions(config.getStringList("commands"));
    }

    @Data
    public static class RunCommandCustomOptions implements QuestCustomOptions {
        private final List<String> commands;
    }
}