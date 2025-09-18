package net.mat0u5.lifeseries.dependencies;

import com.moulberry.flashback.playback.ReplayServer;
import net.minecraft.server.MinecraftServer;

public class FlashbackCompatibility {
    public static boolean isReplayServer(MinecraftServer server) {
        return server instanceof ReplayServer;
    }
}
