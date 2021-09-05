package io.github.lukeeey.simplequests.event;

import io.github.lukeeey.simplequests.quest.Quest;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

@Data
public class QuestStartEvent extends QuestEvent {
    private static final HandlerList handlers = new HandlerList();

    private final Quest quest;
    private final Player player;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
