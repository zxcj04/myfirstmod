package com.fanrende.myfirstmod.items;

import com.fanrende.myfirstmod.entities.InfinityEyeEntity;
import com.fanrende.myfirstmod.setup.ModSetup;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
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

	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn)
	{
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		HitResult hitresult = getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.NONE);
		if (hitresult.getType() == HitResult.Type.BLOCK && worldIn.getBlockState(( (BlockHitResult) hitresult ).getBlockPos())
				.is(Blocks.END_PORTAL_FRAME))
		{
			return InteractionResultHolder.pass(itemstack);
		}
		else
		{
			playerIn.startUsingItem(handIn);
			if (worldIn instanceof ServerLevel)
			{
				BlockPos blockpos = ( (ServerLevel) worldIn ).getChunkSource()
						.getGenerator()
						.findNearestMapFeature((ServerLevel) worldIn,
								StructureFeature.STRONGHOLD,
								playerIn.blockPosition(),
								100,
								false
						);
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
					if (playerIn instanceof ServerPlayer)
					{
						CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayer) playerIn, blockpos);
					}

					worldIn.playSound((Player) null,
							playerIn.getX(),
							playerIn.getY(),
							playerIn.getZ(),
							SoundEvents.ENDER_EYE_LAUNCH,
							SoundSource.NEUTRAL,
							0.5F,
							0.4F / ( worldIn.getRandom().nextFloat() * 0.4F + 0.8F )
					);
					worldIn.levelEvent((Player) null, 1003, playerIn.blockPosition(), 0);
					if (!playerIn.getAbilities().instabuild)
					{
						itemstack.shrink(1);
					}

					playerIn.awardStat(Stats.ITEM_USED.get(this));
					playerIn.swing(handIn, true);
					return InteractionResultHolder.success(itemstack);
				}
			}

			return InteractionResultHolder.consume(itemstack);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(
			ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn
	)
	{
		if (Screen.hasShiftDown())
		{
			tooltip.add(new TranslatableComponent("message.infinityeye"));
		}
		else
		{
			tooltip.add(new TranslatableComponent("message.pressshift"));
		}
	}
}
