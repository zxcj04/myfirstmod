package com.fanrende.myfirstmod.commands;

import com.fanrende.myfirstmod.network.Networking;
import com.fanrende.myfirstmod.network.PacketOpenGui;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class CommandSpawn implements Command<CommandSourceStack>
{
	public static final CommandSpawn CMD = new CommandSpawn();

	public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		return Commands.literal("spawn").requires(cs -> cs.hasPermission(0)).executes(CMD);
	}

	@Override
	public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		ServerPlayer player = context.getSource().getPlayerOrException();
		Networking.sendToClient(new PacketOpenGui(), player);
		return 0;
	}
}
