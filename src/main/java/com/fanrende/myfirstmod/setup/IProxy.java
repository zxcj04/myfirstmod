package com.fanrende.myfirstmod.setup;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface IProxy
{
	Level getClientWorld();

	Player getClientPlayer();
}
