package net.mat0u5.lifeseries.seasons.boogeyman.advanceddeaths;

import net.mat0u5.lifeseries.utils.player.PlayerUtils;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public abstract class AdvancedDeath {
    protected UUID playerUUID;
    protected boolean started = false;
    protected boolean finished = false;
    protected long ticks = -100;
    public AdvancedDeath(ServerPlayerEntity player) {
        this.playerUUID = player.getUuid();
    }

    protected abstract void tick(ServerPlayerEntity player);
    protected abstract void begin(ServerPlayerEntity player);
    protected abstract void end();
    protected abstract int maxTime();
    protected abstract DamageSource damageSource(ServerPlayerEntity player);

    public ServerPlayerEntity getPlayer() {
        return PlayerUtils.getPlayer(playerUUID);
    }

    public void onTick() {
        if (playerNotFound()) return;
        ServerPlayerEntity player = getPlayer();

        ticks++;

        if (ticks >= maxTime()) {
            ranOutOfTime(player);
            onEnd();
            return;
        }

        if (ticks >= 0) {
            if (!started) {
                started = true;
                begin(player);
            }
            tick(player);
        }
    }

    public boolean playerNotFound() {
        ServerPlayerEntity player = getPlayer();
        return player == null || !player.isAlive();
    }

    public void onEnd() {
        finished = true;
        end();
    }

    public void ranOutOfTime(ServerPlayerEntity player) {
        PlayerUtils.killFromSource(player, damageSource(player));
    }

    public boolean isFinished() {
        return finished;
    }
}
