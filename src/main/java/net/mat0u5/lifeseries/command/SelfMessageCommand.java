package net.mat0u5.lifeseries.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.mat0u5.lifeseries.command.manager.Command;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class SelfMessageCommand extends Command {

    @Override
    public boolean isAllowed() {
        return true;
    }

    @Override
    public Text getBannedText() {
        return Text.of("");
    }

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal("selfmsg")
                .then(argument("message", StringArgumentType.greedyString())
                    .executes(context -> execute(
                        context.getSource(),
                        StringArgumentType.getString(context ,"message")
                    ))
                )
        );

    }

    public int execute(ServerCommandSource source, String string) {
        if (checkBanned(source)) return -1;
        source.sendMessage(Text.of(string));
        return 1;
    }
}
