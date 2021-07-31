package com.fanrende.myfirstmod.commands;

import com.fanrende.myfirstmod.MyFirstMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class ModCommands
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		LiteralCommandNode<CommandSource> cmd = dispatcher.register(Commands.literal(MyFirstMod.MODID)
				.then(CommandHello.register(dispatcher))
				.then(CommandSpawn.register(dispatcher)));

		dispatcher.register(Commands.literal("mfm").redirect(cmd));
	}
}
