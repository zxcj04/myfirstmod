package com.fanrende.myfirstmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.block.AbstractBlock.Properties;

public class MagicBlock extends Block
{
	private static final VoxelShape SHAPE = VoxelShapes.box(0.0, 0.0, 0.0, 1.0, 0.15, 1.0);
	private static final VoxelShape RENDER_SHAPE = VoxelShapes.empty();

	public MagicBlock()
	{
		super(Properties.of(Material.STONE).sound(SoundType.STONE).strength(2.0f));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(
			ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn
	)
	{
		if (Screen.hasShiftDown())
		{
			tooltip.add(new TranslationTextComponent("message.magicblock"));
		}
		else
		{
			tooltip.add(new TranslationTextComponent("message.pressshift"));
		}
	}

	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (state.hasTileEntity() && state.getBlock() != newState.getBlock())
		{
			TileEntity tileEntity = worldIn.getBlockEntity(pos);
			// drops everything in the inventory
			tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h ->
			{
				for (int i = 0; i < h.getSlots(); i++)
					popResource(worldIn, pos, h.getStackInSlot(i));
			});

			worldIn.updateNeighbourForOutputSignal(pos, this);

			tileEntity.save(new CompoundNBT());
		}

		super.onRemove(state, worldIn, pos, newState, isMoving);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state)
	{
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, World worldIn, BlockPos pos)
	{
		AtomicBoolean itemIn = new AtomicBoolean(false);

		worldIn.getBlockEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h ->
		{
			itemIn.set(!h.getStackInSlot(0).isEmpty());
		});

		return itemIn.get() ? 15 : 0;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(
			BlockState state, IBlockReader world
	)
	{
		return new MagicBlockTile();
	}

	@Override
	public VoxelShape getShape(
			BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context
	)
	{
		return SHAPE;
	}

	@Override
	public VoxelShape getOcclusionShape(
			BlockState state, IBlockReader worldIn, BlockPos pos
	)
	{
		return RENDER_SHAPE;
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state)
	{
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public ActionResultType use(
			BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit
	)
	{
		if (!world.isClientSide)
		{
			TileEntity tileEntity = world.getBlockEntity(pos);
			tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h ->
			{
				ItemStack stack = player.getItemInHand(hand);

				if (stack.isEmpty())
					stack = h.extractItem(0, h.getStackInSlot(0).getCount(), false);
				else if (h.getStackInSlot(0).isEmpty())
					stack = h.insertItem(0, stack, false);
				else
				{
					popResource(world, pos, h.getStackInSlot(0));
					h.extractItem(0, h.getStackInSlot(0).getCount(), false);
				}

				player.setItemInHand(hand, stack);
			});
		}

		return ActionResultType.SUCCESS;
	}
}
