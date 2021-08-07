package com.fanrende.myfirstmod.items;

import com.fanrende.myfirstmod.entities.InfinityPearlEntity;
import com.fanrende.myfirstmod.setup.ModSetup;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class InfinityPearl extends Item
{
	public InfinityPearl()
	{
		super(new Item.Properties().stacksTo(1).tab(ModSetup.ITEM_GROUP));
	}

	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		worldIn.playSound(
				(PlayerEntity) null,
				playerIn.getX(),
				playerIn.getY(),
				playerIn.getZ(),
				SoundEvents.ENDER_PEARL_THROW,
				SoundCategory.NEUTRAL,
				0.5F,
				0.4F / ( random.nextFloat() * 0.4F + 0.8F )
		);
		playerIn.getCooldowns().addCooldown(this, 20);
		if (!worldIn.isClientSide)
		{
			EnderPearlEntity enderpearlentity = new EnderPearlEntity(worldIn, playerIn);
			enderpearlentity.setItem(itemstack);
			enderpearlentity.shootFromRotation(
					playerIn,
					playerIn.xRot,
					playerIn.yRot,
					0.0F,
					1.5F,
					1.0F
			);
			worldIn.addFreshEntity(enderpearlentity);
		}

		playerIn.awardStat(Stats.ITEM_USED.get(this));

		return ActionResult.sidedSuccess(itemstack, worldIn.isClientSide());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(
			ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn
	)
	{
		if (Screen.hasShiftDown())
		{
			tooltip.add(new TranslationTextComponent("message.infinitypearl"));
		}
		else
		{
			tooltip.add(new TranslationTextComponent("message.pressshift"));
		}
	}
}
