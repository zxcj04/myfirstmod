package com.fanrende.myfirstmod.blocks;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
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

import javax.annotation.Nullable;
import java.util.List;

public class BakedBlock extends Block implements EntityBlock
{
	private final VoxelShape shape = Shapes.box(.2, .2, .2, .8, .8, .8);

	public BakedBlock()
	{
		super(Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(1.0f));
	}

	@Override
	public VoxelShape getShape(
			BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context
	)
	{
		return shape;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new BakedBlockTile(pos, state);
	}

	@Override
	public InteractionResult use(
			BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace
	)
	{
		ItemStack item = player.getItemInHand(hand);
		if (!item.isEmpty() && item.getItem() instanceof BlockItem)
		{
			if (!world.isClientSide)
			{
				BlockEntity te = world.getBlockEntity(pos);
				if (te instanceof BakedBlockTile)
				{
					BlockState mimicState = ( (BlockItem) item.getItem() ).getBlock().defaultBlockState();
					( (BakedBlockTile) te ).setMimic(mimicState);
				}
			}
			return InteractionResult.SUCCESS;
		}

		return super.use(state, world, pos, player, hand, trace);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(
			ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn
	)
	{
		if (Screen.hasShiftDown())
		{
			tooltip.add(new TranslatableComponent("message.bakedblock"));
		}
		else
		{
			tooltip.add(new TranslatableComponent("message.pressshift"));
		}
	}

	@Override
	public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos)
	{
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof BakedBlockTile)
		{
			BlockState mimic = ( (BakedBlockTile) blockEntity ).getMimic();
			if (mimic != null)
				return mimic.getLightEmission(world, pos);
		}

		return super.getLightEmission(state, world, pos);
	}
}
