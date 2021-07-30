package com.fanrende.myfirstmod.setup;

import com.fanrende.myfirstmod.blocks.FirstBlockScreen;
import com.fanrende.myfirstmod.entities.WeirdMobRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import static com.fanrende.myfirstmod.setup.Registration.FIRSTBLOCK_CONTAINER;
import static com.fanrende.myfirstmod.setup.Registration.WEIRDMOB;

public class ClientProxy implements IProxy
{
	@Override
	public World getClientWorld()
	{
		return Minecraft.getInstance().world;
	}

	@Override
	public PlayerEntity getClientPlayer()
	{
		return null;
	}
}
