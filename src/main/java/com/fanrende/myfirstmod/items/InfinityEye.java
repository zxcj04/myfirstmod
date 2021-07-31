package com.fanrende.myfirstmod.items;

import com.fanrende.myfirstmod.entities.InfinityEyeEntity;
import com.fanrende.myfirstmod.setup.ModSetup;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class InfinityEye extends Item
{
	public InfinityEye()
	{
		super(new Item.Properties().maxStackSize(1).group(ModSetup.ITEM_GROUP));
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.NONE);
		if (raytraceresult.getType() == RayTraceResult.Type.BLOCK && worldIn.getBlockState(( (BlockRayTraceResult) raytraceresult ).getPos())
				.getBlock() == Blocks.END_PORTAL_FRAME)
		{
			return ActionResult.resultPass(itemstack);
		}
		else
		{
			playerIn.setActiveHand(handIn);
			if (worldIn instanceof ServerWorld)
			{
				BlockPos blockpos = ( (ServerWorld) worldIn ).getChunkProvider()
						.getChunkGenerator()
						.findNearestStructure(worldIn, "Stronghold", new BlockPos(playerIn), 100, false);
				if (blockpos != null)
				{
					InfinityEyeEntity infinityEyeEntity = new InfinityEyeEntity(worldIn,
							playerIn.getPosX(),
							playerIn.getPosYHeight(0.5D),
							playerIn.getPosZ()
					);
					infinityEyeEntity.func_213863_b(itemstack);
					infinityEyeEntity.moveTowards(blockpos);
					worldIn.addEntity(infinityEyeEntity);
					if (playerIn instanceof ServerPlayerEntity)
					{
						CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayerEntity) playerIn, blockpos);
					}

					worldIn.playSound((PlayerEntity) null,
							playerIn.getPosX(),
							playerIn.getPosY(),
							playerIn.getPosZ(),
							SoundEvents.ENTITY_ENDER_EYE_LAUNCH,
							SoundCategory.NEUTRAL,
							0.5F,
							0.4F / ( random.nextFloat() * 0.4F + 0.8F )
					);
					worldIn.playEvent((PlayerEntity) null, 1003, new BlockPos(playerIn), 0);

					playerIn.addStat(Stats.ITEM_USED.get(this));
					playerIn.swing(handIn, true);
					return ActionResult.resultSuccess(itemstack);
				}
			}

			return ActionResult.resultConsume(itemstack);
		}
	}
}
