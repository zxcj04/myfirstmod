package com.fanrende.myfirstmod.setup;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ServerProxy implements IProxy
{
	@Override
	public Level getClientWorld()
	{
		throw new IllegalStateException("Only run this in client");
	}

	@Override
	public Player getClientPlayer()
	{
		return null;
	}
}
