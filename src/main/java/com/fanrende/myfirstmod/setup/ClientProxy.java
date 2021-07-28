package com.fanrende.myfirstmod.setup;

import com.fanrende.myfirstmod.blocks.FirstBlockScreen;
import com.fanrende.myfirstmod.blocks.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.world.World;

public class ClientProxy implements IProxy
{
	@Override
	public void init()
	{
		ScreenManager.registerFactory(ModBlocks.FIRSTBLOCK_CONTAINER, FirstBlockScreen::new);
	}

	@Override
	public World getClientWorld()
	{
		return Minecraft.getInstance().world;
	}
}
