package com.fanrende.myfirstmod.commands;

import com.fanrende.myfirstmod.MyFirstMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ModCommands
{
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(Commands.literal(MyFirstMod.MODID)
				.then(CommandHello.register(dispatcher))
				.then(CommandSpawn.register(dispatcher)));

		dispatcher.register(Commands.literal("mfm").redirect(cmd));
	}
}
