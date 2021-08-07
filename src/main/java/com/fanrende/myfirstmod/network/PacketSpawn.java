package com.fanrende.myfirstmod.network;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class PacketSpawn
{
	private final ResourceLocation id;
	private final RegistryKey<World> type;
	private final BlockPos pos;

	public PacketSpawn(PacketBuffer buffer)
	{
		id = buffer.readResourceLocation();
		type = RegistryKey.create(Registry.DIMENSION_REGISTRY, buffer.readResourceLocation());
		pos = buffer.readBlockPos();
	}

	public PacketSpawn(ResourceLocation id, RegistryKey<World> type, BlockPos pos)
	{
		this.id = id;
		this.type = type;
		this.pos = pos;
	}

	public void toBytes(PacketBuffer buffer)
	{
		buffer.writeResourceLocation(id);
		buffer.writeResourceLocation(type.location());
		buffer.writeBlockPos(pos);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			ServerWorld spawnWorld = ctx.get().getSender().level.getServer().getLevel(type);
			EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(id);

			if (entityType == null)
			{
				throw new IllegalStateException("This cannot happen! Unknown id '" + id + "'!");
			}

			entityType.spawn(spawnWorld, null, null, pos, SpawnReason.SPAWN_EGG, true, true);
		});

		ctx.get().setPacketHandled(true);
	}
}
