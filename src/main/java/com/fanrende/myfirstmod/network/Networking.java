package com.fanrende.myfirstmod.network;

import com.fanrende.myfirstmod.MyFirstMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class Networking
{
	private static SimpleChannel INSTANCE;

	private static int ID = 0;

	public static int nextID()
	{
		return ID++;
	}

	public static void registerMessages()
	{
		INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MyFirstMod.MODID, "myfirstmod"),
				() -> "1.0",
				s -> true,
				s -> true
		);

		INSTANCE.messageBuilder(PacketOpenGui.class, nextID()).encoder((packetOpenGui, packetBuffer) ->
		{
		}).decoder(buf -> new PacketOpenGui()).consumer(PacketOpenGui::handle).add();

		INSTANCE.messageBuilder(PacketSpawn.class, nextID())
				.encoder(PacketSpawn::toBytes)
				.decoder(PacketSpawn::new)
				.consumer(PacketSpawn::handle)
				.add();
	}

	public static void sendToClient(Object packet, ServerPlayer player)
	{
		INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
	}

	public static void sendToServer(Object packet)
	{
		INSTANCE.sendToServer(packet);
	}
}
