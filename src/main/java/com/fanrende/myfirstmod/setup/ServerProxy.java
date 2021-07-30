package com.fanrende.myfirstmod.setup;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ServerProxy implements IProxy
{
	@Override
	public World getClientWorld()
	{
		throw new IllegalStateException("Only run this in client");
	}

	@Override
	public PlayerEntity getClientPlayer()
	{
		return null;
	}
}
