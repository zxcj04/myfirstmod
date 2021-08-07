package com.fanrende.myfirstmod.commands;

import com.fanrende.myfirstmod.network.Networking;
import com.fanrende.myfirstmod.network.PacketOpenGui;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;

public class CommandSpawn implements Command<CommandSource>
{
	public static final CommandSpawn CMD = new CommandSpawn();

	public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher)
	{
		return Commands.literal("spawn").requires(cs -> cs.hasPermission(0)).executes(CMD);
	}

	@Override
	public int run(CommandContext<CommandSource> context) throws CommandSyntaxException
	{
		ServerPlayerEntity player = context.getSource().getPlayerOrException();
		Networking.sendToClient(new PacketOpenGui(), player);
		return 0;
	}
}
