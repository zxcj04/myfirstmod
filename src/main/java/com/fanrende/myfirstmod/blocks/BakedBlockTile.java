package com.fanrende.myfirstmod.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

import static com.fanrende.myfirstmod.setup.Registration.BAKEDBLOCK;
import static com.fanrende.myfirstmod.setup.Registration.BAKEDBLOCK_TILE;

public class BakedBlockTile extends TileEntity
{
	public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();

	private BlockState mimic;

	public BakedBlockTile()
	{
		super(BAKEDBLOCK_TILE.get());
	}

	public void setMimic(BlockState mimic)
	{
		if (mimic.getBlock() == BAKEDBLOCK.get())
			return;

		this.mimic = mimic;
		setChanged();
		level.sendBlockUpdated(worldPosition,
				getBlockState(),
				getBlockState(),
				Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS
		);
	}

	public BlockState getMimic()
	{
		return mimic;
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT tag = super.getUpdateTag();
		writeMimic(tag);

		return tag;
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag)
	{
		// This is actually the default but placed here so you
		// know this is the place to potentially have a lighter read() that only
		// considers things needed client-side
		load(state, tag);
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		BlockState oldMimic = mimic;
		CompoundNBT tag = pkt.getTag();
		handleUpdateTag(mimic, tag);

		if (!Objects.equals(oldMimic, mimic))
		{
			ModelDataManager.requestModelDataRefresh(this);
			level.sendBlockUpdated(worldPosition,
					getBlockState(),
					getBlockState(),
					Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS
			);
		}
	}

	@Nonnull
	@Override
	public IModelData getModelData()
	{
		return new ModelDataMap.Builder().withInitial(MIMIC, mimic).build();
	}

	@Override
	public void load(BlockState state, CompoundNBT compound)
	{
		super.load(state, compound);
		readMimic(compound);
	}

	private void readMimic(CompoundNBT tag)
	{
		if (tag.contains("mimic"))
			mimic = NBTUtil.readBlockState(tag.getCompound("mimic"));
	}

	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		writeMimic(compound);
		return super.save(compound);
	}

	private void writeMimic(CompoundNBT tag)
	{
		if (mimic != null)
			tag.put("mimic", NBTUtil.writeBlockState(mimic));
	}
}
