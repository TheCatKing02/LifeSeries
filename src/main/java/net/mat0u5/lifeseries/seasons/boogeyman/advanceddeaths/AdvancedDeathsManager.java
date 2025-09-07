package net.mat0u5.lifeseries.seasons.boogeyman.advanceddeaths;

import net.mat0u5.lifeseries.utils.player.PlayerUtils;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static net.mat0u5.lifeseries.Main.livesManager;

public class AdvancedDeathsManager {
    private static int lastDeathIndex = new Random().nextInt(1000);
    private static final Map<UUID, PlayerAdvancedDeath> queuedDeaths = new HashMap<>();

    public static void tick() {
        if (queuedDeaths.isEmpty()) return;
        List<UUID> toRemove = new ArrayList<>();
        for (Map.Entry<UUID, PlayerAdvancedDeath> entry : queuedDeaths.entrySet()) {
            UUID uuid = entry.getKey();
            PlayerAdvancedDeath playerAdvancedDeath = entry.getValue();

            ServerPlayerEntity player = PlayerUtils.getPlayer(uuid);
            if (player == null || playerAdvancedDeath.queuedDeaths().isEmpty() ||
                    Objects.equals(livesManager.getPlayerLives(player), playerAdvancedDeath.lives())) {
                toRemove.add(uuid);
                continue;
            }
            playerAdvancedDeath.queuedDeaths().getFirst().onTick();
        }
        for (UUID uuid : toRemove) {
            PlayerAdvancedDeath playerAdvancedDeath = queuedDeaths.get(uuid);
            livesManager.setScore(playerAdvancedDeath.nameForScoreboard(), playerAdvancedDeath.lives());
            playerAdvancedDeath.queuedDeaths().forEach(AdvancedDeath::onEnd);
            queuedDeaths.remove(uuid);
        }
    }

    public static void setPlayerLives(ServerPlayerEntity player, int lives) {
        Integer currentLives = livesManager.getPlayerLives(player);
        if (currentLives == null || currentLives <= lives) {
            livesManager.setPlayerLives(player, lives);
            return;
        }

        List<AdvancedDeath> queuedPlayerDeaths = new ArrayList<>();
        for (int i = currentLives; i > lives; i--) {
            AdvancedDeath death = getPseudoRandomDeath(player);
            if (death != null) {
                queuedPlayerDeaths.add(death);
            }
        }
        if (queuedPlayerDeaths.isEmpty()) {
            livesManager.setPlayerLives(player, lives);
            return;
        }

        queuedDeaths.put(player.getUuid(), new PlayerAdvancedDeath(player.getNameForScoreboard(), lives, queuedPlayerDeaths));
    }

    public static void onPlayerDeath(ServerPlayerEntity player) {
        if (queuedDeaths.isEmpty()) return;
        for (Map.Entry<UUID, PlayerAdvancedDeath> entry : queuedDeaths.entrySet()) {
            UUID uuid = entry.getKey();
            if (uuid != player.getUuid()) continue;
            PlayerAdvancedDeath playerAdvancedDeath = entry.getValue();
            playerAdvancedDeath.queuedDeaths().getFirst().onEnd();
            playerAdvancedDeath.queuedDeaths().removeFirst();
        }
    }

    @Nullable
    private static AdvancedDeath getPseudoRandomDeath(ServerPlayerEntity player) {
        List<AdvancedDeaths> advancedDeaths = AdvancedDeaths.getAllDeaths();
        lastDeathIndex = (lastDeathIndex + 1) % advancedDeaths.size();
        return advancedDeaths.get(lastDeathIndex).getInstance(player);
    }

    public record PlayerAdvancedDeath(String nameForScoreboard, int lives, List<AdvancedDeath> queuedDeaths) {}
}
