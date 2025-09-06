package net.mat0u5.lifeseries.seasons.season.pastlife;

import net.mat0u5.lifeseries.config.ConfigManager;
import net.mat0u5.lifeseries.network.NetworkHandlerServer;
import net.mat0u5.lifeseries.seasons.season.Season;
import net.mat0u5.lifeseries.seasons.season.Seasons;
import net.mat0u5.lifeseries.utils.other.TaskScheduler;
import net.mat0u5.lifeseries.utils.player.PlayerUtils;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PastLife extends Season {
    public static final String COMMANDS_ADMIN_TEXT = "/lifeseries, /session, /claimkill, /lives, /boogeyman, /society, /pastlife";
    public static final String COMMANDS_TEXT = "/claimkill, /lives, /society, /initiate";
    @Override
    public Seasons getSeason() {
        return Seasons.PAST_LIFE;
    }

    @Override
    public ConfigManager createConfig() {
        return new PastLifeConfig();
    }

    @Override
    public String getAdminCommands() {
        return COMMANDS_ADMIN_TEXT;
    }

    @Override
    public String getNonAdminCommands() {
        return COMMANDS_TEXT;
    }

    @Override
    public void addSessionActions() {
        if (boogeymanManager.BOOGEYMAN_ENABLED && secretSociety.SOCIETY_ENABLED) {
            TaskScheduler.scheduleTask(20, this::requestSessionAction);
            return;
        }
        super.addSessionActions();
    }

    public void requestSessionAction() {
        for (ServerPlayerEntity player : PlayerUtils.getAdminPlayers()) {
            if (NetworkHandlerServer.wasHandshakeSuccessful(player)) {

            }
            else {
                player.sendMessage(Text.of("§7Past Life session started:"));
                player.sendMessage(Text.of("§7 Type §f\"/pastlife boogeyman\"§7 to have the Boogeyman in this session."));
                player.sendMessage(Text.of("§7 Type §f\"/pastlife society\"§7 to have the Secret Society in this session."));
                player.sendMessage(Text.of("§7 Or type §f\"/pastlife pickRandom\"§7 if you want the game to pick randomly."));
            }
        }
    }
}
