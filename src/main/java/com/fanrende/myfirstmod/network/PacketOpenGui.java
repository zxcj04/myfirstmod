package com.fanrende.myfirstmod.network;

import com.fanrende.myfirstmod.gui.SpawnScreen;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpenGui
{
	public boolean handle(Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(SpawnScreen::open);

		return true;
	}
}
