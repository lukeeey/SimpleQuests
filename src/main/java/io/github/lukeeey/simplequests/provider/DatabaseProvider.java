package io.github.lukeeey.simplequests.provider;

import io.github.lukeeey.simplequests.SimpleQuestsPlugin;
import io.github.lukeeey.simplequests.player.SimpleQuestsPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class DatabaseProvider {
    protected final Map<UUID, SimpleQuestsPlayer> players = new HashMap<>();

    public void onPlayerLogin(Player player) {
        fetchPlayerData(player).whenComplete((simpleQuestsPlayer, throwable) ->
                players.put(simpleQuestsPlayer.getPlayer().getUniqueId(), simpleQuestsPlayer));
    }

    public void onPlayerQuit(Player player) {
        savePlayerData(player);
        players.remove(player.getUniqueId());
    }

    public SimpleQuestsPlayer getPlayer(Player player) {
        return players.get(player.getUniqueId());
    }

    public void saveAllPlayerData() {
        players.forEach((uuid, player) -> savePlayerData(player.getPlayer()));
    }

    public abstract CompletableFuture<SimpleQuestsPlayer> fetchPlayerData(Player player);

    public abstract void savePlayerData(Player player);
}
