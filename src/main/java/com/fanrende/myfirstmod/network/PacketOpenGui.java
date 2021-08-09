package com.fanrende.myfirstmod.network;

import com.fanrende.myfirstmod.gui.SpawnScreen;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpenGui
{
	public boolean handle(Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(SpawnScreen::open);

		return true;
	}
}
