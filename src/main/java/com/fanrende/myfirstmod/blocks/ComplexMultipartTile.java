package com.fanrende.myfirstmod.blocks;

import com.fanrende.myfirstmod.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Constants;

import static com.fanrende.myfirstmod.blocks.ComplexMultipartBlock.*;

public class ComplexMultipartTile extends BlockEntity
{
	private Mode modes[] = new Mode[]{
			Mode.MODE_NONE, Mode.MODE_NONE, Mode.MODE_NONE, Mode.MODE_NONE, Mode.MODE_NONE, Mode.MODE_NONE
	};

	//	public ComplexMultipartTile()
	//	{
	//		super(Registration.COMPLEX_MULTIPART_TILE.get());
	//	}
	public ComplexMultipartTile(
			BlockPos pos, BlockState state
	)
	{
		super(Registration.COMPLEX_MULTIPART_TILE.get(), pos, state);
	}

	public void toggleMode(Direction side)
	{
		switch (modes[side.ordinal()])
		{
			case MODE_NONE:
				modes[side.ordinal()] = Mode.MODE_INPUT;
				break;
			case MODE_INPUT:
				modes[side.ordinal()] = Mode.MODE_OUTPUT;
				break;
			case MODE_OUTPUT:
				modes[side.ordinal()] = Mode.MODE_NONE;
				break;
		}

		updateState();
	}

	public void updateState()
	{
		Mode north = getMode(Direction.NORTH);
		Mode south = getMode(Direction.SOUTH);
		Mode west = getMode(Direction.WEST);
		Mode east = getMode(Direction.EAST);
		Mode up = getMode(Direction.UP);
		Mode down = getMode(Direction.DOWN);

		BlockState state = level.getBlockState(worldPosition);
		level.setBlock(
				worldPosition,
				state.setValue(NORTH, north)
						.setValue(SOUTH, south)
						.setValue(WEST, west)
						.setValue(EAST, east)
						.setValue(UP, up)
						.setValue(DOWN, down),
				Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS
		);

		setChanged();
	}

	public Mode getMode(Direction side)
	{
		return modes[side.ordinal()];
	}

	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);

		CompoundTag modeTag = compound.getCompound("mode");

		modes[0] = Mode.values()[modeTag.getByte("m0")];
		modes[1] = Mode.values()[modeTag.getByte("m1")];
		modes[2] = Mode.values()[modeTag.getByte("m2")];
		modes[3] = Mode.values()[modeTag.getByte("m3")];
		modes[4] = Mode.values()[modeTag.getByte("m4")];
		modes[5] = Mode.values()[modeTag.getByte("m5")];
	}

	@Override
	public CompoundTag save(CompoundTag compound)
	{
		CompoundTag modeTag = new CompoundTag();

		modeTag.putByte("m0", (byte) modes[0].ordinal());
		modeTag.putByte("m1", (byte) modes[1].ordinal());
		modeTag.putByte("m2", (byte) modes[2].ordinal());
		modeTag.putByte("m3", (byte) modes[3].ordinal());
		modeTag.putByte("m4", (byte) modes[4].ordinal());
		modeTag.putByte("m5", (byte) modes[5].ordinal());

		compound.put("mode", modeTag);

		return super.save(compound);
	}

	public enum Mode implements StringRepresentable
	{
		MODE_NONE("none"), MODE_INPUT("input"), MODE_OUTPUT("output");

		private final String name;

		Mode(String name)
		{
			this.name = name;
		}

		@Override
		public String getSerializedName()
		{
			return this.name;
		}

		@Override
		public String toString()
		{
			return getSerializedName();
		}
	}
}
