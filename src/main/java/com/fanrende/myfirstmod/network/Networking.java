package com.fanrende.myfirstmod.network;

import com.fanrende.myfirstmod.MyFirstMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking
{
	public static SimpleChannel INSTANCE;

	private static int ID = 0;

	public static int nextID()
	{
		return ID++;
	}

	public static void registerMessages()
	{
		INSTANCE = NetworkRegistry.newSimpleChannel(
				new ResourceLocation(MyFirstMod.MODID, "myfirstmod"),
				() -> "1.0",
				s -> true,
				s -> true
		);

		INSTANCE.registerMessage(nextID(),
				PacketOpenGui.class,
				PacketOpenGui::toBytes,
				PacketOpenGui::new,
				PacketOpenGui::handle
		);

		INSTANCE.registerMessage(nextID(),
				PacketSpawn.class,
				PacketSpawn::toBytes,
				PacketSpawn::new,
				PacketSpawn::handle
		);
	}
}
