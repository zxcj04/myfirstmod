package com.fanrende.myfirstmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ComplexMultipartBlock extends Block
{
	public static final EnumProperty<ComplexMultipartTile.Mode> NORTH = EnumProperty.create("north", ComplexMultipartTile.Mode.class);
	public static final EnumProperty<ComplexMultipartTile.Mode> SOUTH = EnumProperty.create("south", ComplexMultipartTile.Mode.class);
	public static final EnumProperty<ComplexMultipartTile.Mode>  WEST = EnumProperty.create( "west", ComplexMultipartTile.Mode.class);
	public static final EnumProperty<ComplexMultipartTile.Mode>  EAST = EnumProperty.create( "east", ComplexMultipartTile.Mode.class);
	public static final EnumProperty<ComplexMultipartTile.Mode>    UP = EnumProperty.create(   "up", ComplexMultipartTile.Mode.class);
	public static final EnumProperty<ComplexMultipartTile.Mode>  DOWN = EnumProperty.create( "down", ComplexMultipartTile.Mode.class);

	private static final VoxelShape RENDER_SHAPE = VoxelShapes.create(.1, .1, .1, .9, .9, .9);

	public ComplexMultipartBlock()
	{
		super(Properties.create(Material.IRON)
				.sound(SoundType.METAL)
				.hardnessAndResistance(2.0f)
		);
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new ComplexMultipartTile();
	}

	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return RENDER_SHAPE;
	}

	@Override
	public ActionResultType onBlockActivated(
			BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit
	)
	{
		if(!worldIn.isRemote)
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);

			if(tileEntity instanceof ComplexMultipartTile && Screen.hasShiftDown())
			{
				ComplexMultipartTile dimentionalCellEntity = (ComplexMultipartTile) tileEntity;
				dimentionalCellEntity.toggleMode(hit.getFace());
			}
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public void onBlockPlacedBy(
			World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack
	)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(tileEntity instanceof ComplexMultipartTile)
			((ComplexMultipartTile) tileEntity).updateState();

		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	protected void fillStateContainer(
			StateContainer.Builder<Block, BlockState> builder
	)
	{
		super.fillStateContainer(builder);

		builder.add(NORTH, SOUTH, WEST, EAST, UP, DOWN);
	}
}
