package com.fanrende.myfirstmod.setup;

import com.fanrende.myfirstmod.blocks.FirstBlockScreen;
import com.fanrende.myfirstmod.blocks.ModBlocks;
import com.fanrende.myfirstmod.entities.WeirdMobEntity;
import com.fanrende.myfirstmod.entities.WeirdMobRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy implements IProxy
{
	@Override
	public void init()
	{
		ScreenManager.registerFactory(ModBlocks.FIRSTBLOCK_CONTAINER, FirstBlockScreen::new);
		RenderingRegistry.registerEntityRenderingHandler(WeirdMobEntity.class, WeirdMobRenderer::new);
	}

	@Override
	public World getClientWorld()
	{
		return Minecraft.getInstance().world;
	}
}
