package com.fanrende.myfirstmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
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

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.block.AbstractBlock.Properties;

public class BakedBlock extends Block
{
	private final VoxelShape shape = VoxelShapes.box(.2, .2, .2, .8, .8, .8);

	public BakedBlock()
	{
		super(Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(1.0f));
	}

	@Override
	public VoxelShape getShape(
			BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context
	)
	{
		return shape;
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
		return new BakedBlockTile();
	}

	@Override
	public ActionResultType use(
			BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace
	)
	{
		ItemStack item = player.getItemInHand(hand);
		if (!item.isEmpty() && item.getItem() instanceof BlockItem)
		{
			if (!world.isClientSide)
			{
				TileEntity te = world.getBlockEntity(pos);
				if (te instanceof BakedBlockTile)
				{
					BlockState mimicState = ( (BlockItem) item.getItem() ).getBlock().defaultBlockState();
					( (BakedBlockTile) te ).setMimic(mimicState);
				}
			}
			return ActionResultType.SUCCESS;
		}

		return super.use(state, world, pos, player, hand, trace);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(
			ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn
	)
	{
		if (Screen.hasShiftDown())
		{
			tooltip.add(new TranslationTextComponent("message.bakedblock"));
		}
		else
		{
			tooltip.add(new TranslationTextComponent("message.pressshift"));
		}
	}
}
