package io.github.lukeeey.simplequests.quest.handler;

import io.github.lukeeey.simplequests.SimpleQuestsPlugin;
import io.github.lukeeey.simplequests.player.SimpleQuestsPlayer;
import io.github.lukeeey.simplequests.quest.Quest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class KillMobHandler implements QuestHandler {
    private final Quest quest;

    // TODO: Abstract this
    private KillMobCustomOptions options;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent && getRootDamager(
                ((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager()) instanceof Player) {

            SimpleQuestsPlayer simpleQuestsPlayer = SimpleQuestsPlayer.of(getRootDamager(((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager()));
            if (simpleQuestsPlayer.getCurrentQuest() != quest) {
                // The players current quest is not this one!
                return;
            }
            if (!options.getEntityTypes().isEmpty() && !options.getEntityTypes().contains(event.getEntity().getType())) {
                // They specified specific entity types, and the one we hit wasn't one of them!
                return;
            }
            simpleQuestsPlayer.progress();
        }
    }

    @Override
    public void loadCustomOptions(ConfigurationSection config) {
        options = new KillMobCustomOptions(
                config.getStringList("entity-types").stream().map(EntityType::valueOf).collect(Collectors.toList()));
    }

    /**
     * Returns the root damager in a tree of damagers.
     *
     * @param damager the damager
     * @return the root damager
     */
    private Player getRootDamager(Entity damager) {
        if (damager instanceof Projectile) {
            Projectile projectile = (Projectile) damager;
            if (projectile.getShooter() instanceof Player) {
                return (Player) projectile.getShooter();
            }
        }
        else if (damager instanceof Player) {
            return (Player) damager;
        }
        return null;
    }

    @Data
    public static class KillMobCustomOptions implements QuestCustomOptions {
        private final List<EntityType> entityTypes;
    }
}
