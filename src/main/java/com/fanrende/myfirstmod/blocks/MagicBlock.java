package com.fanrende.myfirstmod.blocks;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MagicBlock extends Block implements EntityBlock
{
	private static final VoxelShape SHAPE = Shapes.box(0.0, 0.0, 0.0, 1.0, 0.15, 1.0);
	private static final VoxelShape RENDER_SHAPE = Shapes.empty();

	public MagicBlock()
	{
		super(Properties.of(Material.STONE).sound(SoundType.STONE).strength(2.0f));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
	{
		return new MagicBlockTile(blockPos, blockState);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(
			ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn
	)
	{
		if (Screen.hasShiftDown())
		{
			tooltip.add(new TranslatableComponent("message.magicblock"));
		}
		else
		{
			tooltip.add(new TranslatableComponent("message.pressshift"));
		}
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (state.hasBlockEntity() && state.getBlock() != newState.getBlock())
		{
			BlockEntity tileEntity = worldIn.getBlockEntity(pos);
			// drops everything in the inventory
			tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h ->
			{
				for (int i = 0; i < h.getSlots(); i++)
					popResource(worldIn, pos, h.getStackInSlot(i));
			});

			worldIn.updateNeighbourForOutputSignal(pos, this);

			tileEntity.save(new CompoundTag());
		}

		super.onRemove(state, worldIn, pos, newState, isMoving);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state)
	{
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos)
	{
		AtomicBoolean itemIn = new AtomicBoolean(false);

		worldIn.getBlockEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h ->
		{
			itemIn.set(!h.getStackInSlot(0).isEmpty());
		});

		return itemIn.get() ? 15 : 0;
	}

	@Override
	public VoxelShape getShape(
			BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context
	)
	{
		return SHAPE;
	}

	@Override
	public VoxelShape getOcclusionShape(
			BlockState state, BlockGetter worldIn, BlockPos pos
	)
	{
		return RENDER_SHAPE;
	}

	@Override
	public RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.INVISIBLE;
	}

	@Override
	public InteractionResult use(
			BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit
	)
	{
		if (!world.isClientSide)
		{
			BlockEntity tileEntity = world.getBlockEntity(pos);
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

		return InteractionResult.SUCCESS;
	}
}
