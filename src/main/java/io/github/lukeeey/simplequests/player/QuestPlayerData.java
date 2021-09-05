package io.github.lukeeey.simplequests.player;

import io.github.lukeeey.simplequests.quest.Quest;
import lombok.Data;

@Data
public class QuestPlayerData {
    private final Quest quest;
    private int amount;
}