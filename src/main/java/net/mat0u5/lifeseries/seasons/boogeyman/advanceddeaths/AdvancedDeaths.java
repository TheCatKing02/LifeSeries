package net.mat0u5.lifeseries.seasons.boogeyman.advanceddeaths;

import net.mat0u5.lifeseries.seasons.boogeyman.advanceddeaths.deaths.DeathAnvil;
import net.mat0u5.lifeseries.seasons.boogeyman.advanceddeaths.deaths.DeathLightning;
import net.mat0u5.lifeseries.seasons.boogeyman.advanceddeaths.deaths.DeathWither;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public enum AdvancedDeaths {
    NULL,
    LIGHTNING,
    WITHER,
    ANVIL;

    @Nullable
    public AdvancedDeath getInstance(ServerPlayerEntity player) {
        if (this == WITHER) return new DeathWither(player);
        if (this == ANVIL) return new DeathAnvil(player);
        if (this == LIGHTNING) return new DeathLightning(player);
        return null;
    }

    public static List<AdvancedDeaths> getAllDeaths() {
        List<AdvancedDeaths> result = new ArrayList<>(List.of(AdvancedDeaths.values()));
        result.remove(NULL);
        return result;
    }
}
