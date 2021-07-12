package dev.jpcode.whisperroulette;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

import static net.minecraft.server.command.CommandManager.argument;

public final class WRCommandRegistry {

    private WRCommandRegistry() {
        //not called
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) -> {
            String resPrefix = "[WhisperRoulette] ";

            dispatcher.register(CommandManager.literal("whisperroulette")
                .requires(source -> source.hasPermissionLevel(0))
                .then(argument("message", StringArgumentType.greedyString())
                    .executes(context -> {
                        List<ServerPlayerEntity> playerList =
                            new ArrayList<>(context.getSource().getMinecraftServer().getPlayerManager().getPlayerList());
                        ServerPlayerEntity source = context.getSource().getPlayer();
                        ServerPlayerEntity target = null;
                        while (target == null || target == source) {
                            int idx = new Random().nextInt(playerList.size());
                            target = playerList.get(idx);
                            playerList.remove(idx);
                        }
                        String message = StringArgumentType.getString(context, "message");

                        target.sendSystemMessage(
                            new TranslatableText("commands.message.display.incoming", "???", message
                                .formatted(Formatting.GRAY, Formatting.ITALIC)),
                            Util.NIL_UUID
                        );

                        context.getSource().sendFeedback(
                            new TranslatableText("commands.message.display.outgoing", target.getDisplayName(), message)
                                .formatted(Formatting.GRAY, Formatting.ITALIC),
                            context.getSource().getMinecraftServer().shouldBroadcastConsoleToOps()
                        );
                        return 1;
                    }))
            );
        });

    }
}
