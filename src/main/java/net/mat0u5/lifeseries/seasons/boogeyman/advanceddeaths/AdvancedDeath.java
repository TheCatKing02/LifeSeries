package net.mat0u5.lifeseries.seasons.boogeyman.advanceddeaths;

import net.minecraft.server.network.ServerPlayerEntity;

public abstract class AdvancedDeath {
    private ServerPlayerEntity player;
    private boolean started = false;
    public AdvancedDeath(ServerPlayerEntity player) {
        this.player = player;
    }

    protected abstract void tick(ServerPlayerEntity player);
    protected abstract void begin(ServerPlayerEntity player);
    protected abstract void end(ServerPlayerEntity player);

    public void onTick() {
        if (player == null || !player.isAlive()) return;

        if (!started) {
            started = true;
            begin(player);
        }

        tick(player);
    }

    public void onEnd() {
        if (player == null || !player.isAlive()) return;
        end(player);
    }
}
