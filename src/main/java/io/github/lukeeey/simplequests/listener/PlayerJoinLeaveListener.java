package io.github.lukeeey.simplequests.listener;

import io.github.lukeeey.simplequests.SimpleQuestsPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class PlayerJoinLeaveListener implements Listener {
    private final SimpleQuestsPlugin plugin;

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        plugin.getDatabaseProvider().onPlayerLogin(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getDatabaseProvider().onPlayerQuit(event.getPlayer());
    }
}
