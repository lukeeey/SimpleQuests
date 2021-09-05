package io.github.lukeeey.simplequests.player;

import com.google.common.base.Preconditions;
import io.github.lukeeey.simplequests.SimpleQuestsPlugin;
import io.github.lukeeey.simplequests.quest.Quest;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Data
public class SimpleQuestsPlayer {
    private final Player player;

    private List<Quest> completedQuests = new ArrayList<>();
    private Quest currentQuest;
    private int currentProgress;

    public static SimpleQuestsPlayer of(Player player) {
        return SimpleQuestsPlugin.getInstance().getDatabaseProvider().getPlayer(player);
    }

    public void onQuestComplete() {
        player.sendTitle("Quest Completed!", "");
        currentQuest.getRewards().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                command.replace("%player_name%", player.getName())));

        completedQuests.add(currentQuest);
        setCurrentQuest(SimpleQuestsPlugin.getInstance().getQuestManager().getNextQuest(currentQuest));
        currentProgress = 0;
    }

    public void setCurrentQuest(Quest quest) {
        if (quest != null) {
            player.sendMessage("Starting Quest: " + quest.getName());
        }
        currentQuest = quest;
    }

    public void progress() {
        if (currentQuest == null) {
            return;
        }
        if (currentProgress == currentQuest.getAmount()) {
            onQuestComplete();
        } else {
            currentProgress++;
        }
        player.sendMessage("Current Progress: " + currentProgress + "/" + currentQuest.getAmount()  + " (" + getCurrentProgressPercentage() + "%)");

    }

    public float getCurrentProgressPercentage() {
        return ((float) currentProgress / (float) currentQuest.getAmount()) * 100;
    }
}