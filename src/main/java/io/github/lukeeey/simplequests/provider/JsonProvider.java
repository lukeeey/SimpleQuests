package io.github.lukeeey.simplequests.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import io.github.lukeeey.simplequests.SimpleQuestsPlugin;
import io.github.lukeeey.simplequests.player.SimpleQuestsPlayer;
import io.github.lukeeey.simplequests.quest.Quest;
import lombok.Data;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class JsonProvider extends DatabaseProvider {
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();

    private final SimpleQuestsPlugin plugin;
    private final Path dataFolderPath;

    public JsonProvider(SimpleQuestsPlugin plugin) {
        this.plugin = plugin;
        this.dataFolderPath = plugin.getDataFolder().toPath().resolve("data");

        ensureDirectoryExists();
    }

    private void ensureDirectoryExists() {
        try {
            if (!Files.exists(dataFolderPath)) {
                Files.createDirectory(dataFolderPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create quests player data directory", e);
        }
    }

    @Override
    public CompletableFuture<SimpleQuestsPlayer> fetchPlayerData(Player player) {
        File file = new File(dataFolderPath.toFile(), player.getUniqueId() + ".json");
        if (!file.exists()) {
            SimpleQuestsPlayer newPlayer = new SimpleQuestsPlayer(player);
            newPlayer.setCurrentQuest(plugin.getQuestManager().getFirstQuest());
            return CompletableFuture.completedFuture(newPlayer);
        }
        try {
            JsonPlayerData data = GSON.fromJson(new JsonReader(new FileReader(file)), JsonPlayerData.class);
            SimpleQuestsPlayer simpleQuestsPlayer = new SimpleQuestsPlayer(player);
            simpleQuestsPlayer.setCurrentQuest(plugin.getQuestManager().getQuest(data.getCurrentQuest()));
            simpleQuestsPlayer.setCurrentProgress(data.getCurrentProgress());
            simpleQuestsPlayer.setCompletedQuests(data.getCompletedQuests().stream()
                    .map(id -> plugin.getQuestManager().getQuest(id))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));

            return CompletableFuture.completedFuture(simpleQuestsPlayer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void savePlayerData(Player player) {
        try {
            Path path = dataFolderPath.resolve(player.getUniqueId() + ".json");
            SimpleQuestsPlayer simpleQuestsPlayer = getPlayer(player);
            JsonPlayerData data = new JsonPlayerData(
                    simpleQuestsPlayer.getCompletedQuests().stream().map(Quest::getId).collect(Collectors.toList()),
                    simpleQuestsPlayer.getCurrentQuest() != null ? simpleQuestsPlayer.getCurrentQuest().getId() : null,
                    simpleQuestsPlayer.getCurrentProgress()
            );
            Files.write(path, GSON.toJson(data).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    private static class JsonPlayerData {
        private final List<String> completedQuests;
        private final String currentQuest;
        private final int currentProgress;
    }
}
