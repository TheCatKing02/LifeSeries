package net.mat0u5.lifeseries.seasons.secretsociety;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.mat0u5.lifeseries.seasons.session.SessionTranscript;
import net.mat0u5.lifeseries.utils.other.OtherUtils;
import net.mat0u5.lifeseries.utils.other.TextUtils;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import static net.mat0u5.lifeseries.Main.currentSeason;
import static net.mat0u5.lifeseries.utils.player.PermissionManager.isAdmin;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SocietyCommands {

    public static SecretSociety get() {
        return currentSeason.secretSociety;
    }

    public static boolean isAllowed() {
        return get().SOCIETY_ENABLED;
    }

    public static boolean checkBanned(ServerCommandSource source) {
        if (isAllowed()) return false;
        source.sendError(Text.of("This command is only available when the Secret Society has been enabled in the Life Series config."));
        return true;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(
                literal("initiate")
                        .executes(context -> initiate(context.getSource()))
        );
        dispatcher.register(
            literal("society")
                .then(literal("success")
                    .executes(context -> societySuccess(context.getSource()))
                )
                .then(literal("fail")
                    .executes(context -> societyFail(context.getSource()))
                )
                .then(literal("begin")
                    .requires(source -> (isAdmin(source.getPlayer()) || (source.getEntity() == null)))
                    .then(argument("secret_word", StringArgumentType.string())
                        .executes(context -> societyBegin(context.getSource(), StringArgumentType.getString(context, "secret_word")))
                    )
                    .executes(context -> societyBegin(context.getSource(), null))
                )
                .then(literal("members")
                    .requires(source -> (isAdmin(source.getPlayer()) || (source.getEntity() == null)))
                        .then(literal("add")
                            .then(argument("player", EntityArgumentType.player())
                                .executes(context -> membersAdd(context.getSource(), EntityArgumentType.getPlayer(context, "player")))
                            )
                        )
                        .then(literal("remove")
                            .then(argument("player", EntityArgumentType.player())
                                .executes(context -> membersRemove(context.getSource(), EntityArgumentType.getPlayer(context, "player")))
                            )
                        )
                        .then(literal("list")
                            .executes(context -> membersList(context.getSource()))
                        )
                )
        );
    }
    public static int membersRemove(ServerCommandSource source, ServerPlayerEntity target) {
        if (checkBanned(source)) return -1;
        SecretSociety society = get();
        if (society == null) return -1;
        if (!checkSocietyStart(source)) return -1;
        if (target == null) return -1;

        society.removeMemberManually(target);
        return 1;
    }

    public static int membersAdd(ServerCommandSource source, ServerPlayerEntity target) {
        if (checkBanned(source)) return -1;
        SecretSociety society = get();
        if (society == null) return -1;
        if (!checkSocietyStart(source)) return -1;
        if (target == null) return -1;

        society.addMemberManually(target);
        return 1;
    }

    public static int membersList(ServerCommandSource source) {
        if (checkBanned(source)) return -1;
        SecretSociety society = get();
        if (society == null) return -1;
        if (!checkSocietyStart(source)) return -1;

        if (society.members.isEmpty()) {
            source.sendError(Text.of("The are no Secret Society members"));
            return -1;
        }

        List<String> societyMembers = new ArrayList<>();
        for (SocietyMember member : society.members) {
            ServerPlayerEntity player = member.getPlayer();
            if (player == null) continue;
            societyMembers.add(player.getNameForScoreboard());
        }
        OtherUtils.sendCommandFeedbackQuiet(source, TextUtils.formatLoosely("Secret Society Members: §7{}"));
        return 1;
    }

    public static boolean checkSocietyStart(ServerCommandSource source) {
        SecretSociety society = get();
        if (society == null) return false;
        if (!society.societyStarted) {
            source.sendError(Text.of("The society has not started yet"));
            OtherUtils.sendCommandFeedbackQuiet(source, Text.of("§7Use '/society begin' or '/society begin <secret_word>' to start"));
            return true;
        }
        return false;
    }

    public static int societyBegin(ServerCommandSource source, String word) {
        if (checkBanned(source)) return -1;
        SecretSociety society = get();
        if (society == null) return -1;

        OtherUtils.sendCommandFeedback(source, Text.of("§7Starting the Secret Society..."));
        society.startSociety(word);
        return 1;
    }

    public static int societyFail(ServerCommandSource source) {
        if (checkBanned(source)) return -1;
        SecretSociety society = get();
        if (society == null) return -1;
        ServerPlayerEntity self = source.getPlayer();
        if (self == null) return -1;

        if (!society.isMember(self)) {
            source.sendError(Text.of("You are not a member of the Secret Society"));
            return -1;
        }
        SocietyMember member = society.getMember(self);
        if (!member.initialized) {
            source.sendError(Text.of("You have not been initialized"));
            return -1;
        }

        society.endFail();
        SessionTranscript.societyEndFail(self);
        return 1;
    }

    public static int societySuccess(ServerCommandSource source) {
        if (checkBanned(source)) return -1;
        SecretSociety society = get();
        if (society == null) return -1;
        ServerPlayerEntity self = source.getPlayer();
        if (self == null) return -1;

        if (!society.isMember(self)) {
            source.sendError(Text.of("You are not a member of the Secret Society"));
            return -1;
        }
        SocietyMember member = society.getMember(self);
        if (!member.initialized) {
            source.sendError(Text.of("You have not been initialized"));
            return -1;
        }

        society.endSuccess();
        SessionTranscript.societyEndSuccess(self);
        return 1;
    }

    public static int initiate(ServerCommandSource source) {
        if (checkBanned(source)) return -1;
        SecretSociety society = get();
        if (society == null) return -1;
        ServerPlayerEntity self = source.getPlayer();
        if (self == null) return -1;

        if (!society.isMember(self)) {
            source.sendError(Text.of("You are not a member of the Secret Society"));
            return -1;
        }
        SocietyMember member = society.getMember(self);
        if (member.initialized) {
            source.sendError(Text.of("You have already been initialized"));
            return -1;
        }

        society.initializeMember(self);
        return 1;
    }
}
