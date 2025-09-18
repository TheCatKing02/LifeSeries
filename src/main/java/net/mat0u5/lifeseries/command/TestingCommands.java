package net.mat0u5.lifeseries.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.mat0u5.lifeseries.command.manager.Command;
import net.mat0u5.lifeseries.seasons.boogeyman.advanceddeaths.AdvancedDeathsManager;
import net.mat0u5.lifeseries.utils.other.OtherUtils;
import net.mat0u5.lifeseries.utils.other.TaskScheduler;
import net.mat0u5.lifeseries.utils.other.TextUtils;
import net.mat0u5.lifeseries.utils.other.WeightedRandomizer;
import net.mat0u5.lifeseries.utils.player.PermissionManager;
import net.mat0u5.lifeseries.utils.player.PlayerUtils;
import net.mat0u5.lifeseries.utils.versions.VersionControl;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

public class TestingCommands extends Command {

    @Override
    public boolean isAllowed() {
        return VersionControl.isDevVersion();
    }

    @Override
    public Text getBannedText() {
        return Text.of("This command is only available when playing a dev version.");
    }

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        if (VersionControl.isDevVersion()) {
            dispatcher.register(
                literal("ls")
                    .requires(PermissionManager::isAdmin)
                    .then(literal("test")
                        .executes(context -> test(context.getSource()))
                    )
                    .then(literal("test1")
                        .executes(context -> test1(context.getSource()))
                    )
                    .then(literal("test2")
                        .executes(context -> test2(context.getSource()))
                    )
                    .then(literal("test3")
                            .executes(context -> test3(context.getSource()))
                    )
                    .then(literal("players")
                        .then(argument("amount", IntegerArgumentType.integer())
                            .executes(context -> spawnPlayers(context.getSource(), IntegerArgumentType.getInteger(context, "amount")))
                        )
                    )
            );
        }

    }

    public int test(ServerCommandSource source) {
        if (checkBanned(source)) return -1;
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) return -1;

        TaskScheduler.scheduleTask(1, () -> {
            List<ServerPlayerEntity> test = List.of(player);
            test.removeFirst();//Will cause an error
        });

        return 1;
    }

    public int test1(ServerCommandSource source) {
        if (checkBanned(source)) return -1;
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) return -1;

        OtherUtils.sendCommandFeedbackQuiet(source, Text.of("Test Command 1"));

        OtherUtils.sendCommandFeedbackQuiet(source, TextUtils.format("Test0: {}", AdvancedDeathsManager.getRandomDeaths(player, 0)));
        OtherUtils.sendCommandFeedbackQuiet(source, TextUtils.format("Test1: {}", AdvancedDeathsManager.getRandomDeaths(player, 1)));
        OtherUtils.sendCommandFeedbackQuiet(source, TextUtils.format("Test2: {}", AdvancedDeathsManager.getRandomDeaths(player, 2)));
        OtherUtils.sendCommandFeedbackQuiet(source, TextUtils.format("Test3: {}", AdvancedDeathsManager.getRandomDeaths(player, 3)));
        OtherUtils.sendCommandFeedbackQuiet(source, TextUtils.format("Test4: {}", AdvancedDeathsManager.getRandomDeaths(player, 4)));
        OtherUtils.sendCommandFeedbackQuiet(source, TextUtils.format("Test5: {}", AdvancedDeathsManager.getRandomDeaths(player, 5)));
        OtherUtils.sendCommandFeedbackQuiet(source, TextUtils.format("Test6: {}", AdvancedDeathsManager.getRandomDeaths(player, 6)));
        OtherUtils.sendCommandFeedbackQuiet(source, TextUtils.format("Test7: {}", AdvancedDeathsManager.getRandomDeaths(player, 7)));

        return 1;
    }

    public int test2(ServerCommandSource source) {
        if (checkBanned(source)) return -1;
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) return -1;

        OtherUtils.sendCommandFeedbackQuiet(source, Text.of("Test Command 2"));
        OtherUtils.sendCommandFeedbackQuiet(source, TextUtils.format("Test: {}", PlayerUtils.getAllPlayers()));

        return 1;
    }

    public int test3(ServerCommandSource source) {
        if (checkBanned(source)) return -1;
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) return -1;

        OtherUtils.sendCommandFeedbackQuiet(source, Text.of("Test Command 3"));

        System.out.println("=== Original Example: Range 0-9, Lives 1-3 ===");
        WeightedRandomizer randomizer = new WeightedRandomizer();

        randomizer.testDistribution(0, 9, 1, 4, 1.5);

        // Test different example: Range 1-100 with 1-5 difficulty levels
        System.out.println("\n=== Different Example: Range 1-100, Difficulty 1-5 ===");
        randomizer.testDistribution(1, 100, 1, 5, 1);

        // Test edge case: Range 0-1 with 1-2 states
        System.out.println("\n=== Edge Case: Range 0-1, States 1-2 ===");
        randomizer.testDistribution(0, 1, 1, 2, 1);

        return 1;
    }

    public int spawnPlayers(ServerCommandSource source, int amount) {
        if (checkBanned(source)) return -1;
        for (int i = 1; i <= amount; i++) {
            OtherUtils.executeCommand(TextUtils.formatString("player Test{} spawn in survival", i));
        }
        return 1;
    }
}
