package com.fanrende.myfirstmod.items;

import com.fanrende.myfirstmod.entities.InfinityEyeEntity;
import com.fanrende.myfirstmod.setup.ModSetup;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class InfinityEye extends Item
{
	public InfinityEye()
	{
		super(new Item.Properties().stacksTo(1).tab(ModSetup.ITEM_GROUP));
	}

	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		RayTraceResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, RayTraceContext.FluidMode.NONE);
		if (raytraceresult.getType() == RayTraceResult.Type.BLOCK && worldIn.getBlockState(( (BlockRayTraceResult) raytraceresult ).getBlockPos())
				.getBlock() == Blocks.END_PORTAL_FRAME)
		{
			return ActionResult.pass(itemstack);
		}
		else
		{
			playerIn.startUsingItem(handIn);
			if (worldIn instanceof ServerWorld)
			{
				BlockPos blockpos = ( (ServerWorld) worldIn ).getChunkSource()
						.getGenerator()
						.findNearestMapFeature((ServerWorld)worldIn, Structure.STRONGHOLD, playerIn.blockPosition(), 100, false);
				if (blockpos != null)
				{
					InfinityEyeEntity infinityEyeEntity = new InfinityEyeEntity(worldIn,
							playerIn.getX(),
							playerIn.getY(0.5D),
							playerIn.getZ()
					);
					infinityEyeEntity.setItem(itemstack);
					infinityEyeEntity.signalTo(blockpos);
					worldIn.addFreshEntity(infinityEyeEntity);
					if (playerIn instanceof ServerPlayerEntity)
					{
						CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayerEntity) playerIn, blockpos);
					}

					worldIn.playSound((PlayerEntity) null,
							playerIn.getX(),
							playerIn.getY(),
							playerIn.getZ(),
							SoundEvents.ENDER_EYE_LAUNCH,
							SoundCategory.NEUTRAL,
							0.5F,
							0.4F / ( random.nextFloat() * 0.4F + 0.8F )
					);
					worldIn.levelEvent((PlayerEntity) null, 1003, playerIn.blockPosition(), 0);

					playerIn.awardStat(Stats.ITEM_USED.get(this));
					playerIn.swing(handIn, true);
					return ActionResult.success(itemstack);
				}
			}

			return ActionResult.consume(itemstack);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(
			ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn
	)
	{
		if (Screen.hasShiftDown())
		{
			tooltip.add(new TranslationTextComponent("message.infinityeye"));
		}
		else
		{
			tooltip.add(new TranslationTextComponent("message.pressshift"));
		}
	}
}
