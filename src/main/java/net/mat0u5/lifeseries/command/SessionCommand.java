package net.mat0u5.lifeseries.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.mat0u5.lifeseries.command.manager.Command;
import net.mat0u5.lifeseries.network.NetworkHandlerServer;
import net.mat0u5.lifeseries.seasons.season.Seasons;
import net.mat0u5.lifeseries.utils.enums.PacketNames;
import net.mat0u5.lifeseries.utils.other.OtherUtils;
import net.mat0u5.lifeseries.utils.other.TextUtils;
import net.mat0u5.lifeseries.utils.player.PermissionManager;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

import static net.mat0u5.lifeseries.Main.currentSeason;
import static net.mat0u5.lifeseries.Main.currentSession;

public class SessionCommand extends Command {
    public static final String INVALID_TIME_FORMAT_ERROR = "Invalid time format. Use h, m, s for hours, minutes, and seconds.";

    @Override
    public boolean isAllowed() {
        return currentSeason.getSeason() != Seasons.UNASSIGNED;
    }

    @Override
    public Text getBannedText() {
        return Text.of("This command is only available when you have selected a Season.");
    }

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal("session")
                .then(literal("start")
                    .requires(PermissionManager::isAdmin)
                    .executes(context -> startSession(
                        context.getSource()
                    ))
                )
                .then(literal("stop")
                    .requires(PermissionManager::isAdmin)
                    .executes(context -> stopSession(
                        context.getSource()
                    ))
                )
                .then(literal("pause")
                    .requires(PermissionManager::isAdmin)
                    .executes(context -> pauseSession(
                        context.getSource()
                    ))
                )
                .then(literal("timer")
                    .then(literal("set")
                        .requires(PermissionManager::isAdmin)
                        .then(argument("time", StringArgumentType.greedyString())
                            .suggests((context, builder) -> CommandSource.suggestMatching(List.of("1h","1h30m","2h"), builder))
                            .executes(context -> setTime(
                                context.getSource(), StringArgumentType.getString(context, "time")
                            ))
                        )
                    )
                    .then(literal("add")
                        .requires(PermissionManager::isAdmin)
                        .then(argument("time", StringArgumentType.greedyString())
                            .suggests((context, builder) -> CommandSource.suggestMatching(List.of("30m", "1h"), builder))
                            .executes(context -> addTime(
                                context.getSource(), StringArgumentType.getString(context, "time")
                            ))
                        )
                    )
                    .then(literal("fastforward")
                        .requires(PermissionManager::isAdmin)
                        .then(argument("time", StringArgumentType.greedyString())
                            .suggests((context, builder) -> CommandSource.suggestMatching(List.of("5m"), builder))
                            .executes(context -> skipTime(
                                context.getSource(), StringArgumentType.getString(context, "time")
                            ))
                        )
                    )
                    .then(literal("remove")
                        .requires(PermissionManager::isAdmin)
                            .then(argument("time", StringArgumentType.greedyString())
                                    .suggests((context, builder) -> CommandSource.suggestMatching(List.of("5m"), builder))
                                    .executes(context -> removeTime(
                                            context.getSource(), StringArgumentType.getString(context, "time")
                                    ))
                            )
                    )
                    .then(literal("remaining")
                        .executes(context -> getTime(
                            context.getSource()
                        ))
                    )
                    .then(literal("showDisplay")
                        .executes(context -> displayTimer(
                            context.getSource()
                        ))
                    )
                )

        );
    }

    public int getTime(ServerCommandSource source) {
        if (checkBanned(source)) return -1;

        if (!currentSession.validTime()) {
            source.sendError(Text.of("The session time has not been set yet"));
            return -1;
        }
        OtherUtils.sendCommandFeedbackQuiet(source, TextUtils.format("The session ends in {}",currentSession.getRemainingTimeStr()));
        return 1;
    }

    public int displayTimer(ServerCommandSource source) {
        if (checkBanned(source)) return -1;
        final ServerPlayerEntity self = source.getPlayer();

        if (self == null) return -1;
        if (NetworkHandlerServer.wasHandshakeSuccessful(self)) {
            NetworkHandlerServer.sendStringPacket(self, PacketNames.TOGGLE_TIMER, "");
        }

        boolean isInDisplayTimer = currentSession.isInDisplayTimer(self);
        if (isInDisplayTimer) currentSession.removeFromDisplayTimer(self);
        else currentSession.addToDisplayTimer(self);
        return 1;
    }

    public int startSession(ServerCommandSource source) {
        if (checkBanned(source)) return -1;

        if (!currentSession.validTime()) {
            source.sendError(Text.of("The session time is not set! Use '/session timer set <time>' to set the session time."));
            return -1;
        }
        if (currentSession.statusStarted()) {
            source.sendError(Text.of("The session has already started"));
            return -1;
        }
        if (currentSession.statusPaused()) {
            OtherUtils.sendCommandFeedback(source, Text.of("§7Unpausing session..."));
            currentSession.sessionPause();
            return 1;
        }

        OtherUtils.sendCommandFeedback(source, Text.of("Starting session..."));
        if (!currentSession.sessionStart()) {
            source.sendError(Text.of("Could not start session"));
            return -1;
        }

        return 1;
    }

    public int stopSession(ServerCommandSource source) {
        if (checkBanned(source)) return -1;

        if (currentSession.statusNotStarted() || currentSession.statusFinished()) {
            source.sendError(Text.of("The session has not yet started"));
            return -1;
        }

        OtherUtils.sendCommandFeedback(source, Text.of("§7Stopping session..."));
        currentSession.sessionEnd();
        return 1;
    }

    public int pauseSession(ServerCommandSource source) {
        if (checkBanned(source)) return -1;

        if (currentSession.statusNotStarted() || currentSession.statusFinished()) {
            source.sendError(Text.of("The session has not yet started"));
            return -1;
        }

        if (currentSession.statusPaused()) {
            OtherUtils.sendCommandFeedback(source, Text.of("§7Unpausing session..."));
        }
        else {
            OtherUtils.sendCommandFeedback(source, Text.of("§7Pausing session..."));
        }
        currentSession.sessionPause();

        return 1;
    }

    public int skipTime(ServerCommandSource source, String timeArgument) {
        if (checkBanned(source)) return -1;

        Integer totalTicks = OtherUtils.parseTimeFromArgument(timeArgument);
        if (totalTicks == null) {
            source.sendError(Text.literal(INVALID_TIME_FORMAT_ERROR));
            return -1;
        }
        OtherUtils.sendCommandFeedback(source, TextUtils.format("Skipped {} in the session length", OtherUtils.formatTime(totalTicks)));
        currentSession.passedTime+=totalTicks;
        return 1;
    }

    public int setTime(ServerCommandSource source, String timeArgument) {
        if (checkBanned(source)) return -1;

        Integer totalTicks = OtherUtils.parseTimeFromArgument(timeArgument);
        if (totalTicks == null) {
            source.sendError(Text.literal(INVALID_TIME_FORMAT_ERROR));
            return -1;
        }
        currentSession.setSessionLength(totalTicks);

        OtherUtils.sendCommandFeedback(source, TextUtils.format("The session length has been set to {}", OtherUtils.formatTime(totalTicks)));
        return 1;
    }

    public int addTime(ServerCommandSource source, String timeArgument) {
        if (checkBanned(source)) return -1;

        Integer totalTicks = OtherUtils.parseTimeFromArgument(timeArgument);
        if (totalTicks == null) {
            source.sendError(Text.literal(INVALID_TIME_FORMAT_ERROR));
            return -1;
        }
        currentSession.addSessionLength(totalTicks);

        OtherUtils.sendCommandFeedback(source, TextUtils.format("Added {} to the session length", OtherUtils.formatTime(totalTicks)));
        return 1;
    }

    public int removeTime(ServerCommandSource source, String timeArgument) {
        if (checkBanned(source)) return -1;

        Integer totalTicks = OtherUtils.parseTimeFromArgument(timeArgument);
        if (totalTicks == null) {
            source.sendError(Text.literal(INVALID_TIME_FORMAT_ERROR));
            return -1;
        }
        currentSession.removeSessionLength(totalTicks);

        OtherUtils.sendCommandFeedback(source, TextUtils.format("Removed {} from the session length", OtherUtils.formatTime(totalTicks)));
        return 1;
    }
}

